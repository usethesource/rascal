module lang::rascalcore::compile::Examples::Tst1


//import List;
//import Node;
//import Exception;


private &T something(set[&T] x) {
   if (e <- x) 
     return e;
   // this should not happen because returning &T should
   // add the matching constraint that &T is not void  
   assert false;  
   throw "what?";
}

private default value something({}) = "hello";

test bool parameterizedFunctionsDoNotReturnVoid() {
  set[value] emptySet = {};
  return "hello" == something(emptySet);
}

test bool functionTypeArgumentVariance1() =
  int (value _) _ := int (int x ) { return 1; };
  
//test bool functionTypeArgumentVariance2() =
//  int (int _) _ := int (value x ) { return 1; };
//  
//test bool functionTypeArgumentVariance3() {
//  value f = int (str x ) { return 1; };
//  return int (int _) _ !:= f;
//} 
//
//test bool higherOrderFunctionCompatibility1() {
//   // the parameter function is specific to int
//   int parameter(int _) { return 0; }
//   
//   // the higher order function expects to call the
//   // parameter function with other things too
//   int hof(int (value) p, value i) { return p(i); }
//   
//   // still this is ok, since functions in Rascal
//   // are partial. This call should simply succeed:
//   if (hof(parameter, 1) != 0) {
//     return false;
//   }
//   
//   // but the next call produces a CallFailed, since
//   // the parameter function is not defined on strings:
//   try {
//     // statically allowed! but dynamically failing
//     hof(parameter, "string");
//     return false;
//   } 
//   catch CallFailed(_):
//     return true; 
//}
//
//test bool higherOrderFunctionCompatibility2() {
//   // the parameter function is very generic
//   int parameter(value _) { return 0; }
//   
//   // the higher order function expects to call the
//   // parameter function with only integers
//   int hof(int (int) p, value i) { return p(i); }
//   
//   // this is ok, a more generic function should be
//   // able to handle ints. This call should simply succeed:
//   if (hof(parameter, 1) != 0) {
//     return false;
//   }
//   
//   return true;
//}
//
//@ignore{this fails also in the interpreter because the algorithm
//for binding type parameter uses `match` in two directions which 
//implements comparability rather than intersectability}
//test bool higherOrderFunctionCompatibility3() {
//   // the parameter function is specific for tuple[int, value]
//   int parameter(tuple[int, value] _) { return 0; }
//   
//   // the higher order function expects to call the
//   // parameter function with tuple[value, int]
//   int hof(int (tuple[value, int]) p, tuple[value,int] i) { return p(i); }
//   
//   // this is ok, the parameter function's type has a non-empty
//   // intersection at tuple[int, int], so at least for such 
//   // tuples the function should succeed
//   if (hof(parameter, <1,1>) != 0) {
//     return false;
//   }
//   
//   // however, when called with other tuples the parameter fails
//   // at run-time:
//   try {
//     // statically allowed! but dynamically failing
//     hof(parameter, <"1", 1>);
//     return false;
//   } 
//   catch CallFailed(_):
//     return true; 
//     
//   return true;
//}
  



//bool  solver(bool (value, value) unify1) = true;
//   
//bool unify(int x, str y) = true;
//bool unify(int x, str y, bool b) = b; //<<< uncomment en krijg CallFailed
//
//bool newSolver(){
//    return solver(unify);
//}
//
//value main() = newSolver();

//test bool transEq1(value x, value y, value z) = (x == y && y == z) ==> (x == z);
                                          
//data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);
//
//value main()//test bool matchListSet7() 
//    = ([a(), f({a(), b(), DATA X6})] := [a(), f({a(),b(),c()})]) && (X6 == c());
//test bool matchListSet9() = ([a(), f({a(), b(), DATA X8}), *DATA Y8] := [a(), f({a(),b(),c()}), b()]) && (X8 == c() && Y8 == [b()]);


//data F = f(F left, F right) | g(int N);
//test bool descendant33() = [1, [F] /f(/g(2), F _), 3] := [1, f(g(1),f(g(2),g(3))), 3];

//value main() // test bool descendant34() 
//    = [1, /f(/g(2),/g(3)), 3] := [1, f(g(1),f(g(2),g(3))), 3];
    
//test bool descendant37() = [1, /g(int N2), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N2 == 2;
//test bool descendant38() = [1, /g(int N3), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N3 == 3;


   
//////////
//value main() = {<int a, int b>, <b, b>, *c} := {<1, 2>, <2, 2>, <3,4>};
//value main() {list[int] L; return ([1, *L, 4, 5] := [1, 2, 3, 4, 5] && L == [2, 3]);}


// int f11(int c = 10){
//        int g11(int d = 100){
//            return c + d;
//        }
//        return g11();
//    }
//    
//value main(){ //test bool keywordParam92(){
//    return f11();
//}

//int f13(int n, str s = "") = n when s == "";
//int f13(int n, str s = "") = -n when s != "";
//
//value main() // test bool when1() 
//    = f13(10);// == 10;

//data D[&T] = d1(&T fld);
//
//value main() = #type[D[int]]
 

//syntax Sym
//    =   
//     empty: "(" ")"
//    ;
//
//syntax Type
//    = bracket \bracket: "(" Type type ")" 
//    | symbol: Sym symbol
//    ;
//    
//value main() = true;
//
//
//data Symbol     // <2>
//     = \label(str name, Symbol symbol)
//     ;


