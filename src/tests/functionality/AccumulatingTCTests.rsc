module tests::functionality::AccumulatingTCTests

import StaticTestingUtils;

public test bool testAppendHasLexicalScopingFunction() = unexpectedType("for (x \<- [1,2,3]) f();", initialDecls=["bool f() { append 3; }"]); //TODO
