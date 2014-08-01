module experiments::vis2::Tst3

data D = d(int n, str s = "def");

value main(list[value] args) = d(10).s;
