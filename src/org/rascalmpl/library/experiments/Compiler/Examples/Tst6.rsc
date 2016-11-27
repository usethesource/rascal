module experiments::Compiler::Examples::Tst6

import List;
bool less(int a, int b) = a < b;
test bool sort2g() = sort([1,2,3],less) == [1,2,3];

value main() = sort([1,2,3],less);