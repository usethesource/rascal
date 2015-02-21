module experiments::Compiler::Examples::Tst5

value main(list[value] args) = {m = (1:10,2:20); return {m[n] | n <- {1,2,3}, m[n]?};};
