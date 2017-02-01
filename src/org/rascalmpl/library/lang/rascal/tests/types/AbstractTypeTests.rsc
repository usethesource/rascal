@bootstrapParser
module lang::rascal::tests::types::AbstractTypeTests

import lang::rascal::types::AbstractType;
import lang::rascal::\syntax::Rascal;
import ValueIO;
import ParseTree;

@ignore{This test seems to be too demanding}
test bool tstPrettyPrintType(Symbol s) = parse(#Type, prettyPrintType(s)) == s;

value main() = subtype(sort("Program"), adt("Tree",[]));