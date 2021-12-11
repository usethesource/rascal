@bootstrapParser
module lang::rascal::tests::concrete::Syntax4

import lang::rascal::\syntax::Rascal;
import List;

test bool lexicalMatch1() = (Name) `a` := [Name] "a";
test bool lexicalMatch2() = (Name) `ab` := [Name] "ab";

test bool lexicalMatch3() = (QualifiedName) `a` := [QualifiedName] "a";
test bool lexicalMatch4() = (QualifiedName) `ab` := [QualifiedName] "ab";

test bool lexicalMatch5() = (Expression) `a` := [Expression] "a";
test bool lexicalMatch6() = (Expression) `ab` := [Expression] "ab";

int cntStats(Statement* stats) = size([s | s <- stats ]);

test bool cntStats1() = cntStats(((Expression) `{x=1;}`).statements) == 1;
test bool cntStats2() = cntStats(((Expression) `{x=1;x=2;}`).statements) == 2;

test bool matchPat1() = (Pattern) `<Type _> <Name _>` := [Pattern] "int x";

