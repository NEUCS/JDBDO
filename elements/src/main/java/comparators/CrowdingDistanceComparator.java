package comparators;

import individual.Individual;

import java.util.Comparator;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/21 21:31
 */
public class CrowdingDistanceComparator<I extends Individual<?>> implements
        Comparator<I> {
    @Override
    public int compare(I individual1, I individual2) {
        int result;
        if (individual1 == null){
            if (individual2 == null){
                return 0;
            }else {
                return 1;
            }
        }else if (individual2 == null){
            return -1;
        }else {
             double distance1 =
                    (double) individual1.getAttribute("CrowdingDistance");
            double distance2 =
                    (double) individual2.getAttribute("CrowdingDistance");
            if (distance1 > distance2){
                return -1;
            }else if (distance1 < distance2){
                return 1;
            }else{
                return 0;
            }
        }
    }
}