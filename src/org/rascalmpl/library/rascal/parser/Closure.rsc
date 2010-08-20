module rascal::parser::Closure
  
import rascal::parser::Grammar;
import rascal::parser::Regular;
import ParseTree;
import List;
import Set;
import IO;
import Exception;

data Symbol = eoi();     // end-of-input marker

// Define an internal "kernel" grammar format that is more convenient.It essentially removes all
// the above combinators

public alias KernelProduction  = tuple[Symbol nonTerminal, list[Symbol] symbols];
public data KernelGrammar      = kernelGrammar(set[Symbol] start, set[KernelProduction] productions); 

public KernelGrammar importGrammar(Grammar G) {
   if (grammar(set[Symbol] starts, set[Production] nonterminals) := G) { 
     return kernelGrammar(starts, { <rhs,removeLabels(lhs)> | /prod(lhs,rhs,_) <- expandRegularSymbols(G).productions});
   }
   else if (grammar(set[Symbol] starts, map[Symbol,set[Production]] rules) := G) {
     return kernelGrammar(starts, { <rhs, removeLabels(lhs)> | /prod(lhs,rhs,_) <- expandRegularSymbols(G).rules});
   }
} 

// Utilities on Symbols

public list[Symbol] removeLabels(list[Symbol] syms) {
  return visit(syms) {
    case label(_,sym) => sym
  }
}

public set[Symbol] usedSymbols(KernelGrammar G){
   return { s |  KernelProduction p <- G.productions, /Symbol s <- p.symbols };
}

public set[Symbol] definedSymbols(KernelGrammar G) {
   return { p.nonTerminal |  KernelProduction p <- G.productions};
}

public set[Symbol] allSymbols(KernelGrammar G){
   return definedSymbols(G) + usedSymbols(G);
}

public set[Symbol] terminalSymbols(KernelGrammar G){
   return { S | S:\char-class(_) <- usedSymbols(G)};
}

// ---------------- Compute first set -------------------------------

alias SymbolUse = map[Symbol, set[Symbol]] ;

public set[Symbol] first(list[Symbol] symbols, SymbolUse FIRST){
  set[Symbol] result = {};
	
  for (Symbol S <- symbols) {
    f = FIRST[S];
    if (empty() notin f) {
      return result + f;
    } else {
      result += f;
    }
  }
  
  return result;
}

// First set of a grammar

public SymbolUse first(KernelGrammar G){
        defSymbols = definedSymbols(G);

	SymbolUse FIRST = (trm : {trm} | Symbol trm <- terminalSymbols(G)) + 
	                  (S : {}      | Symbol S   <- defSymbols);
	        
	solve (FIRST) 
          for (Symbol S <- defSymbols, list[Symbol] symbols <- G.productions[S]) 	
             FIRST[S] += isEmpty(symbols) ? {empty()} : first(symbols, FIRST) - {empty()};
          
		
	return FIRST;
}

public SymbolUse follow(KernelGrammar G,  SymbolUse FIRST){
   defSymbols = definedSymbols(G);
   FOLLOW = (S : {eoi()} | Symbol S <- G.start) + (S : {} | Symbol S <- defSymbols);
  
   solve (FOLLOW) {
     for (KernelProduction p <- G.productions, [_*, current, symbols*] := p.symbols) {
       flw =  first(symbols, FIRST);
       if (current in defSymbols) {
         if (empty() in flw || isEmpty(symbols)) {
           FOLLOW[current] += FOLLOW[p.nonTerminal] + (flw - {empty()});
         }
         else {
           FOLLOW[current] += flw;
         }
       }
     }
   }

   return FOLLOW;
}

public tuple[SymbolUse, SymbolUse] firstAndFollow(Grammar G){
  try {
    K = importGrammar(G);
    fst = first(K);
    return <mergeCC(fst), mergeCC(follow(K,fst))>;
  }
  catch NoSuchKey(Symbol s) : throw "Undefined non-terminal <s>";
}

private SymbolUse mergeCC(SymbolUse su) {
  return innermost visit(su) {
     case {\char-class(r1),\char-class(r2),a*} => {a,\char-class(r1+r2)}
  }
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
	first([pr(sort("E"), [sort("E"), lit("*"), sort("B")]),
     	 pr(sort("E"), [sort("E"), lit("+"), sort("B")])
    	]),
	pr(sort("E"), [sort("B")]),
	choice({pr(sort("B"), [lit("0")]),
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
      sort("E1"):{lit("+"),empty()},
      sort("T1"):{lit("*"),empty()},
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
      sort("Facts"):{lit("!"),empty()},
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

