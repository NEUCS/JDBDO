package problem;

import java.util.LinkedList;
import java.util.List;

public interface BoundaryTestProblem<I> extends TestProblem<I>{
    Double getUpperBound(int idx);
    Double getLowerBound(int idx);
    List<LinkedList<Double>> getLowerAndUpperBounds();
}
