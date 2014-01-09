module experiments::Compiler::Examples::OverloadingPlusPolymorphism1

import Exception;
import List;

int inc(int n) { return n + 1; } 

public value main(list[value] args) = mapper([1, 2, 3], inc);