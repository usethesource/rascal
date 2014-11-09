@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Anastasia Izmaylova - Anastasia.Izmaylova@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::AbstractType

import Set;
import String;
extend ParseTree;

import lang::rascal::types::AbstractName;
import lang::rascal::\syntax::Rascal;

@doc{Annotation for parameterized types indicating whether the bound was explicitly given.}
public anno bool Symbol@boundGiven;

@doc{Extension to add new types used internally during name resolution and checking.}
public data Symbol =
	  \user(RName rname, list[Symbol] parameters)
	| failure(set[Message] messages)
	| \inferred(int uniqueId)
	| \overloaded(set[Symbol] overloads, set[Symbol] defaults)
	| deferred(Symbol givenType)
	;

@doc{Extension to add a production type.}
public data Symbol = \prod(Symbol \sort, str name, list[Symbol] parameters, set[Attr] attributes);

@doc{Annotations to hold the type assigned to a tree.}
public anno Symbol Tree@rtype;

@doc{Annotations to hold the location at which a type is declared.}
public anno loc Symbol@at; 

@doc{Pretty printer for Rascal abstract types.}
public str prettyPrintType(Symbol::\int()) = "int";
public str prettyPrintType(Symbol::\bool()) = "bool";
public str prettyPrintType(Symbol::\real()) = "real";
public str prettyPrintType(Symbol::\rat()) = "rat";
public str prettyPrintType(Symbol::\str()) = "str";
public str prettyPrintType(Symbol::\num()) = "num";
public str prettyPrintType(Symbol::\node()) = "node";
public str prettyPrintType(Symbol::\void()) = "void";
public str prettyPrintType(Symbol::\value()) = "value";
public str prettyPrintType(Symbol::\loc()) = "loc";
public str prettyPrintType(Symbol::\datetime()) = "datetime";
public str prettyPrintType(Symbol::\label(str s, Symbol t)) = "<prettyPrintType(t)> <s>";
public str prettyPrintType(Symbol::\parameter(str pn, Symbol t)) = "&<pn> \<: <prettyPrintType(t)>";
public str prettyPrintType(Symbol::\set(Symbol t)) = "set[<prettyPrintType(t)>]";
public str prettyPrintType(Symbol::\rel(list[Symbol] ts)) = "rel[<intercalate(", ", [ prettyPrintType(t) | t <- ts ])>]";
public str prettyPrintType(Symbol::\lrel(list[Symbol] ts)) = "lrel[<intercalate(", ", [ prettyPrintType(t) | t <- ts ])>]";
public str prettyPrintType(Symbol::\tuple(list[Symbol] ts)) = "tuple[<intercalate(", ", [ prettyPrintType(t) | t <- ts ])>]";
public str prettyPrintType(Symbol::\list(Symbol t)) = "list[<prettyPrintType(t)>]";
public str prettyPrintType(Symbol::\map(Symbol d, Symbol r)) = "map[<prettyPrintType(d)>, <prettyPrintType(r)>]";
public str prettyPrintType(Symbol::\bag(Symbol t)) = "bag[<prettyPrintType(t)>]";
public str prettyPrintType(Symbol::\adt(str s, list[Symbol] ps)) = s when size(ps) == 0;
public str prettyPrintType(Symbol::\adt(str s, list[Symbol] ps)) = "<s>[<intercalate(", ", [ prettyPrintType(p) | p <- ps ])>]" when size(ps) > 0;
public str prettyPrintType(Symbol::\cons(Symbol a, str name, list[Symbol] fs)) = "<prettyPrintType(a)> <name> : (<intercalate(", ", [ prettyPrintType(f) | f <- fs ])>)";
public str prettyPrintType(Symbol::\alias(str s, list[Symbol] ps, Symbol t)) = "alias <s> = <prettyPrintType(t)>" when size(ps) == 0;
public str prettyPrintType(Symbol::\alias(str s, list[Symbol] ps, Symbol t)) = "alias <s>[<intercalate(", ", [ prettyPrintType(p) | p <- ps ])>] = <prettyPrintType(t)>" when size(ps) > 0;
public str prettyPrintType(Symbol::\func(Symbol rt, list[Symbol] ps)) = "fun <prettyPrintType(rt)>(<intercalate(", ", [ prettyPrintType(p) | p <- ps])>)";
public str prettyPrintType(Symbol::\var-func(Symbol rt, list[Symbol] ps, Symbol va)) = "fun <prettyPrintType(rt)>(<intercalate(", ", [ prettyPrintType(p) | p <- ps+va])>...)";
public str prettyPrintType(Symbol::\reified(Symbol t)) = "type[<prettyPrintType(t)>]";
public str prettyPrintType(Symbol::\user(RName rn, list[Symbol] ps)) = "<prettyPrintName(rn)>[<intercalate(", ", [ prettyPrintType(p) | p <- ps ])>]";
public str prettyPrintType(Symbol::failure(set[Message] ms)) = "fail"; // TODO: Add more detail?
public str prettyPrintType(Symbol::\inferred(int n)) = "inferred(<n>)";
public str prettyPrintType(Symbol::\overloaded(set[Symbol] os, set[Symbol] defaults)) = "overloaded:\n\t\t<intercalate("\n\t\t",[prettyPrintType(\o) | \o <- os + defaults])>";
public str prettyPrintType(Symbol::deferred(Symbol givenType)) = "deferred(<prettyPrintType(givenType)>)";
// named non-terminal symbols
public str prettyPrintType(Symbol::\sort(str name)) = name;
public str prettyPrintType(Symbol::\start(Symbol s)) = "start[<prettyPrintType(s)>]";
public str prettyPrintType(Symbol::\prod(Symbol s, str name, list[Symbol] fs, set[Attr] atrs)) = "<prettyPrintType(s)> <name> : (<intercalate(", ", [ prettyPrintType(f) | f <- fs ])>)";
public str prettyPrintType(Symbol::\lex(str name)) = name;
public str prettyPrintType(Symbol::\layouts(str name)) = name;
public str prettyPrintType(Symbol::\keywords(str name)) = name;
public str prettyPrintType(Symbol::\parameterized-sort(str name, list[Symbol] parameters)) = name when size(parameters) == 0;
public str prettyPrintType(Symbol::\parameterized-sort(str name, list[Symbol] parameters)) = "<name>[<intercalate(", ", [ prettyPrintType(p) | p <- parameters ])>]" when size(parameters) > 0;
public str prettyPrintType(Symbol::\parameterized-lex(str name, list[Symbol] parameters)) = name when size(parameters) == 0;
public str prettyPrintType(Symbol::\parameterized-lex(str name, list[Symbol] parameters)) = "<name>[<intercalate(", ", [ prettyPrintType(p) | p <- parameters ])>]" when size(parameters) > 0;
// terminal symbols
public str prettyPrintType(Symbol::\lit(str string)) = string;
public str prettyPrintType(Symbol::\cilit(str string)) = string;
public str prettyPrintType(Symbol::\char-class(list[CharRange] ranges)) = "[<intercalate(" ", [ "<r.begin>-<r.end>" | r <- ranges ])>]";
// regular symbols
public str prettyPrintType(Symbol::\empty()) = "()";
public str prettyPrintType(Symbol::\opt(Symbol symbol)) = "<prettyPrintType(symbol)>?";
public str prettyPrintType(Symbol::\iter(Symbol symbol)) = "<prettyPrintType(symbol)>+";
public str prettyPrintType(Symbol::\iter-star(Symbol symbol)) = "<prettyPrintType(symbol)>*";
public str prettyPrintType(Symbol::\iter-seps(Symbol symbol, list[Symbol] separators)) = "{<prettyPrintType(symbol)> <intercalate(" ", [ prettyPrintType(sep) | sep <- separators ])>}+";
public str prettyPrintType(Symbol::\iter-star-seps(Symbol symbol, list[Symbol] separators)) = "{<prettyPrintType(symbol)> <intercalate(" ", [ prettyPrintType(sep) | sep <- separators ])>}*";
public str prettyPrintType(Symbol::\alt(set[Symbol] alternatives)) = "( <intercalate(" | ", [ prettyPrintType(a) | a <- alternatives ])> )" when size(alternatives) > 1;
public str prettyPrintType(Symbol::\seq(list[Symbol] sequence)) = "( <intercalate(" ", [ prettyPrintType(a) | a <- sequence ])> )" when size(sequence) > 1;
public str prettyPrintType(Symbol::\conditional(Symbol symbol, set[Condition] conditions)) = "<prettyPrintType(symbol)> { <intercalate(" ", [ prettyPrintCond(cond) | cond <- conditions ])> }";
//public default str prettyPrintType(Symbol s) = "<type(s,())>";

