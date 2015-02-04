module experiments::Compiler::Examples::Tst2

data F = f() | f(int n) | g(int n) | deep(F f);
anno int F @ pos;
  	

value main(list[value] args) { return ([1,2,3][10])?; }