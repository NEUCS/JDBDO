package dmops.df;

import individual.ContinuousIndividual;

import java.util.ArrayList;
import java.util.List;

public class DF9 extends DF {
    static int D = 10;

    public DF9() {
        this(D, 2);
    }

    public DF9(Integer numberOfVariables, Integer numberOfObjectives) {
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

    double N(double t) {
        return 1 + Math.floor(10 * Math.abs(Math.sin(0.5 * Math.PI * t)));
    }

    double g(double[] x, double t) {
        double sum = 1;
        for (int i = 1; i < D; ++i) {
            double temp = x[i] - Math.cos(4 * t + x[0] + x[i - 1]);
            sum += temp * temp;
        }
        return sum;
    }

    double f1(double[] x, double t) {
        return (g(x, t) * (x[0] + Math.max(0, (1.0 / (2 * N(t)) + 0.1) *
                Math.sin(2 * N(t) * Math.PI * x[0])))) + 2 * t;
    }

    double f2(double[] x, double t) {
        return (g(x, t) * (1 - x[0] + Math.max(0, (1.0 / (2 * N(t)) + 0.1) *
                Math.sin(2 * N(t) * Math.PI * x[0])))) + 2 * t;
    }

    @Override
    public double getTime() {
        return t;
    }
}
