package Matrices;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;

/**
 * The 'Big' List
 * in Task1, this list will contain all SCC that we find after traversing each index (not repetitive)
 */

public class ConnectedComponents implements Serializable {
    private HashSet<ConnectedComponent> set = new HashSet<>();

    public HashSet<ConnectedComponent> getSet() {
        return set;
    }

    public boolean containsIndex(Index index) {
        for (var cc : this.set) {
            if (cc.hasIndex(index)) {
                return true;
            }
        }

        return false;
    }

    public void addToSet(ConnectedComponent cc) {
        var set = cc.getSet();
        for (var index : set) {
            if (this.containsIndex(index)) {
                return;
            }
        }
        this.set.add(cc);
    }

    @Override
    public String toString() {
        String s = "{ \n";
        for (var item : set) {
            s += "\t" + item + "\n";
        }
        s += "}";

        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectedComponents that = (ConnectedComponents) o;
        return Objects.equals(set, that.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(set);
    }

}


