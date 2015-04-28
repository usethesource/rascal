module lang::rascal::tests::imports::CyclicImportTests1

import lang::rascal::tests::imports::C1;
import lang::rascal::tests::imports::C2;

test bool Cycle1() = isDint(d(13)) == true;
test bool Cycle2() = isDint(d("abc")) == false;

test bool Cycle1() = isDstr(d("abc")) == true;
test bool Cycle2() = isDstr(d(13)) == false;


