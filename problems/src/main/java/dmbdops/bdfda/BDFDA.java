package dmbdops.bdfda;

import individual.ContinuousIndividual;
import problem.AbstractContinuousTestProblem;
import problem.DynamicTestProblem;

public abstract class BDFDA extends AbstractContinuousTestProblem implements
        DynamicTestProblem<ContinuousIndividual> {
    protected double time;
    private boolean changeFlag = false;


    public BDFDA(){
        time = 0;
    }

    @Override
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