module experiments::Compiler::Examples::Tst6


test bool T1() =  1 == 1;

// should fail
test bool T2() =  1 == 2;

@expected{ArithmeticException}
test bool T3() = 2/0 == 1;

// should fail
@expected{ArithmeticException}
test bool T4() = 2/1 == 1;

test bool T5(int n, list[int] L) = true;

value main() = true;