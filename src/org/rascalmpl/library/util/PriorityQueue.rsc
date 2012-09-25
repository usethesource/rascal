@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module util::PriorityQueue

/*
 * Priority queues maintain (value, priority) pairs in sorted order.
 * They are implemented using a binomial heap, see http://en.wikipedia.org/wiki/Binomial_heap
 * The following operations are provided:
 * - PriorityQueue priorityQueue():
 *		create an empty queue.
 * - PriorityeQueue priorityQueue(int priority, int val):
 *		create queue with one pair.
 * - bool isEmpty(PriorityQueue Q):
 *		test for empty queue.
 * - PriorityQueue insertElement(PriorityQueue Q, int priority, int val):
 *		insert pair in queue.
 * - int findMinimum(PriorityQueue Q):
 *		find the minmium priority.
 * - tuple[int, int, PriorityQueue] extractMinimum(PriorityQueue Q):
 *		find the pair with minimum priority and delete it.
 */
 
 /*
  * TODO: the value in each pair is now an int but should become &T.
 */

import util::Math;
import List;
import Set;
import Map;
import IO;

// Binomial Trees

private data BinomialTree[&T] = binomialTree(int priority,            // priority of this tree
                                         int val,                     // payload
                                         int degree,                  // degree of tree
                                         list[BinomialTree] children  // subtrees
                                        );

private BinomialTree addSubTree(BinomialTree p, BinomialTree q){
    return binomialTree(p.priority, p.val, p.degree + 1, p.children + [q]);
}

private BinomialTree mergeTree(BinomialTree p, BinomialTree q){
    return (p.priority <= q.priority) ? addSubTree(p, q) : addSubTree(q, p);
}

private str toString(BinomialTree T){
	str res = "[" + toString(T.root) + "/" + toString(T.val);
	if(!isEmpty(T.children))
		res = res + ":";
	for(BinomialTree child <- T.children){
	    res = res + " " + toString(child);
	}
	return res + "]";
}

// Priority Queues implemented as Binomial Heap

data PriorityQueue = priorityQueue(list[BinomialTree] trees,  // trees in the heap
                                   int minIndex               // index of minimal tree
                                  );

public PriorityQueue mkPriorityQueue(){
   return priorityQueue([], -1);
}

public PriorityQueue mkPriorityQueue(int priority, int val){
   return priorityQueue([binomialTree(priority, val, 0, [])], 0);
}

public bool isEmpty(PriorityQueue Q){
	return size(Q.trees) == 0;
}

public PriorityQueue insertElement(PriorityQueue Q, int priority, int val){
  return mergeQueue(Q, priorityQueue(priority, val));
}

public int findMinimum(PriorityQueue Q){
   return Q.trees[Q.minIndex].priority;  // throw exception for empty queue.
}

public tuple[int, int, PriorityQueue] extractMinimum(PriorityQueue Q){
   minTree = Q.trees[Q.minIndex];
   
   Q.trees = delete(Q.trees, Q.minIndex);
   
   // Determine the new minimal tree
   int minIndex = -1;
   int minPrio = 10000;
   for(int i <- domain(Q.trees)){
       if(Q.trees[i].priority < minPrio){
       	  minPrio = Q.trees[i].priority;
       	  minIndex = i;
       }
   }
   Q.minIndex = minIndex;
       
   return <minTree.priority, minTree.val, mergeQueue(Q, priorityQueue(minTree.children, 0))>;
}

public str toString(PriorityQueue Q){
    str res = "(";
	for(int i <- domain(Q.trees)){
	    res = res + "\n" + toString(i) + ":" + toString(Q.trees[i]);
	}
	return res + "\n)";
}

private int minPrio = 100000;
private int minIndexFromEnd = -1;

private list[BinomialTree] add(list[BinomialTree] heap, BinomialTree t){
  if(isEmpty(heap)){
    if(t.priority == minPrio)
    	minIndexFromEnd = 0;
  	return [t];
  } else if (head(heap).degree == t.degree){
    m = mergeTree(head(heap), t);
    if(m.priority == minPrio){
    	minIndexFromEnd= size(heap);
    }
    return [ m, tail(heap) ];
  } else {
  	if(t.priority == minPrio)
    	minIndexFromEnd= size(heap);
    return [t, heap];
  }
}

private PriorityQueue mergeQueue(PriorityQueue p, PriorityQueue q){
  
    pTrees = p.trees; 
    if(isEmpty(pTrees))
    	return q;
    	
    qTrees = q.trees;
    if(isEmpty(qTrees))
    	return p;
    	
    int fromEnd;   // index of smallest tree from end
    
    if(pTrees[p.minIndex].priority <= qTrees[q.minIndex].priority){
       minPrio = pTrees[p.minIndex].priority;
       fromEnd = size(pTrees) - p.minIndex;
    } else {
       minPrio = qTrees[q.minIndex].priority;
       fromEnd = size(qTrees) - q.minIndex;
    }
    minIndexFromEnd = -1;
    
    list[BinomialTree] heapTrees = [];

    while(!isEmpty(pTrees)  && !isEmpty(qTrees)){
          hp = head(pTrees);
          hq = head(qTrees);
          if(hp.degree < hq.degree){
             heapTrees = add(heapTrees, hp);
             pTrees = tail(pTrees);
          } else if (hp.degree == hq.degree){
             heapTrees = add(heapTrees, mergeTree(hp, hq));
             pTrees = tail(pTrees);
             qTrees = tail(qTrees);
          } else {
             heapTrees = add(heapTrees, hq);
             qTrees = tail(qTrees);
          }
    
    }
    rest =  isEmpty(pTrees) ? qTrees : pTrees;
    heapTrees = rest + heapTrees;
      
    int min;
    
    if(minIndexFromEnd == -1){
       // Minimal element appears in rest
       min = fromEnd;
    } else {
       // Minimal element already seen.
      min = size(heapTrees) - minIndexFromEnd - 1;
    }     
    
   //println("heapTrees=<heapTrees>");
   //println("minIndexFromEnd=<minIndexFromEnd>, minPrio=<minPrio>, min=<min>");
    return priorityQueue(heapTrees, min);
}
    
test bool prioTest() {
   
   Q = mkPriorityQueue();
   
   elms = [10, 8, 50, 30, 1];
   for(int i <- elms)
    	Q = insertElement(Q, i, i);
    
   
   list[int] sorted = [];
   while(size(sorted) < size(elms)){
   	         <min, minVal, Q> = extractMinimum(Q);
   	         sorted = sorted + [min];
   }
   
   println("Q=", toString(Q));
   
   println("sorted=<sorted>");  
   
   return true;
}
		
        
