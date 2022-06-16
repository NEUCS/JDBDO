package optimizerRunner;

import Visualizer.SimpleVisualizer;
import Visualizer.Visualizer;
import algorithm.DynamicMetaheuristic;
import algorithm.Metaheuristic;
import comparators.DominanceComparator;
import dmops.df.*;
import dmops.fda.FDA1;
import dmops.fda.FDA2;
import dmops.fda.FDA3;
import dynamicMetaheuristic.abc.DynamicABC;
import dynamicMetaheuristic.moead.DynamicMOEAD;
import dynamicMetaheuristic.nagaii.DynamicNSGAII;
import dynamicMetaheuristic.nsgaiii.DynamicNSGAIII;
import individual.ContinuousIndividual;
import mops.BigOpt.BigOpt2015;
import mops.uf.*;
import mops.zdt.*;
import operators.crossover.Crossover;
import operators.crossover.DECrossover;
import operators.crossover.SBXCrossover;
import operators.mutation.Mutation;
import operators.mutation.PolynomiaMutation;
import operators.selection.Selection;
import operators.selection.TournamentSelection;
import population.Population;
import population.evaluator.ContinuousPopulationEvaluator;
import population.evaluator.PopulationEvaluator;
import problem.DynamicTestProblem;
import problem.TestProblem;
import staticMetaheuristic.eas.moead.MOEAD;
import staticMetaheuristic.eas.nsgaii.NSGAIIPrototype;
import staticMetaheuristic.eas.nsgaiii.NSGAIII;
import staticMetaheuristic.sis.abc.ABC;

import java.io.IOException;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/8 18:35
 */
public class DMOPOptimizerRunner {
    private int populationSize;
    private DynamicTestProblem<ContinuousIndividual> problem;
    private DynamicMetaheuristic<Population<ContinuousIndividual>>
            metaheuristic;
    private Crossover<ContinuousIndividual> crossover;
    private DECrossover DECrossover;
    private Mutation<ContinuousIndividual> mutation;
    private Selection<Population<ContinuousIndividual>, ContinuousIndividual>
            selection;
    private PopulationEvaluator<ContinuousIndividual> populationEvaluator;
    private int limitedNumberOfEvaluationsOrIterations;
    private Visualizer visualizer;

    public DMOPOptimizerRunner() throws IOException {
        this.populationSize = 100;
        this.limitedNumberOfEvaluationsOrIterations = 25000;
        this.problem = new FDA1();
        this.crossover = new SBXCrossover(1.0, 20.0);
        this.DECrossover = new DECrossover(1.0, 0.5);
        this.mutation =
                new PolynomiaMutation(1.0 / problem.getNumberOfVariables(), 20);
        this.selection = new TournamentSelection<ContinuousIndividual>(
                new DominanceComparator<ContinuousIndividual>(), 2);
        this.populationEvaluator = new ContinuousPopulationEvaluator<>();
        this.metaheuristic =
                new DynamicNSGAII<>(problem, 25000, 100, 100, 100, crossover,
                                    mutation, selection,
                                    new DominanceComparator<>(),
                                    new ContinuousPopulationEvaluator<ContinuousIndividual>(),
                                    10);

    }

    public DMOPOptimizerRunner setPopulationSize(int populationSize) {
        if (populationSize < 0) {
            throw new RuntimeException("Population size can not be negative!");
        } else if (populationSize > 300) {
            throw new RuntimeException("Population size is too large!");
        } else if (populationSize == 0) {
            throw new RuntimeException("Population size can not be zero!");
        }
        this.populationSize = populationSize;
        return this;
    }

    public DMOPOptimizerRunner setLimitedNumberOfEvaluationsOrIterations(
            int limitedNumberOfEvaluationsOrIterations) {
        if (populationSize < 0) {
            throw new RuntimeException(
                    "Number of evaluations or iterations can not be negative!");
        } else if (populationSize == 0) {
            throw new RuntimeException(
                    "Number of evaluations or iterations can not be zero!");
        }
        this.limitedNumberOfEvaluationsOrIterations =
                limitedNumberOfEvaluationsOrIterations;
        return this;
    }

