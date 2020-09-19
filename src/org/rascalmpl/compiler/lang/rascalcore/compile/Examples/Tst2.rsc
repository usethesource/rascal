module lang::rascalcore::compile::Examples::Tst2

import Exception;

@expected{ArithmeticException}
test bool xxx() { 3 / 0; return true; }