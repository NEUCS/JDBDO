package Archive;

import Archive.KRArchive.tools.Quadruple;
import individual.ContinuousIndividual;
import individual.Individual;
import population.Population;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/3 14:43
 */
public abstract class AbstractArchive<I extends ContinuousIndividual> implements Archive{
    protected List<Quadruple<I>> archive;
    protected int archiveSize;
    public AbstractArchive(int archiveSize){
        this.archiveSize = archiveSize;
        initializeArchive();
    }

    public void initializeArchive(){
        this.archive = new LinkedList<>();
    }

    public List<Quadruple<I>> getArchive() {
        return archive;
    }
}