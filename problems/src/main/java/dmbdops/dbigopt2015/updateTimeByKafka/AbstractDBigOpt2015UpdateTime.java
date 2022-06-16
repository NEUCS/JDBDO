package dmbdops.dbigopt2015.updateTimeByKafka;

import individual.ContinuousIndividual;
import problem.AbstractContinuousTestProblem;
import problem.DynamicTestProblem;

public abstract class AbstractDBigOpt2015UpdateTime extends AbstractContinuousTestProblem
        implements DynamicTestProblem<ContinuousIndividual> {
    protected double time;
    private boolean changeFlag = false;


    public AbstractDBigOpt2015UpdateTime() {
    }

    public void updateProblem(int counter) {
    }

    public void setTime(double time){
        this.time = time;
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