private str prettyPrintCond(Condition::\follow(Symbol symbol)) = "\>\> <prettyPrintType(symbol)>";
private str prettyPrintCond(Condition::\not-follow(Symbol symbol)) = "!\>\> <prettyPrintType(symbol)>";
private str prettyPrintCond(Condition::\precede(Symbol symbol)) = "<prettyPrintType(symbol)> \<\<";
private str prettyPrintCond(Condition::\not-precede(Symbol symbol)) = "<prettyPrintType(symbol)> !\<\<";
private str prettyPrintCond(Condition::\delete(Symbol symbol)) = "???";
private str prettyPrintCond(Condition::\at-column(int column)) = "@<column>";
private str prettyPrintCond(Condition::\begin-of-line()) = "^";
private str prettyPrintCond(Condition::\end-of-line()) = "$";
private str prettyPrintCond(Condition::\except(str label)) = "!<label>";

@doc{Create a new int type.}
public Symbol makeIntType() = Symbol::\int();

@doc{Create a new bool type.}
public Symbol makeBoolType() = Symbol::\bool();

@doc{Create a new real type.}
public Symbol makeRealType() = Symbol::\real();

@doc{Create a new rat type.}
public Symbol makeRatType() = Symbol::\rat();

@doc{Create a new str type.}
public Symbol makeStrType() = Symbol::\str();

@doc{Create a new num type.}
public Symbol makeNumType() = Symbol::\num();

@doc{Create a new node type.}
public Symbol makeNodeType() = Symbol::\node();

@doc{Create a new void type.}
public Symbol makeVoidType() = Symbol::\void();

@doc{Create a new value type.}
public Symbol makeValueType() = Symbol::\value();

@doc{Create a new loc type.}
public Symbol makeLocType() = Symbol::\loc();

@doc{Create a new datetime type.}
public Symbol makeDateTimeType() = Symbol::\datetime();

@doc{Create a new set type, given the element type of the set.}
public Symbol makeSetType(Symbol elementType) = Symbol::\set(elementType);

@doc{Create a new rel type, given the element types of the fields. Check any given labels for consistency.}
public Symbol makeRelType(Symbol elementTypes...) {
	set[str] labels = { l | Symbol::\label(l,_) <- elementTypes };
	if (size(labels) == 0 || size(labels) == size(elementTypes)) 
		return \rel(elementTypes);
	else
		throw "For rel types, either all fields much be given a distinct label or no fields should be labeled."; 
}

@doc{Create a new rel type based on a given tuple type.}
public Symbol makeRelTypeFromTuple(Symbol t) = Symbol::\rel(getTupleFields(t));

@doc{Create a new list rel type, given the element types of the fields. Check any given labels for consistency.}
public Symbol makeListRelType(Symbol elementTypes...) {
	set[str] labels = { l | Symbol::\label(l,_) <- elementTypes };
	if (size(labels) == 0 || size(labels) == size(elementTypes)) 
		return Symbol::\lrel(elementTypes);
	else
		throw "For lrel types, either all fields much be given a distinct label or no fields should be labeled."; 
}

@doc{Create a new lrel type based on a given tuple type.}
public Symbol makeListRelTypeFromTuple(Symbol t) = Symbol::\lrel(getTupleFields(t));

