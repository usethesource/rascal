module lang::rascalcore::compile::Examples::Tst4

//syntax A = conditional: A;
//
//data B = conditional(B symbol);
//
//data C = conditional(C,C);
//
//B removeConditionals(B sym) = visit(sym) {
//  case conditional(s) => s
//};
//
//data D = d(int x, int y) | d(int x, int y, int z);
//
//test bool matchSetADTs4() = {d(a, b), *c} := {d(1, 2), d(3, 4)};

data D = d(int n);
test bool setTypeNamedElem2() = {D _:d(1)} := {d(1)};