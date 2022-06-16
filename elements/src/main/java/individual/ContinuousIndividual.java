package individual;

public interface ContinuousIndividual extends Individual<Double>{
    //setter
    void setLowerBound(int idx, double value);
    void setUpperBound(int idx, double value);

    //getter
    Double getLowerBound(int idx);
    Double getUpperBound(int idx);
}
