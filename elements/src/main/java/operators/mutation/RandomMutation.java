package operators.mutation;

import individual.ContinuousIndividual;
import random.MRandom;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/8 15:36
 */
public class RandomMutation implements Mutation<ContinuousIndividual> {
    private double probability;

    public RandomMutation(double probability){
        this.probability = probability;
    }

    @Override
    public ContinuousIndividual execute(ContinuousIndividual individual) {
        if (individual == null){
            throw new RuntimeException("Individual is null!");
        }
        for (int i = 0; i < individual.getNumberOfVariables(); i++) {
            if (MRandom.getInstance().nextDouble() < probability){
                individual.setVariableValue(i, individual.getLowerBound(i) + MRandom.getInstance()
                        .nextDouble()*(individual.getUpperBound(i) - individual.getLowerBound(i)));
            }
        }
        return individual;
    }
}