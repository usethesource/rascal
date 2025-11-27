module lang::rascal::tests::extends1::ABSTRACTTYPE

extend lang::rascal::tests::extends1::PARSETREE;

data SYM = E();

// A <: B
// A <: C
// A <: D
// sort(_) <: TREE
// C <: E
// Z <: A

bool subtype(C(), E()) = true;
bool subtype(Z(), A()) = true;

test bool ATcomparable1() = comparable(A(), B());
test bool ATcomparable2() = comparable(B(), A());

test bool ATcomparable3() = comparable(A(), C());
test bool ATcomparable4() = comparable(C(), A());

test bool ATcomparable5() = comparable(A(), D());
test bool ATcomparable6() = comparable(D(), A());

test bool ATcomparable7() = !comparable(C(), D());
test bool ATcomparable8() = !comparable(D(), C());

test bool ATsubtype1() = subtype(A(), B());
test bool ATsubtype2() = subtype(A(), C());
test bool ATsubtype3() = subtype(A(), D());
test bool ATsubtype4() = subtype(SORT(A()), TREE());
test bool ATsubtype5() = subtype(SORT(B()), TREE());
test bool ATsubtype6() = subtype(SORT(C()), TREE());
test bool ATsubtype7() = subtype(SORT(D()), TREE());
test bool ATubtype8() = subtype(C(), E());
test bool ATubtype9() = subtype(Z(), A());
