module Grammar

import ParseTree;
import List;
import Set;
import IO;

// A grammar is simply a set of productions
data Grammar = grammar(set[Symbol] start, set[Production] productions);

// Here we extend productions with basic combinators allowing to
// construct ordered and un-ordered compositions, and also a difference operator.
//
// The intended semantics are that 
// 		'or' means unordered choice,
// 		'xor' means ordered choice, where alternatives are tried from left to right,
// 		'diff' means all alternatives of the first argument are accepted, unless one
// 		of the alternatives from the right argument are accepted.

data Production = or(set[Production] alternatives)                  
                | xor(list[Production] choices)
                | assoc(Associativity assoc, set[Production] alternatives)               
                | diff(Production language, set[Production] alternatives)
                ;

data Symbol = epsilon() // empty string, used for first/follow analysis
            | eoi()     // end-of-input marker
            ;

// Define an internal "kernel" grammar format that is more convenient.It essentially removes all
// the above combinators

public alias KernelProduction  = tuple[Symbol nonTerminal, list[Symbol] symbols];
public data KernelGrammar      = kernelGrammar(set[Symbol] start, set[KernelProduction] productions); 

// Import an (AsFix-based) grammar and convert it to a KernelGrammar
// Some AsFix features are not (yet) supported:
//   \cf(Symbol symbol)  |
//   \lex(Symbol symbol)  |
//   \empty()  |
//   \seq(list[Symbol] symbols)  |
//   \alt(Symbol lhs, Symbol rhs)  |
//   \tuple(Symbol head, list[Symbol] rest)  | 
//   \iter-n(Symbol symbol, int number)  | 
//   \iter-sep-n(Symbol symbol, Symbol separator, int number)  | 
//   \func(list[Symbol] symbols, Symbol symbol)  | 
//   \parameterized-sort(str sort, list[Symbol] parameters)  | 
//   \strategy(Symbol lhs, Symbol rhs)  |
//   \var-sym(Symbol symbol)  |
//   \layout()  | 
    
public KernelGrammar importGrammar(Grammar G){
   return kernelGrammar(G.start, {getKernelProductions(p) | Production p <- G.productions});
} 

set[KernelProduction] getKernelProductions(Production p){
    switch(p){
    case prod(_,_,_): return {<p.rhs, p.lhs>};
    case or(set[Production] alts): return {getKernelProductions(p) | Production p <- alts};
    case xor(list[Production] alts): return {getKernelProductions(p) | Production p <- alts};
    case assoc(_, set[Production] alts): return {getKernelProductions(p) | Production p <- alts};
    case diff(Production p, set[Production] alts): 
         return {<p.rhs, p.lhs>} + {getKernelProductions(p) | Production p <- alts};
  }
}

// Utilities on Symbols

bool isTerminal(Symbol S){
   switch(S){
   case lit(_): return true;
   case cilit(_): return true;
   case \char-class(_): return true;
   }
   return false;
}

bool isNonTerminal(Symbol S){
	return !isTerminal(S);
}

// Get the symbols that are used in a kernel production

set[Symbol] usedSymbols(KernelProduction p){
  return toSet(p.symbols);
}

// Get the symbols that are defined by a kernel production

set[Symbol] definedSymbols(KernelProduction p){
   return {p.nonTerminal};
}

// Get all the symbols used in a kernel grammar

public set[Symbol] usedSymbols(KernelGrammar G){
   return { usedSymbols(p) | KernelProduction p <- G.productions};
}

// Get all the symbols defined in a kernel grammar

public set[Symbol] definedSymbols(KernelGrammar G){
   return { definedSymbols(p) | KernelProduction p <- G.productions};
}

// Get all the symbols in a kernel grammar

public set[Symbol] allSymbols(KernelGrammar G){
   return definedSymbols(G) + usedSymbols(G);
}

// Get all the terminal symbols in a kernel grammar

public set[Symbol] terminalSymbols(KernelGrammar G){
   return { S | Symbol S <- usedSymbols(G), isTerminal(S)};
}

// Get all the start symbols of kernel grammar

