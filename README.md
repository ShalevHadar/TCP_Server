# TCP_Server
TCP Multi-threaded server - Java

Hi!

My name is Shalev Hadar, I'm a student of computer science (B.Sc.) and this is my TCP server.

One of my favorite TV-Show is 'Rick and Morty', so the greeting prints are from a bot call 'MrMeeseeks'. a bot that receives requests, does them, and then disappears.

The whole project is based on Java programming language.
Using SOLID principles with Abstract Factory design pattern (in AbstractMatrix & StandardMatrix classes).
Liskov substitution is being used a lot in this project in many if not all classes.
The Server is thread-safe and each time you run a task you can watch your thread's usage in the TCP tab.
To run the server: Run TcpServer then run client.

The main goal of this project:

The server will open a socket for the client
Through this socket the client will send requests to the server along with raw data such as:
matrices, indices, etc.
Thread-safe and Multi-thread principles will be applied here.
The server will receive the data from the client and solve the algorithm problems according to these instructions:
/* for all tasks, you can either choose to use a static matrix or compute a matrix. To do it, all you need to do is to uncomment the wished function in either task. In tasks 1-3, neighbors/reachable are considered in diagonal (up down left right) and cross. */

**task1: task1: Find all Strongly-Connected-Components in a Matrix -the server receives a matrix (can handle a 300x300 matrix within a few seconds). -each strongly connected component will be handled in a synchronized thread-safe way to a specific thread. -using the 'join' method on the threads will guarantee that no thread will 'interrupt' another thread. -each thread that handles an SCC will be displayed in the TCP server tab after running. -the server will return a list of list of indices sorted by the number of indices in each small list and each small list will contain the SCC.

**task2: Find all shortest paths from index to index in a Matrix -the server receives a matrix (limit hasn't been tested yet) -user is asked to put 2 indices - a path from starting to destination index -client is sending the server a request to find ALL the shortest paths from start to destination. -since we want to find all the paths and there could be enormous numbers of paths, only 1 thread will handle this task. -server will receive the data and implement a custom BFS algorithm using queue and list as data structures. -the server will return List<List> that includes all the paths from start to dest. -it's important to mention that this task uses #Task1 SCC single algorithm for first checking 2 indices, if the 2 indices are NOT in the same SCC, there isn't a path from src to dest.

**task3: find all 'proper' ships in a Matrix -the server receives a matrix and returns how many 'proper' ships are in it. 'Proper' ship: -Full rectangle(1on1 is considered one) -there's a '0' divided between ships. For example > 1 0 1 : 2 ships with. -if there's 1 ship that isn't proper, the server will return the input is bad regardless of the number of proper ships. -the server will get the data from the client, then check for patterns that can show bad inputs. -after checking bad inputs, each top-left indices with '1' value will be checked if it's a proper rectangle. -then, the server will send back an array containing 2 cells, 1 for proper ship and 1 for not-proper.

**task4: find all the lightest paths from index to index in weighted Matrix -server receives a weighted matrix, starting index, destination index. -based on task2, we implement a custom BFS algorithm that saves the sum of all paths from src to dest. -then, the server returns List of list of indices in which the size of the paths is the lowest (could be multiple).

Pros: - Task1 runtime is Wonderful. There are no repetition of same indices. - Task3 runtime is O(Rows + Column) in total.

Cons: - missing a method that let the user choose if he wants a static / compute / input a matrix. - task 2 can't handle big matrices. - Callables / wrapping algorithms with / ThreadPools / Read/Write locks weren't used in this project. - task4 isn't multi-threaded and uses just 1 thread.
