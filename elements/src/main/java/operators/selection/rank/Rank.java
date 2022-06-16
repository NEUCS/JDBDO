package operators.selection.rank;

import individual.Individual;
import population.Population;

public interface Rank<I extends Individual<?>> {
    Rank<I> compute(Population<I> population);

    Population<I> getSubFront(int rank);
}
