module experiments::Compiler::Examples::OverloadingPlusBacktracking2

import List;
import IO;

list[int] f([*int x, *int y]) { if(size(x) == size(y)) return x; println("<x>; <y>"); fail; }
default list[int] f(list[int] l) = l;

value main(list[value] args) {
    return f([1,2,3,4]);
}