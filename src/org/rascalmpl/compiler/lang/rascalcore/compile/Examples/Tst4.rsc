module lang::rascalcore::compile::Examples::Tst4

import lang::rascalcore::compile::Examples::Tst2;

test bool tstHead(list[&T] L) = head(L) == L;