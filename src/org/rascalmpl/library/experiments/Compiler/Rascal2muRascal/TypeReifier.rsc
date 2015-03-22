module experiments::Compiler::Rascal2muRascal::TypeReifier

/*
 * This module handles the mapping between types and reified types. It defines the following functions:
 *	   (1) void 				  resetTypeReifier()			Reset the global state of this module
 *     (2) map[Symbol,Production] getGrammar() 					Extract only syntax definitions
  *    (3) map[Symbol,Production] getDefinitions() 				Extract all defined symbols
 *     (4) type[value]            symbolToValue(Symbol) 		Compute the reified type for a symbol
 */

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;

import experiments::Compiler::Rascal2muRascal::TypeUtils;

import lang::rascal::grammar::definition::Symbols;

import ParseTree;
import List;
import Map;
import Set;
import Relation;
import Type;

import IO;

private rel[str,Symbol] typeRel = {};
private set[Symbol] types = {};
private rel[Symbol,Symbol] constructors = {};
private rel[Symbol,Symbol] productions = {};
private map[Symbol,Production] grammar = ();
private set[Symbol] starts = {};
private Symbol activeLayout = Symbol::\layouts("$default$");
private rel[value,value] reachableTypes = {};

public void resetTypeReifier() {
    typeRel = {};
    types = {};
    constructors = {};
    productions = {};
    grammar = ();
    starts = {};
    activeLayout = Symbol::\layouts("$default$");
    reachableTypes = {};
}

// Extract common declaration info and save it for later use by
// - getGrammar
// - symbolToValue

public void getDeclarationInfo(Configuration config){
    resetTypeReifier();
    
    // Collect all the types that are in the type environment
    // TODO: simplify
	typeRel = { < getSimpleName(rname), config.store[config.typeEnv[rname]].rtype > | rname <- config.typeEnv, config.store[config.typeEnv[rname]] has rtype }
	        + { < getSimpleName(rname) , rtype > | int uid <- config.store, sorttype(rname,rtype,_,_) := config.store[uid] }
            + { < getSimpleName(config.store[uid].name), config.store[uid].rtype > | int uid <- config.store, config.store[uid] has name, config.store[uid] has rtype }
            + { <"Tree", adt("Tree",[])> }
            + { <"Symbol", adt("Symbol",[])> }
            + { <"Production", adt("Production",[])> }
            + { <"Attr", adt("Attr",[])> }
            + { <"Associativity", adt("Associativity",[])> }
            + { <"CharRange", adt("CharRange",[])> }
            + { <"Condition", adt("Condition",[])> }
            ;
    

	// Collect all the constructors of the adt types in the type environment
	
	constructors = { <\type.\adt, \type> | int uid <- config.store, 
												constructor(_, Symbol \type, _, _, _) := config.store[uid]
												//\type.\adt in types 
												};

	//constructors += <#Tree.symbol, type(Tree, ())>;
	
    typeRel += { <cns[1].adt.name, cns[1].adt> | cns <- constructors };
    
    types = range(typeRel);
    
	// Collect all the productions of the non-terminal types in the type environment
    
    grammar = ( config.store[uid].rtype : config.grammar[uid] | int uid <- config.grammar, config.store[uid].rtype in types );
   
    productions = { p.def is label ? <p.def.symbol, Symbol::prod(p.def.symbol, p.def.name, p.symbols, p.attributes)>  //TODO: p.def.name???
	                               : <p.def, Symbol::prod(p.def, "", p.symbols, p.attributes)> 						      // TODO ""??
	              | /Production p:prod(_,_,_) := grammar 
	              };
   	starts = { config.store[uid].rtype | int uid <- config.starts, config.store[uid].rtype in types };
   	
   	activeLayouts = { \type | \type <- types, Symbol::layouts(_) := \type };
   	if(!isEmpty(activeLayouts)) {
   		activeLayout = getOneFrom(activeLayouts);
   	}
   	computeReachableTypesAndConstructors();
}

// Extract the declared grammar from a type checker configuration

