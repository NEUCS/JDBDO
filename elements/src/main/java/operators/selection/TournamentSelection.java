package operators.selection;

import individual.Individual;
import population.Population;
import random.MRandom;

import java.util.*;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/23 12:38
 */
public class TournamentSelection<I extends Individual<?>>
        implements Selection<Population<I>, I> {
    private Comparator<I> comparator;
    private final int n_arity;

    public TournamentSelection(Comparator<I> comparator, int n_arity) {
        this.comparator = comparator;
        this.n_arity = n_arity;
    }

    @Override
    public I execute(Population<I> population) {
        if (population.getPopulation() == null) {
            throw new RuntimeException("population is null");
        }
        I selectedIndividual;
        if (population.getPopulation().size() == 1) {
            selectedIndividual = population.getPopulation().get(0);
        } else {
            selectedIndividual = selectRandomIndividual(population);
            for (int i = 0; i < n_arity - 1; i++) {
                I candidate = selectRandomIndividual(population);
                selectedIndividual =
                        compare2Individuals(selectedIndividual, candidate,
                                            comparator);
            }
        }
        return selectedIndividual;
    }

    private I selectRandomIndividual(Population<I> population) {
        if (population.getPopulation().size() == 1) {
            return population.getPopulation().get(0);
        } else {
            int index = randomValue(0, population.getPopulation().size() - 1);
            return population.getPopulation().get(index);
        }
    }

    private I compare2Individuals(I individual1, I individual2,
            Comparator<I> comparator) {
        int result = comparator.compare(individual1, individual2);
        if (result == -1) {
            return individual1;
        } else if (result == 1) {
            return individual2;
        } else {
            if (randomValue() < 0.5) {
                return individual1;
            } else {
                return individual2;
            }
        }
    }

    private int randomValue(int l, int u) {

        return MRandom.getInstance().nextInt(l, u);
    }

    private double randomValue() {
        return MRandom.getInstance().nextDouble();
    }
}