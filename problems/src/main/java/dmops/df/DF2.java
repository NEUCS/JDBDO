package dmops.df;

import individual.ContinuousIndividual;

import java.util.ArrayList;
import java.util.List;

public class DF2 extends DF {
    static int D = 10;

    public DF2() {
        this(D, 2);
    }

    public DF2(Integer numberOfVariables, Integer numberOfObjectives) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(numberOfObjectives);
        setName("DF2");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0.0);
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
        return Math.abs(Math.sin(0.5 * Math.PI * t));
    }

    int r(double t) {
        return (int) Math.floor((D - 1) * G(t));
    }

    double f1(double[] x, double t) {
        return x[r(t)];
    }

    double g(double[] x, double t) {
        double sum = 1;
        for (int i = 0; i < D; ++i) {
            if (i != r(t)) {
                double temp = x[i] - G(t);
                sum += temp * temp;
            }
        }
        return sum;
    }

    double f2(double[] x, double t) {
        double temp = g(x, t);
        return temp * (1 - Math.sqrt(x[r(t)] / temp));
    }

    @Override
    public double getTime() {
        return t;
    }
}
