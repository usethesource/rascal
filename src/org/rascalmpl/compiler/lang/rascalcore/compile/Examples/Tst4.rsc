@bootstrapParser
module lang::rascalcore::compile::Examples::Tst4

import ParseTree;
import lang::rascal::\syntax::Rascal;

test bool expr1() = (Expression) `<Expression _> + <Expression _>` := (Expression) `1 + 2`;

//test bool expr2() = (Expression) `{ <Expression _> }` := (Expression) `{ 1 }`;
//
//test bool pat1()  = (Pattern) `<StringLiteral _>` := (Pattern) `"1"`;
//
//test bool assign1() = (Assignable) `<Assignable _> ? <Expression _>` := (Assignable) `x[1]?0`;
//
//test bool isThisATuple() = (Expression)`\< <{Expression ","}+ _> \>` := parse(#Expression, "\<1\>");
//
//test bool concreteFragmentHasSrc()
//    = e:(Expression) `<Expression x> + <Expression _>` := (Expression) `1 + 2` &&
//               e.src? && x.src?;
               
//test bool concreteFragmentHasLegacyLoc()
//    = e:(Expression) `<Expression x> + <Expression _>` := (Expression) `1 + 2` &&
//               e@\loc? && x@\loc?;
//
//test bool concreteFragmentHasCorrectSrcs()
//  = e:(Expression) `<Expression x> + <Expression y>` := (Expression) `1 + 2` &&
//               e.src.length == 5 &&
//               x.src.offset == e.src.offset &&
//               x.src.length == 1 &&
//               y.src.offset == e.src.offset + 4 &&
//               y.src.length == 1
//               ;