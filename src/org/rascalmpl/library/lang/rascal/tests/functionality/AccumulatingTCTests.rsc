module lang::rascal::tests::functionality::AccumulatingTCTests

import lang::rascal::tests::static::StaticTestingUtils;

public test bool testAppendHasLexicalScopingFunction() = unexpectedType("for (x \<- [1,2,3]) f();", initialDecls=["bool f() { append 3; }"]); //TODO
