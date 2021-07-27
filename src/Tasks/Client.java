/*
to run the application, run TcpServer first then run Client.
This application goal is to solve algorithmic problems by sending requests to the serve along with raw data.
such as Matrix, indices, etc.
and getting back a response from the server along with the solutions.

The app is based on Mr Meeseeks from Rick and Morty show, the bot can solve request and then disappear after doing so.
if you haven't seen the show, it's ok, you don't have to, the app will guide you though.

every task does something different, i gave 2 options:
    1. either run it as is and compute a new matrix for every task every time.
    2. uncomment //StaticMatrix function and insert the values you wish to check before the run.

for explanation on the tasks:
    1.Task1 - @runTask1: checking then printing all the SCC of a matrix, returning it as a linked list ordered by the amount of indices.
    2.Task2 - @runTask2: printing all the shortest path from index 1 to index 2 in a matrix.
    3.Task3 - @runTask3: checking then printing if an input of matrix is valid from certain criteria - ask me to find out what is it.
    4.Task4 - @runTask4: printing all the lightest paths from index 1 to index 2 in a matrix with negative/positive values.

This entire class is divided into methods to avoid duplicate of codes.
for example, the @ComputeMatrix method is used in every class.

 */
package Tasks;

import Matrices.*;
import TcpServer.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Client{

    public static final ExecutorService threadPoolMine = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        threadPoolMine.submit(()->{
            try{
                // using a socket to 'talk' with the server
                Socket socket =new Socket("127.0.0.1",8010);
                System.out.println("client: Created Socket");

                // using socket commands to communicate with the server
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream toServer=new ObjectOutputStream(outputStream);
                ObjectInputStream fromServer=new ObjectInputStream(inputStream);

                // intro to the App
                System.out.println("'This is a Meeseeks box, You make a a request > the Meeseeks fulfill the request and then stop existing'");
                System.out.println("I'm Mr. Meeseeks, Look at me !");
                System.out.println("Hi there human! please tell me which task your wanna run:");
                System.out.println("task1 / task2 / task3 / task4");

                // using a 'flag' to keep the socket opened.
                boolean flag = false;

                // the app is running in a while loop, which means only if the client will insert the command 'stop' the app will stop and close the socket.
                while (!flag)
                {
                    // using a switch/case to determine which task the user want to run. every thing is pretty staright forward.
                    Scanner scan1 = new Scanner(System.in);
                    String temp = scan1.next();
                    switch (temp){
                        case "stop":{
                            flag = true;
                            break;
                        }
                        case "task1":{
                            runTask1(toServer, fromServer);
                            break;
                        }
                        case "task2":{
                            runTask2(toServer, fromServer);
                            break;
                        }
                        case "task3":{
                            runTask3(toServer, fromServer);
                            break;
                        }
                        case "task4":{
                            runTask4(toServer, fromServer);
                            break;
                        }

                        default:{
                            System.out.println("I can't do it Jerry, please try again and do your best");
                            break;
                        }
                    }
                }

                // function to stop close the socket and stop the app.

                stop(socket, toServer, fromServer);
            }
            catch(IOException e){e.printStackTrace();}
            catch (InterruptedException e) { e.printStackTrace(); }
            catch (ClassNotFoundException e) { e.printStackTrace(); }
            }
        );
    }

    /* use cases for task1:
        * can use static/compute matrix - done
        1. Matrix is empty 0x0 - done
        2. Input is invalid - done
        3. Huge Matrix 200x200 - done.

     */
    private static void runTask1(ObjectOutputStream toServer, ObjectInputStream fromServer) throws IOException, ClassNotFoundException, InterruptedException {

        //StaticMatrix(toServer);
        ComputeMatrixUsingMeeseeks(toServer);

        System.out.println("After building the matrix you will see ALL the connected components sorted by the amount of indices they own ");
        // send a request to preform a Traversable method on the matrix
        toServer.writeObject("traverse");

        System.out.println("Let's see which indices are strongly connected to each other\n");
        // get reachable indices as list
        List<List<Index>> ccs = (List<List<Index>>) fromServer.readObject();

        // put the ccs into a List and sort them by size of amount of indices.
        var str = ccs.stream().map(List<Index>::toString)
                .collect(Collectors.joining("\n\t"));
        if(str.isEmpty()) System.out.println("There are not SCC in the graph");
        else System.out.println("from client - Strongly connected components are:  \n\t"+ str);
        System.out.println("\n\nTask1 is Done! either type another task or type 'stop' to end the requests");
        System.out.println("------------------------------------------------\n");
    }

    /* use cases for task2:
        * can use static/compute matrix - done
        1. Matrix is empty 0x0 - done
        2. Input is invalid - done on matrix / TODO: on indices
        3. Huge Matrix 50x50 - done.
        4. There's no path - done
        5. There's no such index - done (says it's no path).
     */
    private static void runTask2(ObjectOutputStream toServer, ObjectInputStream fromServer) throws IOException, ClassNotFoundException, InterruptedException {

        //StaticMatrix(toServer);
        ComputeMatrixUsingMeeseeks(toServer);

        // scan 2 indices from the client.
        scan2indices(toServer);

        // call the function
        toServer.writeObject("ShortestPath");

        // save the paths as Objects, if path is a String = there's is no path
        // if the path is a ListofListofIndex = there's is a path.
        Object reveal = (Object) fromServer.readObject();
        if(reveal instanceof String)
        {
            String temper = (String)reveal;
            System.out.println(reveal);
        }
        else
        {
            List<List<Index>> listy = (List<List<Index>>) reveal;
            for(var item : listy) System.out.println(item);
        }
        System.out.println("\n\nTask2 is Done! either type another task or type 'stop' to end the requests");
        System.out.println("------------------------------------------------\n");
        return;
    }

    /* use cases for task3:
        * can use static/compute matrix - done
        1. Matrix is empty 0x0 - done
        2. Input is invalid - done on matrix.
        3. Huge Matrix 20x20 - done.
     */
    private static void runTask3(ObjectOutputStream toServer, ObjectInputStream fromServer) throws IOException, ClassNotFoundException, InterruptedException {

        StaticMatrix(toServer);
        //ComputeMatrixUsingMeeseeks(toServer);

        // call the function from MatrixHandler
        toServer.writeObject("battlefield");

        /**
         * @task3Array got 2 values:
         *  the [0] = counts how many 'proper' ships exists.
         *  the [1] = if there's 1 not 'proper' ship this cell with != 0.
         */
        int[] task3Array = (int[]) fromServer.readObject();

        // if [1] > 0 which means there's a 'bad' ship,the input isn't good.
        if(task3Array[0] == 0 && task3Array[1] == 0) System.out.println(("Hey, There matrix is empty of '1'!"));
        else if(task3Array[1] > 0) System.out.println("The input isn't good");
        else System.out.println("The input is valid, and there are "+ task3Array[0] +" 'proper' ships");
        System.out.println("\n\nTask3 is Done! either type another task or type 'stop' to end the requests");
        System.out.println("------------------------------------------------\n");
    }

    /* use cases for task4:
        * can use static/compute matrix - done
        1. Matrix is empty 0x0 - done
        2. Input is invalid - done on matrix.
        3. Huge Matrix 20x20 - done.
     */
    private static void runTask4(ObjectOutputStream toServer, ObjectInputStream fromServer) throws InterruptedException, IOException, ClassNotFoundException {

        //build random matrix with values from -1000 to 1000.
        boolean flag = computeMatrixForTask4(toServer);

        /* if you want to use the static matrix*/
        // StaticMatrix(toServer);
        //boolean flag =true
        if (flag)
        {
            System.out.println("\n");

            // scan 2 indices
            scan2indices(toServer);

            // call the method in matrixIHandler
            toServer.writeObject("LightestOfPaths");

            // save the list of list we got from the server into task4list var.
            // then print every item in it.
            LinkedList<List<Index>> task4list = (LinkedList<List<Index>>) fromServer.readObject();
            if(task4list.isEmpty()) System.out.println("No paths!");
            else
            {
                for(var item : task4list)
                {
                    System.out.println(item);
                }
            }
        }
        else System.out.println("Matrix is empty!");

        System.out.println("\n\nTask4 is Done! either type another task or type 'stop' to end the requests");
        System.out.println("------------------------------------------------\n");
    }

    private static boolean computeMatrixForTask4(ObjectOutputStream toServer) throws InterruptedException, IOException {
        boolean holder1 =false, holder2 =false;
        int row =0, column = 0 ;
        System.out.println("\nI'm going to build a Binary Matrix for you ! Look at me !");
        System.out.println("all you need to do is to tell me the size of the matrix..\n");
        while (!holder1)
        {
            System.out.println("Please enter the number of Rows:");
            Scanner item1 = new Scanner(System.in);
            if (item1.hasNextInt())
            {
                row = item1.nextInt();
                holder1=true;
                break;
            }
            else
            {
                System.out.println("wrong! please insert a vaild int");
            }
        }
        if(row == 0) return false;

        while (!holder2)
        {
            System.out.println("Please enter the number of Columns:");
            Scanner item2 = new Scanner(System.in);
            if (item2.hasNextInt())
            {
                column = item2.nextInt();
                holder2=true;
                break;
            }
            else
            {
                System.out.println("wrong! please insert a vaild int");
            }
        }
        if(column == 0) return false;
        int[][] intArray = new int[row][column];
        Random generator = new Random();
        for(int i = 0; i < intArray.length; i++)
        {
            for(int j = 0;j < intArray[i].length;j++)
            {
                intArray[i][j] = generator.nextInt(1000 + 1000) - 1000;
            }
        }
        Matrix mat = new Matrix(intArray);
        toServer.writeObject("ComputeMatrix");
        toServer.writeObject(intArray);
        mat.printMatrix();
        return true;
    }

    private static void scan2indices(ObjectOutputStream toServer) throws IOException {
        System.out.println("In this task, you need to insert 2 indices,1 source and 1 detention ");
        // scan 1st index
        System.out.println("Insert the value of the row, then click 'Enter' then insert the value of the column");
        Scanner scn = new Scanner(System.in);
        int x = scn.nextInt();
        int y = scn.nextInt();
        Index src = new Index(x,y);
        toServer.writeObject("start index");
        toServer.writeObject(src);

        // scan 2nd index
        System.out.println("and now again.");
        Scanner scn1 = new Scanner(System.in);
        int z = scn1.nextInt();
        int w = scn1.nextInt();
        Index dest = new Index(z,w);
        toServer.writeObject("end index");
        toServer.writeObject(dest);
    }

    private static void stop(Socket socket, ObjectOutputStream toServer, ObjectInputStream fromServer) throws IOException {
        System.out.println("Good bye person, have a nice day.\n\n");
        toServer.writeObject("stop");
        System.out.println("client: Close all streams");
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("client: Closed operational socket");
    }

    private static void StaticMatrix(ObjectOutputStream toServer) throws IOException {

        // not ok - 3 submarines but no gap
        int[][] array1 = {
                {1,1,0,1,1},
                {1,0,0,1,1},
                {1,0,0,1,1}
        };

        //not ok - 2 ok 1 bad
        int[][] array2 = {
                {1,0,0,1,1},
                {1,0,0,1,1},
                {0,1,0,1,1}
        };

        // ok 2 submarines
        int[][] array3 = {
                {1,0,0,1,1},
                {1,0,0,1,1},
                {1,0,0,1,1}
        };

        // ok 3 submarines
        int[][] array4 = {
                {1,1,0,1,1},
                {0,0,0,1,1},
                {1,1,0,1,1}

        };
        int[][] array5 = {
                {1,1,1},
                {1,1,1},
                {1,1,1},

        };
        Matrix mat = new Matrix(array4);
        mat.printMatrix();
        toServer.writeObject("ComputeMatrix");
        toServer.writeObject(array4);
    }

    private static void ComputeMatrixUsingMeeseeks(ObjectOutputStream toServer) throws InterruptedException, IOException {
        boolean holder1 =false, holder2 =false;
        int row =0, column = 0 ;
        System.out.println("\nI'm going to build a Binary Matrix for you ! Look at me !");
        System.out.println("all you need to do is to tell me the size of the matrix..\n");
        while (!holder1)
        {
            System.out.println("Please enter the number of Rows:");
            Scanner item1 = new Scanner(System.in);
            if (item1.hasNextInt())
            {
                row = item1.nextInt();
                if (row<=0)
                {
                    System.out.println("wrong! please insert a vaild int");
                }
                else
                {
                    holder1=true;
                    break;
                }

            }
            else
            {
                System.out.println("wrong! please insert a vaild int");
            }
        }

        while (!holder2)
        {
            System.out.println("Please enter the number of Columns:");
            Scanner item2 = new Scanner(System.in);
            if (item2.hasNextInt())
            {
                column = item2.nextInt();
                if(column<=0)
                {
                    System.out.println("wrong! please insert a vaild int");
                }
                else
                {
                    holder2=true;
                    break;
                }
            }
            else
            {
                System.out.println("wrong! please insert a vaild int");
            }
        }

        // declaring on new matrix in the size of the scanned values, according to the abstract class.
        StandardMatrix myArray = new StandardMatrix(row,column);
        int[][] newArray = new int[row][column];
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < column; j++)
            {
                newArray[i][j] = myArray.val(new Index(i,j));
            }

        }
        // print the matrix with the method.
        myArray.printMatrix();
        // send the server a request with "task1" + the matrix
        toServer.writeObject("ComputeMatrix");
        toServer.writeObject(newArray);


    }

}
