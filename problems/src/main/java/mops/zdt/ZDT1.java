package mops.zdt;

import individual.ContinuousIndividual;
import problem.AbstractContinuousTestProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/22 9:56
 */
public class ZDT1 extends AbstractContinuousTestProblem {
    public ZDT1(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(2);

        List<Double> lowerBound = new ArrayList<>(numberOfVariables);
        List<Double> upperBound = new ArrayList<>(numberOfVariables);

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerBound.add(0.0);
            upperBound.add(1.0);
        }
        setBounds(lowerBound, upperBound);
    }

    public ZDT1() {
        this(30);
    }

    @Override
    public void evaluate(ContinuousIndividual individual) {
        double[] f = new double[getNumberOfObjectives()];
        f[0] = individual.getVariableValue(0);
        var g = this.functionG(individual);
        var h = this.functionH(f[0], g);
        f[1] = h * g;
        individual.setObjectiveValue(0, f[0]);
        individual.setObjectiveValue(1, f[1]);
    }

    protected double functionG(ContinuousIndividual individual) {
        double g = 0.0;
        for (int i = 1; i < individual.getNumberOfVariables(); i++) {
            g += individual.getVariableValue(i);
        }
        double constant = 9.0 / (individual.getNumberOfVariables() - 1);

        return constant * g + 1.0;
    }

    protected double functionH(double f, double g) {
        return 1.0 - Math.sqrt(f / g);
    }
}