module lang::rascal::tests::imports::ImportTests7

import lang::rascal::tests::imports::M7;

test bool Test71() = natural() == natural();

test bool Test72() = string() == string();

test bool Test73() = natural() != string();

