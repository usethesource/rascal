module experiments::Compiler::Examples::Tst4

import lang::rascal::tests::types::StaticTestingUtils;
import ParseTree;

value main(list[value] args) {
list[int] L = [0,1,2,3]; L[2] = "abc";
}

//unexpectedType("list[int] L = [0,1,2,3]; L[2] = \"abc\";");