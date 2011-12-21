@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module box::Value

import box::Box;
import box::Box2Text;
import Node;
import List;
import Set;
import ToString;

@doc{
  a function that formats any value to a easily readable string
  mainly intended for debugging purposes
}
// TODO: this function is not finished!
public Box value2box(value y) {
  switch (y) {
    case str x() : return H([L(x),L("("),L(")")])[@hs=0];
    case node x : {
      p = prefix(getChildren(x));
      l = last(getChildren(x));
      return HOV([
               H([L(getName(x)), L("(")])[@hs=0], 
               I([HV([H([value2box(e), L(",")])[@hs=0] | e <- p] + [value2box(l)])[@hs=1]])[@is=1], 
               L(")")
               ])[@hs=0];
    }
    case [ ] : return H([L("["),L("]")])[@hs=0];
    case list[value] x: {
      p = prefix(x);
      l = last(x);
      return H([L("["), I([HOV([H([value2box(e), L(",")])[@hs=0] | e <- p] + [value2box(l)])[@hs=1]])[@is=1],L("]")])[@hs=0];
    }
    case { } : return H([L("{"),L("}")])[@hs=0];
    case set[value] x: {
      <l,p> = takeOneFrom(x);
      return HOV([L("{"), I([HOV([H([value2box(e), L(",")])[@hs=0] | e <- p] + [value2box(l)])[@hs=1]])[@is=1],L("}")])[@hs=0];
    }
    case str x : return L("\"<x>\"");
    default: return L("<y>"); 
  }
}



