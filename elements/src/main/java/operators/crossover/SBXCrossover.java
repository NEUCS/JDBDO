package operators.crossover;

import individual.ContinuousIndividual;
import population.Population;
import random.MRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/22 16:03
 */
public class SBXCrossover implements Crossover<ContinuousIndividual> {
    private static final double EPS = 1.0e-14;
    private double distributionIndex;
    private double crossoverProbability;

    public SBXCrossover(double crossoverProbability, double distributionIndex) {
        this.crossoverProbability = crossoverProbability;
        this.distributionIndex = distributionIndex;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }

    @Override
    public Population<ContinuousIndividual> execute(
            Population<ContinuousIndividual> population) {
        if (population.getPopulation() == null) {
            throw new RuntimeException("Population is null!");
        } else if (population.getPopulation().size() != 2) {
            throw new RuntimeException("Parents number is not equal to 2!");
        }
        return crossover(crossoverProbability,
                         population.getPopulation().get(0),
                         population.getPopulation().get(1));
    }

    public Population<ContinuousIndividual> crossover(
            double crossoverProbability, ContinuousIndividual parent1,
            ContinuousIndividual parent2) {
        List<ContinuousIndividual> offspring = new ArrayList<>(2);

        offspring.add((ContinuousIndividual) parent1.copy());
        offspring.add((ContinuousIndividual) parent2.copy());

        int j;
        double rand;
        double y1, y2;
        double lowerBound, upperBound;
        double c1, c2;
        double alpha, beta, betaq;
        double valueX1, valueX2;

        if (randomValue() <= crossoverProbability) {
            for (j = 0; j < parent1.getNumberOfVariables(); j++) {
                valueX1 = parent1.getVariableValue(j);
                valueX2 = parent2.getVariableValue(j);
                if (randomValue() <= 0.5) {
                    if (Math.abs(valueX1 - valueX2) > EPS) {
                        if (valueX1 < valueX2) {
                            y1 = valueX1;
                            y2 = valueX2;
                        } else {
                            y1 = valueX2;
                            y2 = valueX1;
                        }
                        lowerBound = parent1.getLowerBound(j);
                        upperBound = parent1.getUpperBound(j);

                        rand = randomValue();
                        beta = 1.0 + (2.0 * (y1 - lowerBound) / (y2 - y1));
                        alpha = 2.0 -
                                Math.pow(beta, -(distributionIndex + 1.0));
                        if (rand <= (1.0 / alpha)) {
                            betaq = Math.pow(rand * alpha,
                                             (1.0 / (distributionIndex + 1.0)));
                        } else {
                            betaq = Math.pow(1.0 / (2.0 - rand * alpha),
                                             1.0 / (distributionIndex + 1.0));
                        }
                        c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));

                        beta = 1.0 + (2.0 * (upperBound - y2) / (y2 - y1));
                        alpha = 2.0 -
                                Math.pow(beta, -(distributionIndex + 1.0));

                        if (rand <= (1.0 / alpha)) {
                            betaq = Math.pow((rand * alpha),
                                             (1.0 / (distributionIndex + 1.0)));
                        } else {
                            betaq = Math.pow(1.0 / (2.0 - rand * alpha),
                                             1.0 / (distributionIndex + 1.0));
                        }
                        c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

                        c1 = checkAndResetVariable(c1, lowerBound, upperBound);
                        c2 = checkAndResetVariable(c2, lowerBound, upperBound);

                        if (randomValue() <= 0.5) {
                            offspring.get(0).setVariableValue(j, c2);
                            offspring.get(1).setVariableValue(j, c1);
                        } else {
                            offspring.get(0).setVariableValue(j, c1);
                            offspring.get(1).setVariableValue(j, c2);
                        }
                    } else {
                        offspring.get(0).setVariableValue(j, valueX1);
                        offspring.get(1).setVariableValue(j, valueX2);
                    }
                } else {
                    offspring.get(0).setVariableValue(j, valueX2);
                    offspring.get(1).setVariableValue(j, valueX1);
                }
            }
        }
        Population<ContinuousIndividual> newPopulation = new Population<>();
        newPopulation.setPopulation(offspring);
        return newPopulation;
    }

    public double checkAndResetVariable(double value, double lowerBound,
            double upperBound) {
        if (value < lowerBound) {
            return lowerBound;
        } else
            return Math.min(value, upperBound);
    }

    public double randomValue() {
       return MRandom.getInstance().nextDouble();
    }
}