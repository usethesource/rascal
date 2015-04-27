module lang::rascal::tests::extends::TYPE

data SYM = A() | B();

public bool comparable(SYM s, SYM t) = subtype(s,t) || subtype(t,s);

bool subtype(A(), B()) = true;

default bool subtype(SYM s, SYM t) = false;

test bool comparableT1() = comparable(A(), B());
test bool comparableT2() = comparable(B(), A());