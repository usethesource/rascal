module lang::rascal::syntax::Lookahead
  
import Grammar;
import lang::rascal::syntax::Regular;
import lang::rascal::syntax::Characters;
import lang::rascal::syntax::Normalization;
import ParseTree;
import List;
import Set;
import IO;
import Map;
import Exception;

// This production wrapper encodes what the lookahead set is for the productions it wraps
public data Production = lookahead(Symbol rhs, set[Symbol] classes, Production production);
public data Symbol = eoi();     // end-of-input marker
private data Grammar = simple(set[Symbol] start, set[Production] productions);

@doc{
  This function wraps productions with their single character lookahead sets for 
  parser generation.
  
  'extra' contains extra lookahead symbols per symbol
}
public Grammar computeLookaheads(Grammar G, rel[Symbol,Symbol] extra) {
  G2 = expandRegularSymbols(removeLabels(G));
  <fst, fol> = firstAndFollow(simple(G2.start, { p | /Production p:prod(_,_,_) := G2}));
    
  return visit(G) {
    case Production p:prod(_,restricted(_),_) => p
    case Production p:prod([], Symbol rhs, _) => lookahead(rhs, fol[rhs], p)
    case Production p:prod(list[Symbol] lhs, Symbol rhs, _) : {
      lhs = removeLabels(lhs);
      
	  classes = first(lhs, fst);
	  
	  if (lhs == [] || empty() in classes)
	    classes += fol[rhs];
      
      classes -= empty();
      
      // merge the character classes and construct a production wrapper
      insert lookahead(rhs, mergeCC(classes + extra[rhs]), p);        
    }
  }
}

@doc{
  This function evaluates lookahead sets to obtain an optimal production selection automaton
  As a side-effect it also needs to replace priority ordering and associativity by the simple choice operator!  
}
public Grammar compileLookaheads(Grammar G) {
  // first we remove first and assoc groups for simplicity's sake
  G = visit (G) {
    case lookahead(rhs, {}, a) => choice(rhs, {})
    case first(rhs, order)     => choice(rhs, {p | p <- order})
    case \assoc(rhs, a, alts)  => choice(rhs, alts)
  }
  // give the normalizer the chance to merge choices as much as possible 
  G.rules = (s:{choice(s,G.rules[s])} | s <- G.rules);

  // now we optimize the lookaheads  
  return visit(G) {
    case choice(rhs, alts) => optimizeLookaheads(rhs, alts)
  }
}

public Production optimizeLookaheads(Symbol rhs, set[Production] alts) {
  list[CharRange] l = [];
  list[CharRange] order(list[CharRange] x) {
    return sort([ e | e <- x, e != \empty-range()], lessThan);
  }  
  
  // first we identify which unique ranges there are for all the different productions
  for (lookahead(_,set[Symbol] classes, Production p) <- alts) { 
    for (\char-class(rs) <- classes, r <- rs) {
      // find the first range that is not smaller than the pivot
      if ([pre*, post*] := l, all(z <- post, !lessThan(z,r)) || post == []) {
        // find all ranges that overlap with the pivot
        if ([overlapping*, post2*] := post, all(o <- overlapping, intersect(r,o) != \empty-range())) {
          // overlapping with existing ranges (contained in 'overlap')
          common = intersection(overlapping,[r]);
          onlyR = difference([r],overlapping);
          onlyOverlapping = difference(overlapping,[r]);
          l = pre + order(onlyR+common+onlyOverlapping) + post2;
        }
        else {
          // not overlapping with existing ranges
          l = pre + [r] + post;
        }
      }
      else {
        println("does this ever happen? <r> and <l>");
        l = [r] + l;
      }
    }
  }
 
  // second part; map productions into the ranges
  map[CharRange range,set[Production] prods] m = ();
  set[Production] init = {};
  for (lookahead(_,set[Symbol] classes, Production p) <- alts, \char-class(rs) <- classes) {
    for (CharRange r <- l) {
      if (intersection([r],rs) != []) {
        m[r]?init += {p};
      }
    }
  }

  // third part, group by ranges that predict the same set of productions  
  map[set[Production] prod, set[CharRange] range] mInv = ();
  set[CharRange] init2 = {};
  for (<r,s> <- m<range,prods>) {
    mInv[s]?init2 += {r}; 
  }
  
  endOfInputClasses = { p | lookahead(_,classes,p) <- alts, eoi() in classes};
  
  return choice(rhs, {lookahead(rhs, {\char-class([r | r <- mInv[s]])}, choice(rhs, s)) | s <- mInv}
                  +  ((endOfInputClasses != {}) ? {lookahead(rhs, {eoi()}, choice(rhs, endOfInputClasses))} : {}));
}

