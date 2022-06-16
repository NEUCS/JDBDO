package comparators;

import individual.Individual;

import java.util.Comparator;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/24 9:34
 */
public class RankAndCDComparator<I extends Individual<?>> implements
        Comparator<I> {
    private Comparator<I> rankComparator = new RankComparator<I>();
    private Comparator<I> cdComparator = new CrowdingDistanceComparator<I>();

    @Override
    public int compare(I individual1, I individual2) {
        int result = rankComparator.compare(individual1, individual2);
        if (result == 0) {
            result = cdComparator.compare(individual1, individual2);
        }

        return result;
    }
}