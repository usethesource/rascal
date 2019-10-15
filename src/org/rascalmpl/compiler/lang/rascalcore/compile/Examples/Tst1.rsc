module lang::rascalcore::compile::Examples::Tst1
lexical IntegerLiteral = [0-9]+;           
start syntax Exp = con: IntegerLiteral; 
data Exp = con(int n);
 
value main() {
    Exp c = [Exp] "3";
    return c;
}