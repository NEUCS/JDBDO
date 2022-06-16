package PerformanceIndicator.tools;

import individual.ContinuousIndividual;
import population.Population;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/5 14:32
 */
public class PF {
    protected List<double[]> objectiveValueArray;
    protected int numberOfObjectivePoint;

    public PF() {
        objectiveValueArray = new ArrayList<>();
        numberOfObjectivePoint = 0;
    }

    public PF(Population<ContinuousIndividual> population) {
        numberOfObjectivePoint = population.getPopulation().size();
        objectiveValueArray = new ArrayList<>();
        for (ContinuousIndividual individual : population.getPopulation()) {
            objectiveValueArray.add(individual.copy().getAllObjectivesValue());
        }
    }

    public PF(String path, String regex) {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(path)));
            this.objectiveValueArray = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] readValue = line.split(regex);
                this.objectiveValueArray
                        .add(new double[]{Double.parseDouble(readValue[0]),
                                Double.parseDouble(readValue[1])});
                line = bufferedReader.readLine();
            }
            this.numberOfObjectivePoint = this.objectiveValueArray.size();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public PF(PF pf) {
        this.numberOfObjectivePoint = pf.getNumberOfObjectivePoint();
        this.objectiveValueArray = new ArrayList<>(numberOfObjectivePoint);
        for (int i = 0; i < numberOfObjectivePoint; i++) {
            this.objectiveValueArray.set(i, new double[]{
                    pf.getObjectiveValueArray().get(i)[0],
                    pf.getObjectiveValueArray().get(i)[1]});
        }

    }

    public double[] getObjectiveValues(int index) {
        if (index >= 0 && index < numberOfObjectivePoint) {
            return this.objectiveValueArray.get(index);
        } else {
            throw new RuntimeException("Index is out of bounds!");
        }
    }

    public void setObjectiveValues(int index, double[] objectives) {
        if (index >= 0 && index < numberOfObjectivePoint &&
                objectives.length != 0) {
            this.objectiveValueArray.set(index, objectives);
        } else {
            throw new RuntimeException(
                    "Index is out of bounds! OR Objective array is empty!");
        }
    }

    public int getNumberOfObjectivePoint() {
        return numberOfObjectivePoint;
    }

    public List<double[]> getObjectiveValueArray() {
        return objectiveValueArray;
    }

    public void setNumberOfObjectivePoint(int numberOfObjectivePoint) {
        this.numberOfObjectivePoint = numberOfObjectivePoint;
    }

    public void setObjectiveValueArray(List<double[]> objectiveValueArray) {
        this.objectiveValueArray = objectiveValueArray;
    }
}