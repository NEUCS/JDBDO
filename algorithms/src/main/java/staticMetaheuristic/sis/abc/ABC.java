package staticMetaheuristic.sis.abc;

import comparators.CrowdingDistanceComparator;
import comparators.DominanceComparator;
import comparators.ObjectiveComparator;
import individual.ContinuousIndividual;
import individual.Individual;
import population.Population;
import population.evaluator.PopulationEvaluator;
import population.tools.NonDominatedSolutionsFinder;
import problem.TestProblem;
import random.MRandom;
import staticMetaheuristic.sis.abc.AbstractABC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/28 18:24
 */
public class ABC<I extends ContinuousIndividual> extends AbstractABC<I> {
    protected int iterations;
    protected int limitedNumberOfIterations;
    protected PopulationEvaluator<I> populationEvaluator;
    protected Comparator<I> comparator;
    protected int maxTrial;
    protected double[] iDom;
    protected double[] fitness;
    protected double[] probability;
    protected double sumOfFitness;
    protected final double W1 = 0.7;
    protected final double W2 = 0.8;
    protected double sumOfProb;

    public ABC(TestProblem<I> testProblem, int swarmSize, int maxTrial,
            Comparator<I> comparator,
            PopulationEvaluator<I> populationEvaluator,
            int limitedNumberOfIterations, int archiveSize) {
        super(testProblem);
        setSwarmSize(swarmSize);
        setFoodNumber(swarmSize / 2);
        setArchiveSize(archiveSize);
        this.maxTrial = maxTrial;
        this.comparator = comparator;
        this.populationEvaluator = populationEvaluator;
        this.iDom = new double[getFoodNumber()];
        this.fitness = new double[getFoodNumber()];
        this.probability = new double[getFoodNumber()];
        this.limitedNumberOfIterations = limitedNumberOfIterations;
    }

    @Override
    public Population<I> getNonDominatedSolutions() {
        NonDominatedSolutionsFinder<I> finder =
                new NonDominatedSolutionsFinder<>(swarm, comparator);
        return finder.findNonDominatedSolutions();
    }


    @Override
    protected void initialize() {
        this.iterations = 0;
    }

    @Override
    protected void update() {
        this.iterations += 1;
    }

    @Override
    protected boolean isStoppingCriterionMet() {
        return iterations >= limitedNumberOfIterations;
    }

    @Override
    protected void evaluatePopulation(Population<I> swarm) {
        populationEvaluator.evaluate(swarm, getTestProblem());
    }

    @Override
    protected void sendEmployedBees() {
        for (int i = 0; i < getFoodNumber(); i++) {
            //            Population<I> foodSource = new Population<>();
            //            List<I> indi = new ArrayList<>();
            //            for (int j = 0; j < getSwarm().getPopulation().size
            //            (); j++) {
            //                indi.add((I) getSwarm().getIndividual(j).copy());
            //            }
            //            foodSource.setPopulation(indi);
            int paramToChange = MRandom.getInstance().nextInt(0, getSwarm()
                    .getIndividual(i).getNumberOfVariables() - 1);
            int neighbor = MRandom.getInstance()
                    .nextInt(0, externalArchive.getPopulation().size() - 1);

            List<I> candidateFoodSource = new ArrayList<>(2);

            candidateFoodSource.add(getSwarm().getIndividual(i));
            candidateFoodSource.add(externalArchive.getIndividual(neighbor));

            double newValue =
                    candidateFoodSource.get(0).getVariableValue(paramToChange) +
                            W1 * MRandom.getInstance().nextDouble() *
                                    (candidateFoodSource.get(0)
                                            .getVariableValue(paramToChange) -
                                            candidateFoodSource.get(1)
                                                    .getVariableValue(
                                                            paramToChange));
            if (newValue <
                    candidateFoodSource.get(0).getLowerBound(paramToChange)) {
                newValue =
                        candidateFoodSource.get(0).getLowerBound(paramToChange);
            }
            if (newValue >
                    candidateFoodSource.get(1).getUpperBound(paramToChange)) {
                newValue =
                        candidateFoodSource.get(1).getUpperBound(paramToChange);
            }
            var tempIndividual = candidateFoodSource.get(0).copy();
            tempIndividual.setVariableValue(paramToChange, newValue);
            getTestProblem().evaluate((I) tempIndividual);
            int result = new DominanceComparator<I>()
                    .compare((I) tempIndividual, getSwarm().getIndividual(i));
            if (result == -1) {
                getSwarm().setIndividual(i, (I) tempIndividual);
            } else {
                int trial = ((int) getSwarm().getIndividual(i)
                        .getAttribute("Trial")) + 1;
                getSwarm().getIndividual(i).setAttribute("Trial", trial);
            }
        }
    }

