@bootstrapParser
module lang::rascal::types::tests::AbstractTypeTests

import lang::rascal::types::AbstractType;
import lang::rascal::\syntax::Rascal;
import ValueIO;
import ParseTree;

@ignore{This test seems to be too demanding}
test bool tstPrettyPrintType(Symbol s) = parse(#Type, prettyPrintType(s)) == s;

value main() = subtype(sort("Program"), adt("Tree",[]));