package mops.uf;

import individual.ContinuousIndividual;
import problem.AbstractContinuousTestProblem;
import java.util.ArrayList;
import java.util.List;


public class UF6 extends AbstractContinuousTestProblem {
    int N = 2;
    double eps = 0.1;

    public UF6() {
        this(30);
    }

    public UF6(int numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(2);
        setNumberOfConstraints(0);

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
        int count1 = 0;
        int count2 = 0;
        double y;
        double p;
        double h;
        double sum1 = 0.0;
        double sum2 = 0.0;
        double p1 = 1.0;
        double p2 = 1.0;
        double[] x = new double[getNumberOfVariables()];

        for (int i = 0; i < individual.getNumberOfVariables(); i++) {
            x[i] = individual.getVariableValue(i);
        }

        for (int i = 1; i < getNumberOfVariables(); i++) {
            y = x[i] - Math.sin(6.0 * Math.PI * x[0] + (i + 1) * Math.PI /
                    getNumberOfVariables());
            p = Math.cos(20.0 * y * Math.PI / Math.sqrt((i + 1)));
            if ((i + 1) % 2 == 0) {
                sum2 += Math.pow(y, 2);
                p2 *= p;
                count2++;
            } else {
                sum1 += Math.pow(y, 2);
                p1 *= p;
                count1++;
            }
        }
        h = 2.0 * (0.5 / N + eps) * Math.sin(2.0 * N * Math.PI * x[0]);
        if (h < 0.0) {
            h = 0.0;
        }

        individual.setObjectiveValue(0, x[0] + h +
                2.0 * (4.0 * sum1 - 2.0 * p1 + 2.0) / (double) count1);
        individual.setObjectiveValue(1, 1.0 - x[0] + h +
                2.0 * (4.0 * sum2 - 2.0 * p2 + 2.0) / (double) count2);
    }
}