public map[Symbol,Production] getGrammar() {
	map[Symbol,Production] definitions =   
		( nonterminal : \layouts(grammar[nonterminal]) | nonterminal <- grammar ) 
		+ 
		( Symbol::\start(nonterminal) : \layouts(Production::choice(Symbol::\start(nonterminal), { Production::prod(Symbol::\start(nonterminal), [ Symbol::\label("top", nonterminal) ],{}) })) 
		| nonterminal <- starts );
	if(<str n, Symbol def> <- typeRel, Symbol::\layouts(_) := def) {
 		definitions = reify(def,definitions);
 	}
 	definitions = definitions + (Symbol::\layouts("$default$"):Production::choice(Symbol::\layouts("$default$"),{Production::prod(Symbol::\layouts("$default$"),[],{})}));
 	definitions = definitions + (Symbol::\empty():Production::choice(Symbol::\empty(),{Production::prod(Symbol::\empty(),[],{})}));
 	
 	return definitions;
}

// Extract all declared symbols from a type checker configuration

public map[Symbol,Production] getDefinitions() {
   	// Collect all symbols
   	set[Symbol] symbols = types + carrier(constructors) + carrier(productions) + domain(grammar);
   	
   	map[Symbol,Production] definitions  = (() | reify(symbol, it) | Symbol symbol <- symbols);
 	
 	return definitions;
}

public Production getLabeledProduction(str name, Symbol symbol){
	//println("getLabeledProduction: <getGrammar()[symbol]>");
	name = unescape(name);
	visit(getGrammar()[symbol]){
		case p:prod(\label(name, symbol), _, _): return p;
		case p:regular(\label(name, symbol)): return p;
	};
	throw "No LabeledProduction for <name>, <symbol>";
}

// Type reachability functions

rel[Symbol, Symbol] dependentSymbolsInProduction(Symbol from, Symbol sym) {
	switch(sym){
		case label(_, s):	return /*{<from, sym>} + */dependentSymbolsInProduction(sym, s);
		case conditional(sym, _):
							return {<from, sym>} + dependentSymbolsInProduction(sym, s);
		case adt(_,_):		return {<from, sym>};
		case sort(_):		return {<from, sym>};
		case lex(_):		return {<from, sym>};
		case \iter(s):		return {<from, sym>};
		case \iter-star(s):	return {<from, sym>};
		case \iter-seps(s, seps):
							return {<from, sym>, <sym, s>};
		case \iter-star-seps(s, seps):
							return {<from, sym>, <sym, s>};
		case \parameterized-sort(str name, list[Symbol] parameters):
							return {<from, sym>} + { <from, striprec(param)> | param <- parameters };
		case \parameterized-lex(str name, list[Symbol] parameters):
							return {<from, sym>} + { <from, striprec(param)> | param <- parameters };
		default:			return {};
	};
}	

rel[Symbol, Symbol] getProductionDependencies(Symbol s, p: Production::prod(Symbol def, list[Symbol] symbols, set[Attr] attributes)) {
	res = { *dependentSymbolsInProduction(strip(def), strip(sym)) | sym <- symbols };
	if(strip(s) != strip(def)){
		res += <strip(s), strip(def)>;
	}
	return res;
}

rel[Symbol, Symbol] dependentSymbols(Symbol from, Symbol sym) {
	switch(sym){
		case label(_, s):	return /*{<from, sym>} + */dependentSymbols(sym, s);
		
		case adt(_,_):		return {<from, sym>};
		case sort(_):		return {<from, sym>};
		case lex(_):		return {<from, sym>};
		case \opt(s):		return {<from, sym>} + dependentSymbols(from, s);
		case \iter(s):		return {<from, sym>} + dependentSymbols(from, s);
		case \iter-star(s):	return {<from, sym>} + dependentSymbols(from, s);
		case \iter-seps(s, seps):
							return {<from, sym>} + dependentSymbols(from, s);
		case \iter-star-seps(s, seps):
							return {<from, sym>} + dependentSymbols(from, s);
		case \alt(set[Symbol] syms):
							return {<from, sym>} + { *dependentSymbols(from, s) | s <- syms};
		case \seq(set[Symbol] syms):
							return {<from, sym>} + { *dependentSymbols(from, s) | s <- syms};
		case conditional(sym, _):
							return {<from, sym>} + dependentSymbols(from, s);										
		case \parameterized-sort(str name, list[Symbol] parameters):
							return {<from, sym>} + { <from, striprec(param)> | param <- parameters };
		case \parameterized-lex(str name, list[Symbol] parameters):
							return {<from, sym>} + { <from, striprec(param)> | param <- parameters };
		
		default:			return {<from, sym>};
	};
}	

