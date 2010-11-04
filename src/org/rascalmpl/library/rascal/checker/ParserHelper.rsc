@bootstrapParser
module rascal::checker::ParserHelper

import IO;
import rascal::syntax::RascalRascal;
import ParseTree;

public Tree parseType(str toParse) {
	return parse(#Type,toParse);
}

public Tree parseExpression(str toParse) {
    return parse(#Expression,toParse);
}
