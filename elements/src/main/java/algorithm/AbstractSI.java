package algorithm;

import individual.Individual;
import population.Population;
import problem.TestProblem;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/28 16:54
 */
public abstract class AbstractSI<I extends Individual<?>, P> implements Metaheuristic<P> {
    protected int swarmSize;
    protected Population<I> swarm;
    protected TestProblem<I> testProblem;

    //setter
    public void setSwarmSize(int swarmSize) {
        this.swarmSize = swarmSize;
    }

    public void setSwarm(Population<I> swarm) {
        this.swarm = swarm;
    }

    public void setTestProblem(TestProblem<I> testProblem) {
        this.testProblem = testProblem;
    }

    //getter
    public int getSwarmSize() {
        return swarmSize;
    }

    public Population<I> getSwarm() {
        return swarm;
    }

    public TestProblem<I> getTestProblem() {
        return testProblem;
    }

    @Override
    public abstract P getNonDominatedSolutions();

    //constructor
    public AbstractSI(TestProblem<I> testProblem) {
        setTestProblem(testProblem);
    }

    // abstract methods
    protected abstract void initialize();

    protected abstract void update();

    protected abstract boolean isStoppingCriterionMet();

    protected abstract void evaluatePopulation(
            Population<I> swarm);
}