bool isExpandableSymbol(Symbol sym){
	switch(sym){
		case adt(_,_):		return true;
		case sort(_):		return true;
		case lex(_):		return true;
		case \iter(s):		return isExpandableSymbol(s);
		case \iter-star(s):	return isExpandableSymbol(s);
		case \iter-seps(s, seps):
							return isExpandableSymbol(s);
		case \iter-star-seps(s, seps):
							return isExpandableSymbol(s);
		case \parameterized-sort(str name, list[Symbol] parameters):
							return true;
		case \parameterized-lex(str name, list[Symbol] parameters):
							return true;
		case \value():		return true;
		default:			return false;
	};
}			

rel[Symbol, Symbol] getConstructorDependencies(Symbol from, c:Symbol::\cons(Symbol \adtsym, str name, list[Symbol] parameters)) {
	from = strip(from);
	res = { *dependentSymbols(from, sym) | /Symbol sym := parameters };
	if(from != strip(\adtsym)){
		res += <from, strip(\adtsym)>;
	}
	return res;
}

// Extract the reachability relation for all types. This information can be used by generated code for
// - descendant match
// - visit

private void computeReachableTypesAndConstructors(){
	return;
	definitions = getDefinitions();
	rel[value,value] cleaned_productions = { <strip(def), p> | /Production p:prod(def,_,_) := grammar };
	
	stripped_constructors = {<strip(s1),strip(s2)> | <s1, s2> <- constructors};
	rel[value,value] containment = stripped_constructors
								   + {*getConstructorDependencies(s, c) |  <s, c> <- constructors}
	                               + cleaned_productions
	                               + {*getProductionDependencies(s, p) |  <s, p> <- cleaned_productions};
	
	//println("containment [<size(containment)>] ="); for(elm <- containment) { println("\t<elm>"); }
	
	reachableTypes = containment+;
	reachableTypes = top-down-break visit(reachableTypes){ 
		case prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) 
						=> prod(def, intermix(symbols, activeLayout), attributes)
		case Symbol s 	=> regulars(s, activeLayout) 
	 };
	 //println("reachableTypes [<size(reachableTypes)>] ="); for(elm <- reachableTypes) { println("\t<elm>"); }
}

// Extract the reachable types for a given type
// Note: at runtime RascalPrimitive.$should_descent_in_value takes care of
// - list, set, map, tuple
// - regular, parameterized-lex, parameterized-sort
// - label
// so these are not needed in the generated descent_into set

