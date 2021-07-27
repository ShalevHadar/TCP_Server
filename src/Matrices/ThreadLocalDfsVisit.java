package Matrices;
import TcpServer.*;
import java.util.*;

/**
 * This class enables the scan of some object in Thread-Local manner
 * Will we able to divide and conquer the scanned object:
 * Each thread will get a part of the problem and no other thread can change the
 * data
 *
 * TLS - Thread-local Storage
 * Create one single instance of ThreadLocalDfsVisit and it is ensured that each
 * thread will have its own copy of the stack and set
 */

/*
If we have an object to scan and there are several working threads
 */
public class ThreadLocalDfsVisit<T> {
    protected final ThreadLocal<Stack<Node<T>>> threadLocalStack =
            ThreadLocal.withInitial(()->new Stack<>());

    protected final ThreadLocal<Set<Node<T>>> threadLocalSet =
            ThreadLocal.withInitial(()->new HashSet<>());


    protected void threadLocalPush(Node<T> initialNode){
        this.threadLocalStack.get().push(initialNode);
    }
    protected Node<T> threadLocalPop(){
        return this.threadLocalStack.get().pop();
    }

    // implementing a DFS algorithm using a stack and set.
    public List<T> traverse(Traversable<T> someGraph){
        Stack<Node<T>> localStack = threadLocalStack.get();
        Set<Node<T>> localSet = threadLocalSet.get();
        threadLocalPush(someGraph.getOrigin());
        while(!localStack.isEmpty()){
            Node<T> poppedNode = threadLocalPop();
            localSet.add(poppedNode);
            Collection<Node<T>> rechableNodes = someGraph.getReachableNodes(poppedNode);
            for(Node<T> singleReachableNode: rechableNodes){
                if(!localSet.contains(singleReachableNode) &&
                        !localStack.contains(singleReachableNode)){
                    threadLocalPush(singleReachableNode);
                }
            }

        }
        ArrayList<T> blackList = new ArrayList<>();
        for(Node<T> node: threadLocalSet.get()){
            blackList.add(node.getData());
        }
        return blackList;
    }
}

