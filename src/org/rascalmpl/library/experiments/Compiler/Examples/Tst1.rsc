module experiments::Compiler::Examples::Tst1
       
test bool transEq(value x, value y, value z) = (x == y && y == z) ==> (x == z);
