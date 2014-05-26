module lang::rascal::tests::imports::ImportTests4

import lang::rascal::tests::imports::M4;

test bool Test1() = lang::rascal::tests::imports::M4::m == 2;

test bool Test2() = lang::rascal::tests::imports::M4::f() == 2;

test bool Test3() = lang::rascal::tests::imports::M4::g() == 2;