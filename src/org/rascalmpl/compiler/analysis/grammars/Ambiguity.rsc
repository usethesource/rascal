@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bas Basten - Bas.Basten@cwi.nl - CWI}
module analysis::grammars::Ambiguity
 
import ParseTree;
import ValueIO;
import Message; 
import List;
import Set;
import analysis::graphs::Graph;
import Grammar;
import lang::rascal::format::Grammar;

public list[Message] diagnose(Tree t) {
  return [*findCauses(x) | x <- {a | /Tree a:amb(_) := t}];
}

public list[Message] diagnose(str amb) {
  return diagnose(readTextValueString(#Tree, amb));
}

public list[Message] findCauses(Tree a) {
  return [info("Ambiguity cluster with <size(a.alternatives)> alternatives", a@\loc?|dunno:///|)]
       + [*findCauses(x, y) | [*_,Tree x,*_,Tree y, *_] := toList(a.alternatives), true /* workaround alert*/];
}
    
public list[Message] findCauses(Tree x, Tree y) {
  pX = { p | /Production p := x };
  pY = { p | /Production p := y };
  list[Message] result = [];
  
  if (pX == pY) {
    result += [info("The alternatives use the same productions", x@\loc?|dunno:///|)];
  }
  else {
      result += [info("Production unique to the one alternative: <alt2rascal(p)>;", x@\loc?|dunno:///|) | p <- pX - pY];
      result += [info("Production unique to the other alternative: <alt2rascal(p)>;", x@\loc?|dunno:///|) | p <- pY - pX];
  }  
  
  if (appl(prodX,_) := x, appl(prodY,_) := y) {
    if (prodX == prodY) {
    result += [info("The alternatives have the same production at the top: <alt2rascal(prodX)>", x@\loc?|dunno:///|)];
    } 
    else {
      result += [info("The alternatives have different productions at the top, one has 
                      '  <alt2rascal(prodX)>
                      'while the other has
                      '  <alt2rascal(prodY)>", x@\loc?|dunno:///|)];
    }
  }
  
  result += deeperCauses(x, y, pX, pY);
  result += reorderingCauses(x, y); 
  result += verticalCauses(x, y, pX, pY);
  
  return result;
}

public list[Message] verticalCauses(Tree x, Tree y, set[Production] pX, set[Production] pY) {
  return exceptAdvise(x, y, pX, pY)
       + exceptAdvise(y, x, pY, pX);
}

public list[Message] exceptAdvise(Tree x, Tree y, set[Production] _pX, set[Production] pY) {
  result = [];
  if (appl(p, argsX) := x, appl(q, _argsY) := y) {
    if (i <- index(argsX), appl(apX,_) := argsX[i], apX notin pY) {
      labelApX = "labelX";
      
      if (prod(label(l,_),_,_) := apX) {
        labelApX = l;
      }
      else {
        result += [warning("You should give this production a good label: 
                           '  <alt2rascal(apX)>]",x@\loc?|dunno:///|)];
      }
       
      result += [error("To fix this issue, you could restrict the nesting of
                       '  <alt2rascal(apX)>
                       'under
                       '  <alt2rascal(p)>
                       'using the ! operator on argument <i/2>: !<labelApX>
                       'However, you should realize that you are introducing a restriction that makes the language smaller",x@\loc?|dunno:///|)];
    }
     
  }
  return result;
}

public list[Message] deeperCauses(Tree x, Tree y, set[Production] pX, set[Production] pY) {
  // collect lexical trees
  rX = {<t,yield(t)> | /t:appl(prod(\lex(_),_,_),_) := x} + {<t,yield(t)> | /t:appl(prod(label(_,\lex(_)),_,_),_) := x};
  rY = {<t,yield(t)> | /t:appl(prod(\lex(_),_,_),_) := y} + {<t,yield(t)> | /t:appl(prod(label(_,\lex(_)),_,_),_) := y};
 
  // collect literals
  lX = {<yield(t),t> | /t:appl(prod(lit(_),_,_),_) := x};
  lY = {<yield(t),t> | /t:appl(prod(lit(_),_,_),_) := y};
  // collect layout
  laX = {<t,yield(t)> | /t:appl(prod(layouts(_),_,_),_) := x} + {<t,yield(t)> | /t:appl(prod(label(_,layouts(_)),_,_),_) := x};
  laY = {<t,yield(t)> | /t:appl(prod(layouts(_),_,_),_) := y} + {<t,yield(t)> | /t:appl(prod(label(_,layouts(_)),_,_),_) := y};
    
  result = [];
  
  if (rX<0> != rY<0> || lX<1> != lY<1>) {
    result += [info("The alternatives have different lexicals/literals/layout", x@\loc?|dunno:///| )];
    result += [info("Unique lexical to the one: <alt2rascal(p)>;", t[0]@\loc?|dunno:///|) | t <- (rX - rY), p := t[0].prod];
    result += [info("Unique lexical to the other: <alt2rascal(p)>;", t[0]@\loc?|dunno:///|) | t <- (rY - rX), p := t[0].prod];
    result += [info("Unique literal to the one: <symbol2rascal(t[1].prod.def)>", x@\loc?|dunno:///|) | t <- lX - lY];
    result += [info("Unique literal to the other: <symbol2rascal(t[1].prod.def)>", x@\loc?|dunno:///|) | t <- lY - lX];
    result += [info("Unique layout to the one: <symbol2rascal(t[0].prod.def)>", x@\loc?|dunno:///|) | t <- laX - laY];
    result += [info("Unique layout to the other: <symbol2rascal(t[0].prod.def)>", x@\loc?|dunno:///|) | t <- laY - laX];
    
    // literals that became lexicals and vice versa
    result += [error("You might reserve <l> from <symbol2rascal(r.prod.def)>, i.e. using a reject (reserved keyword).", r@\loc?|dunno:///|) | <r,l> <- rX o lY];
    result += [error("You might reserve <l> from <symbol2rascal(r.prod.def)>, i.e. using a reject (reserved keyword).", r@\loc?|dunno:///|) | <r,l> <- rY o lX];
    
    // lexicals that overlap position, but are shorter (longest match issue)
    for (<tX,yX> <- rX, <tY,yY> <- rY, tX != tY) {
      tXl = tX@\loc; 
      tYl = tY@\loc;
      
      // <-------->
      //    <--->
      if (tXl.begin >= tYl.begin && tXl.end <= tYl.end) { 
        result += error("<tX> is overlapping with <tY>, add follow restrictions or a symbol table!", tXl);
      }
      
      //    <--->
      // <-------->
      if (tYl.begin >= tXl.begin && tYl.end <= tXl.end) {
        result += error("<tX> is overlapping with <tY>, add follow/precede restrictions!", tXl);
      }
      
      // <----->
      //    <----->
      if (tXl.end >= tYl.begin && tXl.end <= tYl.end) {
        result += error("<tX> is overlapping with <tY>, add follow/precede restrictions!", tXl);
      }
      
      //     <---->     
      // <----->
      if (tXl.begin >= tYl.begin && tXl.begin <= tYl.end) {
        result += error("<tX> is overlapping with <tY>, add follow/precede restrictions!", tXl);
      }
    }   
  }
 
 
  // find parents of literals, and transfer location
  polX = {<p,l[@\loc=t@\loc?|dunno:///|]> | /t:appl(p,[*_,l:appl(prod(lit(_),_,_),_),*_]) := x, true}; 
  polY = {<l[@\loc=t@\loc?|dunno:///|],p> | /t:appl(p,[*_,l:appl(prod(lit(_),_,_),_),*_]) := y, true};
  overloadedLits = [info("Literal \"<l>\" is used in both
                     '  <alt2rascal(p)> and
                     '  <alt2rascal(q)>", l@\loc) | <p,l> <- polX, <l,q> <- polY, p != q, !(p in pY || q in pX)];
  
  if (overloadedLits != []) {
    result += info("Re-use of these literals is causing different interpretations of the same source.", x@\loc?|dunno:///|);
    result += overloadedLits;
    
    fatherChildX = {<p, size(a), q> | appl(p, [*a,appl(q,_),*_]) := x, q.def is sort || q.def is lex, true};
    fatherChildY = {<p, size(a), q> | appl(p, [*a,appl(q,_),*_]) := y, q.def is sort || q.def is lex, true};
    for (<p,i,q> <- (fatherChildX - fatherChildY) + (fatherChildY - fatherChildX)) {
      labelApX = "labelX";
      
      if (prod(label(l,_),_,_) := q) {
        labelApX = l;
      }
      else {
        result += [warning("You should give this production a good label [<alt2rascal(q)>]",x@\loc?|dunno:///|)];
      }
       
      result += [error("You could safely restrict the nesting of
                       '  <alt2rascal(q)> 
                       'under
                       '  <alt2rascal(p)>
                       'using the ! operator on argument <i/2>: !<labelApX>",x@\loc?|dunno:///|)];
    } 
  }
  
  return result; 
}

public list[int] yield(Tree x) {
  return [i | /Tree y:char(int i) := x, y == x];
}

public list[Message] reorderingCauses(Tree x, Tree y) {
  fatherChildX = {<p, q> | appl(p, [*_,appl(q,_),*_]) := x, true};
  fatherChildY = {<p, q> | appl(p, [*_,appl(q,_),*_]) := y, true};
  result = [];
  
  if (fatherChildX == fatherChildY) {
    result += associativityCauses(x, y);   
  }
  else {
    result += associativityCauses(x, y);
    result += priorityCauses(x, y);
    result += danglingCauses(x, y);
  }
  
  return result;
}

list[Message] priorityCauses(Tree x, Tree y) {
  if (/appl(p,[appl(q,_),_*]) := x, /Tree t:appl(q,[_*,appl(p,_)]) := y, p != q) {
      return [error("You might add this priority rule (or vice versa):
                    '  <alt2rascal(priority(p.def,[p,q]))>", t@\loc)
             ,error("You might add this associativity rule (or right/assoc/non-assoc):
                    '  <alt2rascal(associativity(p.def, \left(), {p,q}))>", t@\loc?|dunno:///|)];
  }
  
  if (/appl(p,[appl(q,_),*_]) := y, /Tree t:appl(q,[*_,appl(p,_)]) := x, p != q) {
      return [error("You might add this priority rule (or vice versa):
                    '  <alt2rascal(priority(p.def,[p,q]))>", t@\loc)
             ,error("You might add this associativity rule (or right/assoc/non-assoc):
                    '  <alt2rascal(associativity(p.def, \left(), {p,q}))>", t@\loc?|dunno:///|)];
  }
  
  return [];
}

list[Message] danglingCauses(Tree x, Tree y) {
  if (appl(p,/appl(q,_)) := x, appl(q,/appl(p,_)) := y) {
    return danglingOffsideSolutions(x, y)
         + danglingFollowSolutions(x, y);
  }

  return [];
}

list[Message] danglingFollowSolutions(Tree x, Tree y) {
  if (prod(_, lhs, _) := x.prod, prod(_, [pref*, _, l:lit(_), *_more], _) := y.prod, lhs == pref) {
    return [error("You might add a follow restriction for <symbol2rascal(l)> on:
                    ' <alt2rascal(x.prod)>", x@\loc?|dunno:///|)]; 
  }
  
  if (prod(_, lhs, _) := y.prod, prod(_, [*pref, _, l:lit(_), *_more], _) := x.prod, lhs == pref) {
    return [error("You might add a follow restriction for <symbol2rascal(l)> on:
                  '  <alt2rascal(y.prod)>", x@\loc?|dunno:///|)]; 
  }
  
  return []; 
}

list[Message] danglingOffsideSolutions(Tree x, Tree y) {
  if (appl(p,/Tree u:appl(q,_)) := x, appl(q,/appl(p,_)) := y
     , (u@\loc).begin.column >= (x@\loc).begin.column
     , (u@\loc).begin.line < (x@\loc).end.line) {
    return [error("You might declare nested <prod2rascal(q)> offside (to the left) of some child of <prod2rascal(p)> using a failing syntax action that compares annotations @loc.start.column", u@\loc)];
  }
  
  if (appl(p,/Tree u:appl(q,_)) := y, appl(q,/appl(p,_)) := x
     , (u@\loc).begin.column >= (y@\loc).begin.column
     , (u@\loc).begin.line < (y@\loc).end.line) {
    return [error("You might declare nested <prod2rascal(q)> offside (to the left) of some child of <prod2rascal(p)> using a failing syntax action that compares annotations @loc.start.column", u@\loc)];
  }
  
  return [];
}

list[Message] associativityCauses(Tree x, Tree y) {
  if (/appl(p,[appl(p,_),*_]) := x, /Tree t:appl(p,[*_,appl(p,_)]) := y) {
    return [error("This rule [<alt2rascal(p)>] may be missing an associativity declaration  (left, right, non-assoc)", t@\loc)];
  }
  
  if (/appl(p,[appl(p,_),*_]) := y, /Tree t:appl(p,[*_,appl(p,_)]) := x) {
    return [error("This rule [<alt2rascal(p)>] may be missing an associativity declaration (left, right, non-assoc)", t@\loc)];
  }
  
  return [];  
}
