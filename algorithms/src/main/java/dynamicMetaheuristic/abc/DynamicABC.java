package dynamicMetaheuristic.abc;

import algorithm.DynamicMetaheuristic;
import algorithm.reinitializer.RandomReinitializer;
import individual.ContinuousIndividual;
import population.Population;
import population.evaluator.PopulationEvaluator;
import problem.DynamicTestProblem;
import staticMetaheuristic.sis.abc.ABC;

import java.util.Comparator;

public class DynamicABC extends ABC<ContinuousIndividual>
        implements DynamicMetaheuristic<Population<ContinuousIndividual>> {
    private DynamicTestProblem<ContinuousIndividual> dynamicTestProblem;
    private int iteration;
    private RandomReinitializer<ContinuousIndividual> randomReinitializer;

    public DynamicABC(DynamicTestProblem<ContinuousIndividual> testProblem,
            int swarmSize, int maxTrial,
            Comparator<ContinuousIndividual> comparator,
            PopulationEvaluator<ContinuousIndividual> populationEvaluator,
            int limitedNumberOfIterations, int archiveSize,
            int numberOfModifiedInidividuals) {
        super(testProblem, swarmSize, maxTrial, comparator, populationEvaluator,
              limitedNumberOfIterations, archiveSize);
        this.dynamicTestProblem = testProblem;
        this.iteration = 0;
        this.randomReinitializer =
                new RandomReinitializer<>(numberOfModifiedInidividuals,
                                          dynamicTestProblem);
    }

    @Override
    public boolean isStoppingCriterionMet() {
        if (this.iterations >= limitedNumberOfIterations) {
            this.dynamicTestProblem.updateProblem(iteration);
            restart();
            for (ContinuousIndividual individual : swarm.getPopulation()) {
                dynamicTestProblem.evaluate(individual);
            }
            iteration++;
            initialize();
        }
        return false;
    }

    @Override
    public DynamicTestProblem<?> getDynamicTestProblem() {
        return dynamicTestProblem;
    }

    @Override
    public void restart() {
        this.randomReinitializer.reinitialize(swarm);
        for (var individal : swarm.getPopulation()) {
            individal.setAttribute("Trial", 0);
        }
    }

    @Override
    public void update() {
        if (dynamicTestProblem.problemChanged()) {
            restart();
            for (ContinuousIndividual individual : swarm.getPopulation()) {
                dynamicTestProblem.evaluate(individual);
            }
            dynamicTestProblem.resetChangeFlag();
        }
        super.update();
    }
}