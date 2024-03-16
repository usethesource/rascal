module lang::rascal::tests::concrete::Syntax3

import ParseTree;

syntax Aas
  = nil: [a]*
  | a:   [a][a]*
  | aas: [a][a][a]*
  ;
  
&T <:Tree ambFilter(amb(set[&T <:Tree] alternatives)) {
  set[&T <:Tree] result = {a | Aas a <- alternatives, !(a is nil)};
  if ({&T <: Tree oneTree} := result) {
    return oneTree;
  }
  return ParseTree::amb(result);
} 

@IgnoreCompiler{
TODO: these tests can only be made to work when the compiled parser generator is integrated
and compiled Rascal functions can be called during parse tree construction
}
test bool resolveableAmbIsGone() = amb(_) !:= parse(#Aas, "a", allowAmbiguity=true, filters={ambFilter});

// this test would throw an exception because the amb constructor would not _statically_ return
// a tree of type Aas:
@IgnoreCompiler{
TODO: Not implemented
}
test bool twoAmbsLeft() = amb({_,_}) := parse(#Aas, "aa", allowAmbiguity=true, filters={ambFilter});
