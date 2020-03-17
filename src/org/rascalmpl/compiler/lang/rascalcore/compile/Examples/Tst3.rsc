module lang::rascalcore::compile::Examples::Tst3

data Tree1
  = \node1(value val)
  |leaf()
  ;
  
  
  
bool foo(Tree1 l, Tree1 r) {
  if (\node1(v1) := l && \node1(v2) := r) {
    return v1 := v2;
  }
  return false;
}