package dmops.df;

import individual.ContinuousIndividual;
import problem.AbstractContinuousTestProblem;
import problem.DynamicTestProblem;

public abstract class DF extends AbstractContinuousTestProblem
        implements DynamicTestProblem<ContinuousIndividual> {
    protected double t;
    private boolean changeFlag = false;
    private final int tauT = 30;
    private final int nT = 10;

    public DF() {
    }

    @Override
    public void updateProblem(int counter) {
        t = (1.0d / (double) nT) * Math.floor(counter / (double) tauT);
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