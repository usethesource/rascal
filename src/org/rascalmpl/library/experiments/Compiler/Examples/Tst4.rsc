module experiments::Compiler::Examples::Tst4

import lang::rascal::tests::types::StaticTestingUtils;
import ParseTree;

value main(list[value] args) = unexpectedType("String vs = visit ([1,2,3]) {case 1: insert \"abc\";} == [\"abc\", 2, 3];;");