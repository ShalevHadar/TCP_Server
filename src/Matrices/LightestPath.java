package Matrices;

import TcpServer.Matrix;
import java.util.*;

public class LightestPath {

    public LightestPath() {
    }


    // Utility function to check if current vertex is already present in path
    // if Index is contain inside path return false, else true;
    public static boolean isNotVisited(Index index, List<Index> path)
    {
        int size = path.size();
        for(int i = 0; i < size; i++)
            if (path.contains(index))
                return false;

        return true;
    }

    /**
     *We will use this method to find the lightest path from a starting index to a detention one.
     * using @tempList as a list that contain all the paths
     * the @returnList will contain only the paths from srt to dest.
     * the @queue to hold items to be added to paths.
     * we will use the @marker param for a flag if have seen a lighter path then what we see.
     * if path is equal we will just add it @returnList.
     * @param matrix
     * @param start
     * @param dest
     * @return
     */

    public LinkedList<List<Index>> findLightestPath(Matrix matrix, Index start, Index dest) {
        double marker = Double.POSITIVE_INFINITY;
        LinkedList<List<Index>> tempList = new LinkedList<>();
        LinkedList<List<Index>> returnList = new LinkedList<>();
        Queue<List<Index>> queue = new LinkedList<>();
        List<Index> path = new ArrayList<>();
        path.add(start);
        queue.offer(path);

        while (!queue.isEmpty()) {
            path = queue.poll();
            Index lastNode = path.get(path.size() - 1);
            int sum = 0;
            if (lastNode.equals(dest))
            {
                for (Index item : path) {
                    sum += matrix.getValue(item);
                }
                if (sum == marker) {
                    marker = sum;
                    returnList.add(path);
                    System.out.println("sum: " + sum);
                }
                if (sum < marker) {
                    returnList.clear();
                    marker = sum;
                    returnList.add(path);
                }
            }

            List<Index> reachable = (List<Index>) matrix.getReachablesULDR(lastNode);
            tempList.add(reachable);

            List<Index> LastListOfNodes = tempList.get(tempList.size() - 1);

            for (int i = 0; i < LastListOfNodes.size(); i++) {
                if (isNotVisited(LastListOfNodes.get(i), path)) {
                    List<Index> newPath = new ArrayList<>(path);
                    newPath.add(LastListOfNodes.get(i));
                    queue.offer(newPath);
                }
            }
        }

        return returnList;
    }

    public static void main(String[] args)
    {
//        int[][] mat = {
//                {100,100,100},
//                {100,-300,100},
//                {500,900,300}
//        };
//
//        Matrix matrix = new Matrix(mat);
//        Index index1 = new Index(2,0);
//        Index index2 = new Index(2,2);
//        LightestPath tempy = new LightestPath();
//        matrix.printMatrix();
//        LinkedList<List<Index>> myList = tempy.findLightestPath(matrix,index1,index2);
//        System.out.println(myList);
    }
}

