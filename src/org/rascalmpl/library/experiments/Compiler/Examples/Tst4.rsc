module experiments::Compiler::Examples::Tst4

import List;

bool less(int a, int b) = a < b;

value main(list[value] args) = sort([5, 3, 10], less);