@bootstrapParser
module lang::rascalcore::check::tests::RegExpTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

@ignore{TODO}
test bool match() = redeclaredVariable("(/\<x:[a-z]+\>-\<x:[a-z]+\>/ !:= \"abc-abc\") && (x == \"abc\");");