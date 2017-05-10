module experiments::Compiler::Examples::Base1

data AType = a1() | a2();

default bool isSubtype(AType atype1, AType atype2) = atype1 == atype2;

bool bug1() = isSubtype(a1(), a2());