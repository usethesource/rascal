module lang::rascalcore::compile::Examples::Tst7
import Exception;
import List;
import IO;

syntax Module = "module";
data Tree;

&T<:Tree parse(type[&T<:Tree] begin, loc input, bool allowAmbiguity=false, int maxAmbDepth=2, bool allowRecovery=false, int maxRecoveryAttempts=30, int maxRecoveryTokens=3, bool hasSideEffects=false, set[Tree(Tree)] filters={})
  = parser(begin, allowAmbiguity=allowAmbiguity, maxAmbDepth=maxAmbDepth, allowRecovery=allowRecovery, maxRecoveryAttempts=maxRecoveryAttempts, maxRecoveryTokens=maxRecoveryTokens, hasSideEffects=hasSideEffects, filters=filters)(input, input);

@javaClass{org.rascalmpl.library.Prelude}
java &T (value input, loc origin) parser(type[&T] grammar, bool allowAmbiguity=false, int maxAmbDepth=2, bool allowRecovery=false, int maxRecoveryAttempts=30, int maxRecoveryTokens=3, bool hasSideEffects=false, set[Tree(Tree)] filters={}); 

public void getRascalGrammar(loc grammarFile) {
  Module \module = 
                  parse 
                  (
                  #start[Module], 
                  grammarFile
                  )
                  .top;
}

alias Corpus = map[str Product, str Version];

data Expr;
data QueryResult
	= exprResult(loc l, Expr e)
	;

public void showUsageCounts(Corpus corpus, lrel[str p, str v, QueryResult qr] res) {
	mr = ( p2 : size([ e | <p1,_,e> <- res ]) | p2 <- corpus );
	for (p <- sort([p | str p <- mr<0>])) println("<p>:<mr[p]>");
}
 

 


//@synopsis{Find the first `haystack` folder the `needle` can be found in and relativize it, or fail.}
loc relativize(loc needle) throws PathNotFound {
    throw PathNotFound(needle);
}
     
 
@javaClass{org.rascalmpl.library.Prelude}
public java &T <: node setKeywordParameters(&T <: node x, map[str,value] keywordParameters);
 
data Tree;

Tree mergeRec(Tree t) {
      return setKeywordParameters(t, ());
    }

list[int] f(list[int] L) = [x | x <- L, x > 0];
    
bool isSorted(list[&T] l, bool (&T a, &T b) less = bool (&T a, &T b) { return a < b; })
 = !any([*_, &T a, &T b, *_] := l, less(b, a));
                     
// int f(int n, int m, bool b = false) = 2;
       
// import ParseTree;

// syntax Aas
//  = nil: [a]*
//  | a:   [a][a]*
//  | aas: [a][a][a]*
//  ;
//  &T <:Tree ambFilter(amb(set[&T <:Tree] alternatives)) {
//  set[&T <:Tree] result = {a | Aas a <- alternatives, !(a is nil)};
//  if ({&T <: Tree oneTree} := result) {
//    return oneTree;
//  }
//  return ParseTree::amb(result);
// }

@javaClass{org.rascalmpl.library.Prelude}
java &U (type[&U] nonterminal, value input, loc origin) parsers(type[&T] grammar); 
          
                   
      
list[&T] emptyList(type[&T] _) = [];  // ok

// &T f(&T x) { &T y = 1; return x;} // <==== error

// void f(&T x) { &T y = 1; }   // <==== error

void g(&T x) { &T <: int y  = 1; }   // ok

// &T get1(list[&T] _) = 1;  // <==== error

&T <: int get2(list[&T] _) = 1;  // ok

&T <: num sub(&T <:num x, &T<:num y) = x - y;  // ok
  
map[&K, &V] domainR1(map[&K, &V] M, set[&K] S)  // ok
   = (k : M[k] | &K k <- M, k in S);

list[&T] tail([&T _, *&T t]) = t;  // ok

&T top([&T t, *&T _]) = t;  // ok
               
&T getFirstFrom([&T f, *&T _]) = f;  // ok

&T max([&T h, *&T t]) = (h | e > it ? e : it | e <- t); //ok
        
&T <: int f(&T <: num _) = 1; // discussie; ik (en checker) denken ok    
       
&T <: int f(&T <: num x = 0) = 1; // discussie; ik (en checker) denken ok    
                   
list[&U] mapper(list[&T] lst, &U (&T) fn) =  [fn(elm) | &T elm <- lst];   // ok

// Following ok
alias GatherResult[&T] = tuple[bool trueOnAllPaths, set[&T] results];
data Graph[&T];     
data CFGNode;           
public GatherResult[&T] gatherOnAllReachedPaths(Graph[CFGNode] g, CFGNode startNode, bool(CFGNode cn) pred, bool(CFGNode cn) stop, &T (CFGNode cn) gather, bool includeStartNode = false) {
	GatherResult[&T] traverser(CFGNode currentNode) = traverser({currentNode});
	
	GatherResult[&T] traverser(set[CFGNode] currentNodes) {
		GatherResult[&T] res = < true, { } >;
		return res;
	}
	
	return traverser(startNode);
} 
  
 