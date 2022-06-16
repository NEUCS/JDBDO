package problem;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/22 10:20
 */
public abstract class AbstractTestProblem<I> implements TestProblem<I> {
    private int numberOfVariables = 0;
    private int numberOfObjectives = 0;
    private int numberOfConstraints = 0;
    private String name = null;

    @Override
    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    @Override
    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    @Override
    public int getNumberOfConstraints() {
        return numberOfConstraints;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    public void setNumberOfObjectives(int numberOfObjectives) {
        this.numberOfObjectives = numberOfObjectives;
    }

    public void setNumberOfConstraints(int numberOfConstraints) {
        this.numberOfConstraints = numberOfConstraints;
    }

    public void setName(String name) {
        this.name = name;
    }
}