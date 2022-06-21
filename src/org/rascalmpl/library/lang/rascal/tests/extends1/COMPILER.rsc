module lang::rascal::tests::extends::COMPILER

import lang::rascal::tests::extends::ABSTRACTTYPE;

// A <: B
// A <: C
// A <: D
// sort(_) <: TREE
// C <: E
// Z <: A

test bool CMcomparable1() = comparable(A(), B());
test bool CMcomparable2() = comparable(B(), A());

test bool CMcomparable3() = comparable(A(), C());
test bool CMcomparable4() = comparable(C(), A());

test bool CMcomparable5() = comparable(A(), D());
test bool CMcomparable6() = comparable(D(), A());

test bool CMcomparable7() = !comparable(C(), D());
test bool CMcomparable8() = !comparable(D(), C());

test bool CMcomparable9() = comparable(C(), E());
test bool CMcomparable10() = comparable(Z(), A());

test bool CMsubtype1() = subtype(A(), B());
test bool CMsubtype2() = subtype(A(), C());
test bool CMsubtype3() = subtype(A(), D());
test bool CMsubtype4() = subtype(SORT(A()), TREE());
test bool CMsubtype5() = subtype(SORT(B()), TREE());
test bool CMsubtype6() = subtype(SORT(C()), TREE());
test bool CMsubtype7() = subtype(SORT(D()), TREE());
test bool CMsubtype8() = subtype(C(), E());
test bool CMsubtype9() = subtype(Z(), A());

value main() = subtype(Z(), A());