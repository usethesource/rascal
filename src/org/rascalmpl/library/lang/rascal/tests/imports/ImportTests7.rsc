module lang::rascal::tests::imports::ImportTests7

import lang::rascal::tests::imports::M7;

test bool Test1() = natural() == natural();

test bool Test2() = string() == string();

test bool Test3() = natural() != string();

