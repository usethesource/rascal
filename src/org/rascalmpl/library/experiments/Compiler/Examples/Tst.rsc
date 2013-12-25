module experiments::Compiler::Examples::Tst

import Exception;
import Set;


 data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)|
         f(DATA left, DATA right);
  data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S); 

bool f1() = f(_):= f(1);
bool f2() = f(_,_):= f(1,2);
bool f2() = (f(n5) := f(1)) && (n5 == 1);