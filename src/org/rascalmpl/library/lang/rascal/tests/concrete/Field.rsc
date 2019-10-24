module lang::rascal::tests::concrete::Field

syntax C = "{" !<< B* bs;
syntax B = "b";

test bool stripLabels() {
  C ex2 = [C] "bbb";
  return B _ <- ex2.bs;
}
