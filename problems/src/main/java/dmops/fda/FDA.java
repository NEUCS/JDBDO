package dmops.fda;

import individual.ContinuousIndividual;
import problem.AbstractContinuousTestProblem;
import problem.DynamicTestProblem;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/1/17 12:12
 */
public abstract class FDA extends AbstractContinuousTestProblem implements
        DynamicTestProblem<ContinuousIndividual> {
    protected double time;
    private boolean changeFlag = false;
    private final int tauT = 5;
    private final int nT = 10;

    public FDA(){
    }

    @Override
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