package individual;

import java.util.*;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/21 11:22
 */
public abstract class AbstractIndividual<T> implements Individual<T> {
    private double[] objectives;
    private List<T> variables;
    private double[] constraints;
    protected Map<Object, Object> attributes;

    protected AbstractIndividual(int numberOfVariables, int numberOfObjectives,
            int numberOfConstraints) {
        //initialize parameters
        objectives = new double[numberOfObjectives];
        variables = new ArrayList<>(numberOfVariables);
        constraints = new double[numberOfConstraints];
        attributes = new HashMap<Object, Object>();

        for (int i = 0; i < numberOfObjectives; i++) {
            objectives[i] = 0.0;
        }

        for (int i = 0; i < numberOfVariables; i++) {
            variables.add(i, null);
        }

        for (int i = 0; i < numberOfConstraints; i++) {
            constraints[i] = 0.0;
        }
    }

    protected AbstractIndividual(int numberOfVariables, int numberOfObjectives){
        this(numberOfVariables, numberOfObjectives, 0);
    }


    @Override
    public void setObjectiveValue(int idx, double value) {
        objectives[idx] = value;
    }

    @Override
    public void setVariableValue(int idx, T value) {
        variables.set(idx, value);
    }


    @Override
    public void setConstraint(int idx, double value) {
        constraints[idx] = value;
    }

    @Override
    public void setAttribute(Object id, Object value) {
        attributes.put(id, value);
    }

    @Override
    public double getObjectiveValue(int idx) {
        return objectives[idx];
    }

    @Override
    public double[] getAllObjectivesValue() {
        return objectives;
    }

    @Override
    public T getVariableValue(int idx) {
        return variables.get(idx);
    }

    @Override
    public List<T> getAllVariables() {
        return variables;
    }

    @Override
    public void setAllVariables(List<T> list) {
        this.variables = list;
    }

    @Override
    public double getConstraint(int idx) {
        return constraints[idx];
    }

    @Override
    public double[] getConstraints() {
        return constraints;
    }

    @Override
    public int getNumberOfVariables() {
        return variables.size();
    }

    @Override
    public int getNumberOfObjectives() {
        return objectives.length;
    }

    @Override
    public int getNumberOfConstraints() {
        return constraints.length;
    }

    @Override
    public Object getAttribute(Object id) {
        return attributes.get(id);
    }

    @Override
    public Map<Object, Object> getAttributes() {
        return attributes;
    }

    @Override
    public boolean hasAttribute(Object id) {
        return attributes.containsKey(id);
    }

    @Override
    public String toString() {
        return "AbstractIndividual{" + "objectives=" +
                Arrays.toString(objectives) + ", variables=" + variables +
                ", constraints=" + Arrays.toString(constraints) +
                ", attributes=" + attributes + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AbstractIndividual<?> that = (AbstractIndividual<?>) o;
        return Arrays.equals(objectives, that.objectives) &&
                Objects.equals(variables, that.variables) &&
                Arrays.equals(constraints, that.constraints) &&
                Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(variables, attributes);
        result = 31 * result + Arrays.hashCode(objectives);
        result = 31 * result + Arrays.hashCode(constraints);
        return result;
    }
}