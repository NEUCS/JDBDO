package dmbdops.bddf;

import individual.ContinuousIndividual;

import java.util.ArrayList;
import java.util.List;

public class BDDF3 extends BDDF {
    static int D = 10;

    public BDDF3() {
        this(D, 2);
    }

    public BDDF3(Integer numberOfVariables, Integer numberOfObjectives) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(numberOfObjectives);

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        lowerLimit.add(0.0);
        upperLimit.add(1.0);
        for (int i = 1; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-1.0);
            upperLimit.add(2.0);
        }

        setBounds(lowerLimit, upperLimit);
    }

    @Override
    public void evaluate(ContinuousIndividual individual) {
        int numberOfVariables = getNumberOfVariables();
        int numberOfObjectives = getNumberOfObjectives();

        double[] f = new double[numberOfObjectives];
        double[] x = new double[numberOfVariables];

        double[] values = new double[D];
        for (int z = 0; z < D; z++) {
            values[z] = individual.getVariableValue(z);
        }
        f[0] = f1(values, t);
        f[1] = f2(values, t);

        individual.setObjectiveValue(0, f[0]);
        individual.setObjectiveValue(1, f[1]);
    }

    double f1(double[] x, double t) {
        return x[0];
    }

    double G(double t) {
        return Math.sin(0.5 * Math.PI * t);
    }

    double H(double t) {
        return 1.5 + G(t);
    }

    double g(double[] x, double t) {
        double sum = 1;
        for (int i = 1; i < D; ++i) {
            double temp = x[i] - G(t) - Math.pow(x[0], H(t));
            sum += temp * temp;
        }
        return sum;
    }

    double f2(double[] x, double t) {
        double temp = g(x, t);
        return temp * (1 - Math.pow(x[0] / temp, H(t)));
    }

    @Override
    public double getTime() {
        return t;
    }
}
