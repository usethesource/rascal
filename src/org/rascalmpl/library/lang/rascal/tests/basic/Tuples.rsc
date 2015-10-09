module lang::rascal::tests::basic::Tuples

import Tuple;

test bool subscription(tuple[&A] T) = <T[0]> == T;
test bool subscription(tuple[&A, &B] T) = <T[0], T[1]> == T;
test bool subscription(tuple[&A, &B, &C] T) = <T[0], T[1], T[2]> == T;

test bool subscriptionWrapped(tuple[&A] T) = <T[-1]> == T;
test bool subscriptionWrapped(tuple[&A, &B] T) = <T[-2], T[-1]> == T;
test bool subscriptionWrapped(tuple[&A, &B, &C] T) = <T[-3], T[-2], T[-1]> == T;

test bool tupleExpressions() {
    value n = 1; 
    value s = "string"; 
    return tuple[int, int] _ := < n, n > && tuple[str, str] _ := < s, s > && tuple[int, str] _ := < n , s >;
}

test bool dropLabelsConcat(tuple[int x, str s] a, tuple[real] b) 
	= !((a + b) has x);

test bool dropLabelsConcat(tuple[int x, str s] a, tuple[real] b) 
	= !((b + a) has x);

test bool dropLabelsConcatDuplicate(tuple[int x, str s] a, tuple[real x] b) 
	= !((a + b) has x);
	
test bool keepLabelsConcat(tuple[int x, str s] a, tuple[real r] b) 
	= ((a + b) has x);
	
// tuples with escped field names	
	
alias TUP = tuple[int \n,str \type];

test bool escape1() {  TUP T = <1, "a">; return T.\n == 1; }
test bool escape2() {  TUP T = <1, "a">; return T.\type == "a"; }

test bool escape3() {  list[TUP] L = [<1, "a">, <2, "b">]; return L[0].\n == 1; }
test bool escape4() {  list[TUP] L = [<1, "a">, <2, "b">]; return L[0].\type == "a"; }

test bool escape5() {  list[TUP] L = [<1, "a">, <2, "b">]; return L.\n == [1, 2]; }

test bool escape6() {  list[TUP] L = [<1, "a">, <2, "b">]; return L<\n> == [1, 2]; }