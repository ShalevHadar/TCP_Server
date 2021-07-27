package Matrices;

import java.io.Serializable;
import java.util.Objects;


public class Index implements Serializable, Comparable<Index> {
    public int row;    // default value = 0
    public int column; // default value = 0

    /**
     * In this class, we will use all the functionality a should have
     * Every Function is self-explanatory.
     * @param aRow
     * @param aColumn
     */
    public Index(int aRow, int aColumn) {
        this.row = aRow;
        this.column = aColumn;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }


    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return row == index.row &&
                column == index.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }



    @Override
    public int compareTo(Index o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return 1;
        Index index = (Index) o;
        if (this.row > index.row) return 1;
        else if (this.row == index.row) return Integer.compare(this.column,index.column);
        else return -1;
    }

    public boolean checkIndicesPath(Index i1, Index i2)
    {
        if (Math.abs(i1.row - i2.row) > 1)
            return false;
        if (Math.abs(i1.column - i2.column) > 1)
            return false;
        else return true;
    }
}