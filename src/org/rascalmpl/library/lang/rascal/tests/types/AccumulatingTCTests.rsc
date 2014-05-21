module lang::rascal::tests::types::AccumulatingTCTests

import lang::rascal::tests::types::StaticTestingUtils;

public test bool testAppendHasLexicalScopingFunction() = unexpectedType("for (x \<- [1,2,3]) f();", initialDecls=["bool f() { append 3; }"]); //TODO
