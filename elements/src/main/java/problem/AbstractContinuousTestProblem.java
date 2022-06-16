package problem;

import individual.ContinuousIndividual;
import individual.ContinuousIndividualInitializer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/22 10:24
 */
public abstract class AbstractContinuousTestProblem
        extends AbstractTestProblem<ContinuousIndividual>
        implements BoundaryTestProblem<ContinuousIndividual> {
    protected List<LinkedList<Double>> bounds = new ArrayList<>();

    @Override
    public Double getUpperBound(int idx) {
        return getLowerAndUpperBounds().get(idx).get(1);
    }

    @Override
    public Double getLowerBound(int idx) {
        return getLowerAndUpperBounds().get(idx).get(0);
    }

    @Override
    public List<LinkedList<Double>> getLowerAndUpperBounds() {
        return bounds;
    }

    public void setBounds(List<Double> lowerBounds, List<Double> upperBounds) {
        if (lowerBounds == null) {
            throw new RuntimeException("lower bounds list is empty!");
        } else if (upperBounds == null) {
            throw new RuntimeException("upper bounds list is empty!");
        } else if (lowerBounds.size() != upperBounds.size()) {
            throw new RuntimeException("two list have different size!");
        } else {
            for (int i = 0; i < lowerBounds.size(); i++) {
                LinkedList<Double> boundPair = new LinkedList<>();
                boundPair.add(lowerBounds.get(i));
                boundPair.add(upperBounds.get(i));
                bounds.add(boundPair);
            }
        }
    }

    @Override
    public ContinuousIndividual initIndividual(int i) {
        return new ContinuousIndividualInitializer(bounds,
                                                   getNumberOfObjectives(),
                                                   getNumberOfConstraints(),i);
    }
}