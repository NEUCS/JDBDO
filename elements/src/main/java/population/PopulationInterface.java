package population;

import individual.Individual;

import java.io.Serializable;
import java.util.List;

public interface PopulationInterface<T extends Individual<?>> extends Serializable {
    //setter
    void setPopulation(List<T> population);
    void setIndividual(int idx, T individual);

    //getter
    T getIndividual(int idx);

    void enlarge(Population<T> otherPopulation);
    void add(T individual);
    List<T> getPopulation();
}
