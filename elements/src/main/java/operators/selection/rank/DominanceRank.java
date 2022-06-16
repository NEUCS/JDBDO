package operators.selection.rank;

import individual.Individual;
import population.Population;

import java.util.*;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/21 16:41
 */
public class DominanceRank<I extends Individual<?>> implements Rank<I> {
    private Comparator<I> comparator;
    private List<Population<I>> rankedPopulation;

    public DominanceRank(Comparator<I> comparator) {
        this.comparator = comparator;
        this.rankedPopulation = new ArrayList<>();
    }

    @Override
    public Rank<I> compute(Population<I> rawPopulation) {
        List<I> population = rawPopulation.getPopulation();
        int[] dominateMe = new int[population.size()];
        List<List<Integer>> iDominate = new ArrayList<>(population.size());
        ArrayList<List<Integer>> front = new ArrayList<>(population.size());

        for (int i = 0; i < population.size() + 1; i++) {
            front.add(new LinkedList<Integer>());
        }

        for (int i = 0; i < population.size(); i++) {
            iDominate.add(new LinkedList<Integer>());
            dominateMe[i] = 0;
        }
        int flag;
        for (int i = 0; i < (population.size() - 1); i++) {
            for (int j = i + 1; j < population.size(); j++) {
                flag = comparator.compare(population.get(i), population.get(j));
                if (flag == -1) {
                    iDominate.get(i).add(j);
                    dominateMe[j]++;
                } else if (flag == 1) {
                    iDominate.get(j).add(i);
                    dominateMe[i]++;
                }
            }
        }
        for (int i = 0; i < population.size(); i++) {
            if (dominateMe[i] == 0) {
                front.get(0).add(i);
                rawPopulation.getPopulation().get(i).setAttribute("Rank", 0);
            }
        }
        int flag_2 = 0;
        Iterator<Integer> iterator1;
        Iterator<Integer> iterator2;
        while (front.get(flag_2).size() != 0) {
            flag_2++;
            iterator1 = front.get(flag_2 - 1).iterator();
            while (iterator1.hasNext()) {
                iterator2 = iDominate.get(iterator1.next()).iterator();
                while (iterator2.hasNext()) {
                    int idx = iterator2.next();
                    dominateMe[idx]--;
                    if (dominateMe[idx] == 0) {
                        front.get(flag_2).add(idx);
                        rawPopulation.getPopulation().get(idx)
                                .setAttribute("Rank", flag_2);
                    }
                }
            }
        }
        Iterator<Integer> iterator3;
        for (int i = 0; i < flag_2; i++) {
            rankedPopulation.add(i, new Population<I>());
            iterator3 = front.get(i).iterator();
            while (iterator3.hasNext()) {
                rankedPopulation.get(i)
                        .add(rawPopulation.getIndividual(iterator3.next()));
            }
        }
        return this;
    }

    @Override
    public Population<I> getSubFront(int rank) {
        if (rank >= rankedPopulation.size()) {
            throw new RuntimeException("Invalid rank!");
        }
        return rankedPopulation.get(rank);
    }
}