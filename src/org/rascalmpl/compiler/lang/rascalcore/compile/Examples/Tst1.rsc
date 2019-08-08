module lang::rascalcore::compile::Examples::Tst1

value main() {  list[tuple[int n,str \type]] L = [<1, "a">, <2, "b">, <1, "c">]; return L[1]; }
  
//import Boolean;
//import Exception;
//// Operators
//
//test bool sanity() = true != false;
//
//test bool or(bool b) { if (true || b == true, b || true == true, false || false == false) return true; else return false; }  
//  
//test bool and(bool b) { if ((false && b) == false, (b && false) == false, (true && true) == true) return true; else return false; }
//
//test bool not(bool b) = !!b == b;
//
//test bool not() = (!true == false) && (!false == true);
//
//test bool equiv(bool b1, bool b2) = (b1 <==> b2) <==> (!b1 && !b2 || b1 && b2);
//
//test bool impl(bool b1, bool b2) = (b1 ==> b2) <==> !(b1 && !b2);
//
//// Library functions
//
//test bool tstArbBool() { b = arbBool() ; return b == true || b == false; }
//
//test bool fromString1() = fromString("true") == true && fromString("false") == false;
//
//@expected{IllegalArgument}
//test bool fromString1(str s) = fromString(s); // will fail in the rare situation that "true" or "false" are passed as argument.
//
//test bool tstToInt() = toInt(false) == 0 && toInt(true) == 1;
//
//test bool tstToReal() = toReal(false) == 0.0 && toInt(true) == 1.0;
//
//test bool tstToString() = toString(false) == "false" && toString(true) == "true";
//
//test bool shortCircuiting() { 
//    try { return false ==> (1/0 == 0) && true || (1/0 == 0) && !(false && (1/0 == 0)); }
//    catch ArithmeticException(str _): { return false; }
//    }
//
//test bool compositeAnd1a() {
//    b = 10;
//    return [*int x, int  y, *int z] := [1,2,3] && b > 9;
//}
//
//test bool compositeAnd1b() {
//    n = 0;
//    b = 10;
//    if( [*int x, int  y, *int z] := [1,2,3] && b > 9 )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 3;
//}
//
//test bool compositeAnd1c() {
//    n = 0;
//    b = 10;
//    if( [*int x, int  y, *int z] := [1,2,3] , b > 9 )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 3;
//}
//
//
//test bool compositeAnd2a() {
//    b = 10;
//    return b > 9 && [*int x, int  y, *int z] := [1,2,3];
//}
//
//test bool compositeAnd2b() {
//    n = 0;
//    b = 10;
//    if( b > 9 && [*int x, int  y, *int z] := [1,2,3] )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 3;
//}
//
//test bool compositeAnd2c() {
//    n = 0;
//    b = 10;
//    if( b > 9 && [*int x, int  y, *int z] := [1,2,3] )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 3;
//}
//
//test bool compositeAnd3a() {
//    b = 10;
//    return [*int x, int  y, *int z] := [1,2,3] && [*int p, int  q, *int r] := [4, 5, 6];
//}
//
//test bool compositeAnd3b() {
//    n = 0;
//    b = 10;
//    if( [*int x, int  y, *int z] := [1,2,3] && [*int p, int  q, *int r] := [4, 5, 6] )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 9;
//}
//
//test bool compositeAnd3c() {
//    n = 0;
//    b = 10;
//    if( [*int x, int  y, *int z] := [1,2,3] , [*int p, int  q, *int r] := [4, 5, 6] )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 9;
//}
//
//test bool compositeAndOr() {
//    n = 0;
//    if( ([*int x,*int y] := [1,2,3] && int z := 3) || ([*int x,*int y] := [4,5,6] && int z := 4) )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 8;
//    }
    
   
//-----------------------------------------
//import IO;
//int main() { bool b = true; c = b && true; return c ? 1 : 2;; }

//int main() { bool b = [x*,y,*z] := [1,2,3] && true; return 13; }

//bool main() { bool b = [x*,y,*z] := [1,2,3] ; return b; }
  
//str main() {
//    n = 0;
//    
//   x = "<while(n < 0) { n += 1;>x< }>";
//   return x;
//} 
 
//public default (&T <:num) xxx([(&T <: num) hd, *(&T <: num) tl])
//    = (hd | it + i | i <- tl);          

//value main() = [ x | x <- [ n | n <- [1..4], n >= 2]];  

//value main() = [*int x] := [1,2,3] || 3 > 2;

//value main() = [*int x, y] := [1,2,3] || 5 > 7;
                       
                 
//int main() {
//    n = 0;
//    l = if( ([*int x,*int y] := [1,2,3] && int z := 3) || ([*int x,*int y] := [4,5,6] && int z := 4) )  {
//        n = n + 1;
//        fail;
//    }
//    
//    return n; 
//} 

//bool f(bool b) = b;
//
//bool main() {
//    return f([*int x, int  y, *int z] := [1,2,3] &&  4 > 5);
//}     
//   

//   
//bool main() {
//    b = [_] := [1,2,3];
//    return b;
//}      
  
         
  
   

//bool main() {
//    b = [*int x, int  y, *int z] := [1,2,3] &&  [*int p, int q, *int r] := [4,5,6];
//    return b;
//}   

//int main() {
//    int n = 0;
//    if([*int x, int  y, *int z] := [1,2,3]){
//        n += 1;
//        fail;
//    }
//    return n;
//}    
         
//int main() {
//    int n = 0;
//    if(5 > 4 && [*int p, int q, *int r] := [4,5,6] && 7 > 6){
//        n += 1;
//        fail;
//    }
//    return n;
//}          
    
//int main() {
//    int n = 0;
//    if([*int x, int  y, *int z] := [1,2,3] && [*int p, int q, *int r] := [4,5,6]){
//        n += 1;
//        fail;
//    }
//    return n;
//}         
       
//int main() {
//    int n = 0;
//    if([*int x, int  y, *int z] := [1,2,3] && [*int p, int q, *int r] := [4,5,6] && q > 4){
//        n += 1;
//        fail;
//    }
//    return n;
//}        