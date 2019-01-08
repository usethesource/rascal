module lang::rascal::tests::functionality::ConcreteFieldTests

syntax C = "{" !<< B* bs;
syntax B = "b";

test bool stripLabels() {
  C ex2 = [C] "bbb";
  return B _ <- ex2.bs;
}
