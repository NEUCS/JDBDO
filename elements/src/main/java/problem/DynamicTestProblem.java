package problem;

import individual.Individual;

public interface DynamicTestProblem<I extends Individual<?>> extends TestProblem<I> {
    void updateProblem(int counter);
    boolean problemChanged();
    void resetChangeFlag();
    double getTime ();
}
