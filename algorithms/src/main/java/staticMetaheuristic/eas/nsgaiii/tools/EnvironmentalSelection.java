package staticMetaheuristic.eas.nsgaiii.tools;

import individual.Individual;
import operators.selection.Selection;
import population.Population;
import random.MRandom;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentalSelection<I extends Individual<?>>
        implements Selection<Population<I>, Population<I>> {
    private List<Population<I>> fronts;
    private int numberOfSelectedIndividual;
    private List<ReferencePoint<I>> referencePointList;
    private int numberOfObjectives;


    public EnvironmentalSelection(List<Population<I>> fronts,
            int numberOfSelectedIndividual,
            List<ReferencePoint<I>> referencePointList,
            int numberOfObjectives) {
        this.fronts = fronts;
        this.numberOfSelectedIndividual = numberOfSelectedIndividual;
        this.referencePointList = referencePointList;
        this.numberOfObjectives = numberOfObjectives;
    }

    public List<Double> translateObjectives(Population<I> population) {
        List<Double> ideal_point = new ArrayList<>(this.numberOfObjectives);

        for (int i = 0; i < this.numberOfObjectives; i++) {
            double min = Double.MAX_VALUE;
            for (int j = 0; j < this.fronts.get(0).getPopulation().size();
                    j++) {
                min = Math.min(min, fronts.get(0).getIndividual(0)
                        .getObjectiveValue(i));
            }
            ideal_point.add(min);

            for (Population<I> pop : fronts) {
                for (I individual : pop.getPopulation()) {
                    if (i == 0) {
                        individual.setAttribute("EnvironmentalSelection",
                                                new ArrayList<Double>());
                    }
                    var attr = (List<Double>) individual
                            .getAttribute("EnvironmentalSelection");
                    attr.add(individual.getObjectiveValue(i) - min);
                }
            }
        }
        return ideal_point;
    }

    private double ASF(I individual, int index) {
        double maxRatio = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < individual.getNumberOfObjectives(); i++) {
            double weight = (index == i) ? 1.0 : 0.000001;
            maxRatio = Math.max(maxRatio,
                                individual.getObjectiveValue(i) / weight);
        }
        return maxRatio;
    }

    private Population<I> findExtremePoints(Population<I> population) {
        Population<I> extremePoints = new Population<>();
        I minInidividual = null;
        for (int i = 0; i < this.numberOfObjectives; i++) {
            double minASF = Double.MAX_VALUE;
            for (I individual : this.fronts.get(0).getPopulation()) {
                double asf = ASF(individual, i);
                if (asf < minASF) {
                    minASF = asf;
                    minInidividual = individual;
                }
            }
            extremePoints.add(minInidividual);
        }
        return extremePoints;
    }

    public List<Double> guassianElimination(List<List<Double>> A,
            List<Double> b) {
        List<Double> x = new ArrayList<>();
        int N = A.size();
        for (int i = 0; i < N; i++) {
            A.get(i).add(b.get(i));
        }

        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                double ratio = A.get(j).get(i) / A.get(i).get(i);
                for (int k = 0; k < A.get(i).size(); k++) {
                    A.get(j).set(k, A.get(j).get(k) - A.get(i).get(k) * ratio);
                }
            }
        }

        for (int i = 0; i < N; i++) {
            x.add(0.0);
        }

        for (int i = N - 1; i >= 0; i--) {
            for (int j = i + 1; j < N; j++) {
                A.get(i).set(N, A.get(i).get(N) - A.get(i).get(j) * x.get(j));
            }
            x.set(i, A.get(i).get(N) / A.get(i).get(i));
        }
        return x;
    }

    public List<Double> constructHyperplane(Population<I> population,
            Population<I> extremePoints) {
        boolean duplicate = false;
        for (int i = 0; !duplicate && i < extremePoints.getPopulation().size();
                i++) {
            for (int j = i + 1;
                    !duplicate && j < extremePoints.getPopulation().size();
                    j++) {
                duplicate = extremePoints.getIndividual(i)
                        .equals(extremePoints.getIndividual(j));
            }
        }

        List<Double> intercepts = new ArrayList<>();

        if (duplicate) {
            for (int i = 0; i < this.numberOfObjectives; i++) {
                intercepts.add(extremePoints.getIndividual(i)
                                       .getObjectiveValue(i));
            }
        } else {
            List<Double> b = new ArrayList<>();
            for (int i = 0; i < this.numberOfObjectives; i++) {
                b.add(1.0);
            }

            List<List<Double>> A = new ArrayList<>();
            for (I individual : extremePoints.getPopulation()) {
                List<Double> aux = new ArrayList<>();
                for (int i = 0; i < this.numberOfObjectives; i++) {
                    aux.add(individual.getObjectiveValue(i));
                }
                A.add(aux);
            }
            List<Double> x = guassianElimination(A, b);
            for (int i = 0; i < this.numberOfObjectives; i++) {
                intercepts.add(1.0 / x.get(i));
            }
        }
        return intercepts;
    }

    public void normalizeObjectives(Population<I> population,
            List<Double> intercepts, List<Double> ideal_point) {
        for (int i = 0; i < this.fronts.size(); i++) {
            for (I individual : this.fronts.get(i).getPopulation()) {
                for (int j = 0; j < this.numberOfObjectives; j++) {
                    List<Double> obj = (List<Double>) individual
                            .getAttribute("EnvironmentalSelection");
                    if (Math.abs(
                            Math.abs(intercepts.get(j) - ideal_point.get(j))) >
                            10e-10) {
                        obj.set(j, obj.get(j) /
                                (intercepts.get(j) - ideal_point.get(j)));
                    }else {
                        obj.set(j, obj.get(j) / (10e-10));
                    }
                }
            }
        }
    }

    public double perpendicularDistance(List<Double> direction,
            List<Double> point) {
        double numerator = 0, denominator = 0;
        for (int i = 0; i < direction.size(); i += 1) {
            numerator += direction.get(i) * point.get(i);
            denominator += Math.pow(direction.get(i), 2.0);
        }
        double k = numerator / denominator;

        double d = 0;
        for (int i = 0; i < direction.size(); i += 1) {
            d += Math.pow(k * direction.get(i) - point.get(i), 2.0);
        }
        return Math.sqrt(d);
    }

    public void associate() {
        for (int i = 0; i < fronts.size(); i++) {
            for (I individual : fronts.get(i).getPopulation()) {
                int min_rp = -1;
                double min_dist = Double.MAX_VALUE;
                for (int r = 0; r < this.referencePointList.size(); r++) {
                    double d = perpendicularDistance(
                            this.referencePointList.get(r).position,
                            (List<Double>) individual
                                    .getAttribute("EnvironmentalSelection"));
                    if (d < min_dist) {
                        min_dist = d;
                        min_rp = r;
                    }
                }
                if (i + 1 != fronts.size()) {
                    this.referencePointList.get(min_rp).AddMember();
                } else {
                    this.referencePointList.get(min_rp)
                            .AddPotentialMember(individual, min_dist);
                }
            }
        }
    }

    public int FindNicheReferencePoint() {
        int min_size = Integer.MAX_VALUE;
        for (ReferencePoint<I> referencePoint : this.referencePointList)
            min_size = Math.min(min_size, referencePoint.MemberSize());

        List<Integer> min_rps = new ArrayList<>();

        for (int r = 0; r < this.referencePointList.size(); r ++) {
            if (this.referencePointList.get(r).MemberSize() == min_size) {
                min_rps.add(r);
            }
        }
        return min_rps.get(min_rps.size() > 1 ? MRandom.getInstance()
                .nextInt(0, min_rps.size() - 1) : 0);
    }

    public I SelectClusterMember(ReferencePoint<I> rp) {
        I chosen = null;
        if (rp.HasPotentialMember()) {
            if (rp.MemberSize() == 0)
            {
                chosen = rp.FindClosestMember();
            } else {
                chosen = rp.RandomMember();
            }
        }
        return chosen;
    }

    @Override
    public Population<I> execute(Population<I> population) {
        if (population.getPopulation().size() == this.numberOfSelectedIndividual){
            return population;
        }
        List<Double> ideal_point = translateObjectives(population);
        Population<I> extremePoints = findExtremePoints(population);
        List<Double> intercepts = constructHyperplane(population, extremePoints);
        normalizeObjectives(population, intercepts,ideal_point);
        associate();

        while (population.getPopulation().size() < this.numberOfSelectedIndividual){
            int min = FindNicheReferencePoint();
            I individual =SelectClusterMember(this.referencePointList.get(min));
            if (individual == null){
                this.referencePointList.remove(min);
            }else {
                this.referencePointList.get(min).AddMember();
                this.referencePointList.get(min).RemovePotentialMember(individual);
                population.add(individual);
            }
        }
        return population;
    }
}