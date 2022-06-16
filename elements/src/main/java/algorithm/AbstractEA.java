package algorithm;

import individual.Individual;
import operators.crossover.Crossover;
import operators.mutation.Mutation;
import operators.selection.Selection;
import population.Population;
import problem.TestProblem;

import java.io.IOException;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/21 13:13
 */
public abstract class AbstractEA<I extends Individual<?>, P>
        implements Metaheuristic<P> {
    protected int populationSize;
    protected Population<I> population;
    protected TestProblem<I> testProblem;
    protected Selection<Population<I>, I> selection;
    protected Crossover<I> crossover;
    protected Mutation<I> muation;

    //setter
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setPopulation(Population<I> population) {
        this.population = population;
    }

    public void setTestProblem(TestProblem<I> testProblem) {
        this.testProblem = testProblem;
    }

    //getter
    public int getPopulationSize() {
        return populationSize;
    }

    public Population<I> getPopulation() {
        return population;
    }

    public TestProblem<I> getTestProblem() {
        return testProblem;
    }

    @Override
    public abstract P getNonDominatedSolutions();

    //constructor
    public AbstractEA(TestProblem<I> testProblem) {
        setTestProblem(testProblem);
    }

    // abstract methods
    protected abstract void initialize();

    protected abstract void update();

    protected abstract boolean isStoppingCriterionMet() throws IOException;

    protected abstract Population<I> evaluatePopulation(
            Population<I> population);

    protected abstract Population<I> selection(Population<I> population);

    protected abstract Population<I> crossAndMut(Population<I> population);

    protected abstract Population<I> nextGenPopulation(Population<I> population,
            Population<I> offspringPopulation);


    //methods
    protected Population<I> initializePopulation() {
        Population<I> population = new Population<>(populationSize);
        for (int i = 0; i < getPopulationSize(); i++) {
            var newIndividual = getTestProblem().initIndividual(i);
            population.setIndividual(i, newIndividual);
        }
        return population;
    }


    @Override
    public void run() {
        Population<I> offspring;
        Population<I> matingPopulation;

        population = initializePopulation();
        population = evaluatePopulation(population);
        initialize();
        while (true) {
            try {
                if (!!isStoppingCriterionMet())
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            matingPopulation = selection(population);
            offspring = crossAndMut(matingPopulation);
            offspring = evaluatePopulation(offspring);
            population = nextGenPopulation(population, offspring);
            update();
        }
    }



}