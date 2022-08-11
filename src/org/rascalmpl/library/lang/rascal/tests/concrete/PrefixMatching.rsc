@doc{tests regression of issue #1594}
module lang::rascal::tests::concrete::PrefixMatching

lexical Layout = [\t\ ]+ !>> [\t\ ];
layout LayoutList = Layout* !>> [\t\ ;];
lexical Integer = integer: [0-9];
lexical Identifier = [a-z A-Z 0-9 _];
start syntax Statement = assign: Identifier id "=" Expression val;
syntax Expression = Integer integer;

str input = "V = 3";

test bool prefixAssignStatement() {
    Statement stat = parse(#Statement, input);

    return assign(lhs,rhs) := stat;
}

test bool prefixAssignTree() {
    Tree stat = parse(#Statement, input);

    return assign(lhs,rhs) := stat;
}

test bool prefixAssignValue() {
    value stat = parse(#Statement, input);

    return assign(lhs,rhs) := stat;
}

test bool specialCaseForAppl1() {
    return appl(prod(sort("S"),[],{}),[]) := appl(prod(sort("S"),[],{}),[]);
}

test bool specialCaseForAppl1() {
    return Tree::appl(prod(sort("S"),[],{}),[]) := appl(prod(sort("S"),[],{}),[]);
}

test bool concreteAssignStatement() {
    Statement stat = parse(#Statement, input);
 
    return (Statement)`<Identifier lhs3> = <Expression val>` := stat;
}

test bool concreteAssignTree() {
    Tree stat = parse(#Statement, input);
 
    return (Statement)`<Identifier lhs3> = <Expression val>` := stat;
}

test bool concreteAssignValue() {
    value stat = parse(#Statement, input);
 
    return (Statement)`<Identifier lhs3> = <Expression val>` := stat;
}


