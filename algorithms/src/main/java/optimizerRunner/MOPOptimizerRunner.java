package optimizerRunner;

import Visualizer.SimpleVisualizer;
import Visualizer.Visualizer;
import algorithm.Metaheuristic;
import comparators.DominanceComparator;
import staticMetaheuristic.eas.moead.MOEAD;
import staticMetaheuristic.eas.nsgaiii.NSGAIII;
import staticMetaheuristic.eas.nsgaii.NSGAIIPrototype;
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
import problem.TestProblem;
import staticMetaheuristic.sis.abc.ABC;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/8 18:35
 */
public class MOPOptimizerRunner {
    private int populationSize;
    private TestProblem<ContinuousIndividual> problem;
    private Metaheuristic<Population<ContinuousIndividual>> metaheuristic;
    private Crossover<ContinuousIndividual> crossover;
    private DECrossover DECrossover;
    private Mutation<ContinuousIndividual> mutation;
    private Selection<Population<ContinuousIndividual>, ContinuousIndividual>
            selection;
    private PopulationEvaluator<ContinuousIndividual> populationEvaluator;
    private int limitedNumberOfEvaluationsOrIterations;
    private Visualizer visualizer;

    public MOPOptimizerRunner() {
        this.populationSize = 100;
        this.limitedNumberOfEvaluationsOrIterations = 25000;
        this.problem = new ZDT1();
        this.crossover = new SBXCrossover(1.0, 20.0);
        this.DECrossover = new DECrossover(1.0, 0.5);
        this.mutation =
                new PolynomiaMutation(1.0 / problem.getNumberOfVariables(), 20);
        this.selection = new TournamentSelection<ContinuousIndividual>(
                new DominanceComparator<ContinuousIndividual>(), 2);
        this.populationEvaluator = new ContinuousPopulationEvaluator<>();
        this.metaheuristic = new NSGAIIPrototype<ContinuousIndividual>(problem,
                                                                       limitedNumberOfEvaluationsOrIterations,
                                                                       populationSize,
                                                                       populationSize,
                                                                       populationSize,
                                                                       crossover,
                                                                       mutation,
                                                                       selection,
                                                                       new DominanceComparator<>(),
                                                                       populationEvaluator);

    }

    public MOPOptimizerRunner setPopulationSize(int populationSize) {
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

    public MOPOptimizerRunner setLimitedNumberOfEvaluationsOrIterations(
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

    public MOPOptimizerRunner setProblem(String problemName) {
        if (problemName.equals("ZDT1")) {
            this.problem = new ZDT1();
        } else if (problemName.equals("ZDT2")) {
            this.problem = new ZDT2();
        } else if (problemName.equals("ZDT3")) {
            this.problem = new ZDT3();
        } else if (problemName.equals("ZDT4")) {
            this.problem = new ZDT4();
        } else if (problemName.equals("ZDT6")) {
            this.problem = new ZDT6();
        } else if (problemName.equals("UF1")) {
            this.problem = new UF1();
        } else if (problemName.equals("UF2")) {
            this.problem = new UF2();
        } else if (problemName.equals("UF3")) {
            this.problem = new UF3();
        } else if (problemName.equals("UF4")) {
            this.problem = new UF4();
        } else if (problemName.equals("UF5")) {
            this.problem = new UF5();
        } else if (problemName.equals("UF6")) {
            this.problem = new UF6();
        } else if (problemName.equals("UF7")) {
            this.problem = new UF7();
        } else if (problemName.equals("D4")) {
            this.problem = new BigOpt2015("D4");
        } else if (problemName.equals("D4N")) {
            this.problem = new BigOpt2015("D4N");
        } else if (problemName.equals("D12")) {
            this.problem = new BigOpt2015("D12");
        } else if (problemName.equals("D12N")) {
            this.problem = new BigOpt2015("D12N");
        } else if (problemName.equals("D19")) {
            this.problem = new BigOpt2015("D19");
        } else if (problemName.equals("D19N")) {
            this.problem = new BigOpt2015("D19N");
        } else {
            throw new RuntimeException(
                    "Framework does not contain this problem! Please select " +
                            "from [ZDT1,ZDT2,ZDT3,ZDT4,ZDT6,UF1,UF2,UF3,UF4," +
                            "UF5,UF6,UF7,D4,D4N,D12,D12N,D19,D19N]!");
        }
        return this;
    }

    public MOPOptimizerRunner setCrossover(Crossover<ContinuousIndividual> crossover){
        this.crossover = crossover;
        return this;
    }

    public MOPOptimizerRunner setDECrossover(DECrossover deCrossover){
        this.DECrossover =deCrossover;
        return this;
    }

    public MOPOptimizerRunner setMutation(Mutation<ContinuousIndividual> mutation){
        this.mutation = mutation;
        return this;
    }

    public MOPOptimizerRunner setPopulationEvaluator(PopulationEvaluator<ContinuousIndividual> populationEvaluator){
        this.populationEvaluator = populationEvaluator;
        return this;
    }

    public MOPOptimizerRunner setMetaheuristic(String algorithmName) {
        if (algorithmName.equals("NSGA-II")) {
            this.metaheuristic =
                    new NSGAIIPrototype<ContinuousIndividual>(problem,
                                                              limitedNumberOfEvaluationsOrIterations,
                                                              populationSize,
                                                              populationSize,
                                                              populationSize,
                                                              crossover,
                                                              mutation,
                                                              selection,
                                                              new DominanceComparator<>(),
                                                              populationEvaluator);
        } else if (algorithmName.equals("NSGA-III")) {
            this.metaheuristic = new NSGAIII<ContinuousIndividual>(problem,
                                                              limitedNumberOfEvaluationsOrIterations,
                                                              populationSize,
                                                              crossover,
                                                              mutation,
                                                              selection,
                                                              populationEvaluator,
                                                              12);
        } else if (algorithmName.equals("MOEA/D-DE")) {
            this.metaheuristic = new MOEAD(problem, populationSize, populationSize,
                                      limitedNumberOfEvaluationsOrIterations,
                                      DECrossover, mutation, 0.9, 2, 20);
        } else if (algorithmName.equals("ABC")) {
            this.metaheuristic =
                    new ABC<ContinuousIndividual>(problem, populationSize, 50000,
                                                  new DominanceComparator<ContinuousIndividual>(),
                                                  populationEvaluator,
                                                  limitedNumberOfEvaluationsOrIterations, populationSize);
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
        Population<ContinuousIndividual> result =
                metaheuristic.getNonDominatedSolutions();
        this.visualizer = new SimpleVisualizer(result);
        visualizer.show();
    }
}