module lang::rascalcore::compile::Examples::Tst0

//import String;

//@javaClass{org.rascalmpl.library.Prelude}
//public java int size(list[&T] lst);
//
//@javaClass{org.rascalmpl.library.Type}
//public java bool eq(value x, value y);
//
//bool isEqual(list[&T] A, list[&T] B) { 
//    if(size(A) == size(B)){
//        for(int i <-[0 .. size(A)]){
//            if(!eq(A[i],B[i])) 
//                return false;
//        }
//        return true;
//    }
//    return false;
//}   
//
//test bool notEqual2(list[&T] A, list[&T] B) = (A != B) ? !isEqual(A,B) : isEqual(A,B);

//value main() = /[A-Z]/ !:= "abc";

//value main() = [1] !:= [1,2,3];

//value main() = tstToLowerCase("ABC");
//test bool noMatchImpliesUnequal(int a, int b) = (a !:= b) ==> a != b;
//
//value main() 
//test bool escapedPatternName1b() 
//    = x := 3 && x == 3;

//test bool nodeSetMatch() = { "a"(1) } := { "a"(1) };

//public list[int] primes(int upTo) = []; // TODO: replaced "p <- ..." by "int p <- ..." to help new typechecker
  
//
//public int arbPrime(int upTo) = ps[0] when list[int] ps := primes(upTo);
//
//test bool commutativeEq2(value x, value y) = (x := y) <==> (y := x);

//test bool tupleExpressions() {
//    //value n = 1; 
//    //value s = "string"; 
//    return int _ := 1; //tuple[int, int] _ := < 1, 1 >;// && tuple[str, str] _ := < s, s > && tuple[int, str] _ := < n , s >;
//}
//
//test bool reflexEq2(value x) = x := 1 && x == 1;
//
//test bool matchTypedAnonymous(){
//    b = [int _] := [1];
//    return b;
//}

//value f(value v){
//    if(int n := v) return n;
//    return 0;
//}

test bool transEq2(value x, value y, value z) = (x := y && y := z) ==> (x := z);

//test bool subsetOrdering2(set[value] x, set[value] y) = (x <= y) <==> (x == {} || all(e <- x, e in y));
