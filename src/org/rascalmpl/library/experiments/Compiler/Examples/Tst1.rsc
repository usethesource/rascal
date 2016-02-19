
module experiments::Compiler::Examples::Tst1

import List;
import util::Math;
import IO;
import Type;

test bool dtstTail(list[&T] lst) {
    if(isEmpty(lst)) return true;
    int n = 0;
    if(size(lst) != 1) n = arbInt(size(lst) - 1);
    if(n == 0) return true;
    println("n = <n>");
    lhs = tail(lst, n);
    rhs = [ lst[i] | int i <- [(size(lst) - n)..size(lst)] ];
    println("lhs = <lhs>, <typeOf(lhs)>");
    println("rhs = <rhs>, <typeOf(rhs)>");
    println("lhs == rhs: <lhs == rhs>");
    
    return lhs == rhs && typeOf(lhs) == typeOf(rhs);
}


  
  
    
 
