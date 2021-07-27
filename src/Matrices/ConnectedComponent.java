package Matrices;

import java.io.Serializable;
import java.util.*;

public class ConnectedComponent implements Serializable, Comparable {
    private HashSet<Index> set = new HashSet<>();

    /**
     * This Constructor gets a HashSet of index and build a object called Connected Component
     * the 'CC' will be the smaller set out of a big set ('CCS')
     * in Task1, we will represent each SCC as a CC.
     * @param set
     */
    public ConnectedComponent(HashSet<Index> set) {
        this.set = set;
    }

    @Override
    public String toString() {
        return ""+set+"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectedComponent that = (ConnectedComponent) o;
        return Objects.equals(set, that.set);
    }



    @Override
    public int hashCode() {
        return Objects.hash(set);
    }

    public boolean hasIndex(Index index) {
        return this.set.contains(index);
    }

    public HashSet<Index> getSet() {
        return set;
    }

    public int compareTo(Object o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return 1;
        ConnectedComponent obj = (ConnectedComponent) o;
        if (this.set.size() > obj.set.size()) return -1;
        else if (this.set.size() == obj.set.size()) return 0;
        else return 1;
    }

    // Functions without use for now, but might be used if needed

    public void addToSet(Index index) {
        this.set.add(index);
    }

    public boolean contain(Index i) {
        return this.set.contains(i);
    }

    public int sizeOfSet()
    {
        int counter = 0;
        for (Index i : this.set) counter++;
        return counter;
    }

}
