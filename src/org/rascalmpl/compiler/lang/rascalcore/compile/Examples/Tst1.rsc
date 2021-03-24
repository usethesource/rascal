module lang::rascalcore::compile::Examples::Tst1


import Exception;

// escaped constructor and field names

data DwithEscapes = \f() | \g(int \lft, str \rht);

test bool escape1a() = \f() == \f();
test bool escape1b() = \g(1, "a").\lft == 1;
test bool escape1c() = \g(1, "a").\rht == "a";
test bool escape1d() = \g(1, "a") is \g;
test bool escape1e() = \g(1, "a") has \lft;


value main() = true;