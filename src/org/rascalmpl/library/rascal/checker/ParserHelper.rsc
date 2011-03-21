@bootstrapParser
module rascal::checker::ParserHelper

import IO;
import ParseTree;
import rascal::syntax::RascalRascal;

public Tree parseType(str toParse) {
	return parse(#Type,toParse);
}

public Tree parseExpression(str toParse) {
    return parse(#Expression,toParse);
}

public bool doIMatch(Tree t) {
    return (Expression)`<Name n>` := t;
}

public Tree whatMatched(Tree t) {
    if((Expression)`<Name n>` := t) return n;
    return t;
}

public Tree parseDeclaration(str toParse) {
	return parse(#Declaration,toParse);
}