@doc{Create a new tuple type, given the element types of the fields. Check any given labels for consistency.}
public Symbol makeTupleType(Symbol elementTypes...) {
	set[str] labels = { l | Symbol::\label(l,_) <- elementTypes };
	if (size(labels) == 0 || size(labels) == size(elementTypes)) 
		return \tuple(elementTypes);
	else
		throw "For tuple types, either all fields much be given a distinct label or no fields should be labeled."; 
}

@doc{Create a new list type, given the element type of the list.}
public Symbol makeListType(Symbol elementType) = Symbol::\list(elementType);

@doc{Create a new map type, given the types of the domain and range. Check to make sure field names are used consistently.}
public Symbol makeMapType(Symbol domain, Symbol range) {
	if (\label(l1,t1) := domain && Symbol::\label(l2,t2) := range && l1 != l2)
		return Symbol::\map(domain,range);
	else if (\label(l1,t1) := domain && Symbol::\label(l2,t2) := range && l1 == l2)
		throw "The field names of the map domain and range must be distinct.";
	else if (Symbol::\label(l1,t1) := domain)
		return Symbol::\map(t1,range);
	else if (Symbol::\label(l2,t2) := range)
		return Symbol::\map(domain,t2);
	else
		return Symbol::\map(domain,range);
}

@doc{Create a new map type based on the given tuple.}
public Symbol makeMapTypeFromTuple(Symbol t) {
	list[Symbol] tf = getTupleFields(t);
	if (size(tf) != 2)
		throw "The provided tuple must have exactly 2 fields, one for the map domain and one for the range.";
	return makeMapType(tf[0],tf[1]);
}

@doc{Create a new bag type, given the element type of the bag.}
public Symbol makeBagType(Symbol elementType) = Symbol::\bag(elementType);

@doc{Create a new ADT type with the given name.}
public Symbol makeADTType(str n) = Symbol::\adt(n,[]);

@doc{Create a new parameterized ADT type with the given type parameters}
public Symbol makeParameterizedADTType(str n, Symbol p...) = Symbol::\adt(n,p);

@doc{Create a new constructor type.}
public Symbol makeConstructorType(Symbol adtType, str name, Symbol consArgs...) {    
	set[str] labels = { l | Symbol::\label(l,_) <- consArgs };
	if (size(labels) == 0 || size(labels) == size(consArgs)) 
		return Symbol::\cons(adtType, name, consArgs);
	else
		throw "For constructor types, either all arguments much be given a distinct label or no parameters should be labeled."; 
}

@doc{Create a new constructor type based on the contents of a tuple.}
public Symbol makeConstructorTypeFromTuple(Symbol adtType, str name, Symbol consArgs) {    
    return makeConstructorType(adtType, name, getTupleFields(consArgs)); 
}

@doc{Create a new alias type with the given name and aliased type.}
public Symbol makeAliasType(str n, Symbol t) = Symbol::\alias(n,[],t);

@doc{Create a new parameterized alias type with the given name, aliased type, and parameters.}
public Symbol makeParameterizedAliasType(str n, Symbol t, list[Symbol] params) = Symbol::\alias(n,params,t);

@doc{Marks if a function is a var-args function.}
public anno bool Symbol@isVarArgs;

@doc{Create a new function type with the given return and parameter types.}
public Symbol makeFunctionType(Symbol retType, bool isVarArgs, Symbol paramTypes...) {
	set[str] labels = { l | Symbol::\label(l,_) <- paramTypes };
	if (size(labels) == 0 || size(labels) == size(paramTypes))
		//if (isVarArgs) { 
		//	return \var-func(retType, head(paramTypes,size(paramTypes)-1), last(paramTypes));
		//} else {
			return Symbol::\func(retType, paramTypes)[@isVarArgs=isVarArgs];
		//}
	else
		throw "For function types, either all parameters much be given a distinct label or no parameters should be labeled."; 
}

@doc{Create a new function type with parameters based on the given tuple.}
public Symbol makeFunctionTypeFromTuple(Symbol retType, bool isVarArgs, Symbol paramTypeTuple) { 
	return makeFunctionType(retType, isVarArgs, getTupleFields(paramTypeTuple));
}

@doc{Create a type representing the reified form of the given type.}
public Symbol makeReifiedType(Symbol mainType) = Symbol::\reified(mainType);

@doc{Create a type representing a type parameter (type variable).}
public Symbol makeTypeVar(str varName) = Symbol::\parameter(varName, Symbol::\value())[@boundGiven=false];

@doc{Create a type representing a type parameter (type variable) and bound.}
public Symbol makeTypeVarWithBound(str varName, Symbol varBound) = Symbol::\parameter(varName, varBound)[@boundGiven=true];

@doc{Unwraps aliases, parameters, and labels from around a type.}
public Symbol unwrapType(Symbol::\alias(_,_,at)) = unwrapType(at);
public Symbol unwrapType(Symbol::\parameter(_,tvb)) = unwrapType(tvb);
public Symbol unwrapType(Symbol::\label(_,ltype)) = unwrapType(ltype);
public Symbol unwrapType(Symbol::\conditional(sym,_)) = unwrapType(sym);
public default Symbol unwrapType(Symbol t) = t;

@doc{Get the type that has been reified and stored in the reified type.}
public Symbol getReifiedType(Symbol t) {
    if (Symbol::\reified(rt) := unwrapType(t)) return rt;
    throw "getReifiedType given unexpected type: <prettyPrintType(t)>";
}

@doc{Get the type of the relation fields as a tuple.}
public Symbol getRelElementType(Symbol t) {
    if (Symbol::\rel(ets) := unwrapType(t)) return Symbol::\tuple(ets);
    throw "Error: Cannot get relation element type from type <prettyPrintType(t)>";
}

@doc{Get whether the rel has field names or not.}
public bool relHasFieldNames(Symbol t) {
    if (\rel(tls) := \unwrapType(t)) return size(tls) == size([ti | ti:\label(_,_) <- tls]);
    throw "relHasFieldNames given non-Relation type <prettyPrintType(t)>";
}

