package problem;

import java.io.Serializable;

public interface TestProblem<I> extends Serializable {
    int getNumberOfVariables();
    int getNumberOfObjectives();
    int getNumberOfConstraints();
    String getName();
    void evaluate(I individual);
    I initIndividual(int i);
}
