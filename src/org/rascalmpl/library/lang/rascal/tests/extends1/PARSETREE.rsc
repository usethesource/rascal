module lang::rascal::tests::extends1::PARSETREE

extend lang::rascal::tests::extends1::TYPE;

data SYM = C() | D() | SORT(SYM sym) | TREE();

// A <: B
// A <: C
// A <: D
// sort(_) <: TREE

bool subtype(A(), C()) = true;
bool subtype(A(), D()) = true;

bool subtype(SORT(_), TREE()) = true;

test bool PTcomparable1() = comparable(A(), B());
test bool PTcomparable2() = comparable(B(), A());

test bool PTcomparable3() = comparable(A(), C());
test bool PTcomparableP4() = comparable(C(), A());

test bool PTcomparable5() = comparable(A(), D());
test bool PTcomparable6() = comparable(D(), A());

test bool PTcomparable7() = !comparable(C(), D());
test bool PTcomparable8() = !comparable(D(), C());

test bool PTsubtype1() = subtype(A(), B());
test bool PTsubtype2() = subtype(A(), C());
test bool PTsubtype3() = subtype(A(), D());
test bool PTsubtype4() = subtype(SORT(A()), TREE());
test bool PTsubtype5() = subtype(SORT(B()), TREE());
test bool PTsubtype6() = subtype(SORT(C()), TREE());
test bool PTsubtype7() = subtype(SORT(D()), TREE());
 
@ignore{This failing test seems to be an error in the interpreter; worked before in compiler but is now also broken}
test bool PTsubtype8() = !subtype(Z(), A());

