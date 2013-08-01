module experiments::CoreRascal::Translation::Examples::Example1

//bool n = true;

//int f(int x) {str n = "abc"; {real m = 1.5;}}

int fac(int n) = (n <= 0) ? 1 : n * fac(n - 1);

//int g(int n) = n + 1;
