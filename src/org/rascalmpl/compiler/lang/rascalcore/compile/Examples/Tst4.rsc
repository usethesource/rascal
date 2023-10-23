@bootstrapParser
module lang::rascalcore::compile::Examples::Tst4

import ParseTree;
//import lang::rascalcore::check::AType;

//value main() = asubtype(\start(aadt("A",[],contextFreeSyntax())), aadt("Symbol",[],dataSyntax()));
value main() = subtype(\start(sort("A")), adt("Symbol",[]));