module lang::rascal::tests::library::lang::TupleTests

public test bool tupleExpressions() {
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