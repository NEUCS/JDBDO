package operators.crossover;

import individual.ContinuousIndividual;
import individual.ContinuousIndividualInitializer;
import population.Population;
import random.MRandom;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/2 13:10
 */
public class DECrossover implements Crossover<ContinuousIndividual> {
    private double CR;
    private double F;
    private int numberOfDifferenceVector;
    private ContinuousIndividual current;

    public DECrossover(double CR, double F) {
        this.CR = CR;
        this.F = F;
        this.numberOfDifferenceVector = 1;
        this.current = null;
    }

    public void setCurrent(ContinuousIndividual current) {
        this.current = current;
    }

    public void setCR(double CR) {
        this.CR = CR;
    }

    public void setF(double f) {
        F = f;
    }

    public void setNumberOfDifferenceVector(int numberOfDifferenceVector) {
        this.numberOfDifferenceVector = numberOfDifferenceVector;
    }

    public double getCR() {
        return CR;
    }

    public double getF() {
        return F;
    }

    public int getNumberOfDifferenceVector() {
        return numberOfDifferenceVector;
    }

    @Override
    public int getNumberOfParents() {
        return 1 + numberOfDifferenceVector * 2;
    }

    @Override
    public Population<ContinuousIndividual> execute(
            Population<ContinuousIndividual> population) {
        ContinuousIndividual child = (ContinuousIndividual) current.copy();
        var numberOfVariables =
                population.getIndividual(0).getNumberOfVariables();
        var randomValue =
                MRandom.getInstance().nextInt(0, numberOfVariables - 1);
        Double[][] parents =
                new Double[getNumberOfParents()][numberOfVariables];
        setParents(parents, population, getNumberOfParents());
        for (int i = 0; i < numberOfVariables; i++) {
            if (MRandom.getInstance().nextDouble(0, 1) < CR ||
                    i == randomValue) {
                var value = mutate(parents, i);
                child.setVariableValue(i, value);
            }
        }
        for (int i = 0; i < child.getNumberOfVariables(); i++) {
            var lb = population.getIndividual(0).getLowerBound(i);
            var ub = population.getIndividual(0).getUpperBound(i);
            if (child.getVariableValue(i) < lb) {
                child.setVariableValue(i, lb);
            }
            if (child.getVariableValue(i) > ub) {
                child.setVariableValue(i, ub);
            }
        }
        Population<ContinuousIndividual> result = new Population<>();
        result.add(child);
        return result;
    }

    public void setParents(Double[][] parents,
            Population<ContinuousIndividual> population, int numberOfParents) {
        for (int i = 0; i < numberOfParents; i++) {
            population.getIndividual(i).getAllVariables().toArray(parents[i]);
        }
    }

    public double mutate(Double[][] parents, int index) {
        return parents[2][index] + F * (parents[0][index] - parents[1][index]);
    }
}