module lang::rascalcore::compile::Examples::Tst3
import List;
import Node;

lexical X = [xyzXYZ];
lexical XStar = X* xs;
lexical XPlus = X+ xs1;
lexical XComma = {X ","}* xcommas;

syntax A = [abcABC];
syntax AStar = A* as;
syntax APlus = A+ as1;
syntax AComma = {A ","}* acommas;
syntax ACommaPlus = {A ","}+ acommas;
layout L = [ ]*;


//layout L = " ";

syntax QualifiedName
    = {XPlus "::"}+ names !>> "::" ;
 
bool eqNoSrc(Tree t, Tree u) = unsetRec(t, "src") == unsetRec(u, "src");

public str prettyPrintName(QualifiedName qn){
    nameParts = [ "<n>" | n <- qn.names];
    return intercalate("::", nameParts);
}

test bool QualifiedName1() = prettyPrintName((QualifiedName) `x`) == "x";
test bool QualifiedName2() = prettyPrintName((QualifiedName) `xy`) == "xy";
test bool QualifiedName3() = prettyPrintName((QualifiedName) `x::y`) == "x::y";
test bool QualifiedName4() = prettyPrintName((QualifiedName) `x ::y`) == "x::y";
test bool QualifiedName6() = prettyPrintName((QualifiedName) `x:: y`) == "x::y";
test bool QualifiedName7() = prettyPrintName((QualifiedName) `xyz::zyx`) == "xyz::zyx";

value main() = prettyPrintName([QualifiedName] "abc::def");