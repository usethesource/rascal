module lang::rascalcore::compile::Examples::Tst3


import ParseTree;

syntax Aas
  = nil: [a]*
  | a:   [a][a]*
  | aas: [a][a][a]*
  ;
  
&T <:Tree ambFilter(amb(set[&T <:Tree] alternatives)) {
  result = {a | Aas a <- alternatives, !(a is nil)};
  if ({oneTree} := result) {
    return oneTree;
  }
  return ParseTree::amb(result);
} 


test bool matchSetLists8() {
    if({[1, int n, 3]} := {[1, 2, 3]} && n == -2){
        return false;
    } else {
        return true;
    }
}

test bool matchListTuples8() {
    if([2] := [2] && 2 == -2){
        return false;
    } else {
        return true;
    }
}