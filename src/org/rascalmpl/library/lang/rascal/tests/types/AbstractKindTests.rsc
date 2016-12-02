@bootstrapParser
module lang::rascal::tests::types::AbstractKindTests

import lang::rascal::types::AbstractKind;
import ParseTree;
import lang::rascal::\syntax::Rascal;

test bool tstKind(TagKind t) = convertKind(parse(#Kind, prettyPrintKind(t))) == t;