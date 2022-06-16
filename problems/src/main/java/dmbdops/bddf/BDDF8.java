package dmbdops.bddf;

import individual.ContinuousIndividual;

import java.util.ArrayList;
import java.util.List;

public class BDDF8 extends BDDF {
    static int D = 10;

    public BDDF8() {
        this(D, 2);
    }

    public BDDF8(Integer numberOfVariables, Integer numberOfObjectives) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(numberOfObjectives);

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

    double G(double t) {
        return Math.sin(0.5 * Math.PI * t);
    }

    double a(double t) {
        return 2.25 + 2 * Math.cos(2 * Math.PI * t);
    }

    double g(double[] x, double t) {
        double sum = 1;
        for (int i = 1; i < D; ++i) {
            double Gt = G(t);
            double temp = x[i] -
                    Gt * Math.sin(4 * Math.PI * x[0]) / (1 + Math.abs(Gt));
            sum += temp * temp;
        }
        return sum;
    }

    double f1(double[] x, double t) {
        return (g(x, t) * (x[0] + 0.1 * Math.sin(3 * Math.PI * x[0]))) + 4 * t;
    }

    double f2(double[] x, double t) {
        return (g(x, t) *
                Math.pow(1 - x[0] + 0.1 * Math.sin(3 * Math.PI * x[0]), a(t))) +
                4 * t;
    }

    @Override
    public double getTime() {
        return t;
    }
}
