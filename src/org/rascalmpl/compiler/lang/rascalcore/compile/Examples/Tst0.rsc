module lang::rascalcore::compile::Examples::Tst0



int f(int n, bool b = false) = 0;
int f(int n, str s, bool b = false) = 0;
int f(int n, bool b = false) = 0;
int f(int n) = 0;

value main() = f(0);