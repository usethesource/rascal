module lang::rascal::tests::extends::CHECKTYPES

import lang::rascal::tests::extends::ABSTRACTTYPE;

// A <: B
// A <: C
// A <: D
// sort(_) <: TREE
// C <: E
// Z <: A

test bool CTcomparable1() = comparable(A(), B());
test bool CTcomparable2() = comparable(B(), A());

test bool CTcomparable3() = comparable(A(), C());
test bool CTcomparable4() = comparable(C(), A());

test bool CTcomparable5() = comparable(A(), D());
test bool CTcomparable6() = comparable(D(), A());

test bool CTcomparable7() = !comparable(C(), D());
test bool CTcomparable8() = !comparable(D(), C());

test bool CTsubtype1() = subtype(A(), B());
test bool CTsubtype2() = subtype(A(), C());
test bool CTsubtype3() = subtype(A(), D());
test bool CTsubtype4() = subtype(SORT(A()), TREE());
test bool CTsubtype5() = subtype(SORT(B()), TREE());
test bool CTsubtype6() = subtype(SORT(C()), TREE());
test bool CTsubtype7() = subtype(SORT(D()), TREE());
test bool CTsubtype8() = subtype(C(), E());
test bool CTsubtype9() = subtype(Z(), A());