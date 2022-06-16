package algorithm;

import java.io.Serializable;

public interface Metaheuristic<T> extends Runnable, Serializable {
    void run();
    T getNonDominatedSolutions();
}
