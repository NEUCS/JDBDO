package operators.mutation;

import individual.ContinuousIndividual;
import random.MRandom;

import java.util.Random;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/23 12:27
 */
public class PolynomiaMutation implements Mutation<ContinuousIndividual> {
    private static final double DEFAULT_PROBABILITY = 0.01;
    private static final double DEFAULT_DISTRIBUTION_INDEX = 20.0;
    private double distributionIndex;
    private double mutationProbability;

    public PolynomiaMutation(double mutationProbability,
            double distributionIndex) {
        this.mutationProbability = mutationProbability;
        this.distributionIndex = distributionIndex;
    }

    public PolynomiaMutation() {
        this(DEFAULT_PROBABILITY, DEFAULT_DISTRIBUTION_INDEX);
    }

    @Override
    public ContinuousIndividual execute(ContinuousIndividual individual) {
        if (individual == null) {
            throw new RuntimeException("parameter is null");
        }
        mutation(individual);
        return individual;
    }

    private void mutation(ContinuousIndividual individual) {
        double rnd, delta1, delta2, mutPow, deltaq;
        double y, yl, yu, val, xy;

        for (int i = 0; i < individual.getNumberOfVariables(); i++) {
            if (randomValue() <= mutationProbability) {
                y = individual.getVariableValue(i);
                yl = individual.getLowerBound(i);
                yu = individual.getUpperBound(i);
                if (yl == yu) {
                    y = yl;
                } else {
                    delta1 = (y - yl) / (yu - yl);
                    delta2 = (yu - y) / (yu - yl);
                    rnd = randomValue();
                    mutPow = 1.0 / (distributionIndex + 1.0);
                    if (rnd <= 0.5) {
                        xy = 1.0 - delta1;
                        val = 2.0 * rnd + (1.0 - 2.0 * rnd) *
                                (Math.pow(xy, distributionIndex + 1.0));
                        deltaq = Math.pow(val, mutPow) - 1.0;
                    } else {
                        xy = 1.0 - delta2;
                        val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) *
                                (Math.pow(xy, distributionIndex + 1.0));
                        deltaq = 1.0 - Math.pow(val, mutPow);
                    }
                    y = y + deltaq * (yu - yl);
                    y = checkAndResetVariable(y, yl, yu);
                }
                individual.setVariableValue(i, y);
            }
        }
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