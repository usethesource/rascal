@synopsis{Regression tests for issue #2964}
module lang::rascal::tests::functionality::Issue2964

import ParseTree;
import Set;
import lang::rascal::grammar::ParserGenerator;

layout L = [\ ]* !>> [\ ];

lexical Id = [a-z]+ !>> [a-z];

syntax E
    = id: Id
    | notPos: Id (('NOT' 'IS') | ('IS'? 'NOT')) 'POSITIVE' G
    ;

syntax G = () !>> [*];

// this would fail with a Java compilation error
test bool parseNotIs()
    = (E) `<Id _> NOT IS POSITIVE` := parse(#E, "a NOT IS POSITIVE");

test bool parseIsNot()
    = (E) `<Id _> IS NOT POSITIVE` := parse(#E, "a IS NOT POSITIVE");

test bool parseNot()
    = (E) `<Id _> NOT POSITIVE` := parse(#E, "a NOT POSITIVE");

// the name of an `alt` must not depend on the `id` keyword fields of its elements
test bool altNameIndependentOfIds() {
    Symbol a(int k) = seq([cilit("NOT")[id=k], layouts("L"), cilit("IS")]);
    Symbol b(int k) = seq([opt(cilit("IS")[id=k * 7]), layouts("L"), cilit("NOT")]);

    return size({ v2i(alt({a(k), b(k)})) | k <- [0..100] }) == 1;
}

test bool altNameIndependentOfIdsThreeElements() {
    Symbol a(int k) = seq([lit("a")[id=k], lit("b")]);
    Symbol b(int k) = seq([lit("b"), lit("a")[id=k * 3]]);
    Symbol c(int k) = seq([lit("c")[id=k * 7], lit("a")]);

    return size({ v2i(alt({a(k), b(k), c(k)})) | k <- [0..100] }) == 1;
}

// the name of an `alt` is the same as when it is computed without any `id` fields
test bool altNameEqualsUnsetName() {
    Symbol a(int k) = seq([cilit("NOT")[id=k], layouts("L")[id=k + 1], cilit("IS")[id=k + 2]])[id=k + 3];
    Symbol b(int k) = seq([opt(cilit("IS")[id=k + 4])[id=k + 5], layouts("L")[id=k + 6], cilit("NOT")[id=k + 7]])[id=k + 8];
    str plain = v2i(alt({seq([cilit("NOT"), layouts("L"), cilit("IS")]), seq([opt(cilit("IS")), layouts("L"), cilit("NOT")])}));

    return all(k <- [0..100], v2i(alt({a(k), b(k)})) == plain);
}
