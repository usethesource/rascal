module experiments::Compiler::Examples::Tst1

import Node;

value main(list[value] args) = delAnnotations("f"(1,2,3));

test bool f() = main([]) == "f"(1,2,3);
