module lang::rascalcore::compile::Examples::Tst1

int same1(int \a) = a;

test bool escapedPositionalFormal1() = same1(2) == 2;