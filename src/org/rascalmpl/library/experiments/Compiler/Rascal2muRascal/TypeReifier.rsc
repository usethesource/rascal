module experiments::Compiler::Rascal2muRascal::TypeReifier

/*
 * This module handles the mapping between types and reified types. It defines the following functions:
 *	   (1) void 				  resetTypeReifier()			Reset the global state of this module
 *     (2) map[Symbol,Production] getGrammar() 					Extract only syntax definitions
  *    (3) map[Symbol,Production] getDefinitions() 				Extract all defined symbols
 *     (4) type[value]            symbolToValue(Symbol) 		Compute the reified type for a symbol
 */

import lang::rascal::types::CheckerConfig;
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
private map[Symbol,Production] fullGrammar = ();
private map[Symbol,Production] instantiatedGrammar = ();
private set[Symbol] starts = {};
private Symbol activeLayout = Symbol::\layouts("$default$");
private rel[value,value] reachableTypes = {};
private rel[Symbol,Symbol] reachableConcreteTypes = {};

public void resetTypeReifier() {
    typeRel = {};
    types = {};
    constructors = {};
    productions = {};
    grammar = ();
    fullGrammar = ();
    instantiatedGrammar = ();
    starts = {};
    activeLayout = Symbol::\layouts("$default$");
    reachableTypes = {};
    reachableConcreteTypes = {};
}

// Extract common declaration info and save it for later use by
// - getGrammar
// - symbolToValue

public void extractDeclarationInfo(Configuration config){
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
            + { <"CharClass", adt("CharClass",[])> }
            + { <"Condition", adt("Condition",[])> }
            ;
    //println("typeRel:");for(x <- typeRel) println("\t<x>");
	// Collect all the constructors of the adt types in the type environment
	
	constructors = { <\type.\adt, \type> | int uid <- config.store, 
												constructor(_, Symbol \type, _, _, _) := config.store[uid]
												//\type.\adt in types 
												};

	//constructors += <#Tree.symbol, type(Tree, ())>;
	
    typeRel += { <cns[1].adt.name, cns[1].adt> | cns <- constructors };
    
    types = range(typeRel);
    
	// Collect all the  uctions of the non-terminal types in the type environment
    
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
    fullGrammar = getGrammar();
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
	visit(fullGrammar[symbol]){
		case p:prod(\label(name, symbol), _, _): return p;
		case p:regular(\label(name, symbol)): return p;
	};
	throw "No LabeledProduction for <name>, <symbol>";
}

// Type reachability functions

// Get all dependencies generated by a given type symbol

rel[Symbol,Symbol] getDependencies(s1: Symbol::\set(Symbol s2)) = <s1, s2> + getDependencies(s2);
rel[Symbol,Symbol] getDependencies(s1: Symbol::\rel(list[Symbol] symbols)) = 
	{ <s1, Symbol::\tuple(symbols)> } + {<s1, s2> | s2 <- symbols} + {*getDependencies(s2) | s2 <- symbols};
rel[Symbol,Symbol] getDependencies(s1: Symbol:\lrel(list[Symbol] symbols)) = 
	{ <s1, Symbol::\tuple(symbols)> } + {<s1, s2> | s2 <- symbols} + {*getDependencies(s2) | s2 <- symbols};
rel[Symbol,Symbol] getDependencies(s1: Symbol::\tuple(list[Symbol] symbols)) = {<s1, s2> | s2 <- symbols} + {*getDependencies(s2) | s2 <- symbols};
rel[Symbol,Symbol] getDependencies(s1: Symbol::\list(Symbol s2)) = <s1, s2> + getDependencies(s2);
rel[Symbol,Symbol] getDependencies(s1: Symbol::\map(Symbol key, Symbol val)) = {<s1, key>, <s1, val>} + getDependencies(key) + getDependencies(val);
rel[Symbol,Symbol] getDependencies(s1: Symbol::\bag(Symbol s2)) = <s1, s2> + getDependencies(s2);
rel[Symbol,Symbol] getDependencies(c:  Symbol::\cons(Symbol \adtsym, str name, list[Symbol] parameters)) {
	res = {<c, striprec(sym)> | Symbol sym <- parameters} + { *getDependencies(sym) | Symbol sym <- parameters };
	return res;
}

// TODO: alias, func, var-func, reified

default rel[Symbol,Symbol] getDependencies(Symbol s) = {};

// Extract the reachability relation for all types. This information can be used by generated code for
// - descendant match
// - visit

