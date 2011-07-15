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
import Grammar;
import List; 
import String;    
import Integer;

import lang::rascal::grammar::definition::Modules;
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Characters;
import lang::rascal::grammar::definition::Symbols;
import lang::rascal::grammar::definition::Attributes;
import lang::rascal::grammar::Assimilator;
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

public bool match5(str toParse) {
    pt = parse(#Statement,toParse);
    return ((Statement)`<Expression _>;` := pt);
}

public Tree parseModule(loc mod) {
	return parse(#Module,mod);
}

private set[SyntaxDefinition] collect(Module mod) {
  set[SyntaxDefinition] result = {};
  
  top-down-break visit (mod) {
    case SyntaxDefinition s : result += s; 
    case Body b => b
  }
  return result;
}  

public Grammar getGrammarForRascal() {
	t = parse(#Module,|file:///Users/mhills/Projects/rascal/build/rascal/src/org/rascalmpl/library/lang/rascal/syntax/RascalRascal.rsc|);
	return syntax2grammar(collect(t));
}

public bool isNonterminal(Symbol x) { 
    return \lit(_) !:= x 
       && \empty() !:= x
       && \cilit(_) !:= x 
       && \char-class(_) !:= x 
       && \layouts(_) !:= x
       && \keywords(_) !:= x
       && \start(_) !:= x
       && \parameterized-sort(_,[\parameter(_),_*]) !:= x;
}

public Tree parseSyntax(str toParse) {
	return parse(#SyntaxDefinition,toParse);
}

public Tree getSym(Tree t) {
	if ((SyntaxDefinition) `syntax <Sym s> = <Prod p>;` := t) {
        return s;
    }
	return t;
}

public Tree getProd(Tree t) {
	if ((SyntaxDefinition) `syntax <Sym s> = <Prod p>;` := t) {
        return p;
    }
	return t;
}

public Production prod2prod(Symbol nt, Prod p) {
  switch(p) {
    case (Prod) `<ProdModifier* ms> <Name n> : ()` :
      return prod(label("<n>",nt), [], mods2attrs(ms));
    case (Prod) `<ProdModifier* ms> ()` :
      return prod(nt, [], mods2attrs(ms));
    case (Prod) `<ProdModifier* ms> <Name n> : <Sym* args>` :
      return prod(label("<n>",nt),args2symbols(args),mods2attrs(ms));
    case (Prod) `<ProdModifier* ms> <Sym* args>` :
      return prod(nt, args2symbols(args), mods2attrs(ms));
    case (Prod) `<Prod l> | <Prod r>` :
      return choice(nt,{prod2prod(nt, l), prod2prod(nt, r)});
    case (Prod) `<Prod l> > <Prod r>` : 
      return priority(nt,[prod2prod(nt, l), prod2prod(nt, r)]);
    case (Prod) `left (<Prod q>)` :
      return associativity(nt, \left(), {prod2prod(nt, q)});
    case (Prod) `right (<Prod q>)` :
      return associativity(nt, \right(), {prod2prod(nt, q)});
    case (Prod) `non-assoc (<Prod q>)` :
      return associativity(nt, \non-assoc(), {prod2prod(nt, q)});
    case (Prod) `assoc(<Prod q>)` :
      return associativity(nt, \left(), {prod2prod(nt, q)});
    case (Prod) `...`: return \others(nt);
    case (Prod) `: <Name n>`: return \reference(nt, "<n>");
    default: throw "missed a case <p>";
  } 
}