    @Override
    protected void calculateProbabilities() {
        dom();
        calculateFitness();
        sumOfProb = 0;
        for (int i = 0; i < foodNumber; i++) {
            probability[i] = fitness[i] / sumOfFitness;
            sumOfProb += probability[i];
        }

    }

    @Override
    protected void sendOnLookerBees() {
        for (int i = 0; i < getFoodNumber(); i++) {
            int paramToChange = MRandom.getInstance().nextInt(0, getSwarm()
                    .getIndividual(i).getNumberOfVariables() - 1);
            double random = MRandom.getInstance().nextDouble(0, 1);
            int neighbor = rouletteWheel(random);
            while (neighbor == i) {
                random = MRandom.getInstance().nextDouble(0, 1);
                neighbor = rouletteWheel(random);
            }

            List<I> candidateFoodSource = new ArrayList<>(2);

            candidateFoodSource.add(getSwarm().getIndividual(i));
            candidateFoodSource.add(getSwarm().getIndividual(neighbor));
            double newValue =
                    candidateFoodSource.get(0).getVariableValue(paramToChange) +
                            W2 * MRandom.getInstance().nextDouble() *
                                    (candidateFoodSource.get(0)
                                            .getVariableValue(paramToChange) -
                                            candidateFoodSource.get(1)
                                                    .getVariableValue(
                                                            paramToChange));
            if (newValue <
                    candidateFoodSource.get(0).getLowerBound(paramToChange)) {
                newValue =
                        candidateFoodSource.get(0).getLowerBound(paramToChange);
            }
            if (newValue >
                    candidateFoodSource.get(1).getUpperBound(paramToChange)) {
                newValue =
                        candidateFoodSource.get(1).getUpperBound(paramToChange);
            }
            var tempIndividual = candidateFoodSource.get(0).copy();
            tempIndividual.setVariableValue(paramToChange, newValue);
            getTestProblem().evaluate((I) tempIndividual);
            int result = new DominanceComparator<I>()
                    .compare((I) tempIndividual, getSwarm().getIndividual(i));
            if (result < 0) {
                getSwarm().setIndividual(i, (I) tempIndividual);
            } else {
                int trial = ((int) getSwarm().getIndividual(i)
                        .getAttribute("Trial")) + 1;
                getSwarm().getIndividual(i).setAttribute("Trial", trial);
            }
        }
    }

    //    protected void sendOnLookerBees() {
    //        int t = 0;
    //        int i = 0;
    //        while (t < getFoodNumber()) {
    //            Population<I> foodSource = new Population<>();
    //            for (int j = 0; j < getSwarm().getPopulation().size(); j++) {
    //                foodSource.getPopulation()
    //                        .add((I) getSwarm().getIndividual(j).copy());
    //            }
    //            double r = MRandom.getInstance().nextDouble(0, 0.01);
    //            if (r < probability[i]) {
    //                t++;
    //                r = MRandom.getInstance().nextDouble(0, 1);
    //                int paramToChange = (int) r *
    //                        getSwarm().getIndividual(i)
    //                        .getNumberOfVariables();
    //                r = MRandom.getInstance().nextDouble(0, 1);
    //                int neighbor = (int) r * getFoodNumber();
    //                while (neighbor == i) {
    //                    r = MRandom.getInstance().nextDouble(0, 1);
    //                    neighbor = (int) r * getFoodNumber();
    //                }
    //
    //                System.out.println(r + "," + sumOfProb + "," + neighbor);
    //                List<I> candidateFoodSource = new ArrayList<>(2);
    //
    //                candidateFoodSource.add(foodSource.getIndividual(i));
    //                candidateFoodSource.add(foodSource.getIndividual
    //                (neighbor));
    //                I oldIndi = getSwarm().getIndividual(i);
    //                double newValue = candidateFoodSource.get(0)
    //                        .getVariableValue(paramToChange) +
    //                        W2 * MRandom.getInstance().nextDouble() *
    //                                (candidateFoodSource.get(0)
    //                                        .getVariableValue(paramToChange) -
    //                                        candidateFoodSource.get(1)
    //                                                .getVariableValue(
    //                                                        paramToChange));
    //                if (newValue < candidateFoodSource.get(0)
    //                        .getLowerBound(paramToChange)) {
    //                    newValue = getTestProblem().getLowerBound
    //                    (paramToChange);
    //                } else if (newValue > candidateFoodSource.get(1)
    //                        .getUpperBound(paramToChange)) {
    //                    newValue = getTestProblem().getUpperBound
    //                    (paramToChange);
    //                }
    //                candidateFoodSource.get(0)
    //                        .setVariableValue(paramToChange, newValue);
    //                foodSource.setIndividual(i, candidateFoodSource.get(0));
    //                evaluatePopulation(foodSource);
    //                int result = new DominanceComparator<I>()
    //                        .compare(foodSource.getIndividual(i), oldIndi);
    //                if (result < 0) {
    //                    setSwarm(foodSource);
    //                } else {
    //                    int trial = ((int) getSwarm().getIndividual(i)
    //                            .getAttribute("Trial")) + 1;
    //                    getSwarm().getIndividual(i).setAttribute("Trial",
    //                    trial);
    //                }
    //            }
    //            i++;
    //            if (i == getFoodNumber()){
    //                i = 0;
    //            }
    //        }
    //    }

