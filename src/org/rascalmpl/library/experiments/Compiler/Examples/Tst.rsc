module experiments::Compiler::Examples::Tst

value g(int a, int b) = a / b;

value f(int a, str x = "x") = true ? g(a, 0) : 7;

public value main(list[value] args) { int x = 3; assert 3 == 4 : "abc"; }//f(5, x = "XXXX");
