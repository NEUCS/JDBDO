package staticMetaheuristic.eas.nsgaii;

import algorithm.AbstractEA;
import individual.Individual;
import operators.crossover.Crossover;
import operators.mutation.Mutation;
import operators.selection.NSGAIISelection;
import operators.selection.Selection;
import population.Population;
import population.evaluator.PopulationEvaluator;
import population.tools.NonDominatedSolutionsFinder;
import problem.TestProblem;

import java.io.IOException;
import java.util.Comparator;

public class NSGAIIPrototype<I extends Individual<?>>
        extends AbstractEA<I, Population<I>> {
    protected int evaluations;
    protected int limitedNumberOfEvaluations;
    protected PopulationEvaluator<I> populationEvaluator;
    protected Comparator<I> comparator;
    protected int matingPoolSize;
    protected int offspringSize;

    public NSGAIIPrototype(TestProblem<I> testProblem,
            int limitedNumberOfEvaluations, int populationSize,
            int matingPoolSize, int offspringSize, Crossover<I> crossover,
            Mutation<I> mutation, Selection<Population<I>, I> selection,
            Comparator<I> comparator, PopulationEvaluator<I> evaluator) {
        super(testProblem);
        this.limitedNumberOfEvaluations = limitedNumberOfEvaluations;
        setPopulationSize(populationSize);
        this.matingPoolSize = matingPoolSize;
        this.offspringSize = offspringSize;
        this.crossover = crossover;
        this.muation = mutation;
        this.selection = selection;
        this.comparator = comparator;
        this.populationEvaluator = evaluator;
    }

    @Override
    public Population<I> getNonDominatedSolutions() {
        NonDominatedSolutionsFinder<I> finder =
                new NonDominatedSolutionsFinder<>(getPopulation(), comparator);
        return finder.findNonDominatedSolutions();
    }

    @Override
    protected void initialize() {
        this.evaluations = getPopulationSize();
    }

    @Override
    protected void update() {
        this.evaluations += offspringSize;
    }

    @Override
    protected boolean isStoppingCriterionMet() throws IOException {
        return evaluations >= limitedNumberOfEvaluations;
    }

    @Override
    protected Population<I> evaluatePopulation(Population<I> population) {
        return populationEvaluator.evaluate(population, getTestProblem());
    }

    @Override
    protected Population<I> selection(Population<I> population) {
        Population<I> matingPopulation =
                new Population<>(population.getPopulation().size());
        for (int i = 0; i < getPopulationSize(); i++) {
            I selectedIndividual = selection.execute(population);
            matingPopulation.setIndividual(i, selectedIndividual);
        }
        return matingPopulation;
    }

    @Override
    protected Population<I> crossAndMut(Population<I> population) {
        int parentsNumber = crossover.getNumberOfParents();
        if ((population.getPopulation().size() % parentsNumber) != 0) {
            throw new RuntimeException("Wrong parents size!");
        }
        Population<I> offspring = new Population<>();
        for (int i = 0; i < getPopulationSize(); i += parentsNumber) {
            Population<I> parents = new Population<>(parentsNumber);
            for (int j = 0; j < parentsNumber; j++) {
                parents.setIndividual(j, population.getIndividual(i + j));
            }
            Population<I> off = crossover.execute(parents);
            for (int j = 0; j < off.getPopulation().size(); j++) {
                muation.execute(off.getIndividual(j));
                offspring.getPopulation().add(off.getIndividual(j));
                if (offspring.getPopulation().size() >= offspringSize) {
                    break;
                }
            }
        }
        return offspring;
    }

    @Override
    protected Population<I> nextGenPopulation(Population<I> population,
            Population<I> offspringPopulation) {
        population.enlarge(offspringPopulation);
        return elitePreserving(population);
    }

    private Population<I> elitePreserving(Population<I> combinedPopulation) {
        NSGAIISelection<I> eliteSelect =
                new NSGAIISelection<>(getPopulationSize(), comparator);
        return eliteSelect.execute(combinedPopulation);
    }
}