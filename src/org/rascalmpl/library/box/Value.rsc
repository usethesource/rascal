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
      if (getChildren(x) == []) return H([L(getName(x)),L("("),L(")")])[@hs=0];
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
      if (x == []) return H([L("["),L("]")])[@hs=0];
      p = prefix(x);
      l = last(x);
      return H([L("["), I([HOV([H([value2box(e), L(",")])[@hs=0] | e <- p] + [value2box(l)])[@hs=1]])[@is=1],L("]")])[@hs=0];
    }
    case { } : return H([L("{"),L("}")])[@hs=0];
    case set[value] x: {
       if (x == {}) return H([L("{"),L("}")])[@hs=0];
      <l,p> = takeOneFrom(x);
      return HOV([L("{"), I([HOV([H([value2box(e), L(",")])[@hs=0] | e <- p] + [value2box(l)])[@hs=1]])[@is=1],L("}")])[@hs=0];
    }
    case str x : return L("\"<x>\"");
    default: return L("<y>"); 
  }
}



