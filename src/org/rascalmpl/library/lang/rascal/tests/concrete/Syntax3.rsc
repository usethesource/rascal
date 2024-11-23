module lang::rascal::tests::concrete::Syntax3

import ParseTree;

syntax Aas
  = nil: [a]*
  | a:   [a][a]*
  | aas: [a][a][a]*
  ;
  
&T <: Tree ambFilter(amb(set[&T <: Tree] alternatives)) {
  set[&T <: Tree] result = {a | &T <: Tree a <- alternatives, !(a is nil)};
  
  if ({oneTree} := result) {
    return oneTree;
  }
  
  return ParseTree::amb(result);
} 

test bool resolveableAmbIsGone() = amb(_) !:= parse(#Aas, "a", allowAmbiguity=true, filters={ambFilter});

test bool twoAmbsLeft() = amb({_,_}) := parse(#Aas, "aa", allowAmbiguity=true, filters={ambFilter});
