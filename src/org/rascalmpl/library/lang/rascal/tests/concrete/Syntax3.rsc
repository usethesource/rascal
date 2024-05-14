module lang::rascal::tests::concrete::Syntax3

import ParseTree;

syntax Aas
  = nil: [a]*
  | a:   [a][a]*
  | aas: [a][a][a]*
  ;
  
Aas ambFilter(amb(set[Aas] alternatives)) {
  set[Aas] result = {a | Aas a <- alternatives, !(a is nil)};
  
  if ({oneTree} := result) {
    return oneTree;
  }
  
  return ParseTree::amb(result);
} 

test bool resolveableAmbIsGone() = amb(_) !:= parse(#Aas, "a", allowAmbiguity=true, filters={ambFilter});

test bool twoAmbsLeft() = amb({_,_}) := parse(#Aas, "aa", allowAmbiguity=true, filters={ambFilter});
