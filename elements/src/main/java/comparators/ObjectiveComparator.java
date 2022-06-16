package comparators;

import individual.Individual;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/21 20:27
 */
public class ObjectiveComparator<I extends Individual<?>> implements Comparator<I> {
    private int noOfObjective;

    public ObjectiveComparator(int noOfObjective){
        this.noOfObjective = noOfObjective;
    }

    @Override
    public int compare(I individual1, I individual2) {
        int result;
        if (individual1 == null){
            if (individual2 == null){
                return 0;
            }else {
                return 1;
            }
        }else if (individual2 ==null){
            return -1;
        }else {
            double valueOfObjective1 = individual1.getObjectiveValue(noOfObjective);
            double valueOfObjective2 = individual2.getObjectiveValue(noOfObjective);
            result = Double.compare(valueOfObjective1, valueOfObjective2);
        }
        return result;
    }
}