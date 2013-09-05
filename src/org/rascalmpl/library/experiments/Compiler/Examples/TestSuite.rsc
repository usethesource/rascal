module experiments::Compiler::Examples::TestSuite

test bool tst1() = true == true;

test bool tst2(int n) = n + n - n == n;

test bool tst3(list[&T] lst, set[&T] st) = true;

value main(list[value] args){
  return 42;
}