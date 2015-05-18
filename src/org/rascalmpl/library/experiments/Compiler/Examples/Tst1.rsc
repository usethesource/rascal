module experiments::Compiler::Examples::Tst1

data F = f(F, F) | g(F, F) | i(int i);

test bool nodeGenerator13() = [N | /int N <- f(i(1),g(i(2),i(3)))] == [1,2,3];