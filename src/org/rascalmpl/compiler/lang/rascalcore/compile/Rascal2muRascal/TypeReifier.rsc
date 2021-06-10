module lang::rascalcore::compile::Rascal2muRascal::TypeReifier

/*
 * This module handles the mapping between types and reified types. It defines the following functions:
 *	   (1) void 				  resetTypeReifier()			Reset the global state of this module
 *     (2) map[AType,AProduction] getGrammar() 					Extract only syntax definitions
  *    (3) map[AType,AProduction] getDefinitions() 				Extract all defined symbols
 *     (4) type[value]            symbolToValue(AType) 		Compute the reified type for a symbol
 */

import lang::rascalcore::check::Checker;

import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;

import lang::rascalcore::grammar::definition::Symbols;

import ParseTree;
import List;
import Map;
import Set;
import Relation;

import IO;

private TModel the_tmodel = tmodel();
private rel[str,AType] typeRel = {};
private set[AType] types = {};
private rel[AType,AProduction] constructors = {};
private rel[AType,AType] productions = {};
private map[AType,AProduction] grammar = ();
private map[AType,AProduction] cachedGrammarRules = ();

private set[AType] adts = {};

private map[AType,AProduction] instantiatedGrammar = ();
private set[AType] starts = {};
private AType activeLayout = layouts("$default$");
private rel[value,value] reachableTypes = {};
private rel[AType,AType] reachableConcreteTypes = {};
private bool reachableInfoAvailable = false;

map[tuple[AType symbol, map[AType,AProduction] definitions], map[AType,AProduction]] collectDefsCache = ();

private map[AType symbol, type[value] resType] atypeToValueCache = ();

public void resetTypeReifier() {
    the_tmodel = tmodel();
    typeRel = {};
    types = {};
    constructors = {};
    productions = {};
    grammar = ();
    cachedGrammarRules = ();
    adts = {};
    instantiatedGrammar = ();
    starts = {};
    activeLayout = layouts("$default$");
    reachableTypes = {};
    reachableConcreteTypes = {};
    reachableInfoAvailable = false;
    collectDefsCache = ();
    atypeToValueCache = ();
}

// Extract common declaration info and save it for later use by
// - getGrammar
// - symbolToValue

public void extractDeclarationInfo(TModel tm){
    resetTypeReifier();

    if(AGrammar g := tm.store["grammar"]){
        cachedGrammarRules = g.rules;
    } else {
        throw "Cannot get grammar from tmodel";
    }
    
    if(set[AType] adts_in_store := tm.store[key_ADTs]){
        adts = adts_in_store;
    } else {
        throw "Cannot get ADTs from TModel";
    }
  	computeReachableTypesAndConstructors();
}

private bool hasManualTag(\choice(AType def, set[Production] alternatives)) =
    any(Production alt <- alternatives, hasManualTag(alt));

private bool hasManualTag(Production p) =
    p has attributes && \tag("manual"()) in p.attributes;


// Extract all declared symbols from a type checker tmodel

public map[AType,AProduction] getDefinitions() {
    return (); // TODO
  // 	// Collect all symbols
  // 	set[AType] symbols = types + domain(constructors) + carrier(productions) + domain(grammar);
  // 	
  // 	map[AType,Production] definitions  = (() | collectDefs(symbol, it) | AType symbol <- symbols);
 	//
 	//return definitions;
}

public Production getLabeledProduction(str name, AType symbol){
	//println("getLabeledProduction: <getGrammar()[symbol]>");
	name = unescape(name);
	visit(cachedGrammarRules[symbol]){
		case p:prod(\label(name, symbol), _, _): return p;
		case p:regular(\label(name, symbol)): return p;
	};
	throw "No LabeledProduction for <name>, <symbol>";
}

// Type reachability functions

// Get all dependencies generated by a given type symbol;
// eliminate top-level label(_,_) constructors.

private rel[AType,AType] getDependencies(s1: AType::\label(_,AType s2)) = <s1, s2> + getDependencies(s2);
private rel[AType,AType] getDependencies(s1: aset(AType s2)) = <s1, removeOuterLabel(s2)> + getDependencies(s2);
private rel[AType,AType] getDependencies(s1: arel(atypeList(list[AType] symbols))) = 
	{ <s1, atuple(atypeList(symbols))> } + {<s1, removeOuterLabel(s2)> | s2 <- symbols} + {*getDependencies(s2) | s2 <- symbols};
private rel[AType,AType] getDependencies(s1: alrel(atypeList(list[AType] symbols))) = 
	{ <s1, atuple(atypeList(symbols))> } + {<s1, removeOuterLabel(s2)> | AType s2 <- symbols} + {*getDependencies(s2) | AType s2 <- symbols};
