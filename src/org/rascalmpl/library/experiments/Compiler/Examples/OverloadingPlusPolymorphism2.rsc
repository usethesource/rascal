module experiments::Compiler::Examples::OverloadingPlusPolymorphism2

import Exception;
import Set;

private bool similar(int a, int b) = a % 5 == b % 5;

value main(list[value] args) = group({1,2,3}, similar);