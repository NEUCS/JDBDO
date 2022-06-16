package Archive.KRArchive.tools;

import individual.ContinuousIndividual;
import population.Population;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/3 16:26
 */
public class Quadruple<I extends ContinuousIndividual>  {
    public String feature;
    public Population<I> population;
    public int iteration;
    public int trial;

    public Quadruple(String feature, Population<I> population, int iteration, int trial){
        this.feature = feature;
        this.population = population;
        this.iteration = iteration;
        this.trial = trial;
    }

    public String getFeature() {
        return feature;
    }

    public Population<I> getPopulation() {
        return population;
    }

    public int getIteration() {
        return iteration;
    }

    public int getTrial() {
        return trial;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public void setPopulation(Population<I> population) {
        this.population = population;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public void setTrial(int trial) {
        this.trial = trial;
    }

    public void updateIteration(){
        this.iteration++;
    }

    public void updateTrial(){
        this.trial++;
    }

    public void resetIteration(){
        this.iteration = 0;
    }

    public void resetTrial(){
        this.trial = 0;
    }
}