module lang::rascal::tests::extends::B3

import lang::rascal::tests::extends::B1;
import lang::rascal::tests::extends::B2;

int add3(int n) = n + 3;

// Defined in B[1-3]

test bool tadd1() = add1(5) == 6;
test bool tadd2() = add2(5) == 7;
test bool tadd3() = add3(5) == 8;