@doc{Get the field names of the rel fields.}
public list[str] getRelFieldNames(Symbol t) {
    if (\rel(tls) := unwrapType(t) && relHasFieldNames(t)) return [ l | \label(l,_) <- tls ];
    if (\rel(_) := unwrapType(t)) throw "getRelFieldNames given rel type without field names: <prettyPrintType(t)>";        
    throw "getRelFieldNames given non-Relation type <prettyPrintType(t)>";
}

@doc{Get the fields of a relation.}
public list[Symbol] getRelFields(Symbol t) {
    if (\rel(tls) := unwrapType(t)) return tls;
    throw "getRelFields given non-Relation type <prettyPrintType(t)>";
}

@doc{Get the type of the list relation fields as a tuple.}
public Symbol getListRelElementType(Symbol t) {
    if (\lrel(ets) := unwrapType(t)) return \tuple(ets);
    throw "Error: Cannot get list relation element type from type <prettyPrintType(t)>";
}

@doc{Get whether the list rel has field names or not.}
public bool listRelHasFieldNames(Symbol t) {
    if (\lrel(tls) := \unwrapType(t)) return size(tls) == size([ti | ti:\label(_,_) <- tls]);
    throw "listRelHasFieldNames given non-List-Relation type <prettyPrintType(t)>";
}

@doc{Get the field names of the list rel fields.}
public list[str] getListRelFieldNames(Symbol t) {
    if (\lrel(tls) := unwrapType(t) && listRelHasFieldNames(t)) return [ l | \label(l,_) <- tls ];
    if (\lrel(_) := unwrapType(t)) throw "getListRelFieldNames given lrel type without field names: <prettyPrintType(t)>";        
    throw "getListRelFieldNames given non-List-Relation type <prettyPrintType(t)>";
}

@doc{Get the fields of a list relation.}
public list[Symbol] getListRelFields(Symbol t) {
    if (\lrel(tls) := unwrapType(t)) return tls;
    throw "getListRelFields given non-List-Relation type <prettyPrintType(t)>";
}

@doc{Get the name of a type variable.}
public str getTypeVarName(Symbol t) {
	if (\alias(_,_,Symbol at) := t) return getTypeVarName(at);
	if (\label(_,Symbol lt) := t) return getTypeVarName(lt);
    if (\parameter(tvn,_) := t) return tvn;
    throw "getTypeVarName given unexpected type: <prettyPrintType(t)>";
}

@doc{Get the bound of a type variable.}
public Symbol getTypeVarBound(Symbol t) {
	if (\alias(_,_,Symbol at) := t) return getTypeVarBound(at);
	if (\label(_,Symbol lt) := t) return getTypeVarBound(lt);
    if (\parameter(_,tvb) := t) return tvb;
    throw "getTypeVarBound given unexpected type: <prettyPrintType(t)>";
}

@doc{Get all the type variables inside a given type.}
public set[Symbol] collectTypeVars(Symbol t) {
    return { rt | / Symbol rt : \parameter(_,_) := t };
}

@doc{Provide an initial type map from the variables in the type to void.}
public map[str,Symbol] initializeTypeVarMap(Symbol t) {
    set[Symbol] rt = collectTypeVars(t);
    return ( getTypeVarName(tv) : makeVoidType() | tv <- rt );
}

@doc{See if a type contains any type variables.}
public bool typeContainsTypeVars(Symbol t) = size(collectTypeVars(t)) > 0;

@doc{Return the names of all type variables in the given type.}
public set[str] typeVarNames(Symbol t) {
    return { tvn | \parameter(tvn,_) <- collectTypeVars(t) };
}

//
// Instantiate type variables based on a var name to type mapping.
//
// NOTE: We assume that bounds have already been checked, so this should not violate the bounds
// given on bounded type variables.
//
// NOTE: Commented out for now. Unfortunately, the visit could change some things that we normally
// would not change, so we need to instead do this using standard recursion. It is now in SubTypes
// along with the functionality which finds the mappings.
//public RType instantiateVars(map[RName,RType] varMappings, RType rt) {
//  return visit(rt) {
//      case RTypeVar(RFreeTypeVar(n)) : if (n in varMappings) insert(varMappings[n]);
//      case RTypeVar(RBoundTypeVar(n,_)) : if (n in varMappings) insert(varMappings[n]);   
//  };
//}

@doc{Get a list of arguments for the function.}
public list[Symbol] getFunctionArgumentTypes(Symbol ft) {
    if (Symbol::\func(_, ats) := unwrapType(ft)) return ats;
    throw "Cannot get function arguments from non-function type <prettyPrintType(ft)>";
}

@doc{Get the arguments for a function in the form of a tuple.}
public Symbol getFunctionArgumentTypesAsTuple(Symbol ft) {
    if (Symbol::\func(_, ats) := unwrapType(ft)) return \tuple(ats);
    throw "Cannot get function arguments from non-function type <prettyPrintType(ft)>";
}

@doc{Get the return type for a function.}
public Symbol getFunctionReturnType(Symbol ft) {
    if (Symbol::\func(rt, _) := unwrapType(ft)) return rt; 
    throw "Cannot get function return type from non-function type <prettyPrintType(ft)>";
}

@doc{Indicate if the given tuple has a field of the given name.}
public bool tupleHasField(Symbol t, str fn) {
	return \tuple(tas) := unwrapType(t) && fn in { l | \label(l,_) <- tas } ;
}

@doc{Indicate if the given tuple has a field with the given field offset.}
public bool tupleHasField(Symbol t, int fn) {
	return \tuple(tas) := unwrapType(t) && 0 <= fn && fn < size(tas);
}

@doc{Get the type of the tuple field with the given name.}
public Symbol getTupleFieldType(Symbol t, str fn) {
	if (\tuple(tas) := unwrapType(t)) {
		fieldmap = ( l : ltype | \label(l,ltype) <- tas );
		if (fn in fieldmap) return fieldmap[fn];
		throw "Tuple <prettyPrintType(t)> does not have field <fn>";
	}
    throw "getTupleFieldType given unexpected type <prettyPrintType(t)>";
}

