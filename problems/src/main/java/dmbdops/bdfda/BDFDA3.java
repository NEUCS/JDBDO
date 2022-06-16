package dmbdops.bdfda;
import individual.ContinuousIndividual;

import java.util.ArrayList;
import java.util.List;

public class BDFDA3 extends BDFDA {
    public BDFDA3() {
        this(30, 2);
    }

    public BDFDA3(Integer numberOfVariables, Integer numberOfObjectives) {
        super();
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
    public double getTime() {
        return time;
    }

    @Override
    public void evaluate(ContinuousIndividual individual) {
        double[] f = new double[getNumberOfObjectives()];
        f[0] = this.evalF(individual);
        double g = this.evalG(individual);
        double h = this.evalH(f[0], g);
        f[1] = g * h;
        individual.setObjectiveValue(0, f[0]);
        individual.setObjectiveValue(1, f[1]);
    }

    private double evalF(ContinuousIndividual individual) {
        double f = 0.0d;
        double aux = 2.0d * Math.sin(0.5d * Math.PI * time);
        double Ft = Math.pow(10.0d, aux);
        f += Math.pow(individual.getVariableValue(0), Ft);
        return f;
    }

    private double evalG(ContinuousIndividual individual) {

        double g = 0.0d;
        double Gt = Math.abs(Math.sin(0.5d * Math.PI * time));
        for (int i = 1; i < individual.getNumberOfVariables(); i++) {
            g += Math.pow((individual.getVariableValue(i) - Gt), 2.0);
        }
        g = g + 1.0 + Gt;
        return g;
    }

    private double evalH(double f, double g) {
        return 1.0d - Math.sqrt(f / g);
    }
}