set[value] getReachableTypes(Symbol subjectType, set[str] consNames, set[Symbol] patternTypes, bool concreteMatch){
	return {};
	println("getReachableTypes: <subjectType>, <consNames>, <patternTypes>, <concreteMatch>");
	
	if(concreteMatch){
		return getReachableConcreteTypes(subjectType, consNames, patternTypes);
	}
	desiredPatternTypes = { s | /Symbol s := patternTypes};
	desiredSubjectTypes = { s | /Symbol s := subjectType};
	desiredTypes = desiredSubjectTypes + desiredPatternTypes;
	if(any(sym <- desiredTypes, sort(_) := sym || lex(_) := sym)){
		desiredTypes += adt("Tree", []);
	}
	println("desiredTypes = <desiredTypes>");
	
	set[value] initial_types = 
		desiredTypes + (subjectType == \value()
					   ? { sym | sym <- carrier(reachableTypes),						
					  			 r := reachableTypes[sym],
					  			 size(consNames) != 0 ||
					  			 \value() in r ||
					  			 !isEmpty(desiredTypes & r) }
					   		     
	                   : { sym | stp <- desiredSubjectTypes, 						// We are looking for
	                             Symbol sym <- reachableTypes[stp],  				// all symbols that are reachable from the desiredSubjectTypes
	                             //isExpandableSymbol(sym), 							// that are expandable (or value)
	                             r := reachableTypes[sym], 							// and 
	                             size(consNames) != 0 || 							// either specific constructors have been given (and we are a bit permissive here)
	                             \value() in r || 									// or value can be reached,
	                             !isEmpty(desiredTypes & r)}						// or one or more desiredPatternTypes can be reached from it
	                   );
	 
	set[value] descent_into = desiredSubjectTypes;
	if(\value() in initial_types)
		descent_into += \value();
	if(\str() in initial_types)
		descent_into += \str();
	if(\node() in initial_types)
		descent_into += \node();
	if(adt("Tree", []) in desiredTypes)
		initial_types += adt("Tree", []);
	
	//println("initial_types [<size(initial_types)>]"); for(elm <- initial_types){println("\t<elm>");};
	adts_with_constructors = {};

	for(<Symbol fromSym, value toSym> <- reachableTypes,  fromSym in initial_types){
		//println("fromSym = <fromSym>, toSym = <toSym>");
		if(Symbol c:\cons(Symbol \adtsym, str name, list[Symbol] parameters) := toSym){
			set[value] deps = range(getConstructorDependencies(fromSym, c));
			//println("\tdeps1 = <deps>");
			deps = {s | /Symbol s := deps };
			deps += reachableTypes[deps];
			//println("\tdeps2 = <deps>");
			if(name in consNames || (\adtsym == fromSym && (\value() in deps || !isEmpty(initial_types & deps)))){
			   //println("adding to descent_into: <c>, fromSym = <fromSym>, deps = <deps>");
			   descent_into += c;
			   adts_with_constructors += \adtsym;
			}
		}
	}
	
	//descent_into -= (adts_with_constructors - {\node(), \str(), \value(),adt("Tree", [])});
	//println("descent_into [<size(descent_into)>]:"); for(elm <- descent_into){println("\t<elm>");};
	
	return descent_into;
}

//bool desiredInAbstractMatch(value s1){
//	return prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) !:= s1;
//}

bool desiredInConcreteMatch(value s1){
	switch(s1){
		case \adt(_,_):			return false;
		case \cons(_,_,_):		return false;
		case \lit(_):			return false;
		case \cilit(_):			return false;
		case \char-class(_):	return false;
		case \range(_,_):		return false;
		//case \label(_,_):		return false;
		case \conditional(_,_):	return false;
		case \layouts(_):		return false;
		case \keywords(_):		return false;
		case \iter(s2):			return desiredInConcreteMatch(s2);
		case \iter-star(s2):	return desiredInConcreteMatch(s2);
		case \iter-seps(s2,_):	return desiredInConcreteMatch(s2);
		case \iter-star-seps(s2,_):	
								return desiredInConcreteMatch(s2);
	}
	return true;
}

rel[value,value] removeTreeElements(rel[value, value] reachable, bool(value) desired) =
	{<s1, s2> | <s1, s2> <- reachable, desiredInConcreteMatch(s1), desired(s2) };

