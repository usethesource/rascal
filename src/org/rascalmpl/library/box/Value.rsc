module box::Value

import box::Box;
import box::Box2Text;
import Node;
import List;
import Set;

@doc{
  a function that formats any value to a easily readable string
  mainly intended for debugging purposes
}
// TODO: this function is not finished!
public Box value2box(value x) {
  switch (x) {
    case node x : {
      p = prefix(getChildren(x));
      l = last(getChildren(x));
      return HV([
               H([L(getName(x)), L("(")])[@hs=0], 
               I([HV([H([value2box(e), L(",")])[@hs=0] | e <- p] + [value2box(l)])[@hs=1]]), 
               L(")")
               ])[@hs=0];
    }
    case list[value] x: {
      p = prefix(x);
      l = last(x);
      return H([L("["), I([HOV([H([value2box(e), L(",")])[@hs=0] | e <- p] + [value2box(l)])[@hs=1]]),L("]")])[@hs=0];
    }
    case set[value] x: {
      <l,p> = takeOneFrom(x);
      return H([L("{"), I([HOV([H([value2box(e), L(",")])[@hs=0] | e <- p] + [value2box(l)])[@hs=1]]),L("}")])[@hs=0];
    }
    default: return L("<x>"); 
  }
}


// [ 0: ,1:H[] ,2:V[] ,default: ]

