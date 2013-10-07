module experiments::Compiler::Examples::TestSuite

/*
test bool tst1() = true == true;

test bool tst2(int n) { return n + n - n == n; }

test bool tst3(list[&T] lst, set[&T] st) = true;
*/

//@expected{NoSuchKey}
//test bool tst4(){ ("a" : 1)["b"] == 1; }

test bool tst5(){ ("a" : 1)["b"] == 1; }


value main(list[value] args){
  return 42;
}