@doc{Get the type of the tuple field at the given offset.}
public Symbol getTupleFieldType(Symbol t, int fn) {
	if (\tuple(tas) := unwrapType(t)) {
		if (0 <= fn && fn < size(tas)) return unwrapType(tas[fn]);
		throw "Tuple <prettyPrintType(t)> does not have field <fn>";
	}
    throw "getTupleFieldType given unexpected type <prettyPrintType(t)>";
}

@doc{Get the types of the tuple fields, with labels removed}
public list[Symbol] getTupleFieldTypes(Symbol t) {
	if (\tuple(tas) := unwrapType(t))
		return [ (\label(_,v) := li) ? v : li | li <- tas ];
    throw "Cannot get tuple field types from type <prettyPrintType(t)>"; 
}

@doc{Get the fields of a tuple as a list.}
public list[Symbol] getTupleFields(Symbol t) {
	if (\tuple(tas) := unwrapType(t)) return tas;
    throw "Cannot get tuple fields from type <prettyPrintType(t)>"; 
}

@doc{Get the number of fields in a tuple.}
public int getTupleFieldCount(Symbol t) = size(getTupleFields(t));

@doc{Does this tuple have field names?}
public bool tupleHasFieldNames(Symbol t) {
	if (\tuple(tas) := unwrapType(t)) return size(tas) == size({ti|ti:\label(_,_) <- tas});
    throw "tupleHasFieldNames given non-Tuple type <prettyPrintType(t)>";
}

@doc{Get the names of the tuple fields.}
public list[str] getTupleFieldNames(Symbol t) {
    if (\tuple(tls) := unwrapType(t)) {
        if (tupleHasFieldNames(t)) {
            return [ l | \label(l,_) <- tls ];
        }
        throw "getTupleFieldNames given tuple type without field names: <prettyPrintType(t)>";        
    }
    throw "getTupleFieldNames given non-Tuple type <prettyPrintType(t)>";
}

@doc{Get the name of the tuple field at the given offset.}
public str getTupleFieldName(Symbol t, int idx) {
    list[str] names = getTupleFieldNames(t);
    if (0 <= idx && idx < size(names)) return names[idx];
    throw "getTupleFieldName given invalid index <idx>";
}

@doc{Get the element type of a set.}
public Symbol getSetElementType(Symbol t) {
    if (\set(et) := unwrapType(t)) return et;
    if (\rel(ets) := unwrapType(t)) return \tuple(ets);
    throw "Error: Cannot get set element type from type <prettyPrintType(t)>";
}

@doc{Get the element type of a bag.}
public Symbol getBagElementType(Symbol t) {
    if (\bag(et) := unwrapType(t)) return et;
    throw "Error: Cannot get set element type from type <prettyPrintType(t)>";
}

@doc{Get the domain and range of the map as a tuple.}
public Symbol getMapFieldsAsTuple(Symbol t) {
    if (\map(dt,rt) := unwrapType(t)) return \tuple([dt,rt]);
    throw "getMapFieldsAsTuple called with unexpected type <prettyPrintType(t)>";
}       

@doc{Check to see if a map defines a field (by name).}
public bool mapHasField(Symbol t, str fn) = tupleHasField(getMapFieldsAsTuple(t),fn);

@doc{Check to see if a map defines a field (by index).}
public bool mapHasField(Symbol t, int fn) = tupleHasField(getMapFieldsAsTuple(t),fn);

@doc{Return the type of a field defined on a map (by name).}
public Symbol getMapFieldType(Symbol t, str fn) = getTupleFieldType(getMapFieldsAsTuple(t),fn);

@doc{Return the type of a field defined on a map (by index).}
public Symbol getMapFieldType(Symbol t, int fn) = getTupleFieldType(getMapFieldsAsTuple(t),fn);

@doc{Get the fields in a map as a list of fields.}
public list[Symbol] getMapFields(Symbol t) = getTupleFields(getMapFieldsAsTuple(t));

@doc{Check to see if the map has field names.}
public bool mapHasFieldNames(Symbol t) = tupleHasFieldNames(getMapFieldsAsTuple(t));

@doc{Get the field names from the map fields.}
public tuple[str domainName, str rangeName] getMapFieldNames(Symbol t) {
	if (mapHasFieldNames(t),[\label(l1,_),\label(l2,_)] := getMapFields(t)) {
		return < l1, l2 >;
	}
    throw "getMapFieldNames given map type without field names: <prettyPrintType(t)>";        
}

@doc{Get the field name for the field at a specific index.}
public str getMapFieldName(Symbol t, int idx) = getMapFieldNames(t)[idx];

@doc{Get the domain type of the map.}    
public Symbol getMapDomainType(Symbol t) = unwrapType(getMapFields(t)[0]);

@doc{Get the range type of the map.}
public Symbol getMapRangeType(Symbol t) = unwrapType(getMapFields(t)[1]);

@doc{Get a list of the argument types in a constructor.}
public list[Symbol] getConstructorArgumentTypes(Symbol ct) {
	if (Symbol::\cons(_,_,cts) := unwrapType(ct)) return cts;
    throw "Cannot get constructor arguments from non-constructor type <prettyPrintType(ct)>";
}

@doc{Get a tuple with the argument types as the fields.}
public Symbol getConstructorArgumentTypesAsTuple(Symbol ct) {
	return \tuple(getConstructorArgumentTypes(ct));
}

@doc{Get the ADT type of the constructor.}
public Symbol getConstructorResultType(Symbol ct) {
	if (Symbol::\cons(a,_,_) := unwrapType(ct)) return a;
    throw "Cannot get constructor ADT type from non-constructor type <prettyPrintType(ct)>";
}

@doc{Get the element type of a list.}
public Symbol getListElementType(Symbol t) {
	if (\list(et) := unwrapType(t)) return et;
    if (\lrel(ets) := unwrapType(t)) return \tuple(ets);	
	throw "Error: Cannot get list element type from type <prettyPrintType(t)>";
}

@doc{Get the name of the ADT.}
public str getADTName(Symbol t) {
	if (\adt(n,_) := unwrapType(t)) return n;
	if (Symbol::\cons(a,_,_) := unwrapType(t)) return getADTName(a);
	if (\reified(_) := unwrapType(t)) return "type";
    throw "getADTName, invalid type given: <prettyPrintType(t)>";
}

