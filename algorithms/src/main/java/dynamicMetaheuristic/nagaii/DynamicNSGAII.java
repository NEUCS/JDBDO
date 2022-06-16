package dynamicMetaheuristic.nagaii;

import Archive.KRArchive.KRArchive;
import KnowledgeReuse.KnowledgeReuseStrategy;
import Visualizer.DynamicSimpleVisualizer;
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
import staticMetaheuristic.eas.nsgaii.NSGAIIPrototype;

import java.io.IOException;
import java.util.Comparator;

public class DynamicNSGAII<I extends Individual<?>, KnowledReuseStrategy>
        extends NSGAIIPrototype<I>
        implements DynamicMetaheuristic<Population<I>> {
    private DynamicTestProblem<I> dynamicTestProblem;
    private int iteration;
    private Population<I> lastIterationPopulation;
    private int numberOfModifiedInidividuals;
    private RandomReinitializer<I> randomReinitializer;
    private KRArchive<ContinuousIndividual> krArchive;
    private KnowledgeReuseStrategy kr;
    private double oldTime;
    private double newTime;
    DynamicSimpleVisualizer<ContinuousIndividual> visualizer;

    public DynamicNSGAII(DynamicTestProblem<I> testProblem,
            int limitedNumberOfEvaluations, int populationSize,
            int matingPoolSize, int offspringSize, Crossover<I> crossover,
            Mutation<I> mutation, Selection<Population<I>, I> selection,
            Comparator<I> comparator, PopulationEvaluator<I> evaluator,
            int numberOfModifiedInidividuals) throws IOException {
        super(testProblem, limitedNumberOfEvaluations, populationSize,
              matingPoolSize, offspringSize, crossover, mutation, selection,
              comparator, evaluator);
        this.dynamicTestProblem = testProblem;
        this.iteration = 0;
        this.lastIterationPopulation = new Population<>();
        this.randomReinitializer =
                new RandomReinitializer<>(numberOfModifiedInidividuals,
                                          dynamicTestProblem);
        this.kr = new KnowledgeReuseStrategy(100, 0.01, 10, testProblem);
    }

    @Override
    protected boolean isStoppingCriterionMet() throws IOException {
        if (evaluations >= limitedNumberOfEvaluations) {
            oldTime = getDynamicTestProblem().getTime();
            getDynamicTestProblem().updateProblem(iteration);
            newTime = getDynamicTestProblem().getTime();
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
        this.kr.generateNextInitialPopulation(String.valueOf(oldTime),
                                              String.valueOf(newTime),
                                              getPopulation());
    }
}