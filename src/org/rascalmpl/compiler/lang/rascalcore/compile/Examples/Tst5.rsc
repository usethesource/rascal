module lang::rascalcore::compile::Examples::Tst5

@javaClass{org.rascalmpl.library.util.Math}
public java int round(num d);
public (&T <: num) round(&T <: num r, &T <: num nearest) = 1.0 * nearest;

//public &T <: num max(&T <: num N, &T <: num M)
//{
//    return N > M ? N : M;
//}
//test bool max8() = max(3.5, 10) == 10;

//syntax Mapping[&T]
//    = \default: &T!ifDefinedOtherwise from ":" &T to 
//    ;
//alias X[&T] = int;

//data Maybe[&A] 
//   = nothing() 
//   | just(&A val)
//   ;
//   
//value main() = just(v) := nothing();
   
//@javaClass{org.rascalmpl.library.Prelude}
//java &U (type[&U] nonterminal) parsers(int grammar); 

                            
 //(&T <:num) sum([ /(&T <: num) hd, *(&T <: num) tl]) = (hd | it + i | i <- tl);
 
//value main(){
//    /*x = \seq([]);*/
//    
//    return asubtype(avalue(), overloadedAType({}));
// 
// }
 
 //int f() = true;
 //
 //&T get(list[&T] _) = 1;
 //
 //&T <: num sub(&T <:num x, &T<:num y) = x - y;
 
 //int f() = "a";
 
 //list[&T] f(int _) = []; 
 //&T<:real  f(&T x) = 1.5;
 //&T<:real  f(&T x) = 1;
 
 //&T f(&T _) = 1; 
 
//
//list[&T] f(int _) = [1];
//
//list[&T] emptyList(list[&T] _) = [];
//list[&T] emptyList(list[&T] _) = [1];

//map[&K, &V] emptyMap(type[map[&K,&V]] _) = ();
 
 //set[&T0] domain (rel[&T0,&T1] R){ return R<0>; }
 
 //&T add(&T x, &T y) = y;
 
 //&T <: real f(&T <: int x) = 1.5;
 
 
 //&T <: real f(&T <: int x, &T y) = 1.5;
 
 //&T f(&T x) { &T y = 1; return x;}
 
 //rel[&T, &V] f(&T x) = {<x,x>};
 
 //void f(&T x) { &T y = 1; }
 //bool f(&A <: str a, &B <:int b) = &A _ := b;
 
 //list[&T <: num] f(&T <: num x)  = [1];

 
 //&T <: num f(&T <: int x) = x + 1;
 
 //list[&T <: num] f(&T <: int x) = [x+1];
 
 
 
  
 
 
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


//data Tree;
//&T<:Tree parse(type[&T<:Tree] begin, str input, bool allowAmbiguity=false, bool hasSideEffects=false, set[Tree(Tree)] filters={})
//  = parser(begin, allowAmbiguity=allowAmbiguity, hasSideEffects=hasSideEffects, filters=filters)(input, |unknown:///|);
//
//&T<:Tree parse(type[&T<:Tree] begin, str input, loc origin, bool allowAmbiguity=false, bool hasSideEffects=false, set[Tree(Tree)] filters={})
//  = parser(begin, allowAmbiguity=allowAmbiguity, hasSideEffects=hasSideEffects, filters=filters)(input, origin);
//  
//&T<:Tree parse(type[&T<:Tree] begin, loc input, bool allowAmbiguity=false, bool hasSideEffects=false, set[Tree(Tree)] filters={})
//  = parser(begin, allowAmbiguity=allowAmbiguity, hasSideEffects=hasSideEffects, filters=filters)(input, input);


//@synopsis{Generates a parser from an input grammar.}
//@description{
//This builtin function wraps the Rascal parser generator by transforming a grammar into a parsing function.
//
//The resulting parsing function has the following overloaded signature:
//
//   * Tree parse(str input, loc origin);
//   * Tree parse(loc input, loc origin);
//
//So the parse function reads either directly from a str or via the contents of a loc. It also takes a `origin` parameter
//which leads to the prefix of the `src` fields of the resulting tree.
//
//The parse function behaves differently depending of the given keyword parameters:
//     *  `allowAmbiguity`: if true then no exception is thrown in case of ambiguity and a parse forest is returned. if false,
//                         the parser throws an exception during tree building and produces only the first ambiguous subtree in its message.
//                         if set to `false`, the parse constructs trees in linear time. if set to `true` the parser constructs trees in polynomial time.
//     * 
//     *  `hasSideEffects`: if false then the parser is a lot faster when constructing trees, since it does not execute the parse _actions_ in an
//                         interpreted environment to make side effects (like a symbol table) and it can share more intermediate results as a result.
//}
//@javaClass{org.rascalmpl.library.Prelude}
//java &T (value input, loc origin) parser(type[&T] grammar, bool allowAmbiguity=false, bool hasSideEffects=false, set[Tree(Tree)] filters={}); 

//@javaClass{org.rascalmpl.library.Prelude}
//@synopsis{Generates a parser function that can be used to find the left-most deepest ambiguous sub-sentence.}
//@benefits{
//* Instead of trying to build a polynomially sized parse forest, this function only builds the smallest part of
//the tree that exhibits ambiguity. This can be done very quickly, while the whole forest could take minutes to hours to construct.
//* Use this function for ambiguity diagnostics and regression testing for ambiguity.
//}
//@pitfalls{
//* The returned sub-tree usually has a different type than the parameter of the type[] symbol that was passed in. 
//The reason is that sub-trees typically have a different non-terminal than the start non-terminal of a grammar.
//}
//java Tree (value input, loc origin) firstAmbiguityFinder(type[Tree] grammar, bool hasSideEffects=false, set[Tree(Tree)] filters={}); 

//@synopsis{Generates parsers from a grammar (reified type), where all non-terminals in the grammar can be used as start-symbol.}
//@description{
//This parser generator behaves the same as the `parser` function, but it produces parser functions which have an additional
//nonterminal parameter. This can be used to select a specific non-terminal from the grammar to use as start-symbol for parsing.
//}
//@javaClass{org.rascalmpl.library.Prelude}
//java &U (type[&U] nonterminal, value input, loc origin) parsers(type[&T] grammar, bool allowAmbiguity=false, bool hasSideEffects=false,  set[Tree(Tree)] filters={}); 

//@javaClass{org.rascalmpl.library.Prelude}
//java &U (type[&U] nonterminal, value input, loc origin) parsers(type[&T] grammar, bool allowAmbiguity=false, bool hasSideEffects=false,  set[Tree(Tree)] filters={}); 


//&T <: num makeSmallerThan(&T <: num n) {
//     if (int i := n) {
//         return i;    
//     }
//     return n;
// }
// 
// num main(){
//    num x = makeSmallerThan(2);
//    return x;
// }
// 
//&T <: num makeSmallerThan(&T <: num n) {
//     if (int i := n) {
//         &T <: num x = i;
//         return x;    
//     }
//     return n;
// }
 
  
 

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