@doc{Get the type parameters of an ADT.}
public list[Symbol] getADTTypeParameters(Symbol t) {
	if (\adt(n,ps) := unwrapType(t)) return ps;
	if (Symbol::\cons(a,_,_) := unwrapType(t)) return getADTTypeParameters(a);
	if (\reified(_) := unwrapType(t)) return [];
    throw "getADTTypeParameters given non-ADT type <prettyPrintType(t)>";
}

@doc{Return whether the ADT has type parameters.}
public bool adtHasTypeParameters(Symbol t) = size(getADTTypeParameters(t)) > 0;

@doc{Get the name of a user type.}
public str getUserTypeName(Symbol ut) {
    if (\user(x,_) := unwrapType(ut)) return prettyPrintName(x);
    throw "Cannot get user type name from non user type <prettyPrintType(ut)>";
} 

@doc{Get the type parameters from a user type.}
public list[Symbol] getUserTypeParameters(Symbol ut) {
    if (\user(_,ps) := unwrapType(ut)) return ps;
    throw "Cannot get type parameters from non user type <prettyPrintType(ut)>";
}

@doc{Does this user type have type parameters?}
public bool userTypeHasParameters(Symbol ut) = size(getUserTypeParameters(ut)) > 0;

@doc{Get the name of the type alias.}
public str getAliasName(Symbol t) {
	if (\alias(x,_,_) := t) return x;
	throw "Cannot get the alias name from non alias type <prettyPrintType(t)>";
}

@doc{Get the aliased type of the type alias.}
public Symbol getAliasedType(Symbol t) {
	if (\alias(_,_,at) := t) return at;
	throw "Cannot get the aliased type from non alias type <prettyPrintType(t)>";
}

@doc{Get the type parameters for the alias.}
public list[Symbol] getAliasTypeParameters(Symbol t) {
    if (\alias(_,ps,_) := t) return ps;
    throw "getAliasTypeParameters given non-alias type <prettyPrintType(t)>";
}

@doc{Does this alias have type parameters?}
public bool aliasHasTypeParameters(Symbol t) = size(getAliasTypeParameters(t)) > 0;

@doc{Unwind any aliases inside a type.}
public Symbol unwindAliases(Symbol t) {
    solve(t) {
    	t = visit(t) { case \alias(tl,ps,tr) => tr };
    }
    return t;
}

@doc{Is the provided type a failure type?}
public bool isFailType(failure(_)) = true;
public default bool isFailType(Symbol _) = false;

@doc{Construct a new fail type with the given message and error location.}
public Symbol makeFailType(str s, loc l) = failure({error(s,l)});

@doc{Construct a new fail type with the given message and error location.}
public Symbol makeFailTypeAsWarning(str s, loc l) = failure({warning(s,l)});

@doc{Get the failure messages out of the type.}
public set[Message] getFailures(failure(set[Message] ms)) = ms;

@doc{Extend a failure type with new failure messages.}
public Symbol extendFailType(failure(set[Message] ms), set[Message] msp) = failure(ms + msp);
public default Symbol extendFailType(Symbol t) { 	
	throw "Cannot extend a non-failure type with failure information, type <prettyPrintType(t)>";
}

@doc{Collapse a set of failure types into a single failure type with all the failures included.} 
public Symbol collapseFailTypes(set[Symbol] rt) = failure({ s | failure(ss) <- rt, s <- ss }); 

@doc{Is this type an inferred type?}
public bool isInferredType(\inferred(_)) = true;
public default bool isInferredType(Symbol _) = false;

@doc{Does this type have an inferred type?}
public bool hasInferredType(Symbol \type) = (/\inferred(_) := \type);

@doc{Construct a new inferred type.}
public Symbol makeInferredType(int n) = \inferred(n);

@doc{Get the numeric identifier for the inferred type.}
public int getInferredTypeIndex(Symbol t) {
	if (\inferred(n) := t) return n;
	throw "Error: Cannot get inferred type index from non-inferred type <prettyPrintType(t)>";
}

@doc{Is this type an overloaded type?}
public bool isOverloadedType(\overloaded(_,_)) = true;
public default bool isOverloadedType(Symbol _) = false;

@doc{Get the non-default overloads stored inside the overloaded type.}
public set[Symbol] getNonDefaultOverloadOptions(Symbol t) {
	if (\overloaded(s,_) := t) return s;
	throw "Error: Cannot get non-default overloaded options from non-overloaded type <prettyPrintType(t)>";
}

@doc{Get the default overloads stored inside the overloaded type.}
public set[Symbol] getDefaultOverloadOptions(Symbol t) {
	if (\overloaded(_,defaults) := t) return defaults;
	throw "Error: Cannot get default overloaded options from non-overloaded type <prettyPrintType(t)>";
}

@doc{Construct a new overloaded type.}
public Symbol makeOverloadedType(set[Symbol] options, set[Symbol] defaults) {
	options  = { *( (\overloaded(opts,_) := optItem) ? opts : { optItem } ) | optItem <- options };
	defaults = defaults + { *( (\overloaded(_,opts) := optItem) ? opts : {} ) | optItem <- options };
	return \overloaded(options,defaults);
}

@doc{Ensure that sets of tuples are treated as relations.}
public Symbol \set(Symbol t) = \rel(getTupleFields(t)) when isTupleType(t);

@doc{Ensure that lists of tuples are treated as list relations.}
public Symbol \list(Symbol t) = \lrel(getTupleFields(t)) when isTupleType(t);

@doc{Calculate the lub of a list of types.}
public Symbol lubList(list[Symbol] ts) {
	Symbol theLub = Symbol::\void();
	for (t <- ts) theLub = lub(theLub,t);
	return theLub;
}

