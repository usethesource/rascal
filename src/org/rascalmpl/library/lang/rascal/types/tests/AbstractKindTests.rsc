module lang::rascal::types::tests::AbstractKindTests

import lang::rascal::types::AbstractKind;

test bool tstKind(TagKind t) = convertKind([Kind] prettyPrintKind(t)) == t;