module experiments::Compiler::Examples::Tst3

int replace_me_i() { throw "replace me"; }
int replace_me_s() { throw "replace me"; }
  
test bool q1() = [ replace_me_i() | N <- [1, 2, 3] ] == [0, 1, 2];

test bool q1() = [ replace_me_s() | N <- ["a", "b", "c"] ] == ["aa", "bb", "cc"];