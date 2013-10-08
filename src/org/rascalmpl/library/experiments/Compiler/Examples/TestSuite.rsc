module experiments::Compiler::Examples::TestSuite

import Exception;

/*
test bool tst1() = true == true;

test bool tst2(int n) { return n + n - n == n; }

test bool tst3(list[&T] lst, set[&T] st) = true;
*/

@expected{NoSuchKey}
test bool tst4() = ("a" : 1)["b"] == 1;
test bool tst5() = ("a" : 1)["b"] == 1;

@expected{IndexOutOfBounds}
test bool tst6() = { list[int] l = []; return l[1] == 1; };
test bool tst7() = { list[int] l = []; return l[1] == 1; };

test bool tst8() = { list[int] l = []; try { return l[1] == 1; } catch IndexOutOfBounds(_): { return false; } };

value main(list[value] args){
  return 42;
}