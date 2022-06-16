package individual;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Individual<T> extends Serializable {
    //setter
    void setObjectiveValue(int idx, double value);

    void setVariableValue(int idx, T value);

    void setConstraint(int idx, double value);

    void setAttribute(Object id, Object value);

    //getter
    double getObjectiveValue(int idx);

    double[] getAllObjectivesValue();

    T getVariableValue(int idx);

    List<T> getAllVariables();

    void setAllVariables(List<T> list);

    double getConstraint(int idx);

    double[] getConstraints();

    int getNumberOfVariables();

    int getNumberOfObjectives();

    int getNumberOfConstraints();

    Object getAttribute(Object id);

    Map<Object, Object> getAttributes();


    //methods
    Individual<T> copy();

    boolean hasAttribute(Object id);

}
