module experiments::Compiler::Examples::Tst4

int f(int x) = g(x);

int g(5) = 50;
default int g(int x) = x;

value main(list[value] args) = f (6);