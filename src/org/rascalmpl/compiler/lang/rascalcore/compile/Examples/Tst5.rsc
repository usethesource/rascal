module lang::rascalcore::compile::Examples::Tst5

 
 rel[&T, &V] f(&T x) = {<x,x>};
 
//data Maybe[&A] 
//   = nothing() 
//   | just(&A val)
//   ;
//Maybe[int] f() = nothing();
//
//set[int] main(){
//    just(n) := nothing();
//    if(just(n) := nothing()) return {n};
//    return {};
//}


//tuple[&T, list[&T]] headTail(list[&T] l) {
//      if ([&T h, *&T t] := l) {
//        return <h, t>;
//      }
//      return <0,[]>; 
//   }


//&T f(&T _) = 1;
// 

//list[&T] f(int _) = [];

//list[&T] emptyList(type[&T] _) = [];

//map[&K, &V] emptyMap(type[map[&K,&V]] _) = ();

//data Tree;

//@javaClass{org.rascalmpl.library.Prelude}
//java &T (value input, loc origin) parser(type[&T] grammar, bool allowAmbiguity=false, bool hasSideEffects=false, set[Tree(Tree)] filters={}); 

//@javaClass{org.rascalmpl.library.Prelude}
//java &U (type[&U] nonterminal, value input, loc origin) parsers(type[&T] grammar, bool allowAmbiguity=false, bool hasSideEffects=false,  set[Tree(Tree)] filters={}); 


//&T <: num makeSmallerThan(&T <: num n) {
//     if (int i := n) {
//         return i;    
//     }
//     return n;
// }
// 
//&T <: num makeSmallerThan(&T <: num n) {
//     if (int i := n) {
//         &T <: num x = i;
//         return x;    
//     }
//     return n;
// }
 
  
 //&T <: num f(&T <: int x) = x + 1;
 
 //list[&T <: num] f(&T <: int x) = [x+1];
 
 //&T <: num sub(&T <:num x, &T y) = x - y;

//MH
//public void showUsageCounts(Corpus corpus, lrel[str p, str v, QueryResult qr] res) {
//    mr = ( p : size([ e | <p,_,e> <- res ]) | p <- corpus );
//    for (p <- sort([p | str p <- mr<0>])) println("<p>:<mr[p]>");
//}


//data Wrapper[&SAME] = something(&SAME wrapped);
//
//@synopsis{it matters for testing that '&SAME' is the same name as in the definition of Wrapper}
//&XXXSAME getIt(Wrapper[&XXXSAME] x) = x.wrapped;
//
//value main() { //test bool hygienicGenericADT() {
//  // in the same context, we bind the same type parameter in
//  // different ways to see if nothing is leaking.
//  int i = something(1).wrapped;
//  int j = getIt(something(2));
//  int k = getIt(something(3));
//  str x = something("x").wrapped;
//  str y = getIt(something("y"));
//  
//  return i == 1 && j == 2 && k == 3
//      && x == "x" && y == "y";
//}


