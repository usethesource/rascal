module experiments::Compiler::Rascal2muRascal::TypeReifier

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;

import ParseTree;
import Map;

import IO;

private map[str, Symbol] typeMap = ();
private rel[Symbol, Symbol] constructors = {};
private rel[Symbol, Symbol] productions = {};

public type[value] symbolToValue(Symbol symbol, Configuration config) {
	
	// Collect all the types that are in the type environment
	typeMap = ( getSimpleName(rname) : config.store[config.typeEnv[rname]].rtype | rname <- config.typeEnv );
	// Collect all the constructors of the adt types in the type environment
	types = range(typeMap);
	constructors = { <\type.\adt, \type> | int uid <- config.store, 
												constructor(_, Symbol \type, _, _) := config.store[uid],
												\type.\adt in types };
	// Collects all the productions of the non-terminal types in the type environment									
   	productions = { <\type.\sort, \type> | int uid <- config.store, 
   	  											production(_, Symbol \type, _, _) := config.store[uid],
   	  											\type.\sort in types };
	// Recursively collects all the type definitions associated with a given symbol
 	map[Symbol,Production] definitions = reify(symbol, ());
 	return type(symbol,definitions);
}

// primitive
public map[Symbol,Production] reify(Symbol symbol, Configuration config, map[Symbol,Production] definitions) 
	= definitions when isIntType(symbol) || isBoolType(symbol) || isRealType(symbol) || isRatType(symbol) ||
					   isStrType(symbol) || isNumType(symbol) || isNodeType(symbol) || isVoidType(symbol) ||
					   isValueType(symbol) || isLocType(symbol) || isDateTimeType(symbol);
// labeled					   
public map[Symbol,Production] reify(Symbol::\label(str name, Symbol symbol), map[Symbol,Production] definitions)
	= reify(symbol, definitions);
// set
public map[Symbol,Production] reify(Symbol::\set(Symbol symbol), map[Symbol,Production] definitions)
	= reify(symbol, definitions);
// rel
public map[Symbol,Production] reify(Symbol::\rel(list[Symbol] symbols), map[Symbol,Production] definitions)
	= ( definitions | reify(sym, it) | sym <- symbols );
// list
public map[Symbol,Production] reify(Symbol::\list(Symbol symbol), map[Symbol,Production] definitions)
	= reify(symbol, definitions);
// lrel
public map[Symbol,Production] reify(Symbol::\lrel(list[Symbol] symbols), map[Symbol,Production] definitions)
	= ( definitions | reify(sym, it) | sym <- symbols );
// bag
public map[Symbol,Production] reify(Symbol::\bag(Symbol symbol), map[Symbol,Production] definitions)
	= reify(symbol, definitions);
// tuple
public map[Symbol,Production] reify(Symbol::\tuple(list[Symbol] symbols), map[Symbol,Production] definitions)
	= ( definitions | reify(sym, it) | sym <- symbols );
// map
public map[Symbol,Production] reify(Symbol::\map(Symbol from, Symbol to), map[Symbol,Production] definitions)
	= reify(from, definitions) + reify(to, definitions);
// adt
public map[Symbol,Production] reify(Symbol::\adt(str name, list[Symbol] symbols), map[Symbol,Production] definitions) {
	Symbol adtDef = typeMap[name];
	if(!definitions[adtDef]?) {
		alts = { sym2prod(sym) | sym <- constructors[adtDef] };
		definitions[adtDef] = Production::\choice(adtDef, alts);
		definitions = ( definitions | reify(sym, it) | sym <- constructors[adtDef] );
	}
	definitions = ( definitions | reify(sym, it) | sym <- symbols );
	return definitions;
}
// constructors
public map[Symbol,Production] reify(Symbol::\cons(Symbol \adt, str name, list[Symbol] parameters), map[Symbol,Production] definitions) {
	// adt has been already added to the definitions
	definitions = ( definitions | reify(sym, it) | sym <- parameters );
	return definitions;
}
// alias
public map[Symbol,Production] reify(Symbol::\alias(str name, list[Symbol] parameters, Symbol aliased), map[Symbol,Production] definitions) {
	definitions = reify(aliased, definitions);
	definitions = ( definitions | reify(sym, it) | sym <- parameters );
	return definitions;
}
// function
public map[Symbol,Production] reify(Symbol::\func(Symbol ret, list[Symbol] parameters), map[Symbol,Production] definitions) {
	definitions = reify(ret, definitions);
	definitions = ( definitions | reify(sym, it) | sym <- parameters );
	return definitions;
}
// function with varargs
public map[Symbol,Production] reify(Symbol::\var-func(Symbol ret, list[Symbol] parameters, Symbol varArg), map[Symbol,Production] definitions) {
	definitions = reify(ret, definitions);
	definitions = ( definitions | reify(sym, it) | sym <- parameters );
	definitions = reify(varArg, definitions);
	return definitions;
}
// reified
public map[Symbol,Production] reify(Symbol::\reified(Symbol symbol), map[Symbol,Production] definitions) 
	= reify(ret, definitions);
// parameter
public map[Symbol,Production] reify(Symbol::\parameter(str name, Symbol bound), map[Symbol,Production] definitions)
	= reify(bound, definitions);
		   
public default map[Symbol,Production] reify(Symbol symbol, map[Symbol,Production] definitions) = definitions;


private Production sym2prod(Symbol::\cons(Symbol \type, str name, list[Symbol] parameters)) 
	= Production::\cons(Symbol::label(name, \type), parameters, {}) 
		when Symbol::\adt(str _, list[Symbol] _) := \type;
private Production sym2prod(Symbol::\prod(Symbol \type, str name, list[Symbol] parameters, set[Attr] attributes))
	= Production::\prod(Symbol::\label(name, \type), parameters, attributes) 
		when name != "" && \type has name;
default Production sym2prod(Symbol s) { throw "Could not transform the symbol <s> to a Production node"; }
