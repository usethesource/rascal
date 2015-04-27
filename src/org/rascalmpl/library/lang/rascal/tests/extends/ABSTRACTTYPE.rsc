module lang::rascal::tests::extends::ABSTRACTTYPE

import lang::rascal::tests::extends::PARSETREE;

value main(list[value] args) = comparable(A(), D());

test bool comparableA1() = comparable(A(), B());
test bool comparableA2() = comparable(B(), A());

test bool comparableA3() = comparable(A(), C());
test bool comparableA4() = comparable(C(), A());

test bool comparableA5() = comparable(A(), D());
test bool comparableA6() = comparable(D(), A());

test bool comparableA7() = !comparable(C(), D());
test bool comparableA8() = !comparable(D(), C());