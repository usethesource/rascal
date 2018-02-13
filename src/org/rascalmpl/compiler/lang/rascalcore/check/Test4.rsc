module lang::rascalcore::check::Test4

int n = 0;

@doc{xxxxxx} int f(int x) = n + x;

int g() = f(3);