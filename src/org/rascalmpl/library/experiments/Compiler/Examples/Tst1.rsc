module experiments::Compiler::Examples::Tst1

import Set;
import IO;

value main() =  {<{1,2,3}, 4, 4>, <{3,4,5}, 6, 7>, <{5,6,7}, 8, 9>, <{1,2,3}, 2, 4>, <{1,2,3}, 3, 0>}[{1,2,3}] ;

//value main() {
//
//    R = {<i, j> | i <- [0..100], j <- [10..100] /*, k <- [100..200]*/ };
//    
//    println("<size(R)> elements");
//    
//    for(m <-[ 1 .. 400]){
//        for(n <- [0 .. 100]){
//            R[{1,2}];
//        }
//    }
//}
