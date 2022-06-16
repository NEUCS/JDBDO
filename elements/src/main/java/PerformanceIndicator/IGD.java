package PerformanceIndicator;

import PerformanceIndicator.tools.PF;
import individual.ContinuousIndividual;
import population.Population;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/5 14:30
 */
public class IGD {
    protected PF referencePF;

    public IGD(String path, String regex){
        this.referencePF = new PF(path, regex);
    }

    public IGD(PF pf){
        this.referencePF = new PF(pf);
    }

    public double computeIGD(Population<ContinuousIndividual> population){
        PF candidatePF = new PF(population);
        double temp = 0.0;
        for (int i = 0; i < referencePF.getObjectiveValueArray().size(); i++) {
            temp += Math.pow(computeMinDistance(candidatePF,
                                                referencePF.getObjectiveValues(i)), 2);
        }
        temp = Math.pow(temp, 0.5) / referencePF.getObjectiveValueArray().size();
        return temp;
    }

    public double computeEDistance(double[] a, double[] b){
        double distance = 0.0;

        for (int i = 0; i < a.length; i++) {
            distance += Math.pow(a[i] - b[i],2);
        }
        return Math.sqrt(distance);
    }

    public double computeMinDistance(PF pf, double[] objective){
        double min = Double.MAX_VALUE;
        for (int i = 0; i < pf.getObjectiveValueArray().size(); i++) {
            double distance = computeEDistance(pf.getObjectiveValues(i), objective);
            if (distance < min){
                min = distance;
            }
        }
        return min;
    }
}