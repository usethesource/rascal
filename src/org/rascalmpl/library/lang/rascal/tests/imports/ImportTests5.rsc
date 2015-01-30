module lang::rascal::tests::imports::ImportTests5

import lang::rascal::tests::imports::M5;

import lang::rascal::tests::imports::Mbase;

test bool Test51() = lang::rascal::tests::imports::Mbase::n == 2;

test bool Test52() = lang::rascal::tests::imports::Mbase::f(3) == 6;

test bool Test53() = lang::rascal::tests::imports::M5::m == 3;

test bool Test54() = lang::rascal::tests::imports::M5::g(3) == 9;

test bool Test55() = lang::rascal::tests::imports::M5::h(3) == 6;
