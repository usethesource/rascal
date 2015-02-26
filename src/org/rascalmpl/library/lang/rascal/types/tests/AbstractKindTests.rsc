@bootstrapParser
module lang::rascal::types::tests::AbstractKindTests

import lang::rascal::types::AbstractKind;
import ParseTree;
import lang::rascal::\syntax::Rascal;

test bool tstKind(TagKind t) = convertKind(parse(#Kind, prettyPrintKind(t))) == t;