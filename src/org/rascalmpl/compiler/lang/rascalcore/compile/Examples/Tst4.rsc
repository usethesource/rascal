module lang::rascalcore::compile::Examples::Tst4


int f(int n) = g(n);

int g(1) = 10;
default int g(int n) = n * 100;
