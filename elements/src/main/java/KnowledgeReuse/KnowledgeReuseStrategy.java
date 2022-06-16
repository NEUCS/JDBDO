package KnowledgeReuse;

import Archive.KRArchive.KRArchive;
import Archive.KRArchive.tools.Quadruple;
import PerformanceIndicator.HV;
import algorithm.reinitializer.RandomReinitializer;
import individual.ContinuousIndividual;
import net.sf.javaml.distance.fastdtw.dtw.DTW;
import net.sf.javaml.distance.fastdtw.timeseries.TimeSeries;
import net.sf.javaml.distance.fastdtw.timeseries.TimeSeriesPoint;
import population.Population;
import problem.DynamicTestProblem;

import java.util.LinkedList;
import java.util.List;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/4 18:41
 */
public class KnowledgeReuseStrategy<I extends ContinuousIndividual> {
    private int archiveSize;
    private int maxIteration;
    private int minimumTrial;
    private int numberOfDeleted;
    private double minimumSimilarity;
    private KRArchive<I> archive;
    private DynamicTestProblem<I> problem;

    public KnowledgeReuseStrategy(int archiveSize, double minimumSimilarity,
            int numberOfDeleted, DynamicTestProblem<I> problem) {
        this.archiveSize = archiveSize;
        this.minimumSimilarity = minimumSimilarity;
        this.archive = new KRArchive<>(archiveSize);
        this.numberOfDeleted = numberOfDeleted;
        this.problem = problem;
        this.minimumTrial = 2;
        this.maxIteration = 100;
    }

    public KRArchive<I> getArchive() {
        return archive;
    }

    public Population<I> generateNextInitialPopulation(String oldFeature,
            String newFeature, Population<I> oldPopulation) {
        updateArchive(oldFeature, oldPopulation);
        double[] re = getMinDistanceAndIndex(newFeature);
        int minIndex = (int) re[0];
        double minDistance = re[1];
        for (Quadruple<I> q : this.archive.getArchive()) {
            q.updateIteration();
        }
        if (minDistance < minimumSimilarity) {
            new RandomReinitializer<I>(numberOfDeleted, problem).reinitialize(
                    this.archive.getArchive().get(minIndex).getPopulation());
            for (Quadruple<I> q : this.archive.getArchive()) {
                q.updateTrial();
            }
            return this.archive.getArchive().get(minIndex).getPopulation();

        } else {
            return this.archive.getArchive().get(minIndex).getPopulation();
        }

    }

    public double[] getMinDistanceAndIndex(String f) {
        double minDistance = Double.MAX_VALUE;
        double index = 0;
        double[] distanceArray = new double[this.archive.getArchive().size()];
        TimeSeries t1 = new TimeSeries(1);
        t1.addLast(1, new TimeSeriesPoint(new double[]{Double.parseDouble(f)}));
        for (int i = 0; i < this.archive.getArchive().size(); i++) {
            TimeSeries t2 = new TimeSeries(1);
            t2.addLast(1, new TimeSeriesPoint(new double[]{Double.parseDouble(
                    this.archive.getArchive().get(i).getFeature())}));
            distanceArray[i] = new DTW().getWarpDistBetween(t1, t2);
        }
        for (int i = 0; i < distanceArray.length; i++) {
            if (distanceArray[i] < minDistance) {
                minDistance = distanceArray[i];
                index = i;
            }
        }
        return new double[]{index, minDistance};
    }

    public void updateArchive(String feature, Population<I> population) {
        if (this.archive.getArchive().size() == 0) {
            this.archive.addToArchive(feature, population);
        } else {
            int index;
            double minDistance;
            double[] re = getMinDistanceAndIndex(feature);
            index = (int) re[0];
            minDistance = re[1];
            if (minDistance >= this.minimumSimilarity) {
                if (this.archive.getArchive().size() < this.archiveSize) {
                    this.archive.addToArchive(feature, population);
                } else {
                    List<Quadruple<I>> removeList = new LinkedList<>();
                    List<Integer> indexList = new LinkedList<>();
                    for (int i = 0; i < this.archive.getArchive().size(); i++) {
                        if (this.archive.getArchive().get(i).getIteration() >=
                                maxIteration) {
                            if (this.archive.getArchive().get(i).getTrial() <=
                                    minimumTrial) {
                                removeList
                                        .add(this.archive.getArchive().get(i));
                                indexList.add(i);
                            }
                        }
                    }
                    if (removeList.size() == 0) {
                        return;
                    } else {
                        int removeIndex = 0;
                        int minTial = 1000000000;
                        for (int i = 0; i < removeList.size(); i++) {
                            if (removeList.get(i).getTrial() < minTial) {
                                minTial = removeList.get(i).getTrial();
                                removeIndex = i;
                            }
                        }
                        this.archive.getArchive()
                                .set(indexList.get(removeIndex),
                                     new Quadruple<I>(feature, population, 0,
                                                      0));
                    }
                }
            } else {
                Population<ContinuousIndividual> oldPop =
                        (Population<ContinuousIndividual>) this.archive
                                .getArchive().get(index).getPopulation();
                if (new HV((Population<ContinuousIndividual>) population)
                        .computeHV(
                                (Population<ContinuousIndividual>) population) >
                        new HV(oldPop).computeHV(oldPop)) {
                    this.archive.getArchive().get(index)
                            .setPopulation(population);
                }
            }
        }
    }
}