private void computeReachableTypesAndConstructors(){
	stripped_constructors = {<striprec(s1),striprec(s2)> | <s1, s2> <- constructors};
	rel[value,value] containment = stripped_constructors
								   + {*getDependencies(c)|  <s, c> <- stripped_constructors};
	                              ;
	reachableTypes = containment+;
	
	//println("reachableTypes [<size(reachableTypes)>] ="); //for(elm <- reachableTypes) { println("\t<elm>"); }
	computeReachableConcreteTypes();
}

// Auxiliary functions for computeReachableConcreteTypes

bool isParameterFree(list[Symbol] symbols) = !(/\parameter(name, formals) := symbols);

// Get all instantiated parameters in the complete grammar

set[Symbol] getInstantiatedParameters(){
	instantiated_params = {};
	visit(fullGrammar){
		case sym: \parameterized-sort(name, list[Symbol] args):
			if( isParameterFree(args)) instantiated_params += sym; else fail;
		case sym: \parameterized-lex(name, list[Symbol] args):
			if( isParameterFree(args)) instantiated_params += sym; else fail;
	};
	return instantiated_params;
}

// Instantiate one parameterized production with actual parameters

private Production instantiateParameterizedProduction(Production p, list[Symbol] actuals){
    lhs = p.def;
    instantiated_alts = p.alternatives;
    instantiated_parameters = lhs.parameters;
	for(int i <- [0 .. size(lhs.parameters)]){
	    param = lhs.parameters[i];
	    actual = actuals[i];
	    instantiated_parameters[i] = actual;
		instantiated_alts = visit(instantiated_alts){
		   case \parameter(pname, ptype) => actual when pname == param.name
		}
	}
	p.def.parameters = instantiated_parameters;
	p.alternatives = instantiated_alts;
	return p;
}
// 
private map[Symbol, Production] instantiateAllParameterizedProductions(map[Symbol,Production] grammar){
	instantiated_params = getInstantiatedParameters();
	instantiated_productions = ();
	for(Symbol lhs <- grammar){
		if(\parameterized-sort(name, list[Symbol] parameters) := lhs){
			for(Symbol ip <- instantiated_params, ip.name == name, size(ip.parameters) == size(parameters)){
				iprod = instantiateParameterizedProduction(fullGrammar[lhs], ip.parameters);
				instantiated_productions[iprod.def] = iprod;
			}
		}
	}	
	return instantiated_productions;
}

private void computeReachableConcreteTypes(){
	instantiatedGrammar = fullGrammar + instantiateAllParameterizedProductions(fullGrammar);
	reachableConcreteTypes = {};
	for(/Production p: prod(sym,args,attrs) := instantiatedGrammar){
	    for(/Symbol s := args){
	   		reachableConcreteTypes += <delabel(sym), delabel(s)>;
	   }
	}
	reachableConcreteTypes = reachableConcreteTypes+;
	//println("reachableConcreteTypes [<size(reachableConcreteTypes)>] :");for(elm <- reachableConcreteTypes) { println("\t<elm>"); }
}

// Extract the reachable types for a given type, given
// - a subjectType
// - a set of constructor names that occur in the patterns
// - a set of patternTypes that occur in the patterns

set[value] getReachableTypes(Symbol subjectType, set[str] consNames, set[Symbol] patternTypes, bool concreteMatch){
	//println("getReachableTypes: <subjectType>, <consNames>, <patternTypes>, <concreteMatch>");
	
	consNames = {unescape(name) | name <- consNames};
	if(concreteMatch){
		return getReachableConcreteTypes(subjectType, consNames, patternTypes);
	} else {
		return getReachableAbstractTypes(subjectType, consNames, patternTypes);
	}
}

// Extract the reachable abstract types