set[value] getReachableConcreteTypes(Symbol subjectType, set[str] consNames, set[Symbol] patternTypes){
	desiredPatternTypes = { s | /Symbol s := patternTypes};
	desiredSubjectTypes = { s | /Symbol s := subjectType};
	desiredTypes = desiredSubjectTypes + desiredPatternTypes;
	
	println("desiredTypes = <desiredTypes>");
	reachable = removeTreeElements(reachableTypes, desiredInConcreteMatch);
	println("reachable [<size(reachable)>]"); //for(elm <- reachable){println("\t<elm>");};
	                                                
	//set[value] initial_types = 
	//	desiredTypes + (subjectType == \value()
	//					? desiredTypes + carrier(reachable)
	//                    : desiredTypes + { sym | pts <- desiredTypes, Symbol sym <- reachable[pts], isExpandableSymbol(sym)}
	//                    ); 
	                    
	set[value] initial_types = 
		desiredTypes + (subjectType == \value()
					   ? { sym | sym <- carrier(reachableTypes),						
					  			 r := reachableTypes[sym],
					  			 size(consNames) != 0 ||
					  			 \value() in r ||
					  			 !isEmpty(desiredTypes & r) }
					   		     
	                   : { sym | stp <- desiredSubjectTypes, 						// We are looking for
	                             Symbol sym <- reachableTypes[stp],  				// all symbols that are reachable from the desiredSubjectTypes
	                             //isExpandableSymbol(sym), 							// that are expandable (or value)
	                             r := reachableTypes[sym], 							// and 
	                             size(consNames) != 0 || 							// either specific constructors have been given (and we are a bit permissive here)
	                             \value() in r || 									// or value can be reached,
	                             !isEmpty(desiredTypes & r)}						// or one or more desiredPatternTypes can be reached from it
	                   );
	initial_types -= \value();
	
	set[value] descent_into = initial_types;
	//println("initial: <initial_types>");
	nonterminals_with_productions = {};
	
	for(<Symbol fromSym, value toSym> <- reachableTypes,  fromSym in initial_types){
		if(Production p: prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) := toSym){
			if((def == fromSym && !isEmpty(initial_types & (range(getProductionDependencies(fromSym, p)) - fromSym)))){
			   descent_into += p;
			   nonterminals_with_productions += def;
			}
		}
	}
	
	descent_into -= nonterminals_with_productions;
	
	descent_into = { elm | elm <- descent_into, desiredInConcreteMatch(elm) };

	//println("descent_into [<size(descent_into)>]:"); for(elm <- descent_into){println("\t<elm>");};
	
	return descent_into;
}

// -- isConcreteType

bool isConcreteType(Symbol::\iter(Symbol symbol)) = true;

bool isConcreteType(Symbol::\iter-star(Symbol symbol)) = true;

bool isConcreteType(Symbol::\iter-seps(Symbol symbol, list[Symbol] separators)) = true;

bool isConcreteType(Symbol::\iter-star-seps(Symbol symbol, list[Symbol] separators)) = true;

default bool isConcreteType(Symbol symbol) {
	if(grammar[symbol]?){
		return all(alt <- grammar[symbol].alternatives, Production p := alt);
	}
	return false;
}

// ---------------- symbolToValue ------------------
// TODO: rewrite the following code using
// - exisiting code in lang::rascal::grammar (e.d. striprec, delabel etc.
// - remove duplication of 'layouts', 'regular' and 'intermix', 'sym2prod'
// - descent operator rather than inductive definition.
// Attention points:
// - Introduce a GrammarDefinition as soon as possible, then existing tools can work on it.
// - The type checker introduces a second form of productionL of the form:
//   prod(Symbol def, str cons, list[Symbol] symbols, set[Attr] attributes)
//   to record the constructor name. Remove these as soon as possible.
// - Consistent introduction of layout.
	
public type[value] symbolToValue(Symbol symbol) {
   	
	// Recursively collect all the type definitions associated with a given symbol
	
	//symbol = regulars(symbol,activeLayout);	//TODO still needed?
	
 	map[Symbol,Production] definitions = reify(symbol, ());
 	
 	if(Symbol::\start(Symbol sym) := symbol){
 	    definitions += (symbol : choice(symbol, { prod(symbol,
                                                       [ activeLayout,
                                                         label("top", sym),
                                                         activeLayout
                                                       ],
                                                    {})}));
    }
 	                                             
 	der_symbol = (Symbol::conditional(Symbol sym,_) := symbol || Symbol::\start(Symbol sym) := symbol) ? sym : symbol;
 	
 	if(Symbol::\sort(_):= der_symbol || Symbol::\lex(_):= der_symbol ||
 		Symbol::\parameterized-sort(_,_):= der_symbol || Symbol::\parameterized-lex(_,_):= der_symbol) {
 			if(<str n, Symbol def> <- typeRel, Symbol::\layouts(_) := def) {
 				definitions = reify(def,definitions);
 			}
 			definitions = definitions + (Symbol::\layouts("$default$"):Production::choice(Symbol::\layouts("$default$"),{Production::prod(Symbol::\layouts("$default$"),[],{})}));
 			definitions = definitions + (Symbol::\empty():Production::choice(Symbol::\empty(),{Production::prod(Symbol::\empty(),[],{})}));
 	}
 	return type(symbol, definitions); 
 	//return type(regulars(symbol, activeLayout), definitions); // TODO: still needed?
}