public set[Symbol] getStartSymbols(KernelGrammar G){
   return G.start;
}

// ---------------- Compute first set -------------------------------

alias SymbolUse = map[Symbol, set[Symbol]] ;

// First set of a single symbol

public set[Symbol] first(Symbol sym, SymbolUse FIRST){
   switch(sym){
   case lit(_): return FIRST[sym] ? {sym};
   case sort(_): return FIRST[sym] ? {};
   case iter(Symbol S): return FIRST[S] ? {};
   case iter(Symbol S, Symbol Sep):{
			f = FIRST[S] ? {};
			g = FIRST[Sep] ? {};
			return (epsilon() in f) ? f + g : f;
		}
   case \iter-star(Symbol S):
			return (FIRST[S] ? {}) + {epsilon()};
   case \iter-star-sep(Symbol S, Symbol Sep):{
			f = FIRST[S] ? {};
			g = FIRST[Sep] ? {};
			return {epsilon()} + ((epsilon() in f) ? f + g : f);
		}
   case opt(Symbol S):
   			return (FIRST[S] ? {}) + {epsilon()};   
   }
   throw IllegalArgument(sym);
}

// First set of a list of symbols

public set[Symbol] first(list[Symbol] symbols, SymbolUse FIRST){
    set[Symbol] result = {};
	for(Symbol S <- symbols){
	    f = FIRST[S] ? {};
	    if(epsilon() notin f)
		   return result + f;
		else
		   result += f;
	}
	return result;
}

// First set of a grammar

public SymbolUse first(KernelGrammar G){
	defSymbols = definedSymbols(G);
	FIRST = (trm : {trm} | Symbol trm <- terminalSymbols(G)) + 
	        (S : {} | Symbol S <- defSymbols);
	        
	
	solve (FIRST) {
		for(Symbol S <- defSymbols){	
			for(list[Symbol] symbols <- G.productions[S]){
				FIRST[S] += isEmpty(symbols) ? {epsilon()} : first(symbols, FIRST) - {epsilon()};
			}
		}
	}	
	return FIRST;
}

// Follow set of a grammar

public SymbolUse follow(KernelGrammar G,  SymbolUse FIRST){
   defSymbols = definedSymbols(G);
   FOLLOW = (S : {eoi()} | Symbol S <- G.start) + 
            (S : {} | Symbol S <- defSymbols);
  
   solve(FOLLOW){
   		for(KernelProduction p <- G.productions){
       		symbols = p.symbols;
       		while(!isEmpty(symbols)){
      			current = head(symbols);
      			symbols = tail(symbols);
      			if(current in defSymbols){
      				flw =  first(symbols, FIRST);
      				if(epsilon() in flw || isEmpty(symbols))
      					FOLLOW[current] += FOLLOW[p.nonTerminal] + (flw - {epsilon()});
      				else
      		    		FOLLOW[current] += flw;
      		    }
      		}
       }
   }
   return FOLLOW;
}

// Get first and follow sets for a given grammar

public tuple[SymbolUse, SymbolUse] firstAndFollow(Grammar G){
	K = importGrammar(G);
	fst = first(K);
	return <fst, follow(K,fst)>;
}

// -------- Examples and tests -------------------

// Turn BNF order production into an old SDF style production
Production pr(Symbol nt, list[Symbol] elms){
  return prod(elms, nt, \no-attrs());
} 

public Grammar G0 = grammar({sort("S")},
{
});

test first(importGrammar(G0)) == ();

test firstAndFollow(G0) == <(), (sort("S"):{eoi()})>;

public Grammar G1 = grammar({sort("E")},
{
	pr(sort("E"), [sort("E"), lit("*"), sort("B")]),
	pr(sort("E"), [sort("E"), lit("+"), sort("B")]),
	pr(sort("E"), [sort("B")]),
	pr(sort("B"), [lit("0")]),
	pr(sort("B"), [lit("1")])
});

test usedSymbols(importGrammar(G1)) == {lit("0"),lit("1"),sort("E"),sort("B"),lit("*"),lit("+")};

test definedSymbols(importGrammar(G1)) == {sort("E"),sort("B")};

