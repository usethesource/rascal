module lang::rascalcore::compile::Examples::A
 
data N = succ(N next) | zero();

N make(int i) {
   if (i == 0) { return zero(); }
   return succ(make(i - 1));
}