package dmops.df;

import individual.ContinuousIndividual;

import java.util.ArrayList;
import java.util.List;

public class DF4 extends DF {
    static int D = 10;

    public DF4() {
        this(D, 2);
    }

    public DF4(Integer numberOfVariables, Integer numberOfObjectives) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(numberOfObjectives);

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-2.0);
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

    double a(double t) {
        return Math.sin(0.5 * Math.PI * t);
    }

    double b(double t) {
        return 1 + Math.abs(Math.cos(0.5 * Math.PI * t));
    }

    double c(double t) {
        return Math.max(Math.abs(a(t)), a(t) + b(t));
    }

    double g(double[] x, double t) {
        double sum = 1;
        for (int i = 1; i < D; ++i) {
            double temp = x[i] - a(t) * x[0] * x[0] / (i * c(t) * c(t));
            sum += temp * temp;
        }
        return sum;
    }

    double H(double t) {
        return 1.5 + a(t);
    }

    double f1(double[] x, double t) {
        return (g(x, t) * Math.pow(Math.abs(x[0] - a(t)), H(t))) + 2 * t;
    }

    double f2(double[] x, double t) {
        return (g(x, t) * Math.pow(Math.abs(x[0] - a(t) - b(t)), H(t))) + 2 * t;
    }

    @Override
    public double getTime() {
        return t;
    }
}
