module lang::rascal::tests::functionality::ConcreteSyntaxTests3

import ParseTree;

syntax Aas
  = nil: [a]*
  | a:   [a][a]*
  | aas: [a][a][a]*
  ;
  
&T <:Tree amb(set[&T <:Tree] alternatives) {
  result = {a | Aas a <- alternatives, !(a is nil)};
  if ({oneTree} := result) {
    return oneTree;
  }
  return ParseTree::amb(result);
} 

test bool resolveableAmbIsGone() = amb(_) !:= parse(#Aas, "a");

// this test would throw an exception because the amb constructor would not _statically_ return
// a tree of type Aas:
test bool twoAmbsLeft() = amb({_,_}) := parse(#Aas, "aa");
