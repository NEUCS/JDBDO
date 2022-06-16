package dynamicMetaheuristic.nsgaiii;

import algorithm.DynamicMetaheuristic;
import algorithm.reinitializer.RandomReinitializer;
import individual.ContinuousIndividual;
import individual.Individual;
import operators.crossover.Crossover;
import operators.mutation.Mutation;
import operators.selection.Selection;
import population.Population;
import population.evaluator.PopulationEvaluator;
import problem.DynamicTestProblem;
import staticMetaheuristic.eas.nsgaiii.NSGAIII;

public class DynamicNSGAIII<I extends Individual<?>> extends NSGAIII<I>
        implements DynamicMetaheuristic<Population<I>> {
    private DynamicTestProblem<I> dynamicTestProblem;
    private int iteration;
    private Population<I> lastIterationPopulation;
    private int numberOfModifiedInidividuals;
    private RandomReinitializer<I> randomReinitializer;

    public DynamicNSGAIII(DynamicTestProblem<I> testProblem,
            int limitedNumberOfIteration, int populationSize,
            Crossover<I> crossover, Mutation<I> mutation,
            Selection<Population<I>, I> selection,
            PopulationEvaluator<I> evaluator, int numberOfDivisions,
            int numberOfModifiedInidividuals) {
        super(testProblem, limitedNumberOfIteration, populationSize, crossover,
              mutation, selection, evaluator, numberOfDivisions);
        this.dynamicTestProblem = testProblem;
        this.iteration = 0;
        this.lastIterationPopulation = new Population<>();
        this.randomReinitializer =
                new RandomReinitializer<>(numberOfModifiedInidividuals,
                                          dynamicTestProblem);
    }

    @Override
    protected boolean isStoppingCriterionMet() {
        if (this.numberOfIterations >= limitedNumberOfIteration) {
            getDynamicTestProblem().updateProblem(iteration);
            lastIterationPopulation = getPopulation();
            restart();
            populationEvaluator
                    .evaluate(getPopulation(), getDynamicTestProblem());
            iteration++;
            initialize();
        }
        return false;
    }

    @Override
    public DynamicTestProblem<I> getDynamicTestProblem() {
        return dynamicTestProblem;
    }

    @Override
    protected void update() {
        if (getDynamicTestProblem().problemChanged()) {
            restart();
            populationEvaluator
                    .evaluate(getPopulation(), getDynamicTestProblem());
            getDynamicTestProblem().resetChangeFlag();
        }
        super.update();
    }

    @Override
    public void restart() {
        this.randomReinitializer.reinitialize(getPopulation());
    }
}