
module experiments::Compiler::Examples::SampleFuns

import List; 

data D = d1(int n) | d1(str s) | d2 (str s); // simple constructors

data D = d3(int n, str opt = "abc");

data D(int x = 3) = d4(str z);

int fun1(int n, int delta = 2) = n + delta;

int fun1(list[int] l) = size(l);    // overloaded via list type

int fun1(list[str] l) = 2 * size(l);

int fun1(list[str] l, int n) = n * size(l);

real fun1(real r) = 2.0 * r;

value main() = d1(24,x=33);