public Production optimizeLookaheadsOld(Symbol rhs, set[Production] alts) {
  solve (alts) {
    for (a:lookahead(_,c1,p1) <- alts, b:lookahead(_,c2,p2) <- alts, a != b) {
      if (c1 == c2) {
        // merge similars first (also makes alts smaller for optimal performance and termination
        alts -= {a,b};
        alts += {lookahead(rhs, c1, choice(rhs, {p1,p2}))};
      }
      else {
        common = intersect(c1, c2);
        if (common != {}) {
          // split overlapping classes, making each class smaller for termination
          diff1 = diff(c1,c2);
          diff2 = diff(c2,c1);
          alts -= {a,b};
          alts += {lookahead(rhs, common, choice(rhs, {p1,p2}))};
          if (diff1 != {}) 
            alts += lookahead(rhs, diff1, p1);
          if (diff2 != {})
            alts += lookahead(rhs, diff2, p2); 
        }
      }
    }
  }
  return choice(rhs, alts);
}

public set[Symbol] intersect(set[Symbol] u1, set[Symbol] u2) {
  if ({\char-class(r1), _*} := u1, {\char-class(r2), _*} := u2) {
    return {\char-class(intersection(r1,r2))} + (u1 & u2);
  }
  return u1 & u2;
}

public set[Symbol] diff(set[Symbol] u1, set[Symbol] u2) {
  if ({\char-class(r1), s1*} := u1, {\char-class(r2), s2*} := u2) {
    return {\char-class(difference(r1,r2))} + (s1 - s2);
  }
  return u1 - u2;
}


// Utilities on Symbols

public Grammar removeLabels(Grammar G) {
  return visit(G) {
    case prod(lhs, rhs, a) => prod(removeLabels(lhs), rhs, a)
  }
}

public list[Symbol] removeLabels(list[Symbol] syms) {
  return [(label(_,s2) := s) ? s2 : s | s <- syms ];
}

