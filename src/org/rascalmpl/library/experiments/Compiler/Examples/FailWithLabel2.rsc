module experiments::Compiler::Examples::FailWithLabel2

import IO;

value main(list[value] args) {
    if1:if(x <- [1,2,3,4], x <= 3) {
        if2:if(y <- [4,3,2,1], y >= 3) {
            println("x = <x>; y = <y>");
            if(x != 3) {
                fail if1;
            } else if(y != 3) {
                fail if2;
            }
            return x + y;
        }
    }
}