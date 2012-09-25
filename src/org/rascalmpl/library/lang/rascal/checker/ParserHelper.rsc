@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::checker::ParserHelper

import IO;
import ParseTree;
import List; 
import String;    
import util::Math;

import lang::rascal::syntax::RascalRascal;

public Tree parseType(str toParse) {
	return parse(#Type,toParse);
}

public Tree parseExpression(str toParse) {
    return parse(#Expression,toParse);
}

public Tree parsePattern(str toParse) {
    return parse(#Pattern,toParse);
}

//public bool doIMatch(Tree t) {
//    return (Expression)`<Name n>` := t;
//}

//public Tree whatMatched(Tree t) {
//    if((Expression)`<Name n>` := t) return n;
//    return t;
//}

public Tree parseDeclaration(str toParse) {
	return parse(#Declaration,toParse);
}

public Tree parseStatement(str toParse) {
	return parse(#Statement,toParse);
}

public Tree parseAssignable(str toParse) {
	return parse(#Assignable,toParse);
}

public void howManyMatches(str toParse) {
	Tree pt = parse(#Expression,toParse);
	for ((Expression)`[<{Expression ","}* x>, <{Expression ","}* x2>, <Expression a>, <{Expression ","}* y>]` := pt) println("Found a match: <x> and <x2> and <a> and <y>");
}

public tuple[list[Tree],list[Tree]] rexpVisit(str toParse) {
	Tree pt = parse(#Expression,toParse);
	if ((Expression)`<RegExpLiteral rl>` := pt) {
		iprint(rl);
		list[Tree] nameUses = [];
		list[Tree] nameDefs = [];
	
		visit(rl) {
			case \appl(\prod(lex("RegExp"),[_,\lex("Name"),_],_),list[Tree] prds) : nameUses += prds[1];
			case \appl(\prod(lex("RegExp"),[_,\lex("Name"),_,_,_],_),list[Tree] prds) : nameDefs += prds[1];
			case \appl(\prod(lex("NamedRegExp"),[_,\lex("Name"),_],_),list[Tree] prds) : nameUses += prds[1];
			
		}
		
		return < nameUses, nameDefs >;
	}
	return < [],[] >;
}

public bool isThisATuple(str toParse) {
	return (Expression)`< <{Expression ","}+ es> >` := parse(#Expression, toParse);
}

public bool isThisAList(str toParse) {
	return (Expression)`[ <{Expression ","}* es> ]` := parse(#Expression, toParse);
}