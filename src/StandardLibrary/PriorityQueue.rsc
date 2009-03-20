module PriorityQueue

import List;
import Map;
import IO;

data BinomialHeap = binomialHeap(map[int, BinomialTree] trees);
data BinomialTree = binomialTree(int root, map[int, BinomialTree] children);
data PriorityQueue = priorityQueue(BinomialHeap heap);

private BinomialTree addSubTree(BinomialTree p, BinomialTree q){
     children = p.children;
     children[size(children)] = q;
     return binomialTree(p.root, children);
}

public BinomialTree mergeTree(BinomialTree p, BinomialTree q){
    if(p.root <= q.root)
        return addSubTree(p, q);
    else
        return addSubTree(q, p);
}

public BinomialHeap merge(BinomialHeap p, BinomialHeap q){
    heapTrees = ();
    ih = 0;
    pTrees = p.trees; np = size(pTrees);
    ip = 0;
    qTrees = q.trees; nq = size(qTrees);
    iq = 0;
    println("np=<np>, nq=<nq>");
    while(ip < np || iq < nq){
	    tree = mergeTree(pTrees[ip], qTrees[iq]);
		if(heapTrees[ih]?){
		    tree = mergeTree(tree, heap[ih]);
		}
		println("heapTrees[<ih>] = <tree>");
		heapTrees[ih] = tree;
		ih = ih + 1;
		if(ip < nq) ip = ip + 1;
		if(iq < nq) iq = iq + 1;
	}
	return binomialHeap(heapTrees);
}
		/*
function deleteMin(heap)
    min = heap.trees().first()
    for each current in heap.trees()
        if current.root < min then min = current
    for each tree in min.subTrees()
        tmp.addTree(tree)
    heap.removeTree(min)
    merge(heap, tmp)
    */
    
 public bool test(){
 
   R = mergeTree(binomialTree(10, ()), binomialTree(8, ()));
   println(R);
   
   P = binomialTree(7, (0: binomialTree(12, (0: binomialTree(13, ()))),
                        1: binomialTree(8, ())));
   Q = binomialTree(3, (0: binomialTree(5, (0: binomialTree(9, ()))),
                        1: binomialTree(4, ())));
                        
   R1 = binomialTree(3, (0: binomialTree(7, (0: binomialTree(12, (0: binomialTree(13, ()))),
                                             1: binomialTree(8, ()))),
                         1: binomialTree(5, (0: binomialTree(9, ()))),
                         2: binomialTree(4, ())));
   
   
   R = mergeTree(P, Q);
   
   println(R);
   assert R == R1;
   return true;
 }
		
        