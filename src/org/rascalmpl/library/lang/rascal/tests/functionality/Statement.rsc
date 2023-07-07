@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the EclipseLicense v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl - CWI}
module lang::rascal::tests::functionality::Statement
  
import Exception;

// testAssert
  
test bool testAssert1() {return assert 3 > 2;}
test bool testAssert2() {return assert 3 > 2: "Yes assert succeeds";}
  	   
@expected{
AssertionFailed
}
test bool assertError1() {assert 1 == 2;return false;}

@expected{
AssertionFailed
}
test bool assertError2() {assert 1 == 2: "1 really differs from 2"; return false;}
  
// assignment
  
test bool assignment1() {int x = 3; return x == 3;}
test bool assignment2() {int x = 3; x = 4; return  x == 4;}
test bool assignment3() {return {<x, y> = <3, 4>; (x == 3) && (y == 4);};}
test bool assignment4() {return {<x, y, z> = <3, 4, 5>; (x == 3) && (y == 4) && (z == 5);};}
test bool assignment5() {return {<x, y> = <3, 4>; x = 5; return (x == 5) && (y == 4);};}
  
test bool assignment6() {int x = 3; x += 2; return x == 5;}
test bool assignment7() {int x = 3; x -= 2; return x == 1;}
test bool assignment8() {int x = 3; x *= 2; return x == 6;}
test bool assignment9() {int x = 3; x /= 2; return x == 1;}
  		
test bool assignment10() {list[int] x = [0,1,2]; return x == [0,1,2];}
test bool assignment11() {list[int] x = [0,1,2]; return x[0] == 0;}
test bool assignment12() {list[int] x = [0,1,2]; return x[1] == 1;}
test bool assignment13() {list[int] x = [0,1,2]; return  x[2] == 2;}
test bool assignment14() {return {list[int] x = [0,1,2]; x[1] = 10; (x[0] == 0) && (x[1] == 10) && (x[2] == 2);};}
  
test bool assignment15() {return {map[int,int] x = (0:0,1:10,2:20); x == (0:0,1:10,2:20);};}
test bool assignment16() {return {map[int,int] x = (0:0,1:10,2:20); x[1] = 15; (x[0] == 0) && (x[1] == 15) && (x[2] == 20);};}
  
test bool assignment17() {set[int] x = {0,1,2}; return x == {0,1,2};}
test bool assignment18() {set[int] x = {0,1,2}; x = x + {3,4}; return x == {0,1,2, 3,4};}
  
test bool assignment19() {rel[str,list[int]] s = {<"a", [1,2]>, <"b", []>, <"c", [4,5,6]>}; return s != {};}
test bool assignment20() {rel[str,list[int]] s = {<"a", [1,2]>, <"b", []>, <"c", [4,5,6]>};return s != {};}
  
// block
  
test bool block1() {int x = 3; x = 4; return x ==4;}
test bool block2() {int x = 3; x = 4; return x == 4;}
  
// testBreak
  
test bool testBreak() {int n = 0; while(n < 10){ n = n + 1; break;}; return n == 1;}
  
  
// testContinue
  
  	    /* no tests available */
  
// doWhile
  
test bool doWhile1() {return {int n = 0; m = 2; do {m = m * m; n = n + 1;} while (n < 1); (n == 1) && (m == 4);};}
test bool doWhile2() {return {int n = 0; m = 2; do {m = m * m; n = n + 1;} while (n < 3); m == 256;};}
  	
// testWhile
  
test bool testWhile1() {return {int n = 0; int m = 2; while(n != 0){ m = m * m;}; (n == 0)&& (m == 2);};}
test bool testWhile2() {return {int n = 0; int m = 2; while(n < 3){ m = m * m; n = n + 1;}; (n ==3) && (m == 256);};}

test bool testWhileWithBacktracking1() {
    list[list[int]] res = [];
    l:while([*int x, *int _] := [1,2,3]) {
        res = res + [ x ];
        fail l;
    }
    return res ==  [[],[1],[1,2],[1,2,3]];
}