private rel[AType,AType] getDependencies(s1: atuple(atypeList(list[AType] symbols))) = {<s1, removeOuterLabel(s2)> | AType s2 <- symbols} + {*getDependencies(s2) | AType s2 <- symbols};
private rel[AType,AType] getDependencies(s1: alist(AType s2)) = <s1, removeOuterLabel(s2)> + getDependencies(s2);
private rel[AType,AType] getDependencies(s1: amap(AType key, AType val)) = {<s1, removeOuterLabel(key)>, <s1, removeOuterLabel(val)>} + getDependencies(key) + getDependencies(val);
private rel[AType,AType] getDependencies(s1: abag(AType s2)) = <s1, removeOuterLabel(s2)> + getDependencies(s2);
private rel[AType,AType] getDependencies(c:  acons(AType \adtsym, list[AType] fields, list[Keyword] kwFields)) =
	{<c, removeOuterLabel(sym)> | AType sym <- fields} + { *getDependencies(sym) | AType sym <- fields };    // TODO: check

private rel[AType,AType] getDependencies(s1: \opt(AType s2)) = <s1, removeOuterLabel(s2)> + getDependencies(s2);
private rel[AType,AType] getDependencies(s1: \iter(AType s2)) = <s1, removeOuterLabel(s2)> + getDependencies(s2);
private rel[AType,AType] getDependencies(s1: \iter-star(AType s2)) = <s1, removeOuterLabel(s2)> + getDependencies(s2);
private rel[AType,AType] getDependencies(s1: \iter-star-seps(AType s2, list[AType] separators)) = 
    {<s1, removeOuterLabel(sym)> | AType sym <- separators} + { *getDependencies(sym) | AType sym <- separators };

private rel[AType,AType] getDependencies(s1: \alt(set[AType] alternatives)) = 
    {<s1, removeOuterLabel(sym)> | AType sym <- alternatives} + { *getDependencies(sym) | AType sym <- alternatives };

private rel[AType,AType] getDependencies(s1: \seq(list[AType] symbols)) = 
    {<s1, removeOuterLabel(sym)> | AType sym <- symbols} + { *getDependencies(sym) | AType sym <- symbols };

// TODO Condition and other possible cases are missing here

private rel[AType,AType] getDependencies(s1: \conditional(AType symbol, set[ACondition] conditions)) = 
    {<s1, removeOuterLabel(sym)> | AType sym <- getATypes(conditions)} + { *getDependencies(sym) | AType sym <- getATypes(conditions) };

private set[AType] getATypes(set[Condition] conditions) =
    { c.symbol | c <- conditions, c has symbol };

// Remove outermost label

private AType removeOuterLabel(AType s1) = AType::\label(_,AType s2) := s1 ? s2 : s1;

// TODO: alias, func, var-func, reified

private default rel[AType,AType] getDependencies(AType s) = {};

// Extract the reachability relation for all types. This information can be used by generated code for
// - descendant match
// - visit

private void computeReachableTypesAndConstructors(){
	rel[AType,AType] containment = {<s, rc>, *getDependencies(rc) |  <AType s, \cons(label(s2,n),params,_,_)> <- constructors, AType rc := AType::\cons(n, s2, params)};
	                              
	reachableTypes = containment+;
	
	//println("reachableTypes [<size(reachableTypes)>] ="); //for(elm <- reachableTypes) { println("\t<elm>"); }
	computeReachableConcreteTypes();
	reachableInfoAvailable = true;
}

// Auxiliary functions for computeReachableConcreteTypes

private bool isParameterFree(list[AType] symbols) = !(/\parameter(name, formals) := symbols);

// Get all instantiated parameters in the complete grammar

private set[AType] getInstantiatedParameters(){
	instantiated_params = {};
	visit(cachedGrammarRules){
		case sym: \parameterized-sort(name, list[AType] args):
			if( isParameterFree(args)) instantiated_params += sym; else fail;
		case sym: \parameterized-lex(name, list[AType] args):
			if( isParameterFree(args)) instantiated_params += sym; else fail;
	};
	return instantiated_params;
}

// Instantiate one parameterized production with actual parameters

