package algorithm;

import problem.DynamicTestProblem;

public interface DynamicMetaheuristic<P> extends Metaheuristic<P> {
    DynamicTestProblem<?> getDynamicTestProblem();
    void restart();
}