@doc{Is this type a non-container type?}
public bool isElementType(Symbol t) = 
	isIntType(t) || isBoolType(t) || isRealType(t) || isRatType(t) || isStrType(t) || 
	isNumType(t) || isNodeType(t) || isVoidType(t) || isValueType(t) || isLocType(t) || 
	isDateTimeType(t) || isTupleType(t) || isADTType(t) || isConstructorType(t) ||
	isFunctionType(t) || isReifiedType(t) || isNonTerminalType(t);

@doc{Is this type a container type?}
public bool isContainerType(Symbol t) =
	isSetType(t) || isListType(t) || isMapType(t) || isBagType(t);
	
@doc{Synopsis: Determine if the given type is a nonterminal.}
public bool isNonTerminalType(\alias(_,_,Symbol at)) = isNonTerminalType(at);
public bool isNonTerminalType(\parameter(_,Symbol tvb)) = isNonTerminalType(tvb);
public bool isNonTerminalType(\label(_,Symbol lt)) = isNonTerminalType(lt);
public bool isNonTerminalType(Symbol::\start(Symbol ss)) = isNonTerminalType(ss);
public bool isNonTerminalType(Symbol::\conditional(Symbol ss,_)) = isNonTerminalType(ss);
public bool isNonTerminalType(Symbol::\sort(_)) = true;
public bool isNonTerminalType(Symbol::\lex(_)) = true;
public bool isNonTerminalType(Symbol::\layouts(_)) = true;
public bool isNonTerminalType(Symbol::\keywords(_)) = true;
public bool isNonTerminalType(Symbol::\parameterized-sort(_,_)) = true;
public bool isNonTerminalType(Symbol::\parameterized-lex(_,_)) = true;
public bool isNonTerminalType(Symbol::\iter(_)) = true;
public bool isNonTerminalType(Symbol::\iter-star(_)) = true;
public bool isNonTerminalType(Symbol::\iter-seps(_,_)) = true;
public bool isNonTerminalType(Symbol::\iter-star-seps(_,_)) = true;
public bool isNonTerminalType(Symbol::\empty()) = true;
public bool isNonTerminalType(Symbol::\opt(_)) = true;
public bool isNonTerminalType(Symbol::\alt(_)) = true;
public bool isNonTerminalType(Symbol::\seq(_)) = true;

public default bool isNonTerminalType(Symbol _) = false;	

public bool isNonTerminalIterType(\alias(_,_,Symbol at)) = isNonTerminalIterType(at);
public bool isNonTerminalIterType(\parameter(_,Symbol tvb)) = isNonTerminalIterType(tvb);
public bool isNonTerminalIterType(\label(_,Symbol lt)) = isNonTerminalIterType(lt);
public bool isNonTerminalIterType(Symbol::\iter(_)) = true;
public bool isNonTerminalIterType(Symbol::\iter-star(_)) = true;
public bool isNonTerminalIterType(Symbol::\iter-seps(_,_)) = true;
public bool isNonTerminalIterType(Symbol::\iter-star-seps(_,_)) = true;
public default bool isNonTerminalIterType(Symbol _) = false;	

public Symbol getNonTerminalIterElement(\alias(_,_,Symbol at)) = getNonTerminalIterElement(at);
public Symbol getNonTerminalIterElement(\parameter(_,Symbol tvb)) = getNonTerminalIterElement(tvb);
public Symbol getNonTerminalIterElement(\label(_,Symbol lt)) = getNonTerminalIterElement(lt);
public Symbol getNonTerminalIterElement(Symbol::\iter(Symbol i)) = i;
public Symbol getNonTerminalIterElement(Symbol::\iter-star(Symbol i)) = i;
public Symbol getNonTerminalIterElement(Symbol::\iter-seps(Symbol i,_)) = i;
public Symbol getNonTerminalIterElement(Symbol::\iter-star-seps(Symbol i,_)) = i;
public default Symbol getNonTerminalIterElement(Symbol i) {
	throw "<prettyPrintType(i)> is not an iterable non-terminal type";
}	

public bool isStartNonTerminalType(\alias(_,_,Symbol at)) = isNonTerminalType(at);
public bool isStartNonTerminalType(\parameter(_,Symbol tvb)) = isNonTerminalType(tvb);
public bool isStartNonTerminalType(\label(_,Symbol lt)) = isNonTerminalType(lt);
public bool isStartNonTerminalType(Symbol::\start(_)) = true;
public default bool isStartNonTerminalType(Symbol _) = false;    

public Symbol getStartNonTerminalType(\alias(_,_,Symbol at)) = getStartNonTerminalType(at);
public Symbol getStartNonTerminalType(\parameter(_,Symbol tvb)) = getStartNonTerminalType(tvb);
public Symbol getStartNonTerminalType(\label(_,Symbol lt)) = getStartNonTerminalType(lt);
public Symbol getStartNonTerminalType(Symbol::\start(Symbol s)) = s;
public default Symbol getStartNonTerminalType(Symbol s) {
	throw "<prettyPrintType(s)> is not a start non-terminal type";
}

@doc{Get the name of the nonterminal.}
public str getNonTerminalName(\alias(_,_,Symbol at)) = getNonTerminalName(at);
public str getNonTerminalName(\parameter(_,Symbol tvb)) = getNonTerminalName(tvb);
public str getNonTerminalName(\label(_,Symbol lt)) = getNonTerminalName(lt);
public str getNonTerminalName(Symbol::\start(Symbol ss)) = getNonTerminalName(ss);
public str getNonTerminalName(Symbol::\sort(str n)) = n;
public str getNonTerminalName(Symbol::\lex(str n)) = n;
public str getNonTerminalName(Symbol::\layouts(str n)) = n;
public str getNonTerminalName(Symbol::\keywords(str n)) = n;
public str getNonTerminalName(Symbol::\parameterized-sort(str n,_)) = n;
public str getNonTerminalName(Symbol::\parameterized-lex(str n,_)) = n;
public str getNonTerminalName(Symbol::\iter(Symbol ss)) = getNonTerminalName(ss);
public str getNonTerminalName(Symbol::\iter-star(Symbol ss)) = getNonTerminalName(ss);
public str getNonTerminalName(Symbol::\iter-seps(Symbol ss,_)) = getNonTerminalName(ss);
public str getNonTerminalName(Symbol::\iter-star-seps(Symbol ss,_)) = getNonTerminalName(ss);
public str getNonTerminalName(Symbol::\opt(Symbol ss)) = getNonTerminalName(ss);
public str getNonTerminalName(Symbol::\conditional(Symbol ss,_)) = getNonTerminalName(ss);
public default str getNonTerminalName(Symbol s) { throw "Invalid nonterminal passed to getNonTerminalName: <s>"; }

