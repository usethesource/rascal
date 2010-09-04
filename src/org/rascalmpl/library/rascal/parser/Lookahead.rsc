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

// This production wrapper encodes what the lookahead set is for the productions it wraps
public data Production = lookahead(Symbol rhs, set[Symbol] classes, Production production);
public data Symbol = eoi();     // end-of-input marker
private data Grammar = simple(set[Symbol] start, set[Production] productions);

@doc{This function wraps productions with their single character lookahead sets for parser generation}
public Grammar computeLookaheads(Grammar G) {
  Grammar SimpleG = simple(G.start, { p | /Production p:prod(_,_,_) := G} + {p | /Production p:regular(_,_) := G});
  <fst, fol> = firstAndFollow(expandRegularSymbols(removeLabels(SimpleG)));
    
  return visit(G) {
    case Production p:prod([], Symbol rhs, _) => lookahead(rhs, fol[rhs], p)
    case Production p:prod(list[Symbol] lhs, Symbol rhs, _) : {
      // we start with the first set of the leftmost symbol
      lhs = removeLabels(lhs);
      <h,lhs> = headTail(lhs);
      classes = fst[h];

      // add the first of all symbols from left to right until one does not contain empty
      while (lhs != [], empty() in fst[h]) {
        classes += fst[h];
        <h,lhs> = headTail(lhs);
      }
      // if all symbols had empty in the first, add the follow of the rhs
      if (lhs == [], empty() in fst[h]) {
        classes += fol[rhs];
      }
      // merge the character classes and construct a production wrapper
      // TODO: should we really remove empty here?
      insert lookahead(rhs, classes - {empty()}, p);        
    }
  }
}

@doc{
  This function evaluates lookahead sets to obtain an optimal production selection automaton
  As a side-effect it also needs to replace priority ordering and associativity by the simple choice operator!  
}
public Grammar compileLookaheads(Grammar G) {
  return visit(G) {
    // no lookahead means never
    case lookahead(rhs, {}, a) => choice(rhs, {})
    // merge equal classes  
    case choice(rhs, alts) =>
         choice(rhs, {lookahead(rhs, c,  choice(rhs, {a1,a2}))} + rest)
    // factor commonalities
    case choice(rhs, {lookahead(rhs, c1, Production a1), lookahead(rhs, c2, Production a2), rest* }) =>
         choice(rhs, {lookahead(rhs, common, choice(rhs, {a1,a2})), lookahead(rhs, forA1, a1), lookahead(rhs, forA2, a2)} + rest)
      when common := intersect(c1, c2)
         , common != {}
         , forA1 := diff(c1,c2)
         , forA2 := diff(c2,c1)        
    case first(rhs, list[Production] order) => choice(rhs, { p | p <- order })
    case \assoc(rhs, _, set[Production] a) => choice(rhs, a)
  }
}

public set[Symbol] intersect(set[Symbol] u1, set[Symbol] u2) {
  if ({\char-class(r1), _*} := u1, {\char-class(r2), _*} := u2) {
    return mergeCC({\char-class(intersection(r1,r2))} + (u1 & u2));
  }
  return u1 & u2;
}

public set[Symbol] diff(set[Symbol] u1, set[Symbol] u2) {
  if ({\char-class(r1), _*} := u1, {\char-class(r2), _*} := u2) {
    return mergeCC({\char-class(difference(r1,r2))} + (u1 - u2));
  }
  return u1 - u2;
}


// Utilities on Symbols

public Grammar removeLabels(Grammar G) {
  G.productions = { prod(removeLabels(lhs), s, a) | prod(lhs,s,a) <- G.productions };
  return G;
}

public list[Symbol] removeLabels(list[Symbol] syms) {
  return visit(syms) {
    case label(_,sym) => sym
  }
}

public set[Symbol] usedSymbols(Grammar G){
   return { s |  Production p <- G.productions, /Symbol s <- p.lhs };
}

public set[Symbol] definedSymbols(Grammar G) {
   return { p.rhs |  Production p <- G.productions};
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
    for (S <- defSymbols, prod(lhs, S, _) <- G.productions) {
      FIRST[S] += isEmpty(lhs) ? {empty()} : first(lhs, FIRST) - {empty()};
    }
  }
		
  return FIRST;
}

public SymbolUse follow(Grammar G,  SymbolUse FIRST){
   defSymbols = definedSymbols(G);
   FOLLOW = (S : {eoi()} | Symbol S <- G.start) + (S : {} | Symbol S <- defSymbols);
  
   solve (FOLLOW) {
     for (Production p <- G.productions, [_*, current, symbols*] := p.lhs) {
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
  // try {
    fst = first(G);
    return <mergeCC(fst), mergeCC(follow(G,fst))>;
  // }   
  // catch NoSuchKey(Symbol s) : throw "Undefined non-terminal <s>";
  // throw "wtf?";
}

private SymbolUse mergeCC(SymbolUse su) {
  return innermost visit(su) {
     case {\char-class(r1),\char-class(r2),a*} => {a,\char-class(r1+r2)}
     case {\char-class([]), a*} => a
  }
}

private set[Symbol] mergeCC(set[Symbol] su) {
  switch (su) {
     case {\char-class(r1),\char-class(r2),a*} : return {a,\char-class(r1+r2)};
     case {\char-class([]), a*} : return a;
     default: return su;
  }
}


// -------- Examples and tests -------------------

/*
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
*/
