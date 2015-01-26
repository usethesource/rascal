module lang::rascal::tests::types::VisitTCTests

import lang::rascal::tests::types::StaticTestingUtils;
import ParseTree;

test bool WrongInsert() = unexpectedType("String vs = visit ([1,2,3]) {case 1: insert \"abc\";} == [\"abc\", 2, 3];;");
