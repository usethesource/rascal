module lang::rascal::tests::Functions

data B = and(B lhs, B rhs) | or(B lhs, B rhs) | t() | f();

B and(B b1, and(B b2, B b3)) = and(and(b1,b2),b3);

public test bool normalizedCall(B b1, B b2, B b3) = and(b1, and(b2, b3)) == and(and(b1, b2),b3);

public test bool normalizedVisit() =
  /and(_, and(_, _)) !:= visit (or(or(t(),t()),or(t(),t()))) { case or(a,b) => and(a,b) };