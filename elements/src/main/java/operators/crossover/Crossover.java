package operators.crossover;

import individual.Individual;
import population.Population;

import java.io.Serializable;

public interface Crossover<I extends Individual<?>> extends Serializable {
    int getNumberOfParents();
    Population<I> execute(Population<I> population);
}