//@doc{Check to see if the type allows fields.}
//public bool nonTerminalAllowsFields(\alias(_,_,Symbol at)) = nonTerminalAllowsFields(at);
//public bool nonTerminalAllowsFields(\parameter(_,Symbol tvb)) = nonTerminalAllowsFields(tvb);
//public bool nonTerminalAllowsFields(\label(_,Symbol lt)) = nonTerminalAllowsFields(lt);
//public bool nonTerminalAllowsFields(Symbol::\start(Symbol ss)) = true;
//public bool nonTerminalAllowsFields(Symbol::\sort(str n)) = true;
//public bool nonTerminalAllowsFields(Symbol::\lex(str n)) = true;
//public bool nonTerminalAllowsFields(Symbol::\parameterized-sort(str n,_)) = true;
//public bool nonTerminalAllowsFields(Symbol::\parameterized-lex(str n,_)) = true;
//public bool nonTerminalAllowsFields(Symbol::\opt(Symbol ss)) = true;
//public bool nonTerminalAllowsFields(Symbol::\conditional(Symbol ss,_)) = nonTerminalAllowsFields(ss);
//public default bool nonTerminalAllowsFields(Symbol s) = false;

@doc{Get the type parameters of a nonterminal.}
//public list[Symbol] getNonTerminalTypeParameters(Symbol t) = [ rt | / Symbol rt : \parameter(_,_) := t ];
public list[Symbol] getNonTerminalTypeParameters(Symbol t) {
	t = unwrapType(t);
	if (Symbol::\parameterized-sort(n,ps) := t) return ps;
	if (Symbol::\parameterized-lex(n,ps) := t) return ps;
	if (Symbol::\start(s) := t) return getNonTerminalTypeParameters(s);
	if (Symbol::\iter(s) := t) return getNonTerminalTypeParameters(s);
	if (Symbol::\iter-star(s) := t) return getNonTerminalTypeParameters(s);
	if (Symbol::\iter-seps(s,_) := t) return getNonTerminalTypeParameters(s);
	if (Symbol::\iter-star-seps(s,_) := t) return getNonTerminalTypeParameters(s);
	if (Symbol::\opt(s) := t) return getNonTerminalTypeParameters(s);
	if (Symbol::\conditional(s,_) := t) return getNonTerminalTypeParameters(s);
	if (Symbol::\prod(s,_,_,_) := t) return getNonTerminalTypeParameters(s);
    return [ ];
}

public Symbol provideNonTerminalTypeParameters(Symbol t, list[Symbol] ps) {
	// Note: this function assumes the length is proper -- that we are replacing
	// a list of params with a list of types that is the same length. The caller
	// needs to check this.
	
	t = unwrapType(t);
	
	if (Symbol::\parameterized-sort(n,ts) := t) return t[parameters=ps];
	if (Symbol::\parameterized-lex(n,ts) := t) return t[parameters=ps];
	if (Symbol::\start(s) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
	if (Symbol::\iter(s) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
	if (Symbol::\iter-star(s) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
	if (Symbol::\iter-seps(s,_) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
	if (Symbol::\iter-star-seps(s,_) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
	if (Symbol::\opt(s) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
	if (Symbol::\conditional(s,_) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
	if (Symbol::\prod(s,_,_,_) := t) return t[def=provideNonTerminalTypeParameters(s,ps)];
    return t;
}

@doc{Synopsis: Determine if the given type is a production.}
public bool isProductionType(\alias(_,_,Symbol at)) = isProductionType(at);
public bool isProductionType(\parameter(_,Symbol tvb)) = isProductionType(tvb);
public bool isProductionType(\label(_,Symbol lt)) = isProductionType(lt);
public bool isProductionType(Symbol::\prod(_,_,_,_)) = true;
public default bool isProductionType(Symbol _) = false;	

public Symbol removeConditional(conditional(Symbol s, set[Condition] _)) = s;
public Symbol removeConditional(label(str lab, conditional(Symbol s, set[Condition] _)))
  = label(lab, s);
public default Symbol removeConditional(Symbol s) = s;

@doc{Get a list of the argument types in a production.}
public list[Symbol] getProductionArgumentTypes(Symbol pr) {
	if (Symbol::\prod(_,_,ps,_) := unwrapType(pr)) {
		return [ removeConditional(psi) | psi <- ps, isNonTerminalType(psi) ] ;
	}
    throw "Cannot get production arguments from non-production type <prettyPrintType(pr)>";
}

@doc{Get a tuple with the argument types as the fields.}
public Symbol getProductionArgumentTypesAsTuple(Symbol pr) {
	return \tuple(getProductionArgumentTypes(pr));
}

@doc{Get the sort type of the production.}
public Symbol getProductionSortType(Symbol pr) {
	if (Symbol::\prod(s,_,_,_) := unwrapType(pr)) return s;
    throw "Cannot get production sort type from non-production type <prettyPrintType(pr)>";
}

public bool hasDeferredTypes(Symbol t) = size({d | /d:deferred(_) := t}) > 0;

public bool subtype(Symbol::deferred(Symbol t), Symbol s) = subtype(t,s);
public bool subtype(Symbol t, Symbol::deferred(Symbol s)) = subtype(t,s); 
public bool subtype(Symbol t, Symbol::\adt("Tree",[])) = true when isNonTerminalType(t);
public bool subtype(Symbol t, Symbol::\node()) = true when isNonTerminalType(t);
// TODO: Do we also want to consider the separator?
public bool subtype(Symbol::\iter-seps(Symbol s, _), Symbol::\iter-star-seps(Symbol t, _)) = subtype(s,t);
