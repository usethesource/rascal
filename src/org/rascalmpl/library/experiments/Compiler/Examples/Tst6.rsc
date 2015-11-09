module experiments::Compiler::Examples::Tst6

import IO;

int f(int n) = n;

value main6(){
    x = 42;
    println(x);
    y = f(x) + 10;
    return y;
}