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

// TODO (compiler): these tests can only be made to work when the compiled parser generator is integrated
// and compiled Rascal functions can be called during parse tree construction
test bool resolveableAmbIsGone() = amb(_) !:= parse(#Aas, "a", allowAmbiguity=true);

// this test would throw an exception because the amb constructor would not _statically_ return
// a tree of type Aas:
test bool twoAmbsLeft() = amb({_,_}) := parse(#Aas, "aa", allowAmbiguity=true);
