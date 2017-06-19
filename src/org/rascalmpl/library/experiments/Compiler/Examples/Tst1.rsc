module experiments::Compiler::Examples::Tst1

int f(int n) = n;

int fac(int n) = (n <= 1) ? 1 : n * fac(n-1);
int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));

value main() = fac(1000);