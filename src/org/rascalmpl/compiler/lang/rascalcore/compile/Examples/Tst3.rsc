module lang::rascalcore::compile::Examples::Tst3

data Tree1
  = node1(Tree1 l) | leaf();
       
                        
value foo(Tree1 l) {
  if (node1(l1) := l){
    return l1 := l1;
  }
  return false;
}