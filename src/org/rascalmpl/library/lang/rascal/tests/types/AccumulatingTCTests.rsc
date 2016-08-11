@bootstrapParser
module lang::rascal::tests::types::AccumulatingTCTests

import lang::rascal::tests::types::StaticTestingUtils;

test bool testappendWithoutFor() = illegalUse("append 3;");

test bool testAppendHasLexicalScopingFunction() = illegalUse("for (x \<- [1,2,3]) f();", initialDecls=["bool f() { append 3; }"]); //TODO

test bool testAppendHasLexicalScopingClosure() =
    illegalUse("{ f = () { append 3; }; for (x \<- [1,2,3]) { f(); } } == [3,3,3];");
