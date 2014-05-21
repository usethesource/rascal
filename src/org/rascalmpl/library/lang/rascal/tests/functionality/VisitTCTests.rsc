module lang::rascal::tests::functionality::VisitTCTests

import lang::rascal::tests::static::StaticTestingUtils;

public test bool WrongInsert() = unexpectedType("String vs = visit ([1,2,3]) {case 1: insert \"abc\";} == [\"abc\", 2, 3];;");
