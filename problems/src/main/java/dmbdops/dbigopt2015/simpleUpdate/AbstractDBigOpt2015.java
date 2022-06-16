package dmbdops.dbigopt2015.simpleUpdate;

import individual.ContinuousIndividual;
import problem.AbstractContinuousTestProblem;
import problem.DynamicTestProblem;

public abstract class AbstractDBigOpt2015 extends AbstractContinuousTestProblem
        implements DynamicTestProblem<ContinuousIndividual> {
    protected double time;
    private boolean changeFlag = false;
    private final int tauT = 60;
    private final int nT = 10;

    public AbstractDBigOpt2015() {
    }

    public void updateProblem(int counter) {
        time = (1.0d / (double) nT) * Math.floor(counter / (double) tauT);
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