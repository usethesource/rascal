module lang::rascal::tests::imports::CyclicImports1

import lang::rascal::tests::imports::C1;
import lang::rascal::tests::imports::C2;

test bool Cycle1() = isDint(d(13)) == true;
test bool Cycle2() = isDint(d("abc")) == false;

test bool Cycle3() = isDstr(d("abc")) == true;
test bool Cycle4() = isDstr(d(13)) == false;

test bool Cycle5() = C2func(1) == 1;
test bool Cycle6() = C1func(2) == 2;

test bool Cycle7() = C1testFunction() == 2;
test bool Cycle8() = C2testFunction() == 1;

