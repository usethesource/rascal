@bootstrapParser
module lang::rascalcore::check::tests::VisitTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool WrongInsert() = unexpectedType("String vs = visit ([1,2,3]) {case 1: insert \"abc\";} == [\"abc\", 2, 3];;");
