package algorithm.reinitializer;

import individual.Individual;
import population.Population;
import problem.DynamicTestProblem;
import random.MRandom;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/1/13 18:04
 */
public class RandomReinitializer<I extends Individual<?>> {
    private int numberOfModifiedIndividual;
    private DynamicTestProblem<I> dynamicTestProblem;

    public RandomReinitializer(int numberOfModifiedIndividual,
            DynamicTestProblem<I> dynamicTestProblem) {
        this.numberOfModifiedIndividual = numberOfModifiedIndividual;
        this.dynamicTestProblem = dynamicTestProblem;
    }

    public void reinitialize(Population<I> population) {
        randomlyRemoveIndividuals(population);
        addNewIndividuals(population);
    }

    public void randomlyRemoveIndividuals(Population<I> population) {
        for (int i = 0; i < numberOfModifiedIndividual; i++) {
            int randomIndex = MRandom.getInstance()
                    .nextInt(0, population.getPopulation().size() - 1);
            population.getPopulation().remove(randomIndex);
        }
    }

    public void addNewIndividuals(Population<I> population){
        for (int i = 0; i < numberOfModifiedIndividual; i++) {
            population.getPopulation().add(dynamicTestProblem.initIndividual(i));
        }
    }
}