package dmops.fda;

import individual.ContinuousIndividual;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/1/17 12:20
 */
public class FDA1 extends FDA{
    public FDA1(){
        super();
        int numberOfVariables = 100;
        setNumberOfVariables(numberOfVariables);
        int numberOfObjects = 2;
        setNumberOfObjectives(numberOfObjects);
        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        lowerLimit.add(0.0);
        upperLimit.add(1.0);
        for (int i = 1; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-1.0);
            upperLimit.add(1.0);
        }
        setBounds(lowerLimit, upperLimit);
    }

    @Override
    public void evaluate(ContinuousIndividual individual) {
        double[] f = new double[getNumberOfObjectives()];
        f[0] = individual.getVariableValue(0);
        double g = this.evalG(individual);
        double h = this.evalH(f[0], g);
        f[1] = h * g;
        individual.setObjectiveValue(0, f[0]);
        individual.setObjectiveValue(1, f[1]);
    }

    private double evalG(ContinuousIndividual solution) {
        double gT = Math.sin(0.5 * Math.PI * time);
        double g = 0.0;
        for (int i = 1; i < solution.getNumberOfVariables(); i++) {
            g += Math.pow((solution.getVariableValue(i) - gT), 2);
        }
        g = g + 1.0;
        return g;
    }

    public double evalH(double f, double g) {
        return 1 - Math.sqrt(f / g);
    }

    @Override
    public double getTime() {
        return time;
    }
}