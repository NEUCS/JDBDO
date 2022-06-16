package staticMetaheuristic.sis.abc;

import algorithm.AbstractSI;
import individual.Individual;
import population.Population;
import population.evaluator.PopulationEvaluator;
import problem.TestProblem;

/**
 * @program: framework
 * @description: implementation of artificial bee colony algorithm
 * @author: Zheng Xuanyu
 * @create time: 2021/12/28 17:05
 */
public abstract class AbstractABC<I extends Individual<?>>
        extends AbstractSI<I, Population<I>> {
    protected int foodNumber;
    protected PopulationEvaluator<I> populationEvaluator;
    protected Population<I> externalArchive;
    protected int archiveSize;


    public AbstractABC(TestProblem<I> testProblem) {
        super(testProblem);
    }

    public int getFoodNumber() {
        return foodNumber;
    }

    public void setFoodNumber(int foodNumber) {
        this.foodNumber = foodNumber;
    }

    public void setArchiveSize(int archiveSize){
        this.archiveSize = archiveSize;
    }

    protected abstract void initialize();

    protected abstract void update();

    protected abstract boolean isStoppingCriterionMet();

    protected abstract void sendEmployedBees();

    protected abstract void calculateProbabilities();

    protected abstract void sendOnLookerBees();

    protected abstract void sendScoutBees();

    protected abstract void dom();

    protected abstract void calculateFitness();

    protected abstract void updateArch();


    protected void initializeFoodSource() {
        swarm = new Population<>(getFoodNumber());
        externalArchive = new Population<>();
        for (int i = 0; i < getFoodNumber(); i++) {
            var newIndividual = getTestProblem().initIndividual(i);
            getTestProblem().evaluate(newIndividual);
            swarm.setIndividual(i, newIndividual);
            swarm.getPopulation().get(i).setAttribute("Trial", 0);
            if (externalArchive.getPopulation().size() < archiveSize){
                externalArchive.getPopulation().add((I) newIndividual.copy());
            }
        }
    }

    @Override
    public void run() {
        initializeFoodSource();
        initialize();
        while (!isStoppingCriterionMet()) {
            sendEmployedBees();
            calculateProbabilities();
            sendOnLookerBees();
            sendScoutBees();
            updateArch();
            update();
        }
    }
}