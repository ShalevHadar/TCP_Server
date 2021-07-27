package Matrices;

import TcpServer.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IsMySubmarineOK {

    public IsMySubmarineOK() {
    }

    /** the fixMySquare method will check if there are squares from each top-left index with '1' value.
     * motivation: if from the top-left index you cannot complete a rectangle (1x1 is a rectangle) it means that the
     * 'ship' isn't proper according to rules.
     * @param matrix
     * @param root
     * @param notVisited
     * @param visited
     * @return
     */
    public boolean fixMyRectangle(Matrix matrix, Index root, List<Index> notVisited, List<Index> visited)
    {

        int i = root.row;
        int j = root.column;
        Index tempIndex = new Index(i,j);

        /*
        Checking the index from left to right.
        We will mark all indices we've visited and when we see a '0' value we will stop.
        we will use @ArrayIndexOutOfBoundsException as a safety net that we don't go out of bounds.
         */
    try {
            while (matrix.getValue(tempIndex) == 1)
            {
                visited.add(new Index(i, j));
                notVisited.remove(new Index(i, j));
                j++;
                tempIndex.setColumn(j);
            }}catch (ArrayIndexOutOfBoundsException ignored) { }
            int lastNumColumns = j;
            j = root.column;
            tempIndex = new Index(root.row, root.column);

            if (visited.contains(root))
            {
                visited.remove(root);
            }

        /*
        Checking the index from top to bottom.
        We will mark all indices we've visited and when we see a '0' value we will stop.
        we will use @ArrayIndexOutOfBoundsException as a safety net that we don't go out of bounds.
         */
        try{
            while (matrix.getValue(tempIndex) == 1)
            {
                visited.add(new Index(i, j));
                notVisited.remove(new Index(i, j));
                i++;
                tempIndex.setRow(i);
            }}catch (ArrayIndexOutOfBoundsException ignored) { }

            j = lastNumColumns;
            int lastNumRows = i;
            tempIndex = new Index(root.row + 1, root.column + 1);

            /*
            Completing the square
            if 1 index doesn't have '1' value, return false cause there isn't a full square then we know the ship
            isn't 'good'.
             */
            for (int k = root.row + 1; k < i; k++)
            {
                tempIndex.setRow(k);
                for (int l = root.column + 1; l < j; l++)
                {
                    tempIndex.setColumn(l);
                    if (matrix.getValue(tempIndex) == 1) {
                        visited.add(new Index(k, l));
                        notVisited.remove(new Index(k, l));
                    } else {
                        return false;
                    }
                }
            }

        return true;
    }

    /**
     * The method is divied into 3 parts
     *  Part1: checking if the input is bad - we will look for specific pattern which indicate that there are bad ship
     *      run time O(N).
     *  Part2: Set all indices to be 'NotVisited'
     *  Part3: count how many rectangles we got. each rectangle will represent a ship. if the function will return
     *  1 false it means the input is bad.
     * @param matrix
     * @return
     */
    public int[] findMySubmarine(Matrix matrix){

        // we use AtomicInteger since we wrapped the method with a Runnable.
        AtomicInteger nopeBig = new AtomicInteger();
        AtomicInteger yeaBig = new AtomicInteger();
        List<Index> notVisited = new ArrayList<>();
        List<Index> visited = new ArrayList<>();
        int[] returnArray = new int[2];

        // Part 1: Check for 'bad' parts.
        for (int i = 0; i < matrix.getMatrixRow(); i++)
        {
            for (int j = 0; j < matrix.getMatrixColumn(); j++)
            {
                try{
                if(matrix.getValue(new Index(i,j)) == 1 && matrix.getValue(new Index(i+1,j+1)) == 1)
                {
                    if(matrix.getValue(new Index(i,j+1)) == 0)
                    {
                        returnArray[1] = 2;
                        return returnArray;
                    }
                    else if(matrix.getValue(new Index(i+1, j)) == 0)
                    {
                        returnArray[1] = 2;
                        return returnArray;
                    }
                }}catch (IndexOutOfBoundsException ignored){}
                try {
                    if (matrix.getValue(new Index(i, j + 1)) == 1 && matrix.getValue(new Index(i + 1, j)) == 1) {
                        if (matrix.getValue(new Index(i, j)) == 0) {
                            returnArray[1] = 2;
                            return returnArray;
                        } else if (matrix.getValue(new Index(i + 1, j + 1)) == 0) {
                            returnArray[1] = 2;
                            return returnArray;
                        }
                    }
                }catch (IndexOutOfBoundsException ignored){}
            }
        }




        // Part 2: setting all the indices in the matrix to be 'NotVisited'
        for (int i = 0; i < matrix.getMatrixRow(); i++)
        {
            for (int j = 0; j < matrix.getMatrixColumn(); j++)
            {
                var currentIndex = new Index(i,j);
                notVisited.add(currentIndex);
            }
        }


        // Part3: check rectangle in the matrix, each one you find raise @yeaBig by 1
        for (int i = 0; i < matrix.getMatrixRow(); i++)
        {
            for (int j = 0; j < matrix.getMatrixColumn(); j++)
            {
                var tempIndex = new Index(i,j);
                if (matrix.getValue(new Index(i,j)) == 1 && notVisited.contains(new Index(i,j)))
                {
                    var singleThreadSquare = new Thread(() -> {
                        if (!fixMyRectangle(matrix, tempIndex, notVisited, visited))
                            nopeBig.getAndIncrement();
                        else
                            yeaBig.getAndIncrement();
                        System.out.println(Thread.currentThread().getName());
                    });
                    singleThreadSquare.start();
                    try {
                        singleThreadSquare.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        double yea = yeaBig.doubleValue();
        double nope = nopeBig.doubleValue();
        returnArray[0] = (int)yea;
        returnArray[1] = (int)nope;

        /*
        i've decided to use a 'simple' array:
            - the first cell will represent the 'good' ships
            - the second cell will represent the 'bad' ship
        if nope > 0 then submarine isn't right, else yea = number of 'proper' marine.
         */
        return returnArray;
    }

    public static void main(String[] args) {
//        // not ok - 3 submarines but no gap
//        int[][] submarine1 = {
//                {1,1,0,1,1},
//                {1,0,0,1,1},
//                {1,0,0,1,1}
//        };
//
//        //not ok - 2 ok 1 bad
//        int[][] submarine2 = {
//                {1,0,0,1,1},
//                {1,0,0,1,1},
//                {0,1,0,1,1}
//        };
//
//        // ok 2 submarines
//        int[][] submarine3 = {
//                {1,0,0,1,1},
//                {1,0,0,1,1},
//                {1,0,0,1,1}
//        };
//
//        // ok 3 submarines
//        int[][] submarine4 = {
//                {1,1,0,1,1},
//                {0,0,0,1,1},
//                {1,1,0,1,1}
//        };
//
//        Matrix mat = new Matrix(submarine4);
//        IsMySubmarineOK checking = new IsMySubmarineOK();
//        int[] array = checking.findMySubmarine(mat);
//        if(array[1] > 0)
//            System.out.println("bad input The submarine isn't good");
//        else
//            System.out.println("The input is valid and there are "+ array[0] +" 'proper' ships");

    }
}