set[value] getReachableAbstractTypes(Symbol subjectType, set[str] consNames, set[Symbol] patternTypes){
    desiredPatternTypes = { s | /Symbol s := patternTypes};
	desiredSubjectTypes = { s | /Symbol s := subjectType};
	desiredTypes = desiredSubjectTypes + desiredPatternTypes;
	
	if(any(sym <- desiredTypes, sort(_) := sym || lex(_) := sym || subtype(sym, adt("Tree",[])))){
		// We just give up when abstract and concrete symbols occur together
		//println("descent_into (abstract) [1]: {value()}");
	   return {\value()};
	}
	//println("desiredSubjectTypes = <desiredSubjectTypes>");
	//println("desiredTypes = <desiredTypes>");
	prunedReachableTypes = reachableTypes ;
	if(\value() notin desiredSubjectTypes){
	    // if specific subject types are given, the reachability relation can be further pruned
		prunedReachableTypes = carrierR(reachableTypes,reachableTypes[desiredSubjectTypes]);
		//println("removed from reachableTypes:"); for(x <- reachableTypes - prunedReachableTypes){println("\t<x>");}
	}
	
	//println("prunedReachableTypes:"); for(x <- prunedReachableTypes){println("\t<x>");}
	descent_into = desiredTypes;
	
	// TODO <Symbol from ,Symbol to> <- ... makes the stack validator unhappy.
	for(<Symbol from, Symbol to> <- prunedReachableTypes){
		if(to in desiredTypes){		// TODO || here was the cause 
			descent_into += {from, to};
		} else if(any(Symbol t <- desiredTypes, subtype(t, to))){
			descent_into += {from, to};
		} else if(c:Symbol::\cons(Symbol \adtsym, str name, list[Symbol] parameters) := from  && 
	                        (\adtsym in patternTypes || name in consNames)){
	              descent_into += {from, to};   
		} else if(c:Symbol::\cons(Symbol \adtsym, str name, list[Symbol] parameters) := to  && 
	                        (\adtsym in patternTypes || name in consNames)){
	              descent_into += {from, to};         
	    }
	    ;
	}
	
	//if(\value() in descent_into){
	//    println("replace by value, descent_into [<size(descent_into)>]:"); for(elm <- descent_into){println("\t<elm>");};
	//	descent_into = {\value()};
	//}
	tuples = { Symbol::\tuple(symbols) | sym <- descent_into, \rel(symbols) := sym || \lrel(symbols) := sym };
	descent_into += tuples;
	//println("descent_into (abstract) [<size(descent_into)>]:"); for(elm <- descent_into){println("\t<elm>");};
	
	return descent_into;
}

// Extract the reachable concrete types

set[value] getReachableConcreteTypes(Symbol subjectType, set[str] consNames, set[Symbol] patternTypes){
	desiredPatternTypes = { s | /Symbol s := patternTypes};
	desiredSubjectTypes = { s | /Symbol s := subjectType};
	desiredTypes = desiredPatternTypes;
	
	//println("desiredPatternTypes = <desiredPatternTypes>");
	
	prunedReachableConcreteTypes = reachableConcreteTypes;
	if(\value() notin desiredSubjectTypes){
	    // if specific subject types are given, the reachability relation can be further pruned
		prunedReachableConcreteTypes = carrierR(reachableConcreteTypes, (reachableConcreteTypes)[desiredSubjectTypes] + desiredSubjectTypes);
		//println("removed from reachableConcreteTypes:"); for(x <- reachableConcreteTypes - prunedReachableConcreteTypes){println("\t<x>");}
	}
	
	set [value] descent_into = {};
	
	// Find all concrete types that can lead to a desired type

	for(sym <- invert(prunedReachableConcreteTypes+)[desiredPatternTypes]){
	   alts = instantiatedGrammar[sym];
	   for(/Production p := alts){
	       switch(p){
	       case choice(_, choices): descent_into += choices;
	       case associativity(_, _, choices): descent_into += choices;
	       case priority(_, choices): descent_into += toSet(choices);
	       default:
	    	descent_into += p;
	       }
	    }
	  }  
	
	for(/itr:\iter(s) := descent_into){
	  	descent_into += regular(itr);
	  	if(isAltOrSeq(s)) descent_into += regular(s);
	}	
	for(/itr:\iter-star(s) := descent_into){
	  	descent_into += regular(itr);
	  	if(isAltOrSeq(s)) descent_into += regular(s);
	}	
	for(/itr:\iter-seps(s,_) := descent_into){
	  	descent_into += regular(itr);
	  	if(isAltOrSeq(s)) descent_into += regular(s);
	}	
	for(/itr:\iter-star-seps(s,_) := descent_into){
	    descent_into += regular(itr);
	    if(isAltOrSeq(s)) descent_into += regular(s);
	}	
	//println("descent_into (concrete) [<size(descent_into)>]: "); for(s <- descent_into) println("\t<s>");
	return descent_into;
}

bool isAltOrSeq(Symbol s) = alt(_) := s || seq(_) := s;	                                         

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
    //println("reify alias: <name>, <aliased>");
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
	= Production::\cons(Symbol::label(name, \type), parameters, [], /*(),*/ {}) 
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


public bool hasField(Symbol s, str fieldName){
    //println("hasField: <s>, <fieldName>");

    //if(isADTType(s)){
    //   s2v = symbolToValue(s /*, config*/);
    //   println("s2v = <s2v>");
    //}
    s1 = symbolToValue(s);
    // TODO: this is too liberal, restrict to outer type.
    visit(s1){
       case label(fieldName2, _):	if(unescape(fieldName2) == fieldName) return true;
    }
    return false;
}