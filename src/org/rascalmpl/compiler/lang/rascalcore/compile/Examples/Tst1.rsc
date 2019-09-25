module lang::rascalcore::compile::Examples::Tst1         

data Tree1
  = \node1(value val, Tree1 l, Tree1 r, loc origin)
  |leaf()
  ;
bool foo(Tree1 l, Tree1 r) {
  if (\node1(v1, l1, r1, _) := l && \node1(v2, l2, r2, _) := r) {
    return v1 := v2;
  }
  return false;
}
bool bar(Tree1 l, Tree1 r) {
  if (\node1(v1, l1, r1, _) := l && \node1(value v2, Tree1 l2, Tree1 r2, _) := r) {
    return v1 := v2;
  }
  return false;
}
data Tree2
  = \node2(value val, Tree2 l, Tree2 r, loc origin)
  | leaf2()
  ;
bool foo(Tree2 l, Tree2 r) {
  if (\node2(v1, l1, r1, _) := l && \node2(value v2, Tree2 l2, Tree2 r2, _) := r) {
    return v1 := v2;
  }
  return false;
}
data Tree3
  = \node3(value val, Tree3 l, Tree3 r, loc origin)
  | leaf3(loc origin)
  ;
bool foo(Tree3 l, Tree3 r) {
  if (\node3(v1, l1, r1, _) := l && \node3(value v2, Tree3 l2, Tree3 r2, _) := r) {
    return v1 := v2;
  }
  return false;
}