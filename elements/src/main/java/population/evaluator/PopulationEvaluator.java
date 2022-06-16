package population.evaluator;

import individual.Individual;
import population.Population;
import problem.TestProblem;

import java.io.Serializable;

public interface PopulationEvaluator<I extends Individual<?>> extends Serializable {
    Population<I> evaluate(Population<I> population,
            TestProblem<I> testProblem);
}
