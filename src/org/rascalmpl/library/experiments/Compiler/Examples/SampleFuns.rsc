module experiments::Compiler::Examples::SampleFuns

import List;

data D = d1(int n) | d1(str s) | d2 (str s); // ssimple constructors

int fun1(int n) = n;

int fun1(list[int] l) = size(l);    // overloaded via list type

int fun1(list[str] l) = size(l);

value main() = fun1([1,2,3]) == 3;