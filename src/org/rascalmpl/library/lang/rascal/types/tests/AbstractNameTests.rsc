@bootstrapParser
module lang::rascal::types::tests::AbstractNameTests

import lang::rascal::types::AbstractName;
import lang::rascal::\syntax::Rascal;
import String;
import ParseTree;

test bool tstGetSimpleName1() = getSimpleName(RSimpleName("x")) == "x";
test bool tstGetSimpleName2() = getSimpleName(RCompoundName(["a","b","c","x"])) == "x";

test bool tstConvertName1() = convertName((QualifiedName) `x`) == RSimpleName("x");
test bool tstConvertName2() = convertName((QualifiedName) `a::b::c::x`) == RCompoundName(["a","b","c","x"]);
test bool tstConvertName3() = convertName((Name) `x`) == RSimpleName("x");

test bool tstGetLastName1() = getLastName((QualifiedName) `x`) == (Name) `x`;
test bool tstGetLastName1() = getLastName((QualifiedName) `a::b::c::x`) == (Name) `x`;

test bool tstAppendName1() = appendName(RSimpleName("x"), RSimpleName("y")) == RCompoundName(["x", "y"]);
test bool tstAppendName2() = appendName(RSimpleName("x"), RCompoundName(["y", "z"])) == RCompoundName(["x", "y", "z"]);
test bool tstAppendName3() = appendName(RSimpleName("x"), RCompoundName(["y", "z"])) == RCompoundName(["x", "y", "z"]);
test bool tstAppendName4() = appendName(RCompoundName(["p", "q"]), RCompoundName(["x", "y"])) == RCompoundName(["p", "q", "x", "y"]);

test bool tstPrettyPrintName1() = prettyPrintName(RSimpleName("x")) == "x";
test bool tstPrettyPrintName2() = prettyPrintName(RCompoundName(["a","b","c","x"])) == "a::b::c::x";

test bool tstConvertNameString1() = convertNameString("x") == RSimpleName("x");
test bool tstConvertNameString2() = convertNameString("a::b::c::x") == RCompoundName(["a","b","c","x"]);

value main() = convertName((QualifiedName) `a::b::c::x`);