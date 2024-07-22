@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::rascal::grammar::Lookahead
  
import Grammar;
import lang::rascal::grammar::definition::Regular;
import lang::rascal::grammar::definition::Characters;
import lang::rascal::grammar::definition::Productions;
import ParseTree;
import List;
import IO;

// This production wrapper encodes what the lookahead set is for the productions it wraps
public data Production = lookahead(Symbol def, set[Symbol] classes, Production production);
public data Symbol = eoi();     // end-of-input marker
private data Grammar = simple(set[Symbol] starts, set[Production] productions);

@synopsis{This function wraps productions with their single character lookahead sets for 
  parser generation.
  
  'extra' contains extra lookahead symbols per symbol}
public Grammar computeLookaheads(Grammar G, rel[Symbol,Symbol] extra) {
  G2 = expandRegularSymbols(removeLabels(G));
  <fst, fol> = firstAndFollow(simple(G2.starts, {p | /Production p:prod(_,_,_) := G2}));
    
  return visit(G) {
    case Production p:prod(Symbol rhs,[],  _) => lookahead(rhs, fol[rhs], p)
    case Production p:prod(Symbol rhs,list[Symbol] lhs,  _) : {
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

@synopsis{This function evaluates lookahead sets to obtain an optimal production selection automaton
  As a side-effect it also needs to replace priority ordering and associativity by the simple choice operator!}
public Grammar compileLookaheads(Grammar G) {
  // first we remove first and assoc groups for simplicity's sake
  G = visit (G) {
    case lookahead(rhs, {}, _) => choice(rhs, {})
    case priority(rhs, order)     => choice(rhs, {p | p <- order})
    case associativity(rhs, _, alts)  => choice(rhs, alts)
  }

  // now we optimize the lookaheads  
  return visit(G) {
    case choice(rhs, alts) => optimizeLookaheads(rhs, alts)
  }
}

// TODO: The following function was defined local to optimizeLookaheads
// but this gives a not yet explained validation error for the
// function ids in the corresponding overloaded function

list[CharRange] order(list[CharRange] x) {
    return sort([ e | e <- x, e != \empty-range()], lessThan);
}
  
public Production optimizeLookaheads(Symbol rhs, set[Production] alts) {
  list[CharRange] l = [];
  
  // first we identify which unique ranges there are for all the different productions
  for (lookahead(_,set[Symbol] classes, Production _) <- alts) { 
    for (\char-class(rs) <- classes, r <- rs) {
      // find the first range that is not smaller than the pivot
      if ([*pre, *post] := l, all(z <- post, !lessThan(z,r)) || post == []) {
        // find all ranges that overlap with the pivot
        if ([*overlapping, *post2] := post, all(\o <- overlapping, intersect(r,\o) != \empty-range())) {
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
  if ({\char-class(r1), *_} := u1, {\char-class(r2), *_} := u2) {
    return {\char-class(intersection(r1,r2))} + (u1 & u2);
  }
  return u1 & u2;
}

public set[Symbol] diff(set[Symbol] u1, set[Symbol] u2) {
  if ({\char-class(r1), *s1} := u1, {\char-class(r2), *s2} := u2) {
    return {\char-class(difference(r1,r2))} + (s1 - s2);
  }
  return u1 - u2;
}


// Utilities on Symbols

private Grammar removeLabels(Grammar G) {
  return visit(G) {
    case prod(rhs, lhs, a) => prod(removeLabel(rhs), removeLabels(lhs), a)
  }
}

private Symbol removeLabel(Symbol s) {
  return (label(_,s2) := s) ? s2 : s;
}

private list[Symbol] removeLabels(list[Symbol] syms) {
  return [removeLabel(s) | s <- syms ];
}

private set[Symbol] usedSymbols(Grammar G){
   return { s |  Production p:prod(_,_,_) <- G.productions, /Symbol s <- p.symbols };
}

private set[Symbol] definedSymbols(Grammar G) {
   return { p.def |  Production p <- G.productions};
}

private set[Symbol] terminalSymbols(Grammar G){
   return { S | S:\char-class(_) <- usedSymbols(G)};
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
	        
  def2lhs = {<S , lhs> | S <- defSymbols, prod(S, lhs, _) <- G.productions};
  
  solve (FIRST) {
    for (<S, lhs> <- def2lhs) {
      FIRST[S] += isEmpty(lhs) ? {empty()} : first(lhs, FIRST); //- {empty()};
    }
  }
		
  return FIRST;
}


public SymbolUse follow(Grammar G,  SymbolUse FIRST){
   defSymbols = definedSymbols(G);
   
   rel[Symbol, Symbol] F = {<S, eoi()> | Symbol S <- G.starts};
   
   for (Production p <- G.productions, [*_, current, *symbols] := p.symbols) {
       if (current in defSymbols) {
          flw =  first(symbols, FIRST);
          if (empty() in flw || isEmpty(symbols)) {
             flw -=  {empty()};
             flw += {p.def};
          }
          F += {<current, s> | s <- flw};
       }
   }
  
   F = F*;
   FOLLOW = (defSym : F[defSym] - defSymbols | Symbol defSym <- defSymbols + G.starts);
   return FOLLOW;
}

public tuple[SymbolUse, SymbolUse] firstAndFollow(Grammar G){
    fst = first(G);
    return <mergeCC(fst), mergeCC(follow(G,fst))>;
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


 	 