test G1.start < definedSymbols(importGrammar(G1));

test first(importGrammar(G1)) ==
	 (lit("0"):{lit("0")},
      sort("E"):{lit("0"),lit("1")},
      lit("1"):{lit("1")},
      sort("B"):{lit("0"),lit("1")},
      lit("*"):{lit("*")},
      lit("+"):{lit("+")}
     );
                                 
public Grammar G2 = grammar({sort("E")},
{
	xor([pr(sort("E"), [sort("E"), lit("*"), sort("B")]),
     	 pr(sort("E"), [sort("E"), lit("+"), sort("B")])
    	]),
	pr(sort("E"), [sort("B")]),
	or({pr(sort("B"), [lit("0")]),
    	pr(sort("B"), [lit("1")])
   		})
});

test first(importGrammar(G2)) ==
	(lit("0"):{lit("0")},
     sort("E"):{lit("0"),lit("1")},
     lit("1"):{lit("1")},
     sort("B"):{lit("0"),lit("1")},
     lit("*"):{lit("*")},
     lit("+"):{lit("+")}
    );

public Grammar G3 = grammar( {sort("E")},
{
	pr(sort("E"),  [sort("T"), sort("E1")]),
	pr(sort("E1"), [lit("+"), sort("T"), sort("E1")]),
	pr(sort("E1"), []),
	pr(sort("T"),  [sort("F"), sort("T1")]),
	pr(sort("T1"), [lit("*"), sort("F"), sort("T1")]),
	pr(sort("T1"), []),
	pr(sort("F"),  [lit("("), sort("E"), lit(")")]),
	pr(sort("F"),  [lit("id")])
});

public KernelGrammar K3 = importGrammar(G3);
test first(K3) ==
	 (sort("F"):{lit("id"),lit("(")},
      sort("T"):{lit("id"),lit("(")},
      sort("E"):{lit("id"),lit("(")},
      lit("*"):{lit("*")},
      lit("+"):{lit("+")},
      lit("id"):{lit("id")},
      sort("E1"):{lit("+"),epsilon()},
      sort("T1"):{lit("*"),epsilon()},
      lit("("): {lit("(")},
      lit(")"): {lit(")")}
     );
      
test follow(K3, first(K3)) ==
     (sort("E"):{lit(")"), eoi()},
      sort("E1"):{lit(")"), eoi()},
      sort("T"):{lit("+"), lit(")"), eoi()},
      sort("T1"):{lit("+"), lit(")"), eoi()},
      sort("F"):{lit("+"), lit("*"), lit(")"), eoi()}
     );
       
public Grammar Session = grammar({sort("Session")},
{
	pr(sort("Session"), [sort("Facts"), sort("Question")]),
	pr(sort("Session"), [lit("("), sort("Session"), lit(")"), sort("Session")]),
	pr(sort("Facts"),   [sort("Fact"), sort("Facts")]),
	pr(sort("Facts"),   []),
	pr(sort("Fact"),    [lit("!"), sort("STRING")]),
	pr(sort("Question"),[lit("?"), sort("STRING")]),
	pr(sort("STRING"),  [lit("a")])
});

KernelGrammar KSession = importGrammar(Session);

test first(KSession) ==
     (sort("Question"):{lit("?")},
      sort("Session"):{lit("!"),lit("("), lit("?")},
      sort("Facts"):{lit("!"),epsilon()},
      lit("a"):{lit("a")},
      lit("!"):{lit("!")},
      lit("?"):{lit("?")},
      lit("("):{lit("(")},
      lit(")"):{lit(")")},
      sort("STRING"):{lit("a")},
      sort("Fact"):{lit("!")}
     );
     
test follow(KSession, first(KSession)) ==
 	 (sort("Question"):{lit(")"),eoi()},
 	 sort("Session"):{lit(")"),eoi()},
 	 sort("Facts"):{lit("?")},
 	 sort("STRING"):{lit("!"),lit(")"),lit("?"),eoi()},
 	 sort("Fact"):{lit("!"),lit("?")}
 	 );


                