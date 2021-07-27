package Matrices;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*

Abstract Class - abstract class is a special class that defines at least one abstract method
abstract method is an unimplemented method
AbstractMatrix is the superclass of all classes representing a matrix
it provides common data structures and common functionality that all implementing class require

 */
public abstract class AbstractMatrix {
    int[][] intArray;

    /**
     * This constructor gets num of rows and columns, initializes 2d array
     * and populates it with random values between 0 and 1
     * @param nRows
     * @param nColumns
     */
    public AbstractMatrix(int nRows, int nColumns){
        intArray = new int[nRows][nColumns];
        Random generator = new Random();
        for(int row = 0; row < intArray.length; row++){
            for(int column = 0;column < intArray[row].length;column++){
                intArray[row][column] = generator.nextInt(2);
            }
        }
    }

    public int val(Index index){
        return intArray[index.getRow()][index.getColumn()];
    }

    public abstract List<Index> neighbors(Index index);
    public abstract String demoFunction();

    public void printMatrix(){
        for(int[] row:intArray){
            System.out.println(Arrays.toString(row));
        }
        System.out.println("\n");
    }

    @Override
    public String toString() {
        StringBuilder matrixString = new StringBuilder();
        for (int[] row: intArray){
            matrixString.append(Arrays.toString(row));
            matrixString.append("\n");
        }
        return matrixString.toString();
    }

    public void printMatrix2(){
        for(int row=0;row< intArray.length;row++){
            System.out.println(Arrays.toString(intArray[row]));
        }
        System.out.println("\n");
    }

}
