@bootstrapParser
module lang::rascal::tests::functionality::ConcreteSyntaxTests5

import ParseTree;
import IO;
import lang::rascal::\syntax::Rascal;

test bool expr1() = (Expression) `<Expression a> + <Expression b>` := (Expression) `1 + 2`;

test bool expr2() = (Expression) `{ <Expression a> }` := (Expression) `{ 1 }`;

test bool pat1()  = (Pattern) `<StringLiteral sl>` := (Pattern) `"1"`;

test bool assign1() = (Assignable) `<Assignable receiver> ? <Expression defaultExpression>` := (Assignable) `x[1]?0`;

test bool isThisATuple() = (Expression)`\< <{Expression ","}+ es> \>` := parse(#Expression, "\<1\>");
