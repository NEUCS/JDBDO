package population;

import individual.Individual;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/21 13:30
 */
public class Population<I extends Individual<?>>
        implements PopulationInterface<I>{
    private List<I> population;

    public Population(int populationSize) {
        population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            population.add(i, null);
        }
    }

    public Population(){
        population = new ArrayList<>();
    }

    @Override
    public void setPopulation(List<I> population) {
        this.population = population;
    }

    @Override
    public void setIndividual(int idx, I individual) {
        population.set(idx, individual);
    }

    @Override
    public I getIndividual(int idx) {
        return population.get(idx);
    }

    @Override
    public void enlarge(Population<I> otherPopulation) {
        List<I> newPopulation = new ArrayList<>(population.size()+otherPopulation.getPopulation().size());
        newPopulation.addAll(population);
        newPopulation.addAll(otherPopulation.getPopulation());
        setPopulation(newPopulation);
    }

    @Override
    public void add(I individual) {
        population.add(individual);
    }

    @Override
    public List<I> getPopulation() {
        return population;
    }
}