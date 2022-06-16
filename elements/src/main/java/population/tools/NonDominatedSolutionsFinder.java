package population.tools;

import individual.Individual;
import operators.selection.rank.DominanceRank;
import operators.selection.rank.Rank;
import population.Population;

import java.util.Comparator;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/21 22:13
 */
public class NonDominatedSolutionsFinder<I extends Individual<?>> {
    private Population<I> rawPopulation;
    private Comparator<I> comparator;

    public NonDominatedSolutionsFinder(Population<I> rawPopulation,
            Comparator<I> comparator) {
        this.rawPopulation = rawPopulation;
        this.comparator = comparator;
    }

    public Population<I> findNonDominatedSolutions() {
        Rank<I> rank = new DominanceRank<>(comparator);
        rank.compute(rawPopulation);
        return rank.getSubFront(0);
    }

}