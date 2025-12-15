module lang::rascal::tests::libraries::util::PriorityQueueTests

import List;
import util::PriorityQueue;

test bool prioTest() {
   
   Q = mkPriorityQueue();
   
   elms = [10, 8, 50, 30, 1];
   for(int i <- elms)
        Q = insertElement(Q, i, i);
    
   
   list[int] sorted = [];
   while(size(sorted) < size(elms)){
             <minimum, minVal, Q> = extractMinimum(Q);
             sorted = sorted + [minimum];
   }
   
   return true;
}
