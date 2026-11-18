module lang::rascal::tests::imports::Imports7

import lang::rascal::tests::imports::M7;

alias QualifiedTYPE = lang::rascal::tests::imports::M7::TYPE;

test bool Test71() = natural() == natural();

test bool Test72() = string() == string();

test bool Test73() = natural() != string();

test bool Test74() { QualifiedTYPE t = natural(); return t == natural(); }
