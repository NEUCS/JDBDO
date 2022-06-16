package dmbdops.bddf;

import individual.ContinuousIndividual;
import problem.AbstractContinuousTestProblem;
import problem.DynamicTestProblem;


public abstract class BDDF extends AbstractContinuousTestProblem
        implements DynamicTestProblem<ContinuousIndividual> {
    protected double t;
    private boolean changeFlag = false;

    public BDDF() {
        t = 0;
    }

    @Override
    public void updateProblem(int counter) {
    }

    public void setTime(double time) {
        this.t = time;
        changeFlag = true;
    }

    @Override
    public boolean problemChanged() {
        return changeFlag;
    }

    @Override
    public void resetChangeFlag() {
        changeFlag = false;
    }
}