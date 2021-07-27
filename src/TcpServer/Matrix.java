package TcpServer;
import Matrices.*;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import Matrices.Index;

public class Matrix implements Serializable {

    int[][] primitiveMatrix;

    public Matrix(int[][] oArray){
        List<int[]> list = new ArrayList<>();
        for (int[] row : oArray) {
            int[] clone = row.clone();
            list.add(clone);
        }
        primitiveMatrix = list.toArray(new int[0][]);
    }
    public void printMatrix(){
        for (int[] row : primitiveMatrix) {
            String s = Arrays.toString(row);
            System.out.println(s);
        }
    }

    public int getMatrixRow(){
        return this.primitiveMatrix.length;
    }

    public int getMatrixColumn(){
        return this.primitiveMatrix[0].length;
    }

    public final int[][] getPrimitiveMatrix() {
        return primitiveMatrix;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : primitiveMatrix) {
            stringBuilder.append(Arrays.toString(row));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public Collection<Index> getNeighborsUDLR(final Index index) {
        Collection<Index> list = new ArrayList<>();
        int extracted = -1;
        try {
            extracted = primitiveMatrix[index.row + 1][index.column];
            list.add(new Index(index.row + 1, index.column));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            extracted = primitiveMatrix[index.row][index.column + 1];
            list.add(new Index(index.row, index.column + 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            extracted = primitiveMatrix[index.row - 1][index.column];
            list.add(new Index(index.row - 1, index.column));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            extracted = primitiveMatrix[index.row][index.column - 1];
            list.add(new Index(index.row, index.column - 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return list;
    }

    public Collection<Index> getNeighbors(final Index index){
        Collection<Index> list = new ArrayList<>();
        int extracted = -1;
        try{
            extracted = primitiveMatrix[index.row+1][index.column];
            list.add(new Index(index.row+1,index.column));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row][index.column+1];
            list.add(new Index(index.row,index.column+1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row-1][index.column];
            list.add(new Index(index.row-1,index.column));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row][index.column-1];
            list.add(new Index(index.row,index.column-1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            // up-left
            extracted = primitiveMatrix[index.getRow()-1][index.getColumn()-1];
            list.add(new Index(index.getRow()-1,index.getColumn()-1));
        }catch(ArrayIndexOutOfBoundsException ignored){}
        try{
            //up-right
            extracted = primitiveMatrix[index.getRow()-1][index.getColumn()+1];
            list.add(new Index(index.getRow()-1,index.getColumn()+1));
        }catch(ArrayIndexOutOfBoundsException ignored){}
        try{
            // down-left
            extracted = primitiveMatrix[index.getRow()+1][index.getColumn()-1];
            list.add(new Index(index.getRow()+1,index.getColumn()-1));
        }catch(ArrayIndexOutOfBoundsException ignored){}
        try{
            //down-right
            extracted = primitiveMatrix[index.getRow()+1][index.getColumn()+1];
            list.add(new Index(index.getRow()+1,index.getColumn()+1));
        }catch(ArrayIndexOutOfBoundsException ignored){}
        return list;

    }



    public Collection<Index> getNeighborsCross(final Index index){
        Collection<Index> list = new ArrayList<>();
        int extracted;
        try{
            // up-left
            extracted = primitiveMatrix[index.getRow()-1][index.getColumn()-1];
            list.add(new Index(index.getRow()-1,index.getColumn()-1));
        }catch(IndexOutOfBoundsException outOfBoundsException){}
        try{
            //up-right
            extracted = primitiveMatrix[index.getRow()-1][index.getColumn()+1];
            list.add(new Index(index.getRow()-1,index.getColumn()+1));
        }catch(IndexOutOfBoundsException outOfBoundsException){}
        try{
            // down-left
            extracted = primitiveMatrix[index.getRow()+1][index.getColumn()-1];
            list.add(new Index(index.getRow()+1,index.getColumn()-1));
        }catch(IndexOutOfBoundsException outOfBoundsException){}
        try{
            //down-right
            extracted = primitiveMatrix[index.getRow()+1][index.getColumn()+1];
            list.add(new Index(index.getRow()+1,index.getColumn()+1));
        }catch(IndexOutOfBoundsException outOfBoundsException){}
        return list;
    }


    public int getValue(Index index) {
        return primitiveMatrix[index.row][index.column];
    }

    public Collection<Index> getReachables(Index index) {
        ArrayList<Index> filteredIndices = new ArrayList<>();
        this.getNeighbors(index).stream().filter(i-> getValue(i)==1)
                .map(neighbor->filteredIndices.add(neighbor)).collect(Collectors.toList());
        return filteredIndices;
    }

    public Collection<Index> getReachablesULDR(Index index) {
        ArrayList<Index> filteredIndices = new ArrayList<>();
        this.getNeighborsUDLR(index).stream().map(neighbor->filteredIndices.add(neighbor)).collect(Collectors.toList());
        return filteredIndices;
    }



    public static void main(String[] args) {
    }
}