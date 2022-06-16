package individual;

import random.MRandom;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/22 10:52
 */
public class ContinuousIndividualInitializer extends AbstractIndividual<Double>
        implements ContinuousIndividual {
    protected List<LinkedList<Double>> bounds;

    public ContinuousIndividualInitializer(List<LinkedList<Double>> bounds,
            int numberOfObjective, int numberOfConstraints, int seed) {
        super(bounds.size(), numberOfObjective, numberOfConstraints);
        this.bounds = bounds;
        for (int i = 0; i < bounds.size(); i++) {
            setVariableValue(i, MRandom.getInstance()
                    .nextDouble(bounds.get(i).get(0), bounds.get(i).get(1)));
        }
    }

    public ContinuousIndividualInitializer(
            ContinuousIndividualInitializer initializer) {
        super(initializer.getNumberOfVariables(),
              initializer.getNumberOfObjectives(),
              initializer.getNumberOfConstraints());

        for (int i = 0; i < initializer.getNumberOfObjectives(); i++) {
            setObjectiveValue(i, initializer.getObjectiveValue(i));
        }

        for (int i = 0; i < initializer.getNumberOfVariables(); i++) {
            setVariableValue(i, initializer.getVariableValue(i));
        }

        for (int i = 0; i < initializer.getNumberOfConstraints(); i++) {
            setConstraint(i, initializer.getConstraint(i));
        }

        this.bounds = initializer.bounds;
        attributes = new HashMap<Object, Object>(initializer.attributes);
    }

    @Override
    public void setLowerBound(int idx, double value) {
        bounds.get(idx).set(0, value);
    }

    @Override
    public void setUpperBound(int idx, double value) {
        bounds.get(idx).set(1, value);
    }

    @Override
    public Double getLowerBound(int idx) {
        return this.bounds.get(idx).get(0);
    }

    @Override
    public Double getUpperBound(int idx) {
        return this.bounds.get(idx).get(1);
    }

    @Override
    public Individual<Double> copy() {
        return new ContinuousIndividualInitializer(this);
    }
}