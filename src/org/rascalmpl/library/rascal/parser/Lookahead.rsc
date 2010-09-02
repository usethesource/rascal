module rascal::parser::Lookahead
  
import rascal::parser::Grammar;
import rascal::parser::Regular;
import rascal::parser::Characters;
import rascal::parser::Normalization;
import ParseTree;
import List;
import Set;
import IO;
import Exception;

// This production wrapper encodes what the lookahead is of the first non-terminal of a production
public data Production = lookahead(Symbol rhs, set[Symbol] classes, set[Production] ifMatched);
public data Symbol = eoi();     // end-of-input marker

@doc{This function wraps productions with their single character lookahead sets for parser generation}
public Grammar computeLookaheads(Grammar G) {
  <first, follow> = firstAndFollow(g);
  
  return visit(G) {
    case Production p:prod([], Symbol rhs, _) => lookahead(rhs, follow[rhs], {p})
    case Production p:prod(list[Symbol] lhs, Symbol rhs, _) : {
      // we start with the first set of the leftmost symbol
      <h,lhs> = takeOneFrom(lhs);
      SymbolUse classes = first[h];
      // add the first of all symbols from left to right until one does not contain empty
      while (lhs != [], empty() in first[h]) {
        <h,lhs> = takeOneFrom(lhs);
        classes += first[h];
      }
      // if all symbols had empty in the first, add the follow of the rhs
      if (lhs == [], empty() in first[h]) {
        classes += follow[rhs];
      }
      // merge the character classes and construct a production wrapper
      // TODO: should we really remove empty here?
      insert lookahead(rhs, mergeCC(classes - {empty()}), {p});        
    }
  }
}

@doc{
  This function evaluates lookahead sets to obtain an (near) optimal production selection automaton
  As a side-effect it also erases priority ordering!  
}
public Grammar compileLookaheads(Grammar G) {
  return innermost visit(G) {
    // no lookahead means never
    case lookahead(rhs, {}, a) => choice(rhs, {})
    // merge equal classes
    case choice(rhs, { lookahead(rhs, c, a1), lookahead(rhs, c, a2), rest* }) =>
         choice(rhs, { lookahead(rhs, c, a1 + a2) })
    // factor commonalities
    case choice(rhs, { lookahead(rhs, c1, a1), lookahead(rhs, c2, a2), rest* }) =>
         choice(rhs, { lookahead(rhs, common, a1), lookahead(rhs, forA1, a1), lookahead(rhs, forA2, a2), rest})
      when common := intersect(c1, c2)
         , common != {}
         , forA1 := diff(c1,c2)
         , forA2 := diff(c2,c1)        
    case first(rhs, list[Production] order) => choice(rhs, { p | p <- order })
  }
}

public SymbolUse intersect(SymbolUse u1, SymbolUse u2) {
  if ({\char-class(r1), r1*} := u1, {\char-class(r2), r2*} := u2) {
    return mergeCC({\char-class(intersection(r1,r2))} + (r1 & r2));
  }
  return u1 & u2;
}

public SymbolUse diff(SymbolUse u1, SymbolUse u2) {
  if ({\char-class(r1), r1*} := u1, {\char-class(r2), r2*} := u2) {
    return mergeCC({\char-class(difference(r1,r2))} + (u1 - u2));
  }
  return u1 - u2;
}


// Utilities on Symbols

public list[Symbol] removeLabels(list[Symbol] syms) {
  return visit(syms) {
    case label(_,sym) => sym
  }
}

public set[Symbol] usedSymbols(Grammar G){
   return { s |  Symbol t <- G.rules, Production p <- G.rules[t], /Symbol s <- p.lhs };
}

public set[Symbol] definedSymbols(Grammar G) {
   return { p.rhs |  Symbol t <- G.rules, Production p <- G.rules[t]};
}

public set[Symbol] allSymbols(Grammar G){
   return definedSymbols(G) + usedSymbols(G);
}

public set[Symbol] terminalSymbols(Grammar G){
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

public SymbolUse first(Grammar G){
  defSymbols = definedSymbols(G);

  SymbolUse FIRST = (trm : {trm} | Symbol trm <- terminalSymbols(G)) 
                  + (S : {}      | Symbol S   <- defSymbols);
	        
  solve (FIRST) {
    for (Symbol S <- defSymbols, Production p <- G.rules[S], list[Symbol] symbols := p.lhs) { 	
      FIRST[S] += isEmpty(symbols) ? {empty()} : first(symbols, FIRST) - {empty()};
    }
  }
		
  return FIRST;
}

public SymbolUse follow(Grammar G,  SymbolUse FIRST){
   defSymbols = definedSymbols(G);
   FOLLOW = (S : {eoi()} | Symbol S <- G.start) + (S : {} | Symbol S <- defSymbols);
  
   solve (FOLLOW) {
     for (S <- defSymbols, p <- G.rules[S], [_*, current, symbols*] := p.lhs) {
       flw =  first(symbols, FIRST);
       if (current in defSymbols) {
         if (empty() in flw || isEmpty(symbols)) {
           FOLLOW[current] += FOLLOW[p.rhs] + (flw - {empty()});
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
     case {\char-class([]), a*} => a
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

