module lang::rascal::tests::imports::KeywordParameterTest1

data L(int a = 1, int b = 2 * a) = l(int c = 2 * b);

data L(int d = -1) = m();

L createL1() = l();
L createM1() = l();