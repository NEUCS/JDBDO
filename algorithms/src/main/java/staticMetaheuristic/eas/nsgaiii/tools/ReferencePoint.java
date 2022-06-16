package staticMetaheuristic.eas.nsgaiii.tools;

import individual.Individual;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import random.MRandom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReferencePoint<I extends Individual<?>> {
    public List<Double> position;
    private int memberSize;
    private List<Pair<I, Double>> potentialMembers;

    public ReferencePoint() {
    }

    /**
     * Constructor
     */
    public ReferencePoint(int size) {
        position = new ArrayList<>();
        for (int i = 0; i < size; i++)
            position.add(0.0);
        memberSize = 0;
        potentialMembers = new ArrayList<>();
    }

    public ReferencePoint(ReferencePoint<I> point) {
        position = new ArrayList<>(point.position.size());
        for (Double d : point.position) {
            position.add(new Double(d));
        }
        memberSize = 0;
        potentialMembers = new ArrayList<>();
    }

    public void generateReferencePoints(List<ReferencePoint<I>> referencePoints,
            int numberOfObjectives, int numberOfDivisions) {

        ReferencePoint<I> refPoint = new ReferencePoint<>(numberOfObjectives);
        generateRecursive(referencePoints, refPoint, numberOfObjectives,
                          numberOfDivisions, numberOfDivisions, 0);
    }

    private void generateRecursive(List<ReferencePoint<I>> referencePoints,
            ReferencePoint<I> refPoint, int numberOfObjectives, int left,
            int total, int element) {
        if (element == (numberOfObjectives - 1)) {
            refPoint.position.set(element, (double) left / total);
            referencePoints.add(new ReferencePoint<>(refPoint));
        } else {
            for (int i = 0; i <= left; i += 1) {
                refPoint.position.set(element, (double) i / total);

                generateRecursive(referencePoints, refPoint, numberOfObjectives,
                                  left - i, total, element + 1);
            }
        }
    }

    public List<Double> pos() {
        return this.position;
    }

    public int MemberSize() {
        return memberSize;
    }

    public boolean HasPotentialMember() {
        return potentialMembers.size() > 0;
    }

    public void clear() {
        memberSize = 0;
        this.potentialMembers.clear();
    }

    public void AddMember() {
        this.memberSize++;
    }

    public void AddPotentialMember(I member_ind, double distance) {
        this.potentialMembers
                .add(new ImmutablePair<I, Double>(member_ind, distance));
    }

    public I FindClosestMember() {
        double minDistance = Double.MAX_VALUE;
        I closetMember = null;
        for (Pair<I, Double> p : this.potentialMembers) {
            if (p.getRight() < minDistance) {
                minDistance = p.getRight();
                closetMember = p.getLeft();
            }
        }

        return closetMember;
    }

    public I RandomMember() {
        int index = this.potentialMembers.size() > 1 ? MRandom.getInstance()
                .nextInt(0, this.potentialMembers.size() - 1) : 0;
        return this.potentialMembers.get(index).getLeft();
    }

    public void RemovePotentialMember(I solution) {
        Iterator<Pair<I, Double>> it = this.potentialMembers.iterator();
        while (it.hasNext()) {
            if (it.next().getLeft().equals(solution)) {
                it.remove();
                break;
            }
        }
    }
}