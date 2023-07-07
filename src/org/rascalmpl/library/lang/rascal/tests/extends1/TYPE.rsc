module lang::rascal::tests::extends1::TYPE

// A <: B

data SYM = A() | B() | Z();

public bool comparable(SYM s, SYM t) = subtype(s,t) || subtype(t,s);

bool subtype(A(), B()) = true;

default bool subtype(SYM s, SYM t) = false;

test bool TPcomparable1() = comparable(A(), B());
test bool TPcomparable2() = comparable(B(), A());

@ignoreInterpreter{
This failing test seems to be an error in the interpreter
}
test bool TPsubtype1() = !subtype(Z(), A());