    public DMOPOptimizerRunner setProblem(String problemName) {
        if (problemName.equals("FDA1")) {
            this.problem = new FDA1();
        } else if (problemName.equals("FDA2")) {
            this.problem = new FDA2();
        } else if (problemName.equals("FDA3")) {
            this.problem = new FDA3();
        } else if (problemName.equals("DF1")) {
            this.problem = new DF1();
        } else if (problemName.equals("DF2")) {
            this.problem = new DF2();
        } else if (problemName.equals("DF3")) {
            this.problem = new DF3();
        } else if (problemName.equals("DF4")) {
            this.problem = new DF4();
        } else if (problemName.equals("DF5")) {
            this.problem = new DF5();
        } else if (problemName.equals("DF6")) {
            this.problem = new DF6();
        } else if (problemName.equals("DF7")) {
            this.problem = new DF7();
        } else if (problemName.equals("DF8")) {
            this.problem = new DF8();
        } else if (problemName.equals("DF9")) {
            this.problem = new DF9();
        } else {
            throw new RuntimeException(
                    "Framework does not contain this problem! Please select " +
                            "from [FDA1-FDA3, DF1-DF9]!");
        }
        return this;
    }

    public DMOPOptimizerRunner setCrossover(
            Crossover<ContinuousIndividual> crossover) {
        this.crossover = crossover;
        return this;
    }

    public DMOPOptimizerRunner setDECrossover(DECrossover deCrossover) {
        this.DECrossover = deCrossover;
        return this;
    }

    public DMOPOptimizerRunner setMutation(
            Mutation<ContinuousIndividual> mutation) {
        this.mutation = mutation;
        return this;
    }

    public DMOPOptimizerRunner setPopulationEvaluator(
            PopulationEvaluator<ContinuousIndividual> populationEvaluator) {
        this.populationEvaluator = populationEvaluator;
        return this;
    }

    public DMOPOptimizerRunner setMetaheuristic(String algorithmName)
            throws IOException {
        if (algorithmName.equals("Dynamic NSGA-II")) {
            this.metaheuristic = new DynamicNSGAII<>(problem,
                                                     limitedNumberOfEvaluationsOrIterations,
                                                     populationSize,
                                                     populationSize,
                                                     populationSize, crossover,
                                                     mutation, selection,
                                                     new DominanceComparator<>(),
                                                     new ContinuousPopulationEvaluator<ContinuousIndividual>(),
                                                     10);
        } else if (algorithmName.equals("Dynamic NSGA-III")) {
            this.metaheuristic = new DynamicNSGAIII<>(problem,
                                                      limitedNumberOfEvaluationsOrIterations,
                                                      populationSize, crossover,
                                                      mutation, selection,
                                                      populationEvaluator, 12,
                                                      10);
        } else if (algorithmName.equals("Dynamic MOEA/D-DE")) {
            this.metaheuristic =
                    new DynamicMOEAD(problem, populationSize, populationSize,
                                     limitedNumberOfEvaluationsOrIterations,
                                     DECrossover, mutation, 0.9, 2, 20, 10);
        } else if (algorithmName.equals("Dynamic ABC")) {
            this.metaheuristic = new DynamicABC(problem, populationSize, 50000,
                                                new DominanceComparator<ContinuousIndividual>(),
                                                populationEvaluator,
                                                limitedNumberOfEvaluationsOrIterations,
                                                populationSize, 10);
        }else {
            throw new RuntimeException(
                    "Framework does not contain this algorithm! Please select " +
                            "from [Dynamic NSGA-II, Dynamic NSGA-III, Dynamic MOEA/D-DE. Dynamic ABC]!");
        }
        return this;
    }


    public void run() {
        Thread thread = new Thread(this.metaheuristic);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error");
        }
    }
}