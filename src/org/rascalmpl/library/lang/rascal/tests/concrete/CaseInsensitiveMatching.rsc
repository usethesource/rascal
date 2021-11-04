module lang::rascal::tests::concrete::CaseInsensitiveMatching

lexical CI = 'if' 'fi';

test bool matchAnyway1() = (CI) `IFFI` := (CI) `iffi`;
test bool matchAnyway2() = (CI) `IffI` := (CI) `iFFi`;
test bool matchAnyway3() = (CI) `iffi` := (CI) `IFFI`;
test bool matchAnyway4() = (CI) `IFFI` := (CI) `IFFI`;
test bool matchAnyway5() = (CI) `iffi` := (CI) `iffi`;

