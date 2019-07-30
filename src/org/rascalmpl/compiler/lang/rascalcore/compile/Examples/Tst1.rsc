module lang::rascalcore::compile::Examples::Tst1

test bool tupleExpressions() {
    value n = 1; 
    return int _ := n && str _ := "abc" ;// && tuple[int, str] _ := < n , s >;
}   

value main() = tupleExpressions();  

      