package mops.zdt;

import individual.ContinuousIndividual;

import java.util.ArrayList;
import java.util.List;


public class ZDT4 extends ZDT1 {
    public ZDT4(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(2);

        List<Double> lowerBound = new ArrayList<>(numberOfVariables);
        List<Double> upperBound = new ArrayList<>(numberOfVariables);

        lowerBound.add(0.0);
        upperBound.add(1.0);
        for (int i = 1; i < getNumberOfVariables(); i++) {
            lowerBound.add(-5.0);
            upperBound.add(5.0);
        }
        setBounds(lowerBound, upperBound);
    }

    public ZDT4() {
        this(10);
    }

    @Override
    protected double functionG(ContinuousIndividual individual) {
        double g = 0.0;
        for (int i = 1; i < individual.getNumberOfVariables(); i++) {
            g += Math.pow(individual.getVariableValue(i), 2.0) + -10.0 *
                    Math.cos(4.0 * Math.PI * individual.getVariableValue(i));
        }
        return g + 1.0 + 10.0 * (individual.getNumberOfVariables() - 1);
    }

    @Override
    protected double functionH(double f, double g) {
        return 1.0 - Math.sqrt(f / g);
    }
}