module experiments::Compiler::Examples::Tst5

data F = z(int l = 2) | u();



value main() {
  e = z();
  e.l ?= 3; // set l to 3 if the field is not set, otherwise leave it
  return e.l == 3;
}