module lang::rascal::tests::basic::FunctionCachesAndGlobals

default int f(int _) = 42;
int x = f(0);
int f(0) = 0;

// test bool globalInitLast() = x == 0;

test bool noCacheDuringInit() = f(0) == 0;