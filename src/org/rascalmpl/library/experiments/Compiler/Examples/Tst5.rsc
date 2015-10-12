module experiments::Compiler::Examples::Tst5

int inc (int n, int delta = 1, int mul = 1) = (n + delta) * mul;

value main() = inc(5, mul=2, delta=10);