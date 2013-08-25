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
 	symbol = (Symbol::conditional(Symbol sym,_) := symbol) ? sym : symbol;
 	if(Symbol::\sort(_):= symbol || Symbol::\lex(_):= symbol ||
 		Symbol::\parameterized-sort(_,_):= symbol || Symbol::\parameterized-lex(_,_):= symbol) {
 			if(str n <- typeMap, Symbol::\layouts(_) := typeMap[n]) {
 				definitions = reify(typeMap[n],definitions);
 			}
 			definitions = definitions + (Symbol::\layouts("$default$"):Production::choice(Symbol::\layouts("$default$"),{Production::prod(Symbol::\layouts("$default$"),[],{})}));
 			definitions = definitions + (Symbol::\empty():Production::choice(Symbol::\empty(),{Production::prod(Symbol::\empty(),[],{})}));
 	}
 	return type(symbol, definitions);
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
	assert Symbol::\adt(name,_) := adtDef;
	if(!definitions[adtDef]?) {
		alts = { sym2prod(sym) | sym <- constructors[adtDef] };
		definitions[adtDef] = Production::\choice(adtDef, alts);
		definitions = ( definitions | reify(sym, it) | sym <- constructors[adtDef] );
	}
	definitions = ( definitions | reify(sym, it) | sym <- symbols );
	return definitions;
}
// constructors
public map[Symbol,Production] reify(Symbol::\cons(Symbol \adt, str name, list[Symbol] parameters), map[Symbol,Production] definitions)
	// adt has been already added to the definitions
	= ( definitions | reify(sym, it) | sym <- parameters );
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
	
// sort, lex
public map[Symbol,Production] reify(Symbol symbol, map[Symbol,Production] definitions) 
	= { 
		Symbol nonterminal = typeMap[name]; 
		assert !(Symbol::\adt(name,_) := nonterminal);
		if(!definitions[nonterminal]?) {
			alts = { sym2prod(sym) | sym <- productions[nonterminal] };
			definitions[nonterminal] = Production::\choice(nonterminal, alts);
			definitions = ( definitions | reify(sym, it) | sym <- productions[nonterminal] );
		}
		definitions;
	  } when Symbol::\sort(str name) := symbol || Symbol::\lex(str name) := symbol ||
	  		 Symbol::\layouts(str name) := symbol || Symbol::\keywords(str name) := symbol;
// parameterized-sort, parameterized-lex  
public map[Symbol,Production] reify(Symbol symbol, map[Symbol,Production] definitions) 
	= { 
		Symbol nonterminal = typeMap[name]; 
		assert !(Symbol::\adt(name,_) := nonterminal);
		if(!definitions[nonterminal]?) {
			alts = { sym2prod(sym) | sym <- productions[nonterminal] };
			definitions[nonterminal] = Production::\choice(nonterminal, alts);
			definitions = ( definitions | reify(sym, it) | sym <- productions[nonterminal] );
		}
		definitions = ( definitions | reify(sym, it) | sym <- parameters );
		definitions;
	  } when Symbol::\parameterized-sort(str name, list[Symbol] parameters) := symbol || Symbol::\parameterized-lex(str name, list[Symbol] parameters) := symbol;

public map[Symbol,Production] reify(Symbol symbol, map[Symbol,Production] definitions)
	= reify(sym, definitions)
		when Symbol::\opt(Symbol sym) := symbol || Symbol::\iter(Symbol sym) := symbol ||
			 Symbol::\iter-star(Symbol sym) := symbol || Symbol::\iter-seps(Symbol sym, _) := symbol ||
			 Symbol::\iter-star-seps(Symbol sym, list[Symbol] _) := symbol;

public map[Symbol,Production] reify(Symbol::\alt(set[Symbol] alternatives), map[Symbol,Production] definitions)
	= ( definitions | reify(sym, it) | sym <- alternatives );

public map[Symbol,Production] reify(Symbol::\seq(list[Symbol] symbols), map[Symbol,Production] definitions)
	= ( definitions | reify(sym, it) | sym <- symbols );

public map[Symbol,Production] reify(Symbol::\conditional(Symbol symbol, set[Condition] conditions), map[Symbol,Production] definitions)
	= reify(symbol, definitions) + ( definitions | reify(cond, it) | cond <- conditions );
	
public map[Symbol,Production] reify(Symbol::\prod(Symbol \sort, str name, list[Symbol] parameters, set[Attr] _), map[Symbol,Production] definitions)
	// sort has been already added to the definitions
	= ( definitions | reify(sym, it) | sym <- parameters );
	
public map[Symbol,Production] reify(Condition cond, map[Symbol,Production] definitions)
	= reify(symbol, definitions)
		when Condition::\follow(Symbol symbol) := cond || Condition::\not-follow(Symbol symbol) := cond ||
			 Condition::\precede(Symbol symbol) := cond || Condition::\not-precede(Symbol symbol) := cond ||
			 Condition::\delete(Symbol symbol) := cond;

public map[Symbol,Production] reify(Condition cond, map[Symbol,Production] definitions) = definitions;
		   
public default map[Symbol,Production] reify(Symbol symbol, map[Symbol,Production] definitions) = definitions;


private Production sym2prod(Symbol::\cons(Symbol \type, str name, list[Symbol] parameters)) 
	= Production::\cons(Symbol::label(name, \type), parameters, {}) 
		when Symbol::\adt(str _, list[Symbol] _) := \type;
private Production sym2prod(Symbol::\prod(Symbol \type, str name, list[Symbol] parameters, set[Attr] attributes))
	= Production::\prod(Symbol::\label(name, \type), parameters, attributes) 
		when name != "" && \type has name;
private Production sym2prod(Symbol::\prod(Symbol \type, str name, list[Symbol] parameters, set[Attr] attributes))
	= Production::\prod(\type, parameters, attributes) 
		when name == "" && \type has name;
default Production sym2prod(Symbol s) { throw "Could not transform the symbol <s> to a Production node"; }
