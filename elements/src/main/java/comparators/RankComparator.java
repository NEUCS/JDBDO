package comparators;

import individual.Individual;

import java.util.Comparator;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/24 9:28
 */
public class RankComparator<I extends Individual<?>> implements Comparator<I> {
    @Override
    public int compare(I individual1, I individual2) {
        int result;
        if (individual1 == null) {
            if (individual2 == null) {
                result = 0;
            } else {
                result = 1;
            }
        } else if (individual2 == null) {
            result = -1;
        } else {
            int rank1 = Integer.MAX_VALUE;
            int rank2 = Integer.MAX_VALUE;

            if (individual1.getAttribute("Rank") != null) {
                rank1 = (int) individual1.getAttribute("Rank");
            }

            if (individual2.getAttribute("Rank") != null) {
                rank2 = (int) individual2.getAttribute("Rank");
            }

            result = Integer.compare(rank1, rank2);
        }

        return result;
    }

}