package dynamicMetaheuristic.moead;

import algorithm.DynamicMetaheuristic;
import algorithm.reinitializer.RandomReinitializer;
import individual.ContinuousIndividual;
import operators.crossover.DECrossover;
import operators.mutation.Mutation;
import population.Population;
import problem.DynamicTestProblem;
import staticMetaheuristic.eas.moead.MOEAD;

public class DynamicMOEAD extends MOEAD
        implements DynamicMetaheuristic<Population<ContinuousIndividual>> {
    private DynamicTestProblem<ContinuousIndividual> dynamicTestProblem;
    private int iteration;
    private RandomReinitializer<ContinuousIndividual> randomReinitializer;

    public DynamicMOEAD(DynamicTestProblem<ContinuousIndividual> testProblem,
            int populationSize, int finalPopulationSize,
            int limitedNumberOfEvaluations, DECrossover crossover,
            Mutation<ContinuousIndividual> mutation, double delta, int nr,
            int t, int numberOfModifiedInidividuals) {
        super(testProblem, populationSize, finalPopulationSize,
              limitedNumberOfEvaluations, crossover, mutation, delta, nr, t);
        this.dynamicTestProblem = testProblem;
        this.iteration = 0;
        this.randomReinitializer =
                new RandomReinitializer<>(numberOfModifiedInidividuals,
                                          dynamicTestProblem);
    }

    @Override
    public boolean isStoppingCriterionMet() {
        if (evaluations >= limitedNumberOfEvaluations) {
            this.dynamicTestProblem.updateProblem(iteration);
            restart();
            for (ContinuousIndividual individial : population.getPopulation()) {
                dynamicTestProblem.evaluate(individial);
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

    public void restart() {
        this.randomReinitializer.reinitialize(population);
    }

    public void update() {
        if (dynamicTestProblem.problemChanged()) {
            restart();
            for (ContinuousIndividual individial : population.getPopulation()) {
                dynamicTestProblem.evaluate(individial);
            }
            dynamicTestProblem.resetChangeFlag();
        }
        super.update();
    }

}