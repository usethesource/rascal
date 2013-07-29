module experiments::CoreRascal::Translation::Examples::Example1



int fac(int n) = (n <= 1) ? 1 : n * fac(n - 1);
