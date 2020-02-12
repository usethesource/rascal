@bootstrapParser
module lang::rascal::tests::concrete::Syntax5

import ParseTree;
import lang::rascal::\syntax::Rascal;

test bool expr1() = (Expression) `<Expression _> + <Expression _>` := (Expression) `1 + 2`;

test bool expr2() = (Expression) `{ <Expression _> }` := (Expression) `{ 1 }`;

test bool pat1()  = (Pattern) `<StringLiteral _>` := (Pattern) `"1"`;

test bool assign1() = (Assignable) `<Assignable _> ? <Expression _>` := (Assignable) `x[1]?0`;

test bool isThisATuple() = (Expression)`\< <{Expression ","}+ _> \>` := parse(#Expression, "\<1\>");
