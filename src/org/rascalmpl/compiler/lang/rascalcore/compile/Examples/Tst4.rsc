module lang::rascalcore::compile::Examples::Tst4

import Type;

data A = common();

data B = common();

//data A = common();

value main() = < typeOf(A::common()), typeOf(B::common()), typeOf(common())>;