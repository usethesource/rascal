module lang::rascal::tests::extends1::B3

import lang::rascal::tests::extends1::B1;
import lang::rascal::tests::extends1::B2;

int add3(int n) = n + 3;

// Defined in B[1-3]

test bool b3tadd1() = add1(5) == 6;
test bool b3tadd2() = add2(5) == 7;
test bool b3tadd3() = add3(5) == 8;
