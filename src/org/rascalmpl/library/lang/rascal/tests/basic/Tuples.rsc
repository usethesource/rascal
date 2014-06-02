module lang::rascal::tests::basic::Tuples

import Tuple;

test bool subscription(tuple[&A] T) = <T[0]> == T;
test bool subscription(tuple[&A, &B] T) = <T[0], T[1]> == T;
test bool subscription(tuple[&A, &B, &C] T) = <T[0], T[1], T[2]> == T;

test bool subscriptionWrapped(tuple[&A] T) = <T[-1]> == T;
test bool subscriptionWrapped(tuple[&A, &B] T) = <T[-2], T[-1]> == T;
test bool subscriptionWrapped(tuple[&A, &B, &C] T) = <T[-3], T[-2], T[-1]> == T;

