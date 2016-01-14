module experiments::Compiler::Examples::Tst3

import IO;
import Exception;
test bool visitIsDef1(){
    M = ("a" : 1, "b" : 2);
    
    visit([1,2,1]){
       case int n: M["c"] ? 0 += n;
    }
   return M == ("a" : 1, "b" : 2, "c" : 4);
}

test bool visitTry1(){
    M = ("a" : 1, "b" : 2);
    L = [];
    visit([1,2,1]){
       case int n: try { M["c"]; } catch e: L += <e, n>;
    }
   return L == [<NoSuchKey("c"),1>,<NoSuchKey("c"),2>,<NoSuchKey("c"),1>];
}

test bool visitTry2(){
    M = ("a" : 1, "b" : 2);
    L = [];
    visit([1,2,3]){
       case int n: try { M["c"]; } catch e: L += <e, n>; finally {L += n;}
    }
    println(L);
   return L == [1,<NoSuchKey("c"),1>,<NoSuchKey("c"),2>,2,<NoSuchKey("c"),3>,3];
}

test bool visitCompositeAndOr() {
    n = 0;
    visit([1,2,3]){
    case int k: 

        l = if( [*int x,*int y] := [1,2,k] || ([*int x,*int y] := [4,5,6] && [*int w,*int z] := [7,8,9] ) ) {
                n = n + 1;
                fail;
             }
    }
    return n == 60;
}

test bool visitCompositeAndOr() {
    L1 = [1, 2, 3, 4, 5];
    L2 = [];
    visit(L1){
    case int k: L2 += k;
    }
    
    return L1 == L2;
}

