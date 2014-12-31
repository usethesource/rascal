module lang::rascal::tests::imports::KeywordParameterTest2

data L(str e = "e", str f = e + e) = n(str g = f + f);

data L(str h = "") = p();