test bool testWhileWithBacktracking2() {
    list[list[int]] res = [];
    
    while(true) {
        res = res + [ [999] ];
        fail;
    }
    return res == [[999]];
}

test bool testWhileWithBacktracking3(){
    list[list[int]] res = [];
   
    n = 0;
    while([*int x, *int y] := [3,4,3,4], n < 3) {
        if(x == y) {
            res = res + [ x ];
            n = n + 1;
        } else {
            res = res + [ [0] ];
            fail;
        }
    }
    return res ==  [[0],[0],[3,4],[0],[0],[3,4],[0],[0],[3,4]];
}

test bool testWhileWithBacktracking4(){
    list[list[int]] res = [];

    n = 0;
    while(n < 3) {
        res = res + [ [10] ];
        n = n + 1;
    }
    return res == [[10],[10],[10]];
}

test bool testWhileWithBacktracking5(){
    list[list[int]] res = [];

    n = 0;
    while(1 == 1, n < 3) {
        res = res + [ [11] ];
        n = n + 1;
    }
    return res == [[11],[11],[11]];
}

test bool testWhileWithBacktracking6(){
    list[list[int]] res = [];

    n = 0;
    while(1 == 2 || n < 3) {
        res = res + [ [12] ];
        n = n + 1;
    }
    return res ==  [[12],[12],[12]];
}

@ignoreInterpreter{
Infinite loop
}
test bool labelledWhileContinue(){
  res = "";
  next_fun:
    while(_n <- [3,3]){                       
       try {
           throw "NO";
       } catch "NO": {
           res += "NO";
           continue next_fun;
       }
   }
   return res == "NONO";
}

@ignoreCompiler{
FIXME: pre and post should be reset to undefined on loop entry
}
test bool testWhileWithPatternVariables(){
    syms = [10,9,1,3,5];
    while([*pre, x, y, *post] := syms, x > y){
      syms = [*pre, y, x, *post];
    }
    return syms == [1,3,5,9,10];
}
 	
data D = d(int i) | d();
  
D d(int i) { if (i % 2 == 0) fail d; else return d();}
  
// fail
  
test bool fail1() = d(2) := d(2);
test bool fail2() = d(3) == d();

test bool fail3() {
    int n = 0; 
    loop:for(int _ <- [1,2,3,4], n <= 3) { 
        if(n == 3) {
            fail loop;
        } 
        n = n + 1; 
    } 
    return n == 3;
}

test bool fail4() {
	int main(){
	    if1:if(x <- [1,2,3,4], x <= 3) {
	        if2:if(y <- [4,3,2,1], y >= 3) {
	            if(x != 3) {
	                fail if1;
	            } else if(y != 3) {
	                fail if2;
	            }
	            return x + y;
	        }
	    }
	    return -1;
    }
    return main() == 6;
}

test bool fail5() {
    str trace = "";
    if(true) {
        if(false) {
           ;
        } else {
           trace += "fail inner!";
           fail;
        }
    } else {
        trace += "else outer!";
    }
    return trace == "fail inner!else outer!";
}
  		
// testFor
  
test bool testFor1() {int n = 0; for(int i <- [1,2,3,4]){ n = n + i;} return n == 10;}
test bool testFor2() {int n = 0; for(int i <- [1,2,3,4], n <= 3){ n = n + i;} return n == 6;}
test bool testFor3() {int n = 0; for(int _ <- [1,2,3,4]){ n = n + 1; if (n == 3) break; } return n == 3;}
test bool testFor4() {int n = 0; for(int _ <- [1,2,3,4], n <= 3){ if (n == 3) continue; n = n + 1; } return n == 3;}
test bool testFor5() {int n = 0; loop:for(int _ <- [1,2,3,4], n <= 3){ if (n == 3) fail loop; n = n + 1; } return n == 3;}

