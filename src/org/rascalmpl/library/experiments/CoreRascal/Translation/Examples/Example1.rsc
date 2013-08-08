module experiments::CoreRascal::Translation::Examples::Example1

//bool n = true;

//int f() {str a = "abc"; int b = 5; {bool c = true;}}

int fac(int n) = (n <= 0) ? 1 : n * fac(n - 1);

int g(int n) = n + 1;

// int fac(int n) = (n == 1) ? 1 : n * fac(n - 1);
