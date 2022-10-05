module lang::rascal::tests::concrete::FieldProjectionBug

import ParseTree;

layout Layout = [\ \r\n]+ !>> [\ \r\n];
start syntax Expression = (Identifier i|BuiltIn b) function;
lexical Identifier = [a-z A-Z 0-9]+ !>> [a-z A-Z 0-9] \ Keywords;
lexical BuiltIn = "hoi";
keyword Keywords = "hoi";

Tree T = (Expression) `hoi`.function;

test bool prodFieldProjectionOnAnAlternative() {
    return regular(alt(_)) := T.prod;
}

test bool labeledAlternativeProjection() {
    return BuiltIn _ := T.b;
}
