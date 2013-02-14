module TreeParser

import Boolean;
import List;
import ParseTree;
import Set;
import String;


data Option[&T] = some(&T opt) | none();

public &T parseToAdt(type[&T] \type, Tree::appl(Production prod, list[Tree] args)) {
	args = filterTrees(flatten(args));
	if(some(str l) := getLabel(prod)) return makeAdtNode(\type, l, args);
	if(none() := getLabel(prod) && size(args == 1)) return parseToAdt(\type, args[0]); // tries to parse to the same type
	throw "parse error: Tree -\> Adt";
}

public list[tuple[Tree, Tree]] parseToList(Tree::appl(Production prod, list[Tree] args)) {
	args = filterTrees(flatten(args));
	if(isRegularLists(prod)) return (!isEmpty(args)) ? [<args[0], appl(prod, tail(args))>] : [];
	if(isOptional(prod)) return (!isEmpty(args)) ? [<args[0], appl(prod, tail(args))>] : [];
	throw "parse error: Tree -\> list[&T]";
}

public set[tuple[Tree, Tree]] parseToSet(Tree::appl(Production prod, list[Tree] args)) {
	args = filterTrees(flatten(args));
	if(isRegularLists(prod)) return (!isEmpty(args)) ? {<args[0], appl(prod, tail(args))>} : {};
	if(isOptional(prod)) return (!isEmpty(args)) ? {<args[0], appl(prod, tail(args))>} : {};
	throw "parse error: Tree -\> set[&T]";
}

public &T parseToTuple(type[&T] \type, Tree::appl(Production prod, list[Tree] args)) {
	args = filterTrees(flatten(args));
	if(none() := getLabel(prod)) return makeTupleNode(\type, args);
	throw "parse error: Tree -\> tuple[...]";
}

public int parseToInt(Tree::appl(Production prod, list[Tree] args)) {
	args = filterTrees(flatten(args));
	list[int] chars = [ ch | Tree arg <- args, chat(int ch) := arg ];
	if(size(args) == size(chars)) return toInt(stringChars(chars));
	throw "parse error: Tree -\> int";
}

public int parseToStr(Tree::appl(Production prod, list[Tree] args)) {
	args = filterTrees(flatten(args));
	list[int] chars = [ ch | Tree arg <- args, chat(int ch) := arg ];
	if(size(args) == size(chars)) return stringChars(chars);
	throw "parse error: Tree -\> str";
}

public bool parseToBool(Tree::appl(Production prod, list[Tree] args)) {
	args = filterTrees(flatten(args));
	if(isOptional(prod)) return isEmpty(args);
	list[int] chars = [ ch | Tree arg <- args, chat(int ch) := arg ];
	if(size(args) == size(chars)) return fromString(stringChars(chars));
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
	= [ arg | Tree arg <- args, !(isLayouts(arg) || isLiteral(arg) || isEmpty(arg))];
	
	public list[Tree] flatten(list[Tree] args)
	= [ *( isRegular(prod) ? flatten(as) 
		: ( (none() := getLabel(prod) && size(as) == 1) ? flatten(as) 
		: [ arg ] ) ) | Tree arg <- args, 
						appl(Production prod, list[Trees] as) := arg ];
			               
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
 
public bool isLiteral(Symbol sym) = ( (\lit(str _) := sym) || (\cilit(str _) := sym) ) ? true : false;
public bool isLayouts(Symbol sym) = (\layouts(str _) := sym) ? true : false;
public bool isEmpty(Symbol sym) = (\empty() := sym) ? true : false;

public bool isRegularLists(prod(Symbol def, list[Symbol] _, set[Attr] _)) = isRegularLists(def);
public default bool isRegularLists(Production prod) = false;

public bool isRegularLists(Symbol sym) = ( (\iter(Symbol _) := sym)   
     										|| (\iter-star(Symbol _) := sym)   
     										|| (\iter-seps(Symbol _, list[Symbol] _) := sym)   
     										|| (\iter-star-seps(Symbol _, list[Symbol] _) := sym) ) ? true : false;

public bool isOptional(prod(Symbol def, list[Symbol] _, set[Attr] _)) = isOptional(def);
public default bool isOptional(Production _) = false;

public bool isOptional(Symbol sym) = (\opt(Symbol _) := sym) ? true : false;
