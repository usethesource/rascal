@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bas Basten - Bas.Basten@cwi.nl - CWI}
module Ambiguity

import ParseTree;
import IO;
import ValueIO;
import Message;
import List;
import Set;
import Relation;
import  analysis::graphs::Graph;
import Grammar;
import lang::rascal::format::Grammar;
import lang::rascal::format::Escape;

public list[Message] diagnose(Tree t) {
  return [*findCauses(x) | x <- {a | /Tree a:amb(_) := t}];
}

public list[Message] diagnose(str amb) {
  return diagnose(readTextValueString(#Tree, amb));
}

public list[Message] findCauses(Tree a) {
  return [info("Ambiguity cluster with <size(a.alternatives)> alternatives", a@\loc?|dunno:///|)]
       + [*findCauses(x, y) | [_*,Tree x,_*,Tree y, _*] := toList(a.alternatives), true /* workaround alert*/];
}
    
public list[Message] findCauses(Tree x, Tree y) {
  pX = { p | /Production p := x };
  pY = { p | /Production p := y };
  list[Message] result = [];
  
  if (pX == pY) {
    result += [info("The alternatives use the same productions", x@\loc?|dunno:///|)];
  }
  else {
      result += [info("Production unique to the one: <alt2rascal(p)>;", x@\loc?|dunno:///|) | p <- pX - pY];
      result += [info("Production unique to the other: <alt2rascal(p)>;", x@\loc?|dunno:///|) | p <- pY - pX];
  }  
  
  result += deeperCauses(x, y);
  result += reorderingCauses(x, y); 
  vert = verticalCauses(x, y);
  if (vert == []) {
    result += [info("The ambiguity is horizontal (same productions at the top)", x@\loc?|dunno:///|)];
  }
  result += vert;
  
  return result;
}

public list[Message] verticalCauses(Tree x, Tree y) {
  if (appl(p, _) := x, appl(q, _) := y, p != q) {
    return [error("Vertical ambiguity (different productions at the top) might be solved using a prefer/avoid: <symbol2rascal(p.def)> = <prod2rascal(p)>; versus <symbol2rascal(q.def)> = <prod2rascal(q)>;", x@\loc)];
  }
  return [];
}

public list[Message] deeperCauses(Tree x, Tree y) {
  // collect lexical trees
  rX = {<t,yield(t)> | /t:appl(prod(\lex(_),_,_),_) := x} + {<t,yield(t)> | /t:appl(prod(label(_,\lex(_)),_,_),_) := x};
  rY = {<t,yield(t)> | /t:appl(prod(\lex(_),_,_),_) := y} + {<t,yield(t)> | /t:appl(prod(label(_,\lex(_)),_,_),_) := y};
 
  // collect literals
  lX = {<yield(t),t> | /t:appl(prod(l:lit(_),_,_),_) := x};
  lY = {<yield(t),t> | /t:appl(prod(l:lit(_),_,_),_) := y};
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
  polX = {<p,l[@\loc=t@\loc]> | /t:appl(p,[_*,b,_,l:appl(prod(lit(_),_,_),_),_*]) := x, true}; 
  polY = {<l[@\loc=t@\loc],p> | /t:appl(p,[_*,b,_,l:appl(prod(lit(_),_,_),_),_*]) := y, true};
  overloadedLits = [info("Literal \"<l1>\" is used in both
                     '  <alt2rascal(p1)> and
                     '  <alt2rascal(p2)>", l1@\loc) | <p1,p2> <- polX o polY, p1 != p2
            , l1 <- polX[p1], l2 <- (polY<1,0>)[p2], (l1@\loc).end == (l2@\loc).end];
  
  if (overloadedLits != []) {
    result += info("Overloaded literals may be solved by semantic actions that filter certain nestings", x@\loc?|dunno:///|);
    result += overloadedLits;
    
    fatherChildX = {<p, q> | appl(p, [_*,appl(q,_),_*]) := x, true /* workaround alert*/};
    fatherChildY = {<p, q> | appl(p, [_*,appl(q,_),_*]) := y, true /* workaround alert*/};
    for (<p,q> <- (fatherChildX - fatherChildY) + (fatherChildY - fatherChildX)) {
      result += error("A semantic action filtering <alt2rascal(q)> as a direct child of <alt2rascal(p)> would solve the ambiguity.", x@\loc?|dunno:///|);
    } 
  }
  
  return result; 
}

public list[int] yield(Tree x) {
  return [i | /Tree x:char(int i) := x];
}

public list[Message] reorderingCauses(Tree x, Tree y) {
  fatherChildX = {<p, q> | appl(p, [_*,appl(q,_),_*]) := x, true /* workaround alert*/};
  fatherChildY = {<p, q> | appl(p, [_*,appl(q,_),_*]) := y, true /* workaround alert*/};
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
  
  if (/appl(p,[appl(q,_),_*]) := y, /Tree t:appl(q,[_*,appl(p,_)]) := x, p != q) {
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
  if (prod(_, lhs, _) := x.prod, prod(_, [prefix*, _, l:lit(_), more*], _) := y.prod, lhs == prefix) {
    return [error("You might add a follow restriction for <symbol2rascal(l)> on:
                    ' <alt2rascal(x.prod)>", x@\loc?|dunno:///|)]; 
  }
  
  if (prod(_, lhs, _) := y.prod, prod(_, [prefix*, _, l:lit(_), more*], _) := x.prod, lhs == prefix) {
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
  if (/appl(p,[appl(p,_),_*]) := x, /Tree t:appl(p,[_*,appl(p,_)]) := y) {
    return [error("This rule [<alt2rascal(p)>] may be missing an associativity declaration  (left, right, non-assoc)", t@\loc)];
  }
  
  if (/appl(p,[appl(p,_),_*]) := y, /Tree t:appl(p,[_*,appl(p,_)]) := x) {
    return [error("This rule [<alt2rascal(p)>] may be missing an associativity declaration (left, right, non-assoc)", t@\loc)];
  }
  
  return [];  
}
