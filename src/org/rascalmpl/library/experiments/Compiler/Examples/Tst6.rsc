module experiments::Compiler::Examples::Tst6

data F = f() | f(int n) | g(int n) | deep(F f);
anno int F @ pos;
  
// testAnnotations
 

value main(list[value] args) { X = f(); X @ pos ?= 3; return X @ pos == 3; }