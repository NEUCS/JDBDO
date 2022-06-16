package mops.uf;

import individual.ContinuousIndividual;
import problem.AbstractContinuousTestProblem;
import java.util.ArrayList;
import java.util.List;

public class UF1 extends AbstractContinuousTestProblem {
    public UF1() {
        this(30);
    }

    public UF1(int numberOfVariables) {
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
        double sum1 = 0.0;
        double sum2 = 0.0;
        double[] x = new double[getNumberOfVariables()];

        for (int i = 0; i < individual.getNumberOfVariables(); i++) {
            x[i] = individual.getVariableValue(i);
        }

        for (int i = 1; i < getNumberOfVariables(); i++) {
            y = x[i] - Math.sin(6.0 * Math.PI * x[0] + (i + 1) * Math.PI /
                    getNumberOfVariables());
            if ((i + 1) % 2 == 0) {
                sum2 += Math.pow(y, 2);
                count2++;
            } else {
                sum1 += Math.pow(y, 2);
                count1++;
            }
        }

        individual.setObjectiveValue(0, x[0] + 2.0 * sum1 / (double) count1);
        individual.setObjectiveValue(1, 1.0 - Math.sqrt(x[0]) +
                2.0 * sum2 / (double) count2);
    }
}