package TcpServer;


import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import Matrices.*;
import Tasks.Client;


public class MatrixIHandler<T> implements IHandler, Runnable {
    private Matrix matrix;
    private Index start,end;


    /*
    to clear data members between clients (if same instance is shared among clients/tasks)
     */
    private void resetParams(){
        this.matrix = null;
        this.start = null;
        this.end = null;
    }


    @Override
    public void handle(InputStream fromClient, OutputStream toClient)
            throws IOException, ClassNotFoundException {

        // In order to read either objects or primitive types we can use ObjectInputStream
        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);
        // In order to write either objects or primitive types we can use ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);
        this.resetParams(); // in order to use same handler between tasks/clients

        boolean doWork = true;
        while(doWork){
            /*
             Use switch-case in order to get commands from client
             - client sends a 2D array
             - client send start index
             - client send end index
             - client sends an index and wished to get neighbors
             - client sends an index and wished to get reachable indices
             */

            // client send a verbal command
            switch(objectInputStream.readObject().toString()){

                case "ComputeMatrix":{
                    int[][] primitiveMatrix = (int[][])objectInputStream.readObject();
                    System.out.println("Server: Got 2d array from client");
                    this.matrix = new Matrix(primitiveMatrix);
                    this.matrix.printMatrix();
                    break;
                }

                case "neighbors":{
                    Index findNeighborsIndex = (Index)objectInputStream.readObject();
                    List<Index> neighbors = new ArrayList<>();
                    if(this.matrix!=null){
                        neighbors.addAll(this.matrix.getNeighbors(findNeighborsIndex));
                        // print result in server
                        System.out.println("neighbors of " + findNeighborsIndex + ": " + neighbors);
                        // send to socket's OutputStream
                        objectOutputStream.writeObject(neighbors);
                    }
                    break;
                }

                case "neighborsCross":{
                    Index findNeighborsIndex = (Index)objectInputStream.readObject();
                    List<Index> neighbors = new ArrayList<>();
                    if(this.matrix!=null){
                        neighbors.addAll(this.matrix.getNeighbors(findNeighborsIndex));
                        neighbors.addAll(this.matrix.getNeighborsCross(findNeighborsIndex));
                        // print result in server
                        System.out.println("neighbors of " + findNeighborsIndex + ": " + neighbors);
                        // send to socket's OutputStream
                        objectOutputStream.writeObject(neighbors);
                    }
                    break;
                }

                case "reachables":{
                    Index findNeighborsIndex = (Index)objectInputStream.readObject();
                    List<Index> reachables = new ArrayList<>();
                    if(this.matrix!=null){
                        reachables.addAll(this.matrix.getReachables(findNeighborsIndex));
                        // sorting
                        //sortMylist(reachables);
                        // print result in server
                        System.out.println("reachables of " + findNeighborsIndex + ": " + reachables);
                        // send to socket's OutputStream
                        objectOutputStream.writeObject(reachables);
                    }
                    break;
                }

                case "traverse":{
                    Client.threadPoolMine.submit(() -> {
                    // declaring on traversal matrix and applying the current matrix.
                    TraversableMatrix myTraversable = new TraversableMatrix(this.matrix);

                    // get all nodes -> set S
                    // traverse from node one by one
                    // when traversing remove traversed nodes from list L
                    if (this.matrix != null) {
                        // creating set of Index that includes all of the indices with '1' value and put them in S.
                        Set<Index> S = new HashSet<Index>();
                        addAllIndexesToSet(S);

                        // create the 'Big' set of connected components
                        ConnectedComponents ccs = new ConnectedComponents();

                        // for each index in S do the following:
                        for (var index : S)
                        {
                            // if the index is already in the 'Big' set >> the SCC was already checked therefore, you
                            // shouldn't check this index >> return.
                            if (ccs.containsIndex(index)) continue;

                            var runningCCS = new Thread(() -> {
                                // if he's not there, set the index as a starting point into the new matrix.
                                myTraversable.setStartIndex(index);
                                ThreadLocalDfsVisit<Index> algo = new ThreadLocalDfsVisit<>();

                                // save the List of Indices you got from traversing the index on the matrix in @traversal param
                                // add the list you got into a new 'small' set.
                                var traversal = algo.traverse(myTraversable);
                                var cc = new ConnectedComponent(new HashSet<Index>((Collection<? extends Index>) traversal));
                                // add the 'small' set to the 'Big' set.
                                ccs.addToSet(cc);
                                System.out.println(Thread.currentThread().getName());
                            });

                            runningCCS.start();

                            // since we're using join command, we don't need to use Locks.
                            try {
                                runningCCS.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        List<List<Index>> connectedComponents = new ArrayList<>();
                        for (var cc : ccs.getSet()) {
                            var component = new ArrayList<>(cc.getSet());
                            Collections.sort(component);
                            connectedComponents.add(component);
                        }

                        Collections.sort(connectedComponents, (a,b) -> {
                            if (a.size() > b.size()) {
                                return -1;
                            } else if (a.size() == b.size()) {
                                return 0;
                            } else {
                                return 1;
                            }
                        });

                        try {
                            objectOutputStream.writeObject(connectedComponents);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    });
                    break;
                }

                case "start index":{
                    this.start = (Index)objectInputStream.readObject();
                    break;
                }

                case "end index":{
                    this.end = (Index)objectInputStream.readObject();
                    break;
                }

                case "ShortestPath":{
                    var task2Thread = new Thread(() -> {
                    String teller = "";
                    List<List<Index>> mainList = new ArrayList<>();
                    TraversableMatrix myTraversable = new TraversableMatrix(this.matrix);
                    myTraversable.setStartIndex(this.start);
                    ThreadLocalDfsVisit<Index> algo = new ThreadLocalDfsVisit<>();
                    var traversal = algo.traverse(myTraversable);
                    var singleSCC = new ConnectedComponent(new HashSet<Index>(traversal));

                    if(singleSCC.hasIndex(this.end))
                    {
                        // implementing BFS on traversable (setSCC)
                        BFS bfs = new BFS();
                        mainList = bfs.findpaths(this.start,this.end,this.matrix);
                        try {
                            objectOutputStream.writeObject(mainList);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    else
                    {
                        teller = "There isn't a path from "+this.start+" to "+this.end;
                        try {
                            objectOutputStream.writeObject(teller);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                        System.out.println(Thread.currentThread().getName());
                });

                    task2Thread.start();
                    break;
                }
                case "stop":{
                    doWork = false;
                    break;
                }

                case "battlefield":{
                    IsMySubmarineOK checker = new IsMySubmarineOK();
                    int[] lastArray = checker.findMySubmarine(this.matrix);
                    objectOutputStream.writeObject(lastArray);
                    break;
                }

                case "LightestOfPaths":{
                    var task4Thread = new Thread(() -> {
                    LightestPath lp = new LightestPath();
                    LinkedList<List<Index>> listOfLP = lp.findLightestPath(this.matrix,this.start,this.end);
                    System.out.println(Thread.currentThread().getName());
                        try {
                            objectOutputStream.writeObject(listOfLP);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    task4Thread.start();
                    break;

                }
            }
        }
    }

    /**
     *
     * @param myTraversable
     * @param ccs
     * @param index
     */

    /**
     * Gets a set, Inserts all indexes with a '1'
     * @param S The set to add them to
     */
    private void addAllIndexesToSet(Set<Index> S) {
        for (int i = 0; i < this.matrix.getMatrixRow(); i++)
        {
            for (int j = 0; j < this.matrix.getMatrixColumn(); j++)
            {
                var currentIndex = new Index(i,j);
                if(this.matrix.getValue(currentIndex) == 1)
                {
                    S.add(currentIndex);
                }
            }
        }
    }

    @Override
    public void run() {

    }

}