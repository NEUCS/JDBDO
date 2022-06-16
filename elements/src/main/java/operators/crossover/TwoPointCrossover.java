package operators.crossover;

import individual.ContinuousIndividual;
import population.Population;
import random.MRandom;

import java.util.ArrayList;
import java.util.List;

public class TwoPointCrossover implements Crossover<ContinuousIndividual> {
    private double crossoverProbability;

    public TwoPointCrossover(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public TwoPointCrossover() {
        this.crossoverProbability = 1.0;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }

    @Override
    public Population<ContinuousIndividual> execute(
            Population<ContinuousIndividual> population) {
        if (MRandom.getInstance().nextDouble() < crossoverProbability) {
            ContinuousIndividual individual1 = population.getIndividual(0);
            ContinuousIndividual individual2 = population.getIndividual(1);

            List<Integer> index = new ArrayList<>(2);
            index.add(MRandom.getInstance().nextInt(0, individual1
                    .getNumberOfVariables() - 1));
            index.add(MRandom.getInstance().nextInt(0, individual1
                    .getNumberOfVariables() - 1));

            var newIndividual1 = individual1.copy();
            var newIndividual2 = individual2.copy();

            boolean flag = false;
            for (int i = 0; i < individual1.getNumberOfVariables(); i++) {
                if (flag) {
                    newIndividual2.setVariableValue(i, individual1
                            .getVariableValue(i));
                    newIndividual1.setVariableValue(i, individual2
                            .getVariableValue(i));
                }
                if (index.contains(i)) {
                    flag = !flag;
                }
            }
            Population<ContinuousIndividual> offSpring = new Population<>();
            offSpring.add((ContinuousIndividual) newIndividual1);
            offSpring.add((ContinuousIndividual) newIndividual2);
            return offSpring;
        }
        return population;
    }
}