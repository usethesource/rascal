module experiments::Compiler::Examples::Tst1

lexical IntegerLiteral = [0-9]+;           

start syntax Exp = con: IntegerLiteral;

data Exp = con(int n);