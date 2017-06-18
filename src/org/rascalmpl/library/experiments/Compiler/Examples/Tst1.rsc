module experiments::Compiler::Examples::Tst1

int f(int n, int m) = n * m;

//int   fac(int n) = (n <= 1) ? 1 : n * fac(n-1);

value main() { n = f(2,3); return n; }