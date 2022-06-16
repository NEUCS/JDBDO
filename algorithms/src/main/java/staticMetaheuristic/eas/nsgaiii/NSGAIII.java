package staticMetaheuristic.eas.nsgaiii;

import algorithm.AbstractEA;
import comparators.DominanceComparator;
import staticMetaheuristic.eas.nsgaiii.tools.EnvironmentalSelection;
import staticMetaheuristic.eas.nsgaiii.tools.ReferencePoint;
import individual.Individual;
import operators.crossover.Crossover;
import operators.mutation.Mutation;
import operators.selection.Selection;
import operators.selection.rank.DominanceRank;
import operators.selection.rank.Rank;
import population.Population;
import population.evaluator.PopulationEvaluator;
import population.tools.NonDominatedSolutionsFinder;
import problem.TestProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class NSGAIII<I extends Individual<?>>
        extends AbstractEA<I, Population<I>> {
    protected int numberOfIterations;
    protected int limitedNumberOfIteration;
    protected PopulationEvaluator<I> populationEvaluator;
    protected int numberOfDivisions;
    protected List<ReferencePoint<I>> referencePointList = new Vector<>();

    public NSGAIII(TestProblem<I> testProblem, int limitedNumberOfIteration,
            int populationSize, Crossover<I> crossover, Mutation<I> mutation,
            Selection<Population<I>, I> selection,
            PopulationEvaluator<I> evaluator, int numberOfDivisions) {
        super(testProblem);
        this.limitedNumberOfIteration = limitedNumberOfIteration;
        this.crossover = crossover;
        this.muation = mutation;
        this.selection = selection;
        this.populationEvaluator = evaluator;
        this.numberOfDivisions = numberOfDivisions;
        (new ReferencePoint<I>())
                .generateReferencePoints(this.referencePointList,
                                         getTestProblem()
                                                 .getNumberOfObjectives(),
                                         numberOfDivisions);
        int pop = populationSize;
        while (pop % 4 > 0) {
            pop++;
        }
        setPopulationSize(pop);
    }

    @Override
    public Population<I> getNonDominatedSolutions() {
        NonDominatedSolutionsFinder<I> finder =
                new NonDominatedSolutionsFinder<>(getPopulation(),
                                                  new DominanceComparator<>());
        return finder.findNonDominatedSolutions();
    }

    @Override
    protected void initialize() {
        this.numberOfIterations = 1;
    }

    @Override
    protected void update() {
        this.numberOfIterations++;
    }

    @Override
    protected boolean isStoppingCriterionMet() {
        return this.numberOfIterations >= this.limitedNumberOfIteration;
    }

    @Override
    protected Population<I> evaluatePopulation(Population<I> population) {
        return populationEvaluator.evaluate(population, getTestProblem());
    }

    @Override
    protected Population<I> selection(Population<I> population) {
        Population<I> matingPopulation =
                new Population<>(population.getPopulation().size());
        for (int i = 0; i < getPopulationSize(); i++) {
            I selectedIndividual = selection.execute(population);
            matingPopulation.setIndividual(i, selectedIndividual);
        }
        return matingPopulation;
    }

    @Override
    protected Population<I> crossAndMut(Population<I> population) {
        Population<I> offspring = new Population<>();
        for (int i = 0; i < getPopulationSize(); i += 2) {
            Population<I> parents = new Population<>(2);
            parents.setIndividual(0, population.getIndividual(i));
            parents.setIndividual(1, population
                    .getIndividual(Math.min(i + 1, getPopulationSize() - 1)));
            Population<I> off = crossover.execute(parents);
            for (int j = 0; j < off.getPopulation().size(); j++) {
                muation.execute(off.getIndividual(j));
                offspring.getPopulation().add(off.getIndividual(j));
            }
        }
        return offspring;
    }

    private List<ReferencePoint<I>> getReferencePointsCopy() {
        List<ReferencePoint<I>> copy = new ArrayList<>();
        for (ReferencePoint<I> r : this.referencePointList) {
            copy.add(new ReferencePoint<>(r));
        }
        return copy;
    }

    @Override
    protected Population<I> nextGenPopulation(Population<I> population,
            Population<I> offspringPopulation) {
        population.enlarge(offspringPopulation);
        Rank<I> rank = new DominanceRank<>(new DominanceComparator<>());
        rank.compute(population);
        Population<I> pop = new Population<>();
        List<Population<I>> fronts = new ArrayList<>();
        int rankingIndex = 0;
        int candidateIndividuals = 0;
        while (candidateIndividuals < getPopulationSize()) {
            fronts.add(rank.getSubFront(rankingIndex));
            candidateIndividuals +=
                    rank.getSubFront(rankingIndex).getPopulation().size();
            if ((pop.getPopulation().size() +
                    rank.getSubFront(rankingIndex).getPopulation().size()) <=
                    getPopulationSize()) {
                var front = rank.getSubFront(rankingIndex).getPopulation();
                for (int i = 0; i < front.size(); i++) {
                    pop.add(front.get(i));
                }
            }
            rankingIndex++;
        }

        EnvironmentalSelection<I> sel =
                new EnvironmentalSelection<>(fronts, getPopulationSize(),
                                             getReferencePointsCopy(),
                                             getTestProblem()
                                                     .getNumberOfObjectives());
        pop = sel.execute(pop);
        return pop;
    }


}