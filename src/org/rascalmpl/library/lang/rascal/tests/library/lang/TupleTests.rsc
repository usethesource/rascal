module lang::rascal::tests::library::lang::TupleTests

public test bool tupleExpressions() {
    value n = 1; 
    value s = "string"; 
    return tuple[int, int] _ := < n, n > && tuple[str, str] _ := < s, s > && tuple[int, str] _ := < n , s >;
}