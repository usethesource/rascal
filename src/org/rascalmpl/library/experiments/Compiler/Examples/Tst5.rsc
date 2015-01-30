module experiments::Compiler::Examples::Tst5


map[str, int] M = ("a" : 1, "b" : 2);

bool isdef(map[str,int] m, str k) = M[k]?;

value main(list[value] args) = { M[k] | k <- {"a", "b", "c"}, M[k]?};