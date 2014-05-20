module lang::rascal::tests::Functions

data B = and(B lhs, B rhs) | or(B lhs, B rhs) | t() | f();

B and(B b1, and(B b2, B b3)) = and(and(b1,b2),b3);

public test bool normalizedCall(B b1, B b2, B b3) = and(b1, and(b2, b3)) == and(and(b1, b2),b3);

public test bool normalizedVisit() =
  /and(_, and(_, _)) !:= visit (or(or(t(),t()),or(t(),t()))) { case or(a,b) => and(a,b) };
  
private test bool callKwp() {
  kwp(x = 2); // this would previously change the static type of the x argument of kwp to int
  return true;
}

private void kwp(value x = 1) {
  // this is a regression test for a bug;
  x = "node"(); // if the static type of the kwp is value, then we should be allowed to assign a node into it.
}
