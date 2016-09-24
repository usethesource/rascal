module experiments::Compiler::Examples::Tst6

int incr(int n, int delta = 1) = n + delta;

value main() = incr(5, 2);