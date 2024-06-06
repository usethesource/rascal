module lang::rascal::tests::imports::Imports4

import lang::rascal::tests::imports::M4;

test bool Test41() = lang::rascal::tests::imports::M4::m == 2;

test bool Test42() = lang::rascal::tests::imports::M4::f() == 2;

test bool Test43() = lang::rascal::tests::imports::M4::g() == 2;
