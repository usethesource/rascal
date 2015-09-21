@bootstrapParser
module lang::rascal::types::tests::UtilTests

import Relation;
import ParseTree;
import lang::rascal::types::Util;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::\syntax::Rascal;

test bool tstGetIntroducedNames1() = domain(getIntroducedNames((Assignable) `x`)) == {RSimpleName("x")};
test bool tstGetIntroducedNames2() = domain(getIntroducedNames((Assignable) `a::b::c::x`)) == {RCompoundName(["a","b","c","x"])};

test bool tstGetPatternNames1() =  domain(getPatternNames((Pattern) `x`)) == {RSimpleName("x")};
test bool tstGetPatternNames2() =  domain(getPatternNames((Pattern) `a::b::c::x`)) == {RCompoundName(["a","b","c","x"])};
test bool tstGetPatternNames3() =  domain(getPatternNames((Pattern) `[x]`)) == {RSimpleName("x")};
test bool tstGetPatternNames4() =  domain(getPatternNames((Pattern) `\<x, y\>`)) == {RSimpleName("x"),RSimpleName("y")};
test bool tstGetPatternNames5() =  domain(getPatternNames((Pattern) `/\<x:[a-z]+\>/`)) == {RSimpleName("x")};
test bool tstGetPatternNames6() =  domain(getPatternNames((Pattern) `/^\<x:[a-z]+\>aaa\<y:[0-9]+\>$/`)) == {RSimpleName("x"),RSimpleName("y")};

value main() = domain(getPatternNames((Pattern) `/\<x:[a-z]+\>/`)) ;