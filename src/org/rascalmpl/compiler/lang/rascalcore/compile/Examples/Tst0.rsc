module lang::rascalcore::compile::Examples::Tst0

import ParseTree;

start syntax A = "a";
layout WS = [\ \t\n\r]*;

test bool saveAndRestoreParser() {
  storeParsers(#start[A], |memory://test-tmp/parsers.jar|);
  p = loadParsers(|memory://test-tmp/parsers.jar|);

  x = p(type(\start(sort("A")), ()), "a", |origin:///|);
  y = parse(#start[A], "a", |origin:///|);
 
  return x == y;  // (I)
}

Tree f(Symbol sym,  map[Symbol, Production] rules, str input)
    = ParseTree::parse(type(sym, rules), input, |todo:///|); // (II)
    
/* A catch22:
   How to assign a type to a reified type of the form type(...)?
   
   My current solution: compute the most specific type for the first argument of type(...).
   This assigns type Symbol to x
   This accepts (II) but fails for (I) with the message "Comparison not defined on `Symbol` and `start[A]`"
   
   An alternative solution (suggested by comments Jurgen added in CollectExpression): always return value()
   This accepts (I) but fails for (II) with the message:
   "Cannot instantiate formal parameter type `type[&T \<: Tree]`: Type parameter `T` should be less than `Tree`, but is bound to `value`"
   
   The question: how to assign a type to type(...) that accepts both these legal cases.
*/
