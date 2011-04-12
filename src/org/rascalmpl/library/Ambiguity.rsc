@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module Ambiguity

import ParseTree;
import IO;
import ValueIO;
import Message;
import List;
import Set;
import Relation;
import Graph;
import lang::rascal::syntax::Grammar2Rascal;

public list[Message] diagnose(Tree t) {
  return [findCauses(a) | /Tree a:amb(_) := t];
}

public void diagnose(str amb) {
  diagnose(readTextValueString(#Tree, amb));
}

public list[Message] findCauses(Tree a) {
  return [info("Ambiguity cluster with <size(a.alternatives)> alternatives", a@\loc)]
       + [findCauses(x, y) | [_*,Tree x,_*,Tree y, _*] := toList(a.alternatives)];
}

public list[Message] findCauses(Tree x, Tree y) {
  pX = { p | /Production p := x };
  pY = { p | /Production p := y };
  result = [];
  
  if (pX == pY) {
    result += reorderingCauses(x, y); 
  }
  else {
    result += reorderingCauses(x, y);
    result += info("Productions unique to the one alternative are: <for (p <- pX - pY) {><symbol2rascal(p.rhs)> = <prod2rascal(p)>; <}>", x@\loc);
    result += info("Productions unique to the other alternative are: <for (p <- pY - pX) {><symbol2rascal(p.rhs)> = <prod2rascal(p)>; <}>", y@\loc);
  }
  
  return result;
}

public list[Message] reorderingCauses(Tree x, Tree y) {
  fatherChildX = {<p, q> | appl(p, [_*,appl(q,_),_*]) := x, bprint(""/* workaround alert*/)};
  fatherChildY = {<p, q> | appl(p, [_*,appl(q,_),_*]) := y, bprint(""/* workaround alert*/)};
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
  if (/appl(p,[appl(q,_),_*]) := x, /Tree t:appl(q,[_*,appl(p,_)]) := y) {
      return [error("A priority rule is missing [syntax <symbol2rascal(p.rhs)> = <prod2rascal(p)> \> <prod2rascal(q)>] (or vice versa)", t@\loc)];
  }
  
  if (/appl(p,[appl(q,_),_*]) := y, /Tree t:appl(q,[_*,appl(p,_)]) := x) {
      return [error("A priority rule is missing [syntax <symbol2rascal(p.rhs)> = <prod2rascal(p)> \> <prod2rascal(q)>] (or vice versa)", t@\loc)];
  }
  
  return [];
}

list[Message] danglingCauses(Tree x, Tree y) {
  if (appl(p,/appl(q,_)) := x, appl(q,/appl(p,_)) := y) {
    return [info("dangling issue between <prod2rascal(p)> and <prod2rascal(q)>", x@\loc)]
         + danglingOffsideSolutions(x, y)
         + danglingFollowSolutions(x, y);
  }

  return [];
}

list[Message] danglingFollowSolutions(Tree x, Tree y) {
  if (prod(lhs, _, _) := x.prod, prod([prefix*, _, l:lit(_), more*], _, _) := y.prod, lhs == prefix) {
    return [warning("You might add a follow restriction to <prod2rascal(x.prod)> for <symbol2rascal(l)>", x@\loc)]; 
  }
  
  if (prod(lhs, _, _) := y.prod, prod([prefix*, _, l:lit(_), more*], _, _) := x.prod, lhs == prefix) {
    return [warning("You might add a follow restriction to <prod2rascal(x.prod)> for <symbol2rascal(l)>", x@\loc)]; 
  }
  
  return []; 
}

list[Message] danglingOffsideSolutions(Tree x, Tree y) {
  if (appl(p,/Tree u:appl(q,_)) := x, appl(q,/appl(p,_)) := y, (u@\loc).begin.column > (x@\loc).begin.column) {
    return [warning("You might declare nested <prod2rascal(q)> offside (to the left) of some child of <prod2rascal(p)> using a failing syntax action that compares annotations @loc.start.column", u@\loc)];
  }
  if (appl(p,/Tree u:appl(q,_)) := y, appl(q,/appl(p,_)) := x, (u@\loc).begin.column > (y@\loc).begin.column) {
    return [warning("You might declare nested <prod2rascal(q)> offside (to the left) of some child of <prod2rascal(p)> using a failing syntax action that compares annotations @loc.start.column", u@\loc)];
  }
  
  return [];
}

list[Message] associativityCauses(Tree x, Tree y) {
  if (/appl(p,[appl(p,_),_*]) := x, /Tree t:appl(p,[_*,appl(p,_)]) := y) {
    return [error("This rule [syntax <symbol2rascal(p.rhs)> = <prod2rascal(p)>] is missing an associativity declaration", t@\loc)];
  }
  
  if (/appl(p,[appl(p,_),_*]) := y, /Tree t:appl(p,[_*,appl(p,_)]) := x) {
    return [error("This rule [syntax <symbol2rascal(p.rhs)> = <prod2rascal(p)>] is missing an associativity declaration (left, right, non-assoc)", t@\loc)];
  }
  
  return [];  
}