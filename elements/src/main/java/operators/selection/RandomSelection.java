package operators.selection;

import individual.Individual;
import population.Population;
import random.MRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/8 15:44
 */
public class RandomSelection<I extends Individual<?>>
        implements Selection<Population<I>, Population<I>> {
    private int numberOfSelected;

    public RandomSelection(int numberOfSelected) {
        this.numberOfSelected = numberOfSelected;
    }

    public RandomSelection() {
        this.numberOfSelected = 1;
    }

    @Override
    public Population<I> execute(Population<I> population) {
        if (population.getPopulation().size() < numberOfSelected) {
            throw new RuntimeException(
                    "Number of individuals in population is less than " +
                            "required number of individuals to select!");
        }
        List<Integer> index = new ArrayList<>(numberOfSelected);
        Population<I> selectedPopulation = new Population<>();
        for (int i = 0; i < numberOfSelected; i++) {
            index.add(MRandom.getInstance().nextInt(0,
                                                    population.getPopulation()
                                                            .size() - 1));
        }
        for (int idx : index) {
            selectedPopulation.add(population.getIndividual(idx));
        }
        return selectedPopulation;
    }
}