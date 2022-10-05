module lang::rascal::tests::basic::Tuples
  
 
test bool subscription1(tuple[&A] T) = <T[0]> == T;
test bool subscription2(tuple[&A, &B] T) = <T[0], T[1]> == T;
test bool subscription3(tuple[&A, &B, &C] T) = <T[0], T[1], T[2]> == T;

test bool subscriptionWrapped1(tuple[&A] T) = <T[-1]> == T;
test bool subscriptionWrapped2(tuple[&A, &B] T) = <T[-2], T[-1]> == T;
test bool subscriptionWrapped3(tuple[&A, &B, &C] T) = <T[-3], T[-2], T[-1]> == T;

test bool tupleExpressions() {
    value n = 1; 
    value s = "string"; 
    return tuple[int, int] _ := < n, n > && tuple[str, str] _ := < s, s > && tuple[int, str] _ := < n , s >;
}
  
test bool dropLabelsConcat1(tuple[int x, str s] a, tuple[real] b) 
	= !((a + b) has x);

test bool dropLabelsConcat2(tuple[int x, str s] a, tuple[real] b) 
	= !((b + a) has x);

test bool dropLabelsConcatDuplicate(tuple[int x, str s] a, tuple[real x] b) 
	= !((a + b) has x);
	   
test bool keepLabelsConcat(tuple[int x, str s] a, tuple[real r] b) 
	= ((a + b) has x); 
	
// tuples with escaped field names	

test bool escape1() {  tuple[int \n,str \type] T = <1, "a">; return T.\n == 1; }
test bool escape2() {  tuple[int \n,str \type] T = <1, "a">; return T.\type == "a"; }

test bool escape3() {  list[tuple[int \n,str \type]] L = [<1, "a">, <2, "b">]; return L[0].\n == 1; }
test bool escape4() {  list[tuple[int \n,str \type]] L = [<1, "a">, <2, "b">]; return L[0].\type == "a"; }
test bool escape5() {  list[tuple[int \n,str \type]] L = [<1, "a">, <2, "b">]; return L.\n == [1, 2]; }
test bool escape6() {  list[tuple[int \n,str \type]] L = [<1, "a">, <2, "b">]; return L.\type == ["a", "b"]; }
test bool escape7() {  list[tuple[int \n,str \type]] L = [<1, "a">, <2, "b">]; return L<\n> == [1, 2]; }
test bool escape8() {  list[tuple[int \n,str \type]] L = [<1, "a">, <2, "b">]; return L<\type> == ["a", "b"]; }

test bool escape9() {  set[tuple[int \n,str \type]] S = {<1, "a">, <2, "b">}; return S.\n == {1, 2}; }
test bool escape10() {  set[tuple[int \n,str \type]] S = {<1, "a">, <2, "b">}; return S.\type == {"a", "b"}; }
test bool escape11() {  set[tuple[int \n,str \type]] S = {<1, "a">, <2, "b">}; return S<\n> == {1, 2}; }
test bool escape12() {  set[tuple[int \n,str \type]] S = {<1, "a">, <2, "b">}; return S<\type> == {"a", "b"}; }

// tuples with escaped field names via alias
 
alias TUP = tuple[int \n,str \type];

test bool escapeAndAlias() {  TUP T = <1, "a">; return T.\n == 1; }
test bool escapeAndAlias2() {  TUP T = <1, "a">; return T.\type == "a"; }

test bool escapeAndAlias3() {  list[TUP] L = [<1, "a">, <2, "b">]; return L[0].\n == 1; }
test bool escapeAndAlias4() {  list[TUP] L = [<1, "a">, <2, "b">]; return L[0].\type == "a"; }
test bool escapeAndAlias5() {  list[TUP] L = [<1, "a">, <2, "b">]; return L.\n == [1, 2]; }
test bool escapeAndAlias6() {  list[TUP] L = [<1, "a">, <2, "b">]; return L.\type == ["a", "b"]; }
test bool escapeAndAlias7() {  list[TUP] L = [<1, "a">, <2, "b">]; return L<\n> == [1, 2]; }
test bool escapeAndAlias8() {  list[TUP] L = [<1, "a">, <2, "b">]; return L<\type> == ["a", "b"]; }

test bool escapeAndAlias9() {  set[TUP] S = {<1, "a">, <2, "b">}; return S.\n == {1, 2}; }
test bool escapeAndAlias10() {  set[TUP] S = {<1, "a">, <2, "b">}; return S.\type == {"a", "b"}; }
test bool escapeAndAlias11() {  set[TUP] S = {<1, "a">, <2, "b">}; return S<\n> == {1, 2}; }
test bool escapeAndAlias12() {  set[TUP] S = {<1, "a">, <2, "b">}; return S<\type> == {"a", "b"}; }
