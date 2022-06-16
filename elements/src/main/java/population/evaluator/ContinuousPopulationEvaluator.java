package population.evaluator;

import individual.ContinuousIndividual;
import individual.Individual;
import population.Population;
import problem.TestProblem;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/24 9:55
 */
public class ContinuousPopulationEvaluator<I extends Individual<?>>
        implements PopulationEvaluator<I> {
    @Override
    public Population<I> evaluate(Population<I> population,
            TestProblem<I> testProblem) {
        for (I i : population.getPopulation()) {
            testProblem.evaluate(i);
        }
        return population;
    }
}