test bool labelledForContinue(){
  res = "";
  next_fun:
    for(_n <- [3,3]){                       
       try {
           throw "NO";
       } catch "NO": {
           res += "NO";
           continue next_fun;
       }
   }
   return res == "NONO";
}

// testAppend

/*TODO:?*/
//test bool testAppend() for(int i <- [1,2,3,4]){ 3 * i; } == 12;));
test bool testAppend1() { L = for(int i <- [1,2,3,4]){ append 3 * i; }; return L == [3,6,9,12];}
test bool testAppend2() { L = for(int i <- [1,2,3,4]){ append 3 * i; append 4 *i;}; return L == [3,4,6,8,9,12,12,16];}

test bool testAppend3() {
    res1 = for(2 > 1) append 0;
    res1 = res1 + [ 1 | 2 > 1 ];
    res2 = for(2 < 1) append 2;
    res2 = res2 + [ 3 | 2 < 1 ];
    return res1 + res2 == [0, 1];
}

// We no longer allow dynamically scoped appends
//test bool testAppend4() {
//    res = for(x <- [1,2,3,4]) { int f() { append x; return 4; }; append f(); };
//    return res == [1,4,2,4,3,4,4,4];
//}

// ifThen
  
test bool ifThen1() {int n = 10; if(n < 10){n = n - 4;} return n == 10;}
test bool ifThen2() {int n = 10; if(n < 15){n = n - 4;} return n == 6;}
test bool ifThen3() {int n = 10; l:if(int i <- [1,2,3]){ if (i % 2 != 0) { n = n + 4; fail l; } n = n - 4;} return n == 10;}

// ifThenElse
  
test bool ifThenElse1() {int n = 10; if(n < 10){n = n - 4;} else { n = n + 4;} return n == 14;}
test bool ifThenElse2() {int n = 12; if(n < 10){n = n - 4;} else { n = n + 4;} return n == 16;}

//  solve

rel[int,int] R1 =  {<1,2>, <2,3>, <3,4>};
  
test bool solve1() {
  		  rel[int,int] T =    R1;
  		  solve (T)  T = T + (T o R1);
  		  return T == {<1,2>, <1,3>,<1,4>,<2,3>,<2,4>,<3,4>};
}	
  
test bool solve2() {
  		  int j = 0;
  		  solve (j) if (j < 1000) j += 1;
  		  return j == 1000;
}	
        
@expected{
IndexOutOfBounds
}
test bool solveIndexOutOfBounds1() {
  			  rel[int,int] T =    R1;
  		  solve (T; -1)  T = T + (T o R1);
    		return T == {<1,2>, <1,3>,<1,4>,<2,3>,<2,4>,<3,4>};
		}

data PAIR = pair(int x, int y);		

set[PAIR] removeIdPairs(set[PAIR] inp){
   res = inp;
   solve(res) {
         if ( { pair(a, b), pair(b, b), *c } := res ) 
              res = { *c, pair(a, b) };
   }
   return res;
}
 
test bool removeIdPairs1() = removeIdPairs({}) == {};
test bool removeIdPairs2() = removeIdPairs({pair(1,2),pair(2,3)}) == {pair(1,2),pair(2,3)};
test bool removeIdPairs3() = removeIdPairs({pair(1,2),pair(2,3),pair(2,2)}) == {pair(1,2),pair(2,3)};
test bool removeIdPairs4() = removeIdPairs({pair(1,2),pair(2,2),pair(2,3),pair(3,3)}) == {pair(1,2),pair(2,3)};
test bool removeIdPairs5() = removeIdPairs({pair(2,2),pair(1,2),pair(2,2),pair(2,3),pair(3,3)}) == {pair(1,2),pair(2,3)};
test bool removeIdPairs6() = removeIdPairs({pair(2,2),pair(3,3),pair(1,2),pair(2,2),pair(2,3),pair(3,3)}) == {pair(1,2),pair(2,3)};
