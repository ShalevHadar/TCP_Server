/**
 * this Class is meant for printing all the paths from starting index to destination one.
 */

package Matrices;

import TcpServer.Matrix;
import java.util.*;

public class BFS {
    public BFS() {
    }

    /*
    Utility function to check if current vertex is already present in path
    if Index is contain inside path return false, else true;

    */

    public static boolean isNotVisited(Index index, List<Index> path)
    {
        int size = path.size();
        for(int i = 0; i < size; i++)
            if (path.contains(index))
                return false;

        return true;
    }

    /*
    Utility function for finding paths in graph from source to destination
    list of Path, index start, index dest,
     */
    public LinkedList<List<Index>> findpaths(Index start, Index dest, Matrix matrix)
    {
        // set max value as infinity to use as a marker
        double max= Double.POSITIVE_INFINITY;

        /**
         * @lastList saving all paths from src to dest
         * @array holding all the paths size (number of indices)
         * @queue queue for the 'waiting' nodes
         * @path a list of indices that mark the path from src to dest
         */
        LinkedList<List<Index>> bigList = new LinkedList<>();
        LinkedList<List<Index>> tempList = new LinkedList<>();
        ArrayList<Integer> array = new ArrayList<>();
        Queue<List<Index> > queue = new LinkedList<>();
        List<Index> path = new ArrayList<>();
        path.add(start);
        queue.offer(path);

        // applying normal BFS algorithm
        while (!queue.isEmpty())
        {
            List<Index> tempSizelist = queue.element();
            path = queue.poll();

            /*
            if path.size() is bigger then max (our marker for maximum path size) which mean there's no need
            to check this path and in this method we gain efficiency
             */
            if(path.size()>max)
            {
                path.clear();
                break;
            }

            Index lastNode = path.get(path.size()-1);


            /*
            if the lats node in path == to dest --> we're done.
            if path.size() is <= then max --> we have new max size for paths, add path to @lastList and save the size
            of the path in array.

            else, clear the path so we don't use it anymore and remove path from queue so it won't be checked.
             */
            if(lastNode.equals(dest))
            {
                if (path.size() <= max)
                {
                    max=path.size();
                    array.add(path.size());
                    tempList.add(path);
                }
                else if (path.size() > max)
                {
                    path.clear();
                    queue.remove(path);
                    break;
                }
            }

            /*
            find all reachable indices from the last node in the path.
            add the it into reachPath and set as ArrayList so we can resize it.
             */
            List<Index> reachPath = (List<Index>) matrix.getReachables(lastNode);
            bigList.add(reachPath);
            List<Index> LastListOfNodes = (ArrayList)bigList.get(bigList.size()-1);



            /*
            forEach index in the last 'big' list, check the paths and add it to a new path.
            Then offer this new path to queue.
             */
            for (int i = 0; i < LastListOfNodes.size(); i++)
            {
                if (isNotVisited(LastListOfNodes.get(i), path))
                {
                    List<Index> newPath = new ArrayList<>(path);
                    newPath.add(LastListOfNodes.get(i));
                    queue.offer(newPath);
                }
            }
        }
        return tempList;
    }

    // Driver code
    public static void main(String[] args) {


//        BFS bfs = new BFS();
//        List<List<Index>> mainList = new ArrayList<>();
//        Index i1 = new Index(0,0);
//        Index i2 = new Index(2,5);
//        int[][] array = {
//                {1,1,0,0,0,0},
//                {1,1,1,1,1,1},
//                {1,1,1,1,1,1},
//                {1,1,1,1,1,1},
//                {1,1,1,1,1,1},
//
//        };
//        Matrix matrix = new Matrix(array);
//        mainList = bfs.findpaths(i1,i2,matrix);
//        matrix.printMatrix();
//        for(var item: mainList)
//        {
//            System.out.println(item);
//        }


    }
}

