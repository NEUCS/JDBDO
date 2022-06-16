package dmbdops.dbigopt2015.updateByFile;

import individual.ContinuousIndividual;
import problem.AbstractContinuousTestProblem;
import problem.DynamicTestProblem;

public abstract class AbstractDBigOpt2015UpdateDataByKafka extends AbstractContinuousTestProblem
        implements DynamicTestProblem<ContinuousIndividual> {

    private boolean changeFlag = false;


    public AbstractDBigOpt2015UpdateDataByKafka() {
    }

    public void updateProblem(int counter) {
    }

    @Override
    public boolean problemChanged() {
        return changeFlag;
    }

    @Override
    public void resetChangeFlag() {
        changeFlag = false;
    }

    public void problemHasBeenModified(){
        changeFlag = true;
    }
}