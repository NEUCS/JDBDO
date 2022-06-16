package staticMetaheuristic.eas.moead;

import individual.ContinuousIndividual;
import operators.crossover.DECrossover;
import operators.mutation.Mutation;
import population.Population;
import problem.TestProblem;

public class MOEAD extends MOEADprototype<ContinuousIndividual> {

    public MOEAD(TestProblem<ContinuousIndividual> testProblem,
            int populationSize, int finalPopulationSize,
            int limitedNumberOfEvaluations, DECrossover crossover,
            Mutation<ContinuousIndividual> mutation, double delta, int nr,
            int t) {
        super(testProblem, populationSize, finalPopulationSize,
              limitedNumberOfEvaluations, crossover, mutation, delta, nr, t);
    }

    @Override
    public void run() {
        initializePopulation();
        weightInitialization();
        neighborInitialization();
        updateZ(population);
        initialize();
        while (!isStoppingCriterionMet()) {
            var permutation = new int[populationSize];
            randomPermutation(permutation, populationSize);
            for (int i = 0; i < populationSize; i++) {
                var subproblemId = permutation[i];
                var type = chooseNeighborType();
                var parents = selectParents(subproblemId, type);
                crossover.setCurrent((ContinuousIndividual) population
                        .getIndividual(subproblemId));
                Population<ContinuousIndividual> child =
                        crossover.execute(parents);
                ContinuousIndividual individual = child.getIndividual(0);
                mutation.execute(individual);
                testProblem.evaluate(individual);
                updateZ(individual.getAllObjectivesValue());
                updateNeighborhood(individual, subproblemId, type);
            }
            update();
        }
    }

    public void initialize(){
        this.evaluations = populationSize;
    }

    public void update(){
        this.evaluations += populationSize;
    }
}