public set[Symbol] usedSymbols(Grammar G){
   return { s |  Production p:prod(_,_,_) <- G.productions, /Symbol s <- p.lhs };
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

public bool isTerminalSymbol(Symbol s){
  return \char-class(_) := s;
}

// ---------------- Compute first set -------------------------------

alias SymbolUse = map[Symbol, set[Symbol]] ;

public set[Symbol] first(list[Symbol] symbols, SymbolUse FIRST){
  set[Symbol] result = {};
	
  for (Symbol S <- symbols) {
    f = FIRST[S];
    if (empty() notin f) {
      return result - empty() + f;
    } else {
      result += f;
    }
  }
  return result;
}

// First set of a grammar

public SymbolUse first(Grammar G) {
  defSymbols = definedSymbols(G);

  SymbolUse FIRST = (trm : {trm} | Symbol trm <- terminalSymbols(G)) 
                  + (S : {}      | Symbol S   <- defSymbols);
	        
  def2lhs = {<S , lhs> | S <- defSymbols, prod(lhs, S, _) <- G.productions};
  
  solve (FIRST) {
    for (<S, lhs> <- def2lhs) {
      FIRST[S] += isEmpty(lhs) ? {empty()} : first(lhs, FIRST); //- {empty()};
    }
  }
		
  return FIRST;
}

/********* Original definition  ****
public SymbolUse first(Grammar G) {
  defSymbols = definedSymbols(G);

  SymbolUse FIRST = (trm : {trm} | Symbol trm <- terminalSymbols(G)) 
                  + (S : {}      | Symbol S   <- defSymbols);
	        
  solve (FIRST) {
    for (S <- defSymbols, prod(lhs, S, _) <- G.productions) {
      FIRST[S] += isEmpty(lhs) ? {empty()} : first(lhs, FIRST); //- {empty()};
    }
  }
		
  return FIRST;
}
******************************/

public SymbolUse follow(Grammar G,  SymbolUse FIRST){
   defSymbols = definedSymbols(G);
   
   rel[Symbol, Symbol] F = {<S, eoi()> | Symbol S <- G.start};
   
   for (Production p <- G.productions, [_*, current, symbols*] := p.lhs) {
       if (current in defSymbols) {
          flw =  first(symbols, FIRST);
          if (empty() in flw || isEmpty(symbols)) {
             flw -=  {empty()};
             flw += {p.rhs};
          }
          F += {<current, s> | s <- flw};
       }
   }
  
   F = F*;
   FOLLOW = (defSym : F[defSym] - defSymbols | Symbol defSym <- defSymbols + G.start);
   return FOLLOW;
}

/*******  Original definition

public SymbolUse follow0(Grammar G,  SymbolUse FIRST){
   defSymbols = definedSymbols(G);
   SymbolUse FOLLOW = (S : {eoi()} | Symbol S <- G.start) + (S : {} | Symbol S <- defSymbols);
  
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

***************************/

public tuple[SymbolUse, SymbolUse] firstAndFollow(Grammar G){
  // try {
    fst = first(G);
    return <mergeCC(fst), mergeCC(follow(G,fst))>;
  // }   
  // catch NoSuchKey(Symbol s) : throw "Undefined non-terminal <s>";
  // throw "wtf?";
}
  
private SymbolUse mergeCC(SymbolUse su) {
  for (Symbol s <- su)
    su[s] = mergeCC(su[s]); 
  return su;
}

private set[Symbol] mergeCC(set[Symbol] su) {
  result = {};
  if (empty() in su) 
    result += empty();
    
  if (eoi() in su) 
    result += eoi();
  
  rs = [];  
  for (\char-class(r) <- su) 
    rs = union(rs, r);
  
  if (rs != []) 
    result += \char-class(rs);
  
  return result;  
}


// -------- Examples and tests -------------------

public Grammar G0 = simple({sort("S")}, {});

test first(G0) == ();

test firstAndFollow(G0) == <(), (sort("S"):{eoi()})>;

private Production pr(Symbol rhs, list[Symbol] lhs) {
  return prod(lhs, rhs, \no-attrs());
}

public Grammar Lit1 = simple({}, {
  pr(lit("*"),[\char-class([range(42,42)])]),
	pr(lit("+"),[\char-class([range(43,43)])]),
	pr(lit("0"),[\char-class([range(48,48)])]),
	pr(lit("1"),[\char-class([range(49,49)])])
});

public Grammar G1 = simple({sort("E")},
{
	pr(sort("E"), [sort("E"), lit("*"), sort("B")]),
	pr(sort("E"), [sort("E"), lit("+"), sort("B")]),
	pr(sort("E"), [sort("B")]),
	pr(sort("B"), [lit("0")]),
	pr(sort("B"), [lit("1")])
} + Lit1.productions);

test usedSymbols(G1) >= {lit("0"),lit("1"),sort("E"),sort("B"),lit("*"),lit("+")};

test definedSymbols(G1) == {sort("E"),sort("B"),lit("+"),lit("*"),lit("0"),lit("1")};

test G1.start < definedSymbols(G1);

public SymbolUse firstLit1 = (
  lit("0"):{\char-class([range(48,48)])},
  lit("1"):{\char-class([range(49,49)])},
  lit("*"):{\char-class([range(42,42)])},
  lit("+"):{\char-class([range(43,43)])}
);

test SymbolUse F := first(G1) 
     && F[sort("E")] == {\char-class([range(49,49)]),\char-class([range(48,48)])}
     && F[sort("B")] == {\char-class([range(49,49)]),\char-class([range(48,48)])}
     ;
                       
public Grammar G2 = simple({sort("E")},
{
	pr(sort("E"), [sort("E"), lit("*"), sort("B")]),
    pr(sort("E"), [sort("E"), lit("+"), sort("B")]),
	pr(sort("E"), [sort("B")]),
	pr(sort("B"), [lit("0")]),
    pr(sort("B"), [lit("1")])
} + Lit1.productions);

test SymbolUse F := first(G2)
     && F[sort("E")] == {\char-class([range(48,48)]),\char-class([range(49,49)])}
     && F[sort("B")] == {\char-class([range(48,48)]),\char-class([range(49,49)])}
     ;

public Grammar G3 = simple( {sort("E")},
{
	pr(sort("E"),  [sort("T"), sort("E1")]),
	pr(sort("E1"), [lit("+"), sort("T"), sort("E1")]),
	pr(lit("+"), [\char-class([range(43,43)])]),
	pr(sort("E1"), []),
	pr(sort("T"),  [sort("F"), sort("T1")]),
	pr(sort("T1"), [lit("*"), sort("F"), sort("T1")]),
	pr(lit("*"), [\char-class([range(42,42)])]),
	pr(sort("T1"), []),
	pr(sort("F"),  [lit("("), sort("E"), lit(")")]),
	pr(lit("("), [\char-class([range(40,40)])]),
	pr(lit(")"), [\char-class([range(41,41)])]),
	pr(sort("F"),  [lit("id")]),
	pr(lit("id"), [\char-class([range(105,105)]),\char-class([range(100,100)])])
});

private SymbolUse F3 = first(G3);

test F3[sort("F")] == {\char-class([range(105,105)]),\char-class([range(40,40)])};
test F3[sort("T")] == F3[sort("F")];
test F3[sort("E")] == F3[sort("T")];
test F3[lit("*")] == {\char-class([range(42,42)])};
test F3[lit("+")] == {\char-class([range(43,43)])};
test F3[lit("id")] == {\char-class([range(105,105)])};
test F3[sort("E1")] == {empty()} + F3[lit("+")];
test F3[sort("T1")] == {empty()} + F3[lit("*")];
test F3[lit("(")] == {\char-class([range(40,40)])};
test F3[lit(")")] == {\char-class([range(41,41)])};
     
      
public SymbolUse Fol3 = follow(G3, first(G3));
 
test Fol3[sort("E")] == {\char-class([range(41,41)]), eoi()};
test Fol3[sort("E1")] == {\char-class([range(41,41)]), eoi()};
test Fol3[sort("T")] == {\char-class([range(43,43)]),\char-class([range(41,41)]),eoi()};
test Fol3[sort("T1")] == {\char-class([range(43,43)]),\char-class([range(41,41)]),eoi()};
test Fol3[sort("F")] == {\char-class([range(43,43)]),\char-class([range(42,42)]),\char-class([range(41,41)]),eoi()};
     
       
public Grammar Session = simple({sort("Session")},
{
	pr(sort("Session"), [sort("Facts"), sort("Question")]),
	pr(sort("Session"), [lit("("), sort("Session"), lit(")"), sort("Session")]),
	pr(lit("("), [\char-class([range(40,40)])]),
	pr(lit(")"), [\char-class([range(41,41)])]),
	pr(sort("Facts"),   [sort("Fact"), sort("Facts")]),
	pr(sort("Facts"),   []),
	pr(sort("Fact"),    [lit("!"), sort("STRING")]),
	pr(lit("!"), [\char-class([range(33,33)])]),
	pr(sort("Question"),[lit("?"), sort("STRING")]),
	pr(lit("?"), [\char-class([range(63,63)])]),
	pr(sort("STRING"),  [lit("a")]),
	pr(lit("a"), [\char-class([range(97,97)])])
});

private SymbolUse SF = first(Session);

test SF[sort("Question")] == {\char-class([range(63,63)])};
test SF[sort("Session")] == {\char-class([range(33,33)]),\char-class([range(40,40)]),\char-class([range(63,63)])};
test SF[sort("Facts")] == {\char-class([range(33,33)]),empty()};
test SF[lit("a")] == {\char-class([range(97,97)])};
test SF[lit("!")] == {\char-class([range(33,33)])};
test SF[lit("?")] == {\char-class([range(63,63)])};
test SF[lit("(")] == {\char-class([range(40,40)])};
test SF[lit(")")] == {\char-class([range(41,41)])};
test SF[sort("STRING")] == {\char-class([range(97,97)])};
test SF[sort("Fact")] == {\char-class([range(33,33)])};
     
test follow(Session, first(Session)) >=
 	 (sort("Question"):{\char-class([range(41,41)]),eoi()},
 	 sort("Session"):{\char-class([range(41,41)]),eoi()},
 	 sort("Facts"):{\char-class([range(63,63)])},
 	 sort("STRING"):{\char-class([range(33,33),range(41,41),range(63,63)]),eoi()},
 	 sort("Fact"):{\char-class([range(33,33),range(63,63)])}
 	 );
