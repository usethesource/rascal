module box::Value

import box::Box;
import box::Box2Text;
import Node;

@doc{
  a function that formats any value to a easily readable string
  mainly intended for debugging purposes
}
// TODO: this function is not finished!
public str formatValue(value x) {
  switch (x) {
    case node x: return V([H([L(getName(x)), L("(")]), HV([formatValue(e) | e <- getChildren(x)]), L(")")]);
    case list[value] x: return H([L("["), HOV([formatValue(e) | e <- x])]); 
  }
}