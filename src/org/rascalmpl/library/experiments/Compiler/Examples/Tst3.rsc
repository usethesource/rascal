module experiments::Compiler::Examples::Tst3

data D = d1() |  d2(int n) | d3(str s, bool b = true);

value main() = d3("a") has b;