// primitive
public map[Symbol,Production] reify(Symbol symbol, map[Symbol,Production] definitions) 
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
	set[Symbol] defs = typeRel[name];
	//println("reify adt: <name>, <symbols>, <defs>, <constructors>");

    for(Symbol s <- defs){
	   if(adtDef: Symbol::\adt(name,_) := s){
          //assert Symbol::\adt(name,_) := adtDef;
          if(!definitions[adtDef]?) {
        	 alts = { sym2prod(sym) | sym <- constructors[adtDef] };
        	 definitions[adtDef] = Production::\choice(adtDef, alts);
        	 definitions = ( definitions | reify(sym, it) | sym <- constructors[adtDef] );
          }
          definitions = ( definitions | reify(sym, it) | sym <- symbols );
          //println("reify adt <name> =\> <definitions>");
          return definitions;
       }
    }
    throw "No definition for ADT <name>(<symbols>)";
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
    //println("reify function: <ret>, <parameters>, <definitions>");
	definitions = reify(ret, definitions);
	definitions = ( definitions | reify(sym, it) | Symbol sym <- parameters );
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
public map[Symbol,Production] reify(Symbol::\reified(Symbol ret), map[Symbol,Production] definitions) 
	= reify(ret, definitions);
	
// parameter
public map[Symbol,Production] reify(Symbol::\parameter(str name, Symbol bound), map[Symbol,Production] definitions)
	= reify(bound, definitions);
	
// sort, lex
public map[Symbol,Production] reify(Symbol symbol, map[Symbol,Production] definitions) 
	= { 
	    set[Symbol] defs = typeRel[name];
		//println("reify: symbol-<symbol>, name=<name>"); 
		//assert !(Symbol::\adt(name,_) := nonterminal);
		for(Symbol nonterminal <- defs){
		    if(Symbol::\adt(name,_) !:= nonterminal){
		       //println("nonterminal1=<nonterminal>");
		       if(prod(Symbol s, _, _, _) := nonterminal){
		          nonterminal = s;
		       }
		       // println("nonterminal2=<nonterminal>");
		       if(!definitions[nonterminal]?) {
		          //println("grammar:\n----------");
            //      for(s <- grammar) println("<s>: <grammar[s]>");
            //      println("----------");
			      definitions[nonterminal] = \layouts(grammar[nonterminal]); // inserts an active layout
			      if(nonterminal in starts) {
				     definitions[Symbol::\start(nonterminal)] = \layouts(Production::choice(Symbol::\start(nonterminal),
																					   { Production::prod(Symbol::\start(nonterminal), [ Symbol::\label("top", nonterminal) ],{}) }));
			      }
			     //println("Productions[nonterminal]: <productions[nonterminal]>");
			     //println("Domain(grammar): <domain(grammar)>");
			    definitions = ( definitions | reify(sym, it) | sym <- productions[nonterminal] );
		     }
		     return definitions;
		  }
		}
		throw "No definition for symbol <name>";
	  } when Symbol::\sort(str name) := symbol || Symbol::\lex(str name) := symbol ||
	  		 (Symbol::\layouts(str name) := symbol && name != "$default$") || Symbol::\keywords(str name) := symbol;

