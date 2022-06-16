package population.evaluator;

import individual.Individual;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import population.Population;
import problem.TestProblem;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/8 16:10
 */
public class ParallelizedEvaluator<I extends Individual<?>>
        implements PopulationEvaluator<I> {
    private JavaSparkContext sparkContext;

    public ParallelizedEvaluator(JavaSparkContext sparkContext) {
        this.sparkContext = sparkContext;
    }

    @Override
    public Population<I> evaluate(Population<I> population,
            TestProblem<I> testProblem) {
        JavaRDD<I> RDD = sparkContext.parallelize(population.getPopulation())
                .map((Function<I, I>) individual -> {
                    testProblem.evaluate(individual);
                    return individual;
                });
        population.setPopulation(RDD.take(population.getPopulation().size()));
        return population;
    }
}