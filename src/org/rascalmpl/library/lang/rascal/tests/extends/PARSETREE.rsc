module lang::rascal::tests::extends::PARSETREE

extend lang::rascal::tests::extends::TYPE;

data SYM = C() | D();

bool subtype(A(), C()) = true;
bool subtype(A(), D()) = true;

test bool comparableP1() = comparable(A(), B());
test bool comparableP2() = comparable(B(), A());

test bool comparableP3() = comparable(A(), C());
test bool comparableP4() = comparable(C(), A());

test bool comparableP5() = comparable(A(), D());
test bool comparableP6() = comparable(D(), A());

test bool comparableP7() = !comparable(C(), D());
test bool comparableP8() = !comparable(D(), C());