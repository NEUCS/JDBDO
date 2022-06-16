package dmops.fda;

import individual.ContinuousIndividual;
import java.util.ArrayList;
import java.util.List;

public class FDA2 extends FDA{
    public FDA2() {
        this(31, 2);
    }

    public FDA2(Integer numberOfVariables, Integer numberOfObjectives) {
        super();
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(numberOfObjectives);
        setName("FDA2");

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
    public void evaluate(ContinuousIndividual solution) {
        double[] f = new double[getNumberOfObjectives()];
        f[0] = solution.getVariableValue(0);
        double g = this.evalG(solution, 1,
                              (solution.getNumberOfVariables() / 2) + 1);
        double h = this.evalH(f[0], g);
        f[1] = g * h; // 1-Math.sqrt(f[0]);
        solution.setObjectiveValue(0, f[0]);
        solution.setObjectiveValue(1, f[1]);
    }

    private double evalG(ContinuousIndividual solution, int limitInf, int limitSup) {

        double g = 0.0;
        for (int i = limitInf; i < limitSup; i++) {
            g += Math.pow(solution.getVariableValue(i), 2.0);
        }
        for (int i = limitSup; i < solution.getNumberOfVariables(); i++) {
            g += Math.pow((solution.getVariableValue(i) + 1.0), 2.0);
        }
        g = g + 1.0;
        return g;
    }

    private double evalH(double f, double g) {
        double HT = 0.2 + 4.8 * Math.pow(time, 2.0);
        return 1.0 - Math.pow((f / g), HT);
    }

    @Override
    public double getTime() {
        return time;
    }
}
