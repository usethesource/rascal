module experiments::Compiler::Examples::Tst1
  
data D(int a = 10, int b = 2 * a) = d1(int x, int y = x + a + b) | d2();

value main() = d1(20).y;