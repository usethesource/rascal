module experiments::Compiler::Examples::Tst2

import List;

int x;

value main(list[value] args) { x = 1; throw "abc"; x = 2; } //x; //head([]);