module lang::rascal::tests::imports::ImportTests5

import lang::rascal::tests::imports::M5;

import lang::rascal::tests::imports::Mbase;

test bool Test1() = lang::rascal::tests::imports::Mbase::n == 2;

test bool Test2() = lang::rascal::tests::imports::Mbase::f(3) == 6;

test bool Test3() = lang::rascal::tests::imports::M5::m == 3;

test bool Test4() = lang::rascal::tests::imports::M5::g(3) == 9;

test bool Test5() = lang::rascal::tests::imports::M5::h(3) == 6;