private Production instantiateParameterizedProduction(Production p, list[AType] actuals){
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
private map[AType, Production] instantiateAllParameterizedProductions(){
	instantiated_params = getInstantiatedParameters();
	instantiated_productions = ();
	for(AType lhs <- cachedGrammarRules){
		if(\aadt(name, list[AType] parameters, SyntaxRole sr) := lhs){
			for(AType ip <- instantiated_params, ip.name == name, size(ip.parameters) == size(parameters)){
				iprod = instantiateParameterizedProduction(cachedGrammarRules[lhs], ip.parameters);
				instantiated_productions[iprod.def] = iprod;
			}
		}
	}	
	return instantiated_productions;
}

private void computeReachableConcreteTypes(){
	instantiatedGrammar = cachedGrammarRules + instantiateAllParameterizedProductions();
	reachableConcreteTypes = {};
	for(/Production p: prod(sym,args,attrs) := instantiatedGrammar){
	    for(/AType s := args){
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

public tuple[set[AType], set[Production]] getReachableTypes(AType subjectType, set[str] consNames, set[AType] patternTypes, bool concreteMatch){
	//println("getReachableTypes: <subjectType>, <consNames>, <patternTypes>, <concreteMatch>");
	
	if(!reachableInfoAvailable){
	   computeReachableTypesAndConstructors();
	}
	consNames = {unescape(name) | name <- consNames};
	if(concreteMatch){
		return getReachableConcreteTypes(subjectType, consNames, patternTypes);
	} else {
		return getReachableAbstractTypes(subjectType, consNames, patternTypes);
	}
}

// Extract the reachable abstract types

private  tuple[set[AType], set[Production]] getReachableAbstractTypes(AType subjectType, set[str] consNames, set[AType] patternTypes){
    return <{}, {}>; // TODO
    desiredPatternTypes = { s | /AType s := patternTypes};
	desiredSubjectTypes = { s | /AType s := subjectType};
	desiredTypes = desiredSubjectTypes + desiredPatternTypes;
	
	if(any(sym <- desiredTypes, sort(_) := sym || lex(_) := sym || subtype(sym, adt("Tree",[])))){
		// We just give up when abstract and concrete symbols occur together
		//println("descend_into (abstract) [1]: {value()}");
	   return <{\value()}, {}>;
	}
	//println("desiredSubjectTypes = <desiredSubjectTypes>");
	//println("desiredTypes = <desiredTypes>");
	prunedReachableTypes = reachableTypes ;
	if(\value() notin desiredSubjectTypes){
	    // if specific subject types are given, the reachability relation can be further pruned
		prunedReachableTypes = carrierR(reachableTypes,reachableTypes[desiredSubjectTypes]);
		//println("removed from reachableTypes:[<size(reachableTypes - prunedReachableTypes)>]"); //for(x <- reachableTypes - prunedReachableTypes){println("\t<x>");}
	}
	
	//println("prunedReachableTypes: [<size(prunedReachableTypes)>]"); //for(x <- prunedReachableTypes){println("\t<x>");}
	descend_into = desiredTypes;
	
	// TODO <AType from ,AType to> <- ... makes the stack validator unhappy.
	for(<AType from, AType to> <- prunedReachableTypes){
		if(to in desiredTypes){		// TODO || here was the cause 
			descend_into += {from, to};
		} else if(any(AType t <- desiredTypes, subtype(t, to))){
			descend_into += {from, to};
		} else if(c:acons(AType \adtsym, list[AType] parameters) := from  && // TODO: check
	                        (\adtsym in patternTypes || name in consNames)){
	              descend_into += {from, to};   
		} else if(c:acons(AType \adtsym, str name, list[AType] parameters) := to  && 
	                        (\adtsym in patternTypes || name in consNames)){
	              descend_into += {from, to};        
	    }
	    ;
	}
	
	//if(\value() in descend_into){
	//    println("replace by value, descend_into [<size(descend_into)>]:"); for(elm <- descend_into){println("\t<elm>");};
	//	descend_into = {\value()};
	//}
	tuples = { atuple(atypeList(symbols)) | sym <- descend_into, \rel(symbols) := sym || \lrel(symbols) := sym };
	descend_into += tuples;
	descend_into = {sym | sym <- descend_into, label(_,_) !:= sym };
	//println("descend_into (abstract) [<size(descend_into)>]:"); //for(elm <- descend_into){println("\t<elm>");};
	
	return <descend_into, {}>;
}

// Extract the reachable concrete types

tuple[set[AType], set[AProduction]] getReachableConcreteTypes(AType subjectType, set[str] consNames, set[AType] patternTypes){
	desiredPatternTypes = { s | /AType s := patternTypes};
	desiredSubjectTypes = { s | /AType s := subjectType};
	desiredTypes = desiredPatternTypes;
	
	//println("desiredPatternTypes = <desiredPatternTypes>");
	
	prunedReachableConcreteTypes = reachableConcreteTypes;
	if(avalue() notin desiredSubjectTypes){
	    // if specific subject types are given, the reachability relation can be further pruned
		prunedReachableConcreteTypes = carrierR(reachableConcreteTypes, (reachableConcreteTypes)[desiredSubjectTypes] + desiredSubjectTypes);
		//println("removed from reachableConcreteTypes:"); for(x <- reachableConcreteTypes - prunedReachableConcreteTypes){println("\t<x>");}
	}
	
	set [AProduction] descend_into = {};
	
	// Find all concrete types that can lead to a desired type
    for(<AType sym, AType tp> <- (prunedReachableConcreteTypes+), tp in desiredPatternTypes){
	   alts = instantiatedGrammar[sym];
	   for(/AProduction p := alts){
	       switch(p){
	       case choice(_, choices): descend_into += choices;
	       case associativity(_, _, set[AProduction] choices): descend_into += choices;
	       case priority(_, list[AProduction] choices): descend_into += toSet(choices);
	       default:
	    	descend_into += p;
	       }
	    }
	} 
	
	set [AProduction] descend_into1 = {};
	
	for(w <- descend_into){
	  visit(w){
	  
	  case itr:\iter(AType s): {
	       descend_into1 += regular(itr);
	       if(isAltOrSeq(s)) descend_into1 += regular(s);
	  }
	  
	  case itr:\iter-star(AType s):{
           descend_into1 += regular(itr);
           if(isAltOrSeq(s)) descend_into1 += regular(s);
      }
	  
	  case itr:\iter-seps(AType s,_):{
           descend_into1 += regular(itr);
           if(isAltOrSeq(s)) descend_into1 += regular(s);
      }
	  
	  case itr:\iter-star-seps(AType s,_):{
           descend_into1 += regular(itr);
           if(isAltOrSeq(s)) descend_into1 += regular(s);
      }
	 
	  }
	  descend_into1 += w;
	
	}
	//println("descend_into (concrete) [<size(descend_into)>]: "); for(s <- descend_into) println("\t<s>");	
	//println("descend_into1 (concrete) [<size(descend_into1)>]: "); for(s <- descend_into1) println("\t<s>");
	return <{}, descend_into + descend_into1>;
}

private bool isAltOrSeq(AType s) = alt(_) := s || seq(_) := s;	  

// ---------------- instantiate --------------------

//AType instantiate(AType s, map[str, AType] bindings){
//
//    top-down-break visit(s){
//        case \alias(str name, list[AType] parameters, AType aliased): {
//              bound = getBoundParaneters(parameters);
//              if(isEmpty(bound)){
//                    fail;
//              } else {
//                insert instantiate(aliased, bound + bindings);
//              }
//          }
//          
//        case \parameter(name, bnd): {
//              if(bindings[name]?){
//                 insert bindings[name];
//              } else {
//                fail;
//              }
//        }
//    }
//}      
//
//map[str, AType] getBoundParameters(list[AType] parameters){
//
//    (p.name, pp <- parameters, p !:= \parameter(_, _)
//
//}                                

// ---------------- symbolToValue ------------------
// TODO: rewrite the following code using
// - exisiting code in lang::rascal::grammar (e.d. striprec, delabel etc.
// - remove duplication of 'layouts', 'regular' and 'intermix', 'sym2prod'
// - descent operator rather than inductive definition.
// Attention points:
// - Introduce a GrammarDefinition as soon as possible, then existing tools can work on it.
// - The type checker introduces a second form of productionL of the form:
//   prod(AType def, str cons, list[AType] symbols, set[Attr] attributes)
//   to record the constructor name. Remove these as soon as possible.
// - Consistent introduction of layout.

// symbolToValue1 is a caching wrapper around symbolToValue

//public AType symbolToValue(AType symbol) {
//    if(atypeToValueCache[symbol]?){
//        return atypeToValueCache[symbol];
//    }
//    res = symbolToValue1(symbol);
//    atypeToValueCache[symbol] = res;
//    return res;
//}

//public AType symbolToValue(AType symbol) {
//   	
//	// Recursively collect all the type definitions associated with a given symbol
//	
// 	map[AType,AProduction] definitions = collectDefs(symbol, ());
// 	
// 	return symbol; //type(symbol, definitions); 
//}

//@memo
//set[AType] layoutDefs(map[AType,AProduction] prod) = {s | AType s <- prod, (layouts(_) := s || label(_,layouts(_)) := s)};

//public map[AType,AProduction] collectDefs(AType symbol, map[AType,AProduction] definitions){
//    tuple[AType symbol, map[AType,AProduction] definitions] tup = <symbol, definitions>;
//    
//    if(collectDefsCache[tup]?){
//       return collectDefsCache[tup];
//    }
//    map[AType, AProduction] result = collectDefs1(symbol, definitions);
//    collectDefsCache[tup] = result;
//    return result;
//}
