module experiments::Compiler::Examples::WeirdAppend

import Exception;
import IO;

value main(list[value] args) {
    l = for(x <- [1,2,3,4]) { int f() { append x; return 4; }; append f(); };
    return l;
}