    @Override
    protected void sendScoutBees() {
        for (int i = 0; i < getFoodNumber(); i++) {
            if ((int) getSwarm().getIndividual(i).getAttribute("Trial") >
                    maxTrial) {

                int random = MRandom.getInstance().nextInt(0, getSwarm()
                        .getIndividual(i).getNumberOfVariables() - 1);
                getSwarm().getIndividual(i).setVariableValue(random, getSwarm()
                        .getIndividual(i).getLowerBound(random) +
                        MRandom.getInstance().nextDouble() *
                                (getSwarm().getIndividual(i)
                                        .getUpperBound(random) -
                                        getSwarm().getIndividual(i)
                                                .getLowerBound(random)));

                getTestProblem().evaluate(getSwarm().getIndividual(i));
                getSwarm().getIndividual(i).setAttribute("Trial", 0);
            }
        }
    }

    @Override
    protected void dom() {
        Comparator<I> dominanceComparator = new DominanceComparator<>();
        initIdom();
        int flag;
        for (int i = 0; i < getFoodNumber() - 1; i++) {
            for (int j = i + 1; j < getFoodNumber(); j++) {
                flag = dominanceComparator.compare(getSwarm().getIndividual(i),
                                                   getSwarm().getIndividual(j));
                if (flag == -1) {
                    iDom[i]++;
                } else if (flag == 1) {
                    iDom[j]++;
                }
            }
        }
    }

    @Override
    protected void calculateFitness() {
        sumOfFitness = 0;
        for (int i = 0; i < getFoodNumber(); i++) {
            fitness[i] = iDom[i] / getFoodNumber();
            sumOfFitness += fitness[i];
        }
    }

    @Override
    protected void updateArch() {
        Population<I> so = getNonDominatedSolutions();
        externalArchive.enlarge(so);
        NonDominatedSolutionsFinder<I> finder =
                new NonDominatedSolutionsFinder<>(swarm,
                                                  new DominanceComparator<>());
        externalArchive.setPopulation(
                finder.findNonDominatedSolutions().getPopulation());
        crowdingDistanceDensity(externalArchive);
        List<I> population = externalArchive.getPopulation();
        population.sort(new CrowdingDistanceComparator<>());
        while (externalArchive.getPopulation().size() > archiveSize) {
            externalArchive.getPopulation()
                    .remove(externalArchive.getPopulation().size() - 1);
        }

    }

    public void initIdom() {
        Arrays.fill(iDom, 0);
    }

    public int rouletteWheel(double random) {
        double sum = 0;
        int result = 0;
        for (int i = 0; i < getFoodNumber(); i++) {
            sum += probability[i];
            if (sum >= random) {
                result = i;
                break;
            }
        }
        return result;
    }

    private void crowdingDistanceDensity(Population<I> rawPopulation) {
        double min;
        double max;
        double distance;
        var population = new Population<>();

        for (int i = 0; i < rawPopulation.getPopulation().size(); i++) {
            population.add(rawPopulation.getPopulation().get(i));
        }

        for (int i = 0; i < rawPopulation.getPopulation().size(); i++) {
            population.getPopulation().get(i)
                    .setAttribute("CrowdingDistance", 0.0);
        }
        for (int i = 0; i <
                rawPopulation.getPopulation().get(0).getNumberOfObjectives();
                i++) {
            List<Individual<?>> sortPopulation =
                    (List<Individual<?>>) population.getPopulation();
            sortPopulation.sort(new ObjectiveComparator<>(i));
            min = sortPopulation.get(0).getObjectiveValue(i);
            max = sortPopulation.get(sortPopulation.size() - 1)
                    .getObjectiveValue(i);
            sortPopulation.get(0)
                    .setAttribute("CrowdingDistance", Double.POSITIVE_INFINITY);
            sortPopulation.get(sortPopulation.size() - 1)
                    .setAttribute("CrowdingDistance", Double.POSITIVE_INFINITY);

            for (int j = 1; j < sortPopulation.size() - 1; j++) {
                distance = (sortPopulation.get(j + 1).getObjectiveValue(i) -
                        sortPopulation.get(j - 1).getObjectiveValue(i)) /
                        (max - min);
                distance += (double) sortPopulation.get(j)
                        .getAttribute("CrowdingDistance");
                sortPopulation.get(j)
                        .setAttribute("CrowdingDistance", distance);
            }
        }
    }

}