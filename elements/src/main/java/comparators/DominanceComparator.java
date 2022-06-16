package comparators;

import individual.Individual;

import java.util.Comparator;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/24 9:44
 */
public class DominanceComparator<I extends Individual<?>> implements
        Comparator<I> {

    @Override
    public int compare(I individual1, I individual2) {
        int bestIsOne = 0;
        int bestIsTwo = 0;
        int result;
        for (int i = 0; i < individual1.getNumberOfObjectives(); i++) {
            double value1 = individual1.getObjectiveValue(i);
            double value2 = individual2.getObjectiveValue(i);
            if (value1 != value2) {
                if (value1 < value2) {
                    bestIsOne = 1;
                }
                if (value2 < value1) {
                    bestIsTwo = 1;
                }
            }
        }
        if (bestIsOne > bestIsTwo) {
            result = -1;
        } else if (bestIsTwo > bestIsOne) {
            result = 1;
        } else {
            result = 0;
        }
        return result;
    }
}