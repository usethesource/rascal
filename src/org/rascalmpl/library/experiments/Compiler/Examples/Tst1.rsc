module experiments::Compiler::Examples::Tst1

import Set;
import IO;

value main() {

    R = {<i, j> | i <- [0..100], j <- [10..100] /*, k <- [100..200]*/ };
    
    println("<size(R)> elements");
    
    for(m <-[ 1 .. 400]){
        for(n <- [0 .. 100]){
            R[{1,2}];
        }
    }
}
