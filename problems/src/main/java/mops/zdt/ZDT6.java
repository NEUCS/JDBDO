package mops.zdt;

import individual.ContinuousIndividual;
import individual.Individual;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/6 13:38
 */
public class ZDT6 extends ZDT1 {
    public ZDT6() {
        this(10);
    }

    public ZDT6(Integer integer) {
        super(integer);
    }

    @Override
    public void evaluate(ContinuousIndividual individual) {
        double[] f = new double[getNumberOfObjectives()];

        double x1;
        x1 = individual.getVariableValue(0);
        f[0] = 1 - Math.exp(-4 * x1) * Math.pow(Math.sin(6 * Math.PI * x1), 6);
        double g = this.functionG(individual);
        double h = this.functionH(f[0], g);
        f[1] = h * g;

        individual.setObjectiveValue(0, f[0]);
        individual.setObjectiveValue(1, f[1]);
    }

    @Override
    protected double functionG(ContinuousIndividual individual) {
        double g = 0.0;
        for (int i = 1; i < individual.getNumberOfVariables(); i++) {
            g += individual.getVariableValue(i);
        }
        return Math.pow(g / (individual.getNumberOfVariables() - 1), 0.25) *
                9.0 + 1.0;
    }

    @Override
    protected double functionH(double f, double g) {
        return 1.0 - Math.pow((f / g), 2);
    }
}