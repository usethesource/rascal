module lang::rascalcore::compile::Examples::Tst3
data Tree1
  = nd(value v)
  | lf()
  ;
bool foo(Tree1 l, Tree1 r) {
  if (nd(v1) := l && nd(v2) := r) {
    return v1 := v2;
  }
  return false;
}
