module lang::rascal::tests::types::RegExpTCTests

import lang::rascal::tests::types::StaticTestingUtils;

public test bool match() = redeclaredVariable("(/\<x:[a-z]+\>-\<x:[a-z]+\>/ !:= \"abc-abc\") && (x == \"abc\");");