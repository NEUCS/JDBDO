package operators.selection;

import comparators.CrowdingDistanceComparator;
import comparators.ObjectiveComparator;
import individual.Individual;
import operators.selection.rank.DominanceRank;
import operators.selection.rank.Rank;
import population.Population;

import java.util.*;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/21 16:04
 */
public class NSGAIISelection<I extends Individual<?>>
        implements Selection<Population<I>, Population<I>> {
    private int selectedNumber;
    private Comparator<I> comparator;

    //constructor
    public NSGAIISelection(int selectedNumber, Comparator<I> comparator) {
        this.selectedNumber = selectedNumber;
        this.comparator = comparator;
    }

    @Override
    public Population<I> execute(Population<I> population) {
        Rank<I> rank = new DominanceRank<>(comparator);
        rank.compute(population);
        List<I> newPopulation = new ArrayList<>();
        int rankIdx = 0;
        while (newPopulation.size() < selectedNumber) {
            if (rank.getSubFront(rankIdx).getPopulation().size() <
                    (selectedNumber - newPopulation.size())) {
                crowdingDistanceDensity(rank.getSubFront(rankIdx));
                for (int i = 0;
                        i < rank.getSubFront(rankIdx).getPopulation().size();
                        i++) {
                    newPopulation
                            .add(rank.getSubFront(rankIdx).getIndividual(i));
                }
                rankIdx++;
            } else {
                crowdingDistanceDensity(rank.getSubFront(rankIdx));
                List<I> front = rank.getSubFront(rankIdx).getPopulation();
                front.sort(new CrowdingDistanceComparator<>());
                int flag = 0;
                while (newPopulation.size() < selectedNumber) {
                    newPopulation.add(front.get(flag));
                    flag++;
                }
            }
        }
        Population<I> finalPopulation = new Population<>(newPopulation.size());
        finalPopulation.setPopulation(newPopulation);
        return finalPopulation;
    }

    private void crowdingDistanceDensity(Population<I> rawPopulation) {
        double min;
        double max;
        double distance;
        var population = new Population<>();

        for (int i = 0; i < rawPopulation.getPopulation().size(); i++) {
            population.add(rawPopulation.getPopulation().get(i));
        }

        for (int i = 0; i < rawPopulation.getPopulation().size(); i++) {
            population.getPopulation().get(i)
                    .setAttribute("CrowdingDistance", 0.0);
        }
        for (int i = 0; i <
                rawPopulation.getPopulation().get(0).getNumberOfObjectives();
                i++) {
            List<I> sortPopulation = (List<I>) population.getPopulation();
            sortPopulation.sort(new ObjectiveComparator<I>(i));
            min = sortPopulation.get(0).getObjectiveValue(i);
            max = sortPopulation.get(sortPopulation.size() - 1)
                    .getObjectiveValue(i);
            sortPopulation.get(0)
                    .setAttribute("CrowdingDistance", Double.POSITIVE_INFINITY);
            sortPopulation.get(sortPopulation.size() - 1)
                    .setAttribute("CrowdingDistance", Double.POSITIVE_INFINITY);

            for (int j = 1; j < sortPopulation.size() - 1; j++) {
                distance = (sortPopulation.get(j + 1).getObjectiveValue(i) -
                        sortPopulation.get(j - 1).getObjectiveValue(i)) /
                        (max - min);
                distance += (double) sortPopulation.get(j)
                        .getAttribute("CrowdingDistance");
                sortPopulation.get(j)
                        .setAttribute("CrowdingDistance", distance);
            }
        }
    }
}