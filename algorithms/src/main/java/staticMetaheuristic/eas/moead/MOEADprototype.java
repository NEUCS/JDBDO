package staticMetaheuristic.eas.moead;

import algorithm.Metaheuristic;
import comparators.DominanceComparator;
import individual.Individual;
import operators.crossover.DECrossover;
import operators.mutation.Mutation;
import population.Population;
import population.tools.NonDominatedSolutionsFinder;
import problem.TestProblem;
import random.MRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MOEADprototype<I extends Individual<?>>
        implements Metaheuristic<Population<I>> {
    protected TestProblem<I> testProblem;
    protected int t;
    protected int nr;
    protected int populationSize;
    protected int finalPopulationSize;
    protected int evaluations;
    protected int limitedNumberOfEvaluations;
    protected double delta;
    protected double[] z;
    protected double[] nadirPoint;
    protected double[][] lambda;
    protected int[][] neighbor;
    protected Population<I> individualArray;
    protected Population<I> population;
    protected Population<I> offspring;
    protected Population<I> jointPopulation;
    protected DECrossover crossover;
    protected Mutation<I> mutation;

    public MOEADprototype(TestProblem<I> testProblem, int populationSize,
            int finalPopulationSize, int limitedNumberOfEvaluations,
            DECrossover crossover, Mutation<I> mutation, double delta, int nr,
            int t) {
        this.testProblem = testProblem;
        this.populationSize = populationSize;
        this.finalPopulationSize = finalPopulationSize;
        this.limitedNumberOfEvaluations = limitedNumberOfEvaluations;
        this.crossover = crossover;
        this.mutation = mutation;
        this.delta = delta;
        this.nr = nr;
        this.t = t;
        this.population = new Population<>(populationSize);
        this.individualArray =
                new Population<>(testProblem.getNumberOfObjectives());
        this.z = new double[testProblem.getNumberOfObjectives()];
        Arrays.fill(z, Double.POSITIVE_INFINITY);
        this.nadirPoint = new double[testProblem.getNumberOfObjectives()];
        Arrays.fill(nadirPoint, Double.NEGATIVE_INFINITY);
        this.neighbor = new int[populationSize][t];
        this.lambda =
                new double[populationSize][testProblem.getNumberOfObjectives()];
    }

    protected void weightInitialization() {
        for (int i = 0; i < populationSize; i++) {
            lambda[i][0] = 1.0 * i / (populationSize - 1);
            lambda[i][1] = 1 - lambda[i][0];
        }
    }

    protected void neighborInitialization() {
        double[] dist = new double[populationSize];
        int[] index = new int[populationSize];

        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < populationSize; j++) {
                dist[j] = computeDistance(lambda[i], lambda[j]);
                index[j] = j;
            }

            sortByDistance(dist, index, populationSize, t);
            System.arraycopy(index, 0, neighbor[i], 0, t);
        }
    }

    protected int chooseNeighborType() {
        double random = MRandom.getInstance().nextDouble();
        int type;
        if (random < delta) {
            type = 0;
        } else {
            type = 1;
        }
        return type;
    }

    protected List<Integer> matingSelect(int subproblemId,
            int numberOfSelectedIndividual, int type) {
        int neighborSize;
        int selectedIndividual;

        List<Integer> individualList =
                new ArrayList<>(numberOfSelectedIndividual);

        neighborSize = neighbor[subproblemId].length;
        while (individualList.size() < numberOfSelectedIndividual) {
            if (type == 0) {
                int ranom = MRandom.getInstance().nextInt(0, neighborSize - 1);
                selectedIndividual = neighbor[subproblemId][ranom];
            } else {
                selectedIndividual =
                        MRandom.getInstance().nextInt(0, populationSize - 1);
            }
            boolean flag = true;
            for (Integer id : individualList) {
                if (id == selectedIndividual) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                individualList.add(selectedIndividual);
            }
        }
        return individualList;
    }

    public Population<I> selectParents(int subproblemId, int type) {
        List<Integer> matingPool = matingSelect(subproblemId, 2, type);

        Population<I> parents = new Population<>();
        parents.add(population.getIndividual(matingPool.get(0)));
        parents.add(population.getIndividual(matingPool.get(1)));
        parents.add(population.getIndividual(subproblemId));

        return parents;
    }

    public void updateNeighborhood(I individual, int subproblemId, int type) {
        int size;
        int counter = 0;

        if (type == 0) {
            size = neighbor[subproblemId].length;
        } else {
            size = population.getPopulation().size();
        }
        int[] permutation = new int[size];
        randomPermutation(permutation, size);

        for (int i = 0; i < size; i++) {
            int temp;
            if (type == 0) {
                temp = neighbor[subproblemId][permutation[i]];
            } else {
                temp = permutation[i];
            }
            var f1 = computeFitness(population.getIndividual(temp),
                                    lambda[temp]);
            var f2 = computeFitness(individual, lambda[temp]);

            if (f2 < f1) {
                population.setIndividual(temp, (I) individual.copy());
                counter++;
            }

            if (counter >= nr) {
                return;
            }

        }

    }


    public double computeDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(sum);
    }

    public void sortByDistance(double[] dist, int[] index, int populationSize,
            int t) {
        for (int i = 0; i < t; i++) {
            for (int j = i + 1; j < populationSize; j++) {
                if (dist[i] > dist[j]) {
                    double temp = dist[i];
                    dist[i] = dist[j];
                    dist[j] = temp;
                    int idx = index[i];
                    index[i] = index[j];
                    index[j] = idx;
                }
            }
        }
    }

    public void randomPermutation(int[] permutation, int size) {
        int[] index = new int[size];
        boolean[] flag = new boolean[size];
        for (int i = 0; i < size; i++) {
            index[i] = i;
            flag[i] = true;
        }
        int temp = 0;
        while (temp < size) {
            int start = MRandom.getInstance().nextInt(0, size - 1);
            while (true) {
                if (flag[start]) {
                    permutation[temp] = index[start];
                    flag[start] = false;
                    temp++;
                    break;
                }
                if (start == (size - 1)) {
                    start = 0;
                } else {
                    start++;
                }
            }
        }
    }

    public double computeFitness(I individual, double[] lambda) {
        //        double sum = 0.0;
        //        for (int i = 0; i < testProblem.getNumberOfObjectives();
        //        i++) {
        //            sum += lambda[i] * individual.getObjectiveValue(i);
        //        }
        //        return sum;
        double maxFun = -1.0e+30;

        for (int n = 0; n < testProblem.getNumberOfObjectives(); n++) {
            double diff = Math.abs(individual.getObjectiveValue(n) - z[n]);

            double feval;
            if (lambda[n] == 0) {
                feval = 0.0001 * diff;
            } else {
                feval = diff * lambda[n];
            }
            if (feval > maxFun) {
                maxFun = feval;
            }
        }

        return maxFun;
    }

    public void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            var individual = testProblem.initIndividual(i);
            testProblem.evaluate(individual);
            population.setIndividual(i, individual);
        }
    }

    public void updateZ(double[] objectives) {
        for (int i = 0; i < z.length; i++) {
            if (z[i] > objectives[i]) {
                z[i] = objectives[i];
            }
        }

    }

    public void updateZ(Population<I> population) {
        for (I individual : population.getPopulation()) {
            var objevtives = individual.getAllObjectivesValue();
            updateZ(objevtives);
        }
    }

    public boolean isStoppingCriterionMet() {
        return evaluations >= limitedNumberOfEvaluations;
    }

    @Override
    public Population<I> getNonDominatedSolutions() {
        NonDominatedSolutionsFinder<I> finder =
                new NonDominatedSolutionsFinder<>(population,
                                                  new DominanceComparator<>());
        return finder.findNonDominatedSolutions();
    }
}