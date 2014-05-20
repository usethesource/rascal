module lang::rascal::tests::functionality::RegExpTCTests

import lang::rascal::tests::static::StaticTestingUtils;

public test bool match() = redeclaredVariable("(/\<x:[a-z]+\>-\<x:[a-z]+\>/ !:= \"abc-abc\") && (x == \"abc\");");