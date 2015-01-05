module lang::rascal::tests::imports::KeywordParameterTest2

data L(str e = "e", str f = e + e) = n(str g = f + f);

data L(str h = "") = p();

L createN2() = n();
L createP2() = p();