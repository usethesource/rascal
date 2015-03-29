module experiments::Compiler::Examples::Tst1

data F = f() | f(int n) | g(int n) | deep(F f);
anno int F @ pos;
  
// testAnnotations

map[str, int] M = ("a" : 1);

int x = 10;
int y = 11;

value main(list[value] args)  { M["b"] ? (x + y * x + y) += 1; }