// parameterized-sort, parameterized-lex  
public map[Symbol,Production] reify(Symbol symbol, map[Symbol,Production] definitions) 
	= { 
	    set[Symbol] defs = typeRel[name];
		//assert !(Symbol::\adt(name,_) := nonterminal);
		for(Symbol nonterminal <- defs){
            if(Symbol::\adt(name,_) !:= nonterminal){
		       if(!definitions[nonterminal]?) {
			      definitions[nonterminal] = \layouts(grammar[nonterminal]); // inserts an active layout
			      if(nonterminal in starts) {
				     definitions[Symbol::\start(nonterminal)] = \layouts(Production::choice(Symbol::\start(nonterminal),
															    { Production::prod(Symbol::\start(nonterminal), [ Symbol::\label("top", nonterminal) ],{}) }));
			      }
			      //println("Productions[nonterminal]: <productions[nonterminal]>");
			     //println("Domain(grammar): <domain(grammar)>");
			     definitions = ( definitions | reify(sym, it) | sym <- productions[nonterminal] );
		      }
		      definitions = ( definitions | reify(sym, it) | sym <- parameters );
		       return definitions;
		   }
		}
		throw "No definition for symbol <name>";
	  } when Symbol::\parameterized-sort(str name, list[Symbol] parameters) := symbol || Symbol::\parameterized-lex(str name, list[Symbol] parameters) := symbol;

public map[Symbol,Production] reify(Symbol symbol, map[Symbol,Production] definitions)
	= reify(sym, definitions)
		when Symbol::\start(Symbol sym) := symbol || Symbol::\opt(Symbol sym) := symbol || Symbol::\iter(Symbol sym) := symbol ||
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
	= Production::\cons(Symbol::label(name, \type), parameters, [], (), {}) 
		when Symbol::\adt(str _, list[Symbol] _) := \type;
		
private Production sym2prod(Symbol::\prod(Symbol \type, str name, list[Symbol] parameters, set[Attr] attributes))
	= Production::\prod(Symbol::\label(name, \type), parameters, attributes) 
		when name != "" && \type has name;
		
private Production sym2prod(Symbol::\prod(Symbol \type, str name, list[Symbol] parameters, set[Attr] attributes))
	= Production::\prod(\type, parameters, attributes) 
		when name == "" && \type has name;
		
default Production sym2prod(Symbol s) { throw "Could not transform the symbol <s> to a Production node"; }

@doc{Intermix with an active layout}
public Production \layouts(Production prod) {
  return top-down-break visit (prod) {
    case Production::prod(\start(y),[Symbol x],as)                                  => Production::prod(\start(y),[activeLayout, x, activeLayout],  as)
    case Production::prod(sort(s),list[Symbol] lhs,as)                              => Production::prod(sort(s),intermix(lhs,activeLayout),as)
    case Production::prod(\parameterized-sort(s,n),list[Symbol] lhs,as)             => Production::prod(\parameterized-sort(s,n),intermix(lhs,activeLayout),as)
    case Production::prod(label(t,sort(s)),list[Symbol] lhs,as)                     => Production::prod(label(t,sort(s)),intermix(lhs,activeLayout),as)
    case Production::prod(label(t,\parameterized-sort(s,n)),list[Symbol] lhs,as)    => Production::prod(label(t,\parameterized-sort(s,n)),intermix(lhs,activeLayout),as) 
  }
} 

private list[Symbol] intermix(list[Symbol] syms, Symbol l) {
  if (syms == []) 
    return syms;
  res = tail([l, regulars(s,l) | s <- syms]);
  //println("intermix(<syms>, <l>)\n=\> <res>");
  return res;
}

private Symbol regulars(Symbol s, Symbol l) {
  return visit(s) {
    case \iter(Symbol n) => \iter-seps(n,[l])
    case \iter-star(Symbol n) => \iter-star-seps(n,[l]) 
    case \iter-seps(Symbol n, [Symbol sep]) => \iter-seps(n,[l,sep,l]) 
    case \iter-star-seps(Symbol n,[Symbol sep]) => \iter-star-seps(n, [l,sep,l])
    case \seq(list[Symbol] elems) => \seq(tail([l, e | e <- elems]))
  }
}

public Symbol insertLayout(Symbol s) = regulars(s, activeLayout);