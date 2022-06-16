package PerformanceIndicator;

import PerformanceIndicator.tools.PF;
import individual.ContinuousIndividual;
import population.Population;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/5 15:37
 */
public class HV {
    protected double[] referencePoint;

    public HV(double[] referencePoint) {
        this.referencePoint = referencePoint;
    }

    public HV(Population<ContinuousIndividual> population) {
        PF rePf = new PF(population);
        double xMax = Double.MIN_VALUE;
        double yMax = Double.MIN_VALUE;
        for (double[] obj : rePf.getObjectiveValueArray()) {
            if (obj[0] > xMax) {
                xMax = obj[0];
            }
            if (obj[1] > yMax) {
                yMax = obj[1];
            }
        }
        this.referencePoint = new double[]{xMax + 0.5, yMax + 0.5};
    }

    public double computeHV(Population<ContinuousIndividual> population) {
        PF pf = new PF(population);
        sortByX(pf);
        double volume = 0.0;
        for (double[] objective : pf.getObjectiveValueArray()) {
            volume += Math.abs(objective[0] - referencePoint[0]) *
                    Math.abs(objective[1] - referencePoint[1]);
            referencePoint[0] -= referencePoint[0] - objective[0];
        }
        return volume;
    }


    public void sortByX(PF pf) {
        for (int i = 0; i < pf.getObjectiveValueArray().size() - 1; i++) {
            double[] temp;
            for (int j = 0; j < pf.getObjectiveValueArray().size() - 1 - i;
                    j++) {
                if (pf.getObjectiveValues(j)[0] <
                        pf.getObjectiveValues(j + 1)[0]) {
                    temp = pf.getObjectiveValues(j);
                    pf.getObjectiveValueArray()
                            .set(j, pf.getObjectiveValues(j + 1));
                    pf.getObjectiveValueArray().set(j + 1, temp);
                }
            }
        }
    }
}