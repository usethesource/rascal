module lang::rascal::tests::functionality::CharacterTests

import ParseTree;

private bool check(type[&T] t, value x) = &T _ := x;

test bool singleA() = check(#[A], char(65));
test bool singleB() = check(#[B], char(66));
test bool notSingleB() = !check(#[A], char(66));
test bool singleAB1() = check(#[A-B], char(65));
test bool singleAB2() = check(#[A-B], char(66));


