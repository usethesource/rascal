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

// Match patterns

test bool matchPat1() = (Pattern) `<Type _> <Name _>` := [Pattern] "int x";
test bool matchSetPat1() = (Pattern) `{10,11}` := [Pattern] "{10,11}";
test bool matchSetPat2() = (Pattern) `{<Pattern _p>,<Pattern _q>}` := [Pattern] "{10,11}";

test bool matchListPat1() = (Pattern) `[10,11]` := [Pattern] "[10,11]";
test bool matchListPat2() = (Pattern) `[<Pattern _p>,<Pattern _q>]` := [Pattern] "[10,11]";

test bool matchQNamePat1() = (Pattern) `x` := [Pattern] "x";
test bool matchQNamePat2() = (Pattern) `<QualifiedName _n>` := [Pattern] "x";

test bool matchMultiPat1() = (Pattern) `x*` := [Pattern] "x*";
test bool matchMultiPat2() = (Pattern) `<QualifiedName _n>*` := [Pattern] "x*";

test bool matchSplice1Pat() = (Pattern) `*x` := [Pattern] "*x";
test bool matchSplice2Pat() = (Pattern) `+x` := [Pattern] "+x";
test bool matchNegativPat() = (Pattern) `-x` := [Pattern] "-x";

test bool matchLitPat1() = (Pattern) `10` := [Pattern] "10";
test bool matchLitPat2() = (Pattern) `<Literal _l>` := [Pattern] "10";

test bool matchTuplePat1() = (Pattern) `\<10,11\>` := [Pattern] "\<10,11\>";
test bool matchTuplePat2() = (Pattern) `\<<Pattern _p>,<Pattern _q>\>` := [Pattern] "\<10,11\>";

test bool matchTypedVarPat1() = (Pattern) `int x` := [Pattern] "int x";
test bool matchTypedVarPat2() = (Pattern) `<Type _t> <Name _n>` := [Pattern] "int x";
test bool matchTypedVarPat3() = (Pattern) `<Type _t> x` := [Pattern] "int x";
test bool matchTypedVarPat4() = (Pattern) `int <Name _n>` := [Pattern] "int x";

@ignore{Not yet implemented}
test bool matchMapPat() = (Pattern) `(1:10,2:11)` := [Pattern] "(1:10,2:11)";

test bool matchCallPat() = (Pattern) `f(10,11)` := [Pattern] "f(10,11)";

test bool matchVarBecomesPat1() = (Pattern) `x:10` := [Pattern] "x:10";
test bool matchVarBecomesPat2() = (Pattern) `<Name _n>:10` := [Pattern] "x:10";

test bool matchAsTypePat() = (Pattern) `[int]10` := [Pattern] "[int]10";
test bool matchDescendentPat() = (Pattern) `/10` := [Pattern] "/10";
test bool matchAntiPat() = (Pattern) `!10` := [Pattern] "!10";

test bool matchTypedVarBecomesPat1() = (Pattern) `int x:3` := [Pattern] "int x:3";
test bool matchTypedVarBecomesPat2() = (Pattern) `<Type _t> x:3` := [Pattern] "int x:3";
test bool matchTypedVarBecomesPat3() = (Pattern) `int <Name _n>:3` := [Pattern] "int x:3";
test bool matchTypedVarBecomesPat4() = (Pattern) `int x:<Pattern _p>` := [Pattern] "int x:3";
test bool matchTypedVarBecomesPat5() = (Pattern) `<Type _t> <Name _n>:<Pattern _p>` := [Pattern] "int x:3";

