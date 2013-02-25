@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI}
module ParseTreeToAdt

import Boolean;
import List;
import ParseTree;
import Set;
import String;

import IO;

@doc{Algebraic view on parsing and implode semantics}
data Option[&T] = some(&T opt) | none();

public &T parseToAdt(type[&T] \type, Tree::appl(Production prod, list[Tree] args)) {
	println(prod);
	args = filterTrees(args); // filters out layouts, literals and empties 
	Option[str] lbl = getLabel(prod);
	if(none() := lbl && size(args) == 1) return parseToAdt(\type, args[0]);
	if(some(str l) := lbl) return makeAdtNode(\type, l, args);
	throw "parse error: Tree -\> Adt: <\type>";
}

public &T parseToTuple(type[&T] \type, Tree::appl(Production prod, list[Tree] args)) {
	println(prod);
	args = filterTrees(args); // filters out layouts, literals and empties
	if(none() := getLabel(prod)) return makeTupleNode(\type, args);
	throw "parse error: Tree -\> tuple[...]: <\type>";
}

public list[tuple[Tree, Tree]] parseToList(t:appl(Production prod, list[Tree] args)) {
	println(prod);
	args = filterTrees(args); // filters out layouts, literals and empties
	if(isRegularLists(prod))
		return (!isEmpty(args)) ? [<args[0], makeAdtNode(#Tree, "appl", prod, tail(args))>] : [];
	if(isOptional(prod)) {
		args = [ *( (appl(Production p, list[Tree] args0) := arg 
					&& none() := getLabel(p) 
					&& list[Tree] args00 := filterTrees(args0) 
					&& size(args00) == 1 
					&& appl(Production p0, list[Tree] args000) := args00[0]
					&& isRegularLists(p0) ) ? filterTrees(args000) : [ arg ] )  | Tree arg <- args ];
		return (!isEmpty(args)) ? [<args[0], makeAdtNode(#Tree, "appl", prod, tail(args))>] : [];
	}
	throw "parse error: Tree -\> list[tuple[Tree, Tree]]";
}

public set[tuple[Tree, Tree]] parseToSet(Tree::appl(Production prod, list[Tree] args)) {
	println(prod);
	args = filterTrees(args); // filters out layouts, literals and empties
	if(isRegularLists(prod)) return (!isEmpty(args)) ? {<args[0], makeAdtNode(#Tree, "appl", prod, tail(args))>} : {};
	throw "parse error: Tree -\> set[tuple[Tree, Tree]]";
}

public int parseToInt(Tree::appl(Production prod, list[Tree] args)) {
	println(prod);
	if(isLexical(prod) || isRegular(prod)) {
		args = flatten(args);
		list[int] chars = [ ch | Tree arg <- args, char(int ch) := arg ];
		if(size(args) == size(chars)) return toInt(stringChars(chars));
	}
	throw "parse error: Tree -\> int";
}

public str parseToStr(Tree::appl(Production prod, list[Tree] args)) {
	println(prod);
	if(isLexical(prod) || isRegular(prod)) {
		args = flatten(args);
		list[int] chars = [ ch | Tree arg <- args, char(int ch) := arg ];
		if(size(args) == size(chars)) return stringChars(chars);
	}
	throw "parse error: Tree -\> str";
}

public bool parseToBool(Tree::appl(Production prod, list[Tree] args)) {
	println(prod);
	args = filterTrees(args);
	if(isOptional(prod)) return isEmpty(args);
	if(isLexical(prod) || isRegular(prod)) {
		args = flatten(args);
		list[int] chars = [ ch | Tree arg <- args, char(int ch) := arg ];
		if(size(args) == size(chars)) return fromString(stringChars(chars));
	}
	throw "parse error: Tree -\> bool";
}

@javaClass{org.rascalmpl.library.Prelude}
@reflect{}
public java &T makeAdtNode(type[&T] \type, str label, value vals...);

@javaClass{org.rascalmpl.library.Prelude}
@reflect{}
public java &T makeTupleNode(type[&T] \type, value vals...);
			
@doc{A bunch of helper functions}          
public list[Tree] filterTrees(list[Tree] args) 
	= [ arg | Tree arg <- args, !( isLayouts(arg) || isLiteral(arg) || isEmpty(arg) )];
	
public list[Tree] flatten(list[Tree] args)
	= [ *( (appl(Production prod, list[Tree] args0) := arg && isRegular(prod)) ? flatten(args0) 
		: [ arg ] ) | Tree arg <- args ];
			               
public Option[str] getLabel(prod(Symbol def, list[Symbol] _, set[Attr] attributes)) {
	if(!(\bracket() in attributes)) return getLabel(def);
	return none();
}
public default Option[str] getLabel(Production prod) = none();

public Option[str] getLabel(\label(str name, Symbol _)) = some(name);
public default Option[str] getLabel(Symbol _) = none();

public bool isLiteral(appl(Production prod, list[Tree] _)) = isLiteral(prod);
public bool isLiteral(Tree _) = false;

public bool isLayouts(appl(Production prod, list[Tree] _)) = isLayouts(prod);
public bool isLayouts(Tree _) = false;

public bool isEmpty(appl(Production prod, list[Tree] _)) = isEmpty(prod);
public bool isEmpty(Tree _) = false;
	
public bool isLiteral(prod(Symbol def, list[Symbol] _, set[Attr] _)) = isLiteral(def);
public default bool isLiteral(Production _) = false;

public bool isLayouts(prod(Symbol def, list[Symbol] _, set[Attr] _)) = isLayouts(def);
public default bool isLayouts(Production _) = false;

public bool isEmpty(prod(Symbol def, list[Symbol] _, set[Attr] _)) = isEmpty(def);
public default bool isEmpty(Production _) = false;

public bool isRegular(regular(Symbol _)) = true;
public default bool isRegular(Production prod) = false;

public bool isLexical(prod(Symbol def, list[Symbol] _, set[Attr] _)) = isLexical(def);
public default bool isLexical(Production prod) = false;
 
public bool isLiteral(Symbol sym) = ( (\lit(str _) := sym) || (\cilit(str _) := sym) ) ? true : false;
public bool isLayouts(Symbol sym) = (\layouts(str _) := sym) ? true : false;
public bool isEmpty(Symbol sym) = (\empty() := sym) ? true : false;

public bool isLexical(Symbol sym) = (\lex(str _) := sym) ? true : false;

public bool isRegularLists(regular(Symbol def)) = isRegularLists(def);
public default bool isRegularLists(Production prod) = false;

public bool isRegularLists(Symbol sym) = ( (\iter(Symbol _) := sym)   
     										|| (\iter-star(Symbol _) := sym)   
     										|| (\iter-seps(Symbol _, list[Symbol] _) := sym)   
     										|| (\iter-star-seps(Symbol _, list[Symbol] _) := sym) ) ? true : false;

public bool isOptional(prod(Symbol def, list[Symbol] _, set[Attr] _)) = isOptional(def);
public bool isOptional(regular(Symbol def)) = isOptional(def);
public default bool isOptional(Production _) = false;

public bool isOptional(Symbol sym) = (\opt(Symbol _) := sym) ? true : false;