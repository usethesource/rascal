module lang::rascalcore::compile::Rascal2muRascal::TypeReifier

/*
 * This module handles the mapping between types and reified types. It defines the following functions:
 *	   (1) void 				  resetTypeReifier()			Reset the global state of this module
 *     (2) map[AType,AProduction] getGrammar() 					Extract only syntax definitions
  *    (3) map[AType,AProduction] getDefinitions() 				Extract all defined symbols
 *     (4) type[value]            symbolToValue(AType) 		Compute the reified type for a symbol
 */

import lang::rascalcore::check::Checker;

//import lang::rascal::types::CheckerConfig;
//import lang::rascal::types::AbstractName;
//import lang::rascal::types::AbstractType;

import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;

import lang::rascalcore::grammar::definition::Symbols;

import ParseTree;
import List;
import Map;
import Set;
import Relation;

import IO;

private rel[str,AType] typeRel = {};
private set[AType] types = {};
private rel[AType,AProduction] constructors = {};
private rel[AType,AType] productions = {};
private map[AType,AProduction] grammar = ();
private map[AType,AProduction] cachedGrammar = ();
private map[AType,AProduction] instantiatedGrammar = ();
private set[AType] starts = {};
private AType activeLayout = layouts("$default$");
private rel[value,value] reachableTypes = {};
private rel[AType,AType] reachableConcreteTypes = {};
private bool reachableInfoAvailable = false;

map[tuple[AType symbol, map[AType,AProduction] definitions], map[AType,AProduction]] reifyCache = ();

private map[AType symbol, type[value] resType] atypeToValueCache = ();

public void resetTypeReifier() {
    typeRel = {};
    types = {};
    constructors = {};
    productions = {};
    grammar = ();
    cachedGrammar = ();
    instantiatedGrammar = ();
    starts = {};
    activeLayout = layouts("$default$");
    reachableTypes = {};
    reachableConcreteTypes = {};
    reachableInfoAvailable = false;
    reifyCache = ();
    atypeToValueCache = ();
}

// Extract common declaration info and save it for later use by
// - getGrammar
// - symbolToValue

public void extractDeclarationInfo(TModel config){}

//public void extractDeclarationInfo(TModel config){
//    resetTypeReifier();
//    
//    // Collect all the types that are in the type environment
//    // TODO: simplify
//	typeRel = { < getSimpleName(rname), config.store[config.typeEnv[rname]].rtype > | rname <- config.typeEnv, config.store[config.typeEnv[rname]] has rtype }
//	        + { < getSimpleName(rname) , rtype > | int uid <- config.store, sorttype(rname,rtype,_,_,_) := config.store[uid] }
//            + { < getSimpleName(config.store[uid].name), config.store[uid].rtype > | int uid <- config.store, config.store[uid] has name, config.store[uid] has rtype }
//            + { <"Tree", adt("Tree",[])> }
//            + { <"AType", adt("AType",[])> }
//            + { <"Production", adt("Production",[])> }
//            + { <"Attr", adt("Attr",[])> }
//            + { <"Associativity", adt("Associativity",[])> }
//            + { <"CharRange", adt("CharRange",[])> }
//            + { <"CharClass", adt("CharClass",[])> }
//            + { <"Condition", adt("Condition",[])> }
//            ;
//    //println("typeRel:");for(x <- typeRel) println("\t<x>");
//	// Collect all the constructors of the adt types in the type environment
//	 
//	KeywordParamMap getCommons(str adt) 
//	  = (config.typeEnv[RSimpleName(adt)]?) ? config.store[config.typeEnv[RSimpleName(adt)]].keywordParams : ();
//	
//	Production value2prod(constructor(_, AType rtype, KeywordParamMap keywordParams, int containedIn, _, loc at), KeywordParamMap commonParams)
//	  = \cons(label(rtype.name, rtype.\adt)
//	         , rtype.parameters
//	         , [label(l, keywordParams[n]) | n:RSimpleName(l) <- keywordParams]
//	         + [label(l, commonParams[n])  | n:RSimpleName(l) <- commonParams]
//	         , {});  
//	  
//	constructors = { <c.rtype.\adt, value2prod(c, getCommons(c.rtype.\adt.name))> 
//	               | int uid <- config.store, c := config.store[uid], c is constructor
//	               };
//
//    typeRel += { <n, a> | <AType a:adt(str n,_),_> <- constructors };
//    
//    types = range(typeRel);
//    
//	// Collect all the fuctions of the non-terminal types in the type environment
//    
//    grammar = ( config.store[uid].rtype : config.grammar[uid] | int uid <- config.grammar, config.store[uid].rtype in types );
//   
//    productions = { p.def is label ? <p.def.symbol, AType::prod(p.def.symbol, p.def.name, p.symbols, p.attributes)>  //TODO: p.def.name???
//	                               : <p.def, AType::prod(p.def, "", p.symbols, p.attributes)> 						  // TODO ""??
//	              | rtype <- grammar, /Production p:prod(_,_,_) := grammar[rtype]
//	              };
//	   
//   	starts = { config.store[uid].rtype | int uid <- config.starts, config.store[uid].rtype in types };
//   	
//   	activeLayouts = { \type | \type <- types, AType::layouts(_) := \type };
//   	if(!isEmpty(activeLayouts)) {
//   	    for(al <- activeLayouts){
//   	        if(!hasManualTag(grammar[al])){
//   	           activeLayout = al;
//   	           break;
//   	        }
//   	    }
//   	}
//    cachedGrammar = getGrammar1();
//    //iprintln(cachedGrammar);
//   	//computeReachableTypesAndConstructors();
//}

private bool hasManualTag(\choice(AType def, set[Production] alternatives)) =
    any(Production alt <- alternatives, hasManualTag(alt));

private bool hasManualTag(Production p) =
    p has attributes && \tag("manual"()) in p.attributes;


private map[AType,Production] getGrammar1() {
    set[AType] theLayoutDefs = layoutDefs(grammar);
    
	map[AType,Production] definitions =   
		( nonterminal : \layouts(grammar[nonterminal], theLayoutDefs) | nonterminal <- grammar) 
		+ 
		( AType::\start(nonterminal) : \layouts(Production::choice(AType::\start(nonterminal), { Production::prod(AType::\start(nonterminal), [ AType::\label("top", nonterminal) ],{}) }),{}) 
		| nonterminal <- starts );
	if(<str n, AType def> <- typeRel, AType::\layouts(_) := def) {
 		definitions = reify(def,definitions);
 	}
 	definitions = definitions + (AType::\layouts("$default$"):Production::choice(AType::\layouts("$default$"),{Production::prod(AType::\layouts("$default$"),[],{})}));
 	definitions = definitions + (AType::\empty():Production::choice(AType::\empty(),{Production::prod(AType::\empty(),[],{})}));
 	
 	//iprintln(definitions);
 	return definitions;
}

// Extract all declared symbols from a type checker configuration

public map[AType,AProduction] getDefinitions() {
    return (); // TODO
  // 	// Collect all symbols
  // 	set[AType] symbols = types + domain(constructors) + carrier(productions) + domain(grammar);
  // 	
  // 	map[AType,Production] definitions  = (() | reify(symbol, it) | AType symbol <- symbols);
 	//
 	//return definitions;
}

public Production getLabeledProduction(str name, AType symbol){
	//println("getLabeledProduction: <getGrammar()[symbol]>");
	name = unescape(name);
	visit(cachedGrammar[symbol]){
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
	visit(cachedGrammar){
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
	for(AType lhs <- cachedGrammar){
		if(\parameterized-sort(name, list[AType] parameters) := lhs){
			for(AType ip <- instantiated_params, ip.name == name, size(ip.parameters) == size(parameters)){
				iprod = instantiateParameterizedProduction(cachedGrammar[lhs], ip.parameters);
				instantiated_productions[iprod.def] = iprod;
			}
		}
	}	
	return instantiated_productions;
}

private void computeReachableConcreteTypes(){
	instantiatedGrammar = cachedGrammar + instantiateAllParameterizedProductions();
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
 //   desiredPatternTypes = { s | /AType s := patternTypes};
	//desiredSubjectTypes = { s | /AType s := subjectType};
	//desiredTypes = desiredSubjectTypes + desiredPatternTypes;
	//
	//if(any(sym <- desiredTypes, sort(_) := sym || lex(_) := sym || subtype(sym, adt("Tree",[])))){
	//	// We just give up when abstract and concrete symbols occur together
	//	//println("descend_into (abstract) [1]: {value()}");
	//   return <{\value()}, {}>;
	//}
	////println("desiredSubjectTypes = <desiredSubjectTypes>");
	////println("desiredTypes = <desiredTypes>");
	//prunedReachableTypes = reachableTypes ;
	//if(\value() notin desiredSubjectTypes){
	//    // if specific subject types are given, the reachability relation can be further pruned
	//	prunedReachableTypes = carrierR(reachableTypes,reachableTypes[desiredSubjectTypes]);
	//	//println("removed from reachableTypes:[<size(reachableTypes - prunedReachableTypes)>]"); //for(x <- reachableTypes - prunedReachableTypes){println("\t<x>");}
	//}
	//
	////println("prunedReachableTypes: [<size(prunedReachableTypes)>]"); //for(x <- prunedReachableTypes){println("\t<x>");}
	//descend_into = desiredTypes;
	//
	//// TODO <AType from ,AType to> <- ... makes the stack validator unhappy.
	//for(<AType from, AType to> <- prunedReachableTypes){
	//	if(to in desiredTypes){		// TODO || here was the cause 
	//		descend_into += {from, to};
	//	} else if(any(AType t <- desiredTypes, subtype(t, to))){
	//		descend_into += {from, to};
	//	} else if(c:acons(AType \adtsym, list[AType] parameters) := from  && // TODO: check
	//                        (\adtsym in patternTypes || name in consNames)){
	//              descend_into += {from, to};   
	//	} else if(c:acons(AType \adtsym, str name, list[AType] parameters) := to  && 
	//                        (\adtsym in patternTypes || name in consNames)){
	//              descend_into += {from, to};        
	//    }
	//    ;
	//}
	//
	////if(\value() in descend_into){
	////    println("replace by value, descend_into [<size(descend_into)>]:"); for(elm <- descend_into){println("\t<elm>");};
	////	descend_into = {\value()};
	////}
	//tuples = { atuple(atypeList(symbols)) | sym <- descend_into, \rel(symbols) := sym || \lrel(symbols) := sym };
	//descend_into += tuples;
	//descend_into = {sym | sym <- descend_into, label(_,_) !:= sym };
	////println("descend_into (abstract) [<size(descend_into)>]:"); //for(elm <- descend_into){println("\t<elm>");};
	//
	//return <descend_into, {}>;
}

// Extract the reachable concrete types

private  tuple[set[AType], set[Production]] getReachableConcreteTypes(AType subjectType, set[str] consNames, set[AType] patternTypes){
	desiredPatternTypes = { s | /AType s := patternTypes};
	desiredSubjectTypes = { s | /AType s := subjectType};
	desiredTypes = desiredPatternTypes;
	
	//println("desiredPatternTypes = <desiredPatternTypes>");
	
	prunedReachableConcreteTypes = reachableConcreteTypes;
	if(\value() notin desiredSubjectTypes){
	    // if specific subject types are given, the reachability relation can be further pruned
		prunedReachableConcreteTypes = carrierR(reachableConcreteTypes, (reachableConcreteTypes)[desiredSubjectTypes] + desiredSubjectTypes);
		//println("removed from reachableConcreteTypes:"); for(x <- reachableConcreteTypes - prunedReachableConcreteTypes){println("\t<x>");}
	}
	
	set [Production] descend_into = {};
	
	// Find all concrete types that can lead to a desired type
    for(<AType sym, AType tp> <- (prunedReachableConcreteTypes+), tp in desiredPatternTypes){
	   alts = instantiatedGrammar[sym];
	   for(/Production p := alts){
	       switch(p){
	       case choice(_, choices): descend_into += choices;
	       case associativity(_, _, set[Production] choices): descend_into += choices;
	       case priority(_, list[Production] choices): descend_into += toSet(choices);
	       default:
	    	descend_into += p;
	       }
	    }
	} 
	
	set [Production] descend_into1 = {};
	
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

public type[value] symbolToValue(AType symbol) {
    if(atypeToValueCache[symbol]?){
        return atypeToValueCache[symbol];
    }
    res = symbolToValue1(symbol);
    atypeToValueCache[symbol] = res;
    return res;
}

private type[value] symbolToValue1(AType symbol) {
   	
	// Recursively collect all the type definitions associated with a given symbol
	
 	map[AType,AProduction] definitions = reify(symbol, ());
 	
 	if(AType::\start(AType sym) := symbol){
 	    definitions += (symbol : choice(symbol, { prod(symbol,
                                                       [ activeLayout,
                                                         label("top", sym),
                                                         activeLayout
                                                       ],
                                                    {})}));
    }
 	                                             
 	der_symbol = (conditional(AType sym,_) := symbol || AType::\start(AType sym) := symbol) ? sym : symbol;
 	
 	if(AType::\sort(_):= der_symbol || AType::\lex(_):= der_symbol ||
 		AType::\parameterized-sort(_,_):= der_symbol || AType::\parameterized-lex(_,_):= der_symbol) {
 			if(<str n, AType def> <- typeRel, AType::\layouts(_) := def) {
 				definitions = reify(def,definitions);
 			}
 			definitions = definitions + (AType::\layouts("$default$"):AProduction::choice(AType::\layouts("$default$"),{AProduction::prod(AType::\layouts("$default$"),[],{})}));
 			definitions = definitions + (AType::\empty():AProduction::choice(AType::\empty(),{AProduction::prod(AType::\empty(),[],{})}));
 	}
 	
 	return type(symbol, definitions); 
}

@memo
set[AType] layoutDefs(map[AType,AProduction] prod) = {s | AType s <- prod, (layouts(_) := s || label(_,layouts(_)) := s)};

public map[AType,AProduction] reify(AType symbol, map[AType,AProduction] definitions){
    tuple[AType symbol, map[AType,AProduction] definitions] tup = <symbol, definitions>;
    
    if(reifyCache[tup]?){
       return reifyCache[tup];
    }
    map[AType, AProduction] result = reify1(symbol, definitions);
    reifyCache[tup] = result;
    return result;
}

map[AType,AProduction] reify1(\cons(AType def, list[AType] symbols, list[AType] kwTypes, set[Attr] attributes), map[AType,AProduction] definitions)
  = (definitions | reify1(sym, it) | AType sym <- symbols + kwTypes);
  
map[AType,AProduction] reify1(prod(AType def, list[AType] symbols, set[Attr] attributes), map[AType,AProduction] definitions)
  = (definitions | reify1(sym, it) | AType sym <- symbols);

// primitive
private map[AType,AProduction] reify1(AType symbol, map[AType,AProduction] definitions) 
	= definitions when isIntType(symbol) || isBoolType(symbol) || isRealType(symbol) || isRatType(symbol) ||
					   isStrType(symbol) || isNumType(symbol) || isNodeType(symbol) || isVoidType(symbol) ||
					   isValueType(symbol) || isLocType(symbol) || isDateTimeType(symbol);
					   
// labeled					   
private map[AType,AProduction] reify1(AType::\label(str name, AType symbol), map[AType,AProduction] definitions)
	= reify1(symbol, definitions);
	
// set
private map[AType,AProduction] reify1(AType::\set(AType symbol), map[AType,AProduction] definitions)
	= reify1(symbol, definitions);
	
// rel
private map[AType,AProduction] reify1(AType::\rel(list[AType] symbols), map[AType,AProduction] definitions)
	= ( definitions | reify1(sym, it) | AType sym <- symbols );
	
// list
private map[AType,AProduction] reify1(AType::\list(AType symbol), map[AType,AProduction] definitions)
	= reify1(symbol, definitions);
	
// lrel
private map[AType,AProduction] reify1(AType::\lrel(list[AType] symbols), map[AType,AProduction] definitions)
	= ( definitions | reify1(sym, it) | sym <- symbols );
	
// bag
private map[AType,AProduction] reify1(AType::\bag(AType symbol), map[AType,AProduction] definitions)
	= reify1(symbol, definitions);
	
// tuple
private map[AType,AProduction] reify1(AType::\tuple(list[AType] symbols), map[AType,AProduction] definitions)
	= ( definitions | reify1(sym, it) | AType sym <- symbols );
	
// map
private map[AType,AProduction] reify1(AType::\map(AType from, AType to), map[AType,AProduction] definitions)
	= reify1(from, definitions) + reify1(to, definitions);



// adt
private map[AType,AProduction] reify1(AType::\adt(str name, list[AType] symbols), map[AType,AProduction] definitions) {
	set[AType] defs = typeRel[name];
	//println("reify1 adt: <name>, <symbols>, <defs>, <constructors>");

    for(AType s <- defs){
	   if(adtDef: AType::\adt(name,_) := s){
          //assert AType::\adt(name,_) := adtDef;
          if(!definitions[adtDef]?) {
        	 alts = constructors[adtDef];
        	 definitions[adtDef] = AProduction::\choice(adtDef, alts);
        	 definitions = ( definitions | reify1(prod, it) | AProduction prod <- constructors[adtDef] );
          }
          definitions = ( definitions | reify1(sym, it) | sym <- symbols );
          //println("reify1 adt <name> =\> <definitions>");
          return definitions;
       }
    }
    throw "No definition for ADT <name>(<symbols>)";
}

// constructors
private map[AType,AProduction] reify1(AType::\cons(AType \adt, str name, list[AType] parameters), map[AType,AProduction] definitions)
	// adt has been already added to the definitions
	= ( definitions | reify1(sym, it) | AType sym <- parameters );
	
// alias
private map[AType,AProduction] reify1(AType::\alias(str name, list[AType] parameters, AType aliased), map[AType,AProduction] definitions) {
    //println("reify1 alias: <name>, <aliased>");
	definitions = reify1(aliased, definitions);
	definitions = ( definitions | reify1(sym, it) | AType sym <- parameters );
	return definitions;
}

// function
private map[AType,AProduction] reify1(AType::\func(AType ret, list[AType] parameters, list[AType] kws), map[AType,AProduction] definitions) {
    //println("reify1 function: <ret>, <parameters>, <definitions>");
	definitions = reify1(ret, definitions);
	definitions = ( definitions | reify1(sym, it) | AType sym <- parameters );
	definitions = ( definitions | reify1(sym, it) | AType sym <- kws );
	return definitions;
}

// function with varargs
private map[AType,AProduction] reify1(AType::\var-func(AType ret, list[AType] parameters, AType varArg), map[AType,AProduction] definitions) {
    //println("reify1 function varargs: <ret>, <parameters>, <varArg>, <definitions>");
	definitions = reify1(ret, definitions);
	definitions = ( definitions | reify1(sym, it) | AType sym <- parameters );
	definitions = reify1(varArg, definitions);
	return definitions;
}

// reified
private map[AType,AProduction] reify1(AType::\reified(AType ret), map[AType,AProduction] definitions) {
    //println("reify1 reified: <ret>, <definitions>");
	return reify1(ret, definitions);
}	
	
// parameter
private map[AType,AProduction] reify1(AType::\parameter(str name, AType bound), map[AType,AProduction] definitions)
	= reify1(bound, definitions);
	
// sort, lex
private map[AType,AProduction] reify1(AType symbol, map[AType,AProduction] definitions) 
	= { 
	    set[AType] defs = typeRel[name];
		//println("reify1: symbol-<symbol>, name=<name>"); 
		//assert !(AType::\adt(name,_) := nonterminal);
		for(AType nonterminal <- defs){
		    if(AType::\adt(name,_) !:= nonterminal){
		       //println("nonterminal1=<nonterminal>");
		       if(prod(AType s, _, _, _) := nonterminal){
		          nonterminal = s;
		       }
		       // println("nonterminal2=<nonterminal>");
		       if(!definitions[nonterminal]?) {
		          //println("grammar:\n----------");
            //      for(s <- grammar) println("<s>: <grammar[s]>");
            //      println("----------");
                  if(grammar[nonterminal]?){
			         definitions[nonterminal] = \layouts(grammar[nonterminal], layoutDefs(grammar)); // inserts an active layout
			         if(nonterminal in starts) {
				        definitions[AType::\start(nonterminal)] = \layouts(AProduction::choice(AType::\start(nonterminal),
																					   { AProduction::prod(AType::\start(nonterminal), [ AType::\label("top", nonterminal) ],{}) }),{});
			         }
			     //println("AProductions[nonterminal]: <productions[nonterminal]>");
			     //println("Domain(grammar): <domain(grammar)>");
			    definitions = ( definitions | reify1(sym, it) | sym <- productions[nonterminal] );
			    } else {
			      println("reify1: <nonterminal> skipped");
			    }
		     }
		     return definitions;
		  }
		}
		throw "No definition for symbol <name>";
	  } when AType::\sort(str name) := symbol || AType::\lex(str name) := symbol ||
	  		 (AType::\layouts(str name) := symbol && name != "$default$") || AType::\keywords(str name) := symbol;

// parameterized-sort, parameterized-lex  
private map[AType,AProduction] reify1(AType symbol, map[AType,AProduction] definitions) 
	= { 
	    set[AType] defs = typeRel[name];
	    set[AType] theLayoutDefs = layoutDefs(definitions);
		//assert !(AType::\adt(name,_) := nonterminal);
		for(AType nonterminal <- defs){
            if(AType::\adt(name,_) !:= nonterminal){
		       if(!definitions[nonterminal]?) {
			      definitions[nonterminal] = \layouts(grammar[nonterminal], theLayoutDefs); // inserts an active layout
			      if(nonterminal in starts) {
				     definitions[AType::\start(nonterminal)] = \layouts(AProduction::choice(AType::\start(nonterminal),
															    { AProduction::prod(AType::\start(nonterminal), [ AType::\label("top", nonterminal) ],{}) }),{});
			      }
			      //println("AProductions[nonterminal]: <productions[nonterminal]>");
			     //println("Domain(grammar): <domain(grammar)>");
			     definitions = ( definitions | reify1(sym, it) | sym <- productions[nonterminal] );
		      }
		      definitions = ( definitions | reify1(sym, it) | sym <- parameters );
		       return definitions;
		   }
		}
		throw "No definition for symbol <name>";
	  } when AType::\parameterized-sort(str name, list[AType] parameters) := symbol || AType::\parameterized-lex(str name, list[AType] parameters) := symbol;

private map[AType,AProduction] reify1(AType symbol, map[AType,AProduction] definitions)
	= reify1(sym, definitions)
		when AType::\start(AType sym) := symbol || AType::\opt(AType sym) := symbol || AType::\iter(AType sym) := symbol ||
			 AType::\iter-star(AType sym) := symbol || AType::\iter-seps(AType sym, _) := symbol ||
			 AType::\iter-star-seps(AType sym, list[AType] _) := symbol;

private map[AType,AProduction] reify1(AType::\alt(set[AType] alternatives), map[AType,AProduction] definitions)
	= ( definitions | reify1(sym, it) | AType sym <- alternatives );

private map[AType,AProduction] reify1(AType::\seq(list[AType] symbols), map[AType,AProduction] definitions)
	= ( definitions | reify1(sym, it) | AType sym <- symbols );

private map[AType,AProduction] reify1(AType::\conditional(AType symbol, set[Condition] conditions), map[AType,AProduction] definitions)
	= reify1(symbol, definitions) + ( definitions | reify1(cond, it) | Condition cond <- conditions );
	
private map[AType,AProduction] reify1(AType::\prod(AType \sort, str name, list[AType] parameters, set[Attr] _), map[AType,AProduction] definitions)
	// sort has been already added to the definitions
	= ( definitions | reify1(sym, it) | AType sym <- parameters );
	
private map[AType,AProduction] reify1(Condition cond, map[AType,AProduction] definitions)
	= reify1(symbol, definitions)
		when Condition::\follow(AType symbol) := cond || Condition::\not-follow(AType symbol) := cond ||
			 Condition::\precede(AType symbol) := cond || Condition::\not-precede(AType symbol) := cond ||
			 Condition::\delete(AType symbol) := cond;
			 
private map[AType,AProduction] reify1(Condition cond, map[AType,AProduction] definitions) = definitions;
		   
private default map[AType,AProduction] reify1(AType symbol, map[AType,AProduction] definitions) = definitions;

@doc{Intermix with an active layout}
public AProduction \layouts(AProduction prod, set[AType] others) {
  return top-down-break visit (prod) {
    case AProduction::prod(\start(y),[AType x],as)                                  => AProduction::prod(\start(y),[activeLayout, x, activeLayout],  as)
    case AProduction::prod(sort(s),list[AType] lhs,as)                              => AProduction::prod(sort(s),intermix(lhs,activeLayout, others),as)
    case AProduction::prod(\parameterized-sort(s,n),list[AType] lhs,as)             => AProduction::prod(\parameterized-sort(s,n),intermix(lhs,activeLayout,others),as)
    case AProduction::prod(label(t,sort(s)),list[AType] lhs,as)                     => AProduction::prod(label(t,sort(s)),intermix(lhs,activeLayout,others),as)
    case AProduction::prod(label(t,\parameterized-sort(s,n)),list[AType] lhs,as)    => AProduction::prod(label(t,\parameterized-sort(s,n)),intermix(lhs,activeLayout,others),as) 
  }
} 

// add layout symbols between every pair of symbols, but not when there is already a layout symbol:
 list[AType] intermix([*AType y, AType a, AType b, *AType z], AType l, set[AType] others) = intermix([*y, regulars(a,l,others), l, regulars(b,l,others), *z], l, others)   
    when Avoid := {*others,l}, a notin Avoid, b notin Avoid;

// for singletons its only important to mix layout in nested regulars as well:
list[AType] intermix([AType a], AType l, set[AType] others) = [regulars(a, l, others)];

// when the first rule is done (which is recursive), this one yields the result:
default list[AType] intermix(list[AType] syms, AType _, set[AType] _) = syms;

private AType regulars(AType s, AType l, set[AType] others) {
  return visit(s) {
    case \iter(AType n) => \iter-seps(n, [l])
    case \iter-star(AType n) => \iter-star-seps(n, [l]) 
    case \iter-seps(AType n, [AType sep]) => \iter-seps(n,[l,sep,l]) when !(sep in others), !(seq([a,_,b]) := sep && (a in others || b in others))
    case \iter-star-seps(AType n,[AType sep]) => \iter-star-seps(n, [l, sep, l]) when !(sep in others), !(seq([a,_,b]) := sep && (a in others || b in others))
    case \seq(list[AType] elems) => \seq(intermix(elems, l, others)) // note that intermix is idempotent
  }
}

public AType insertLayout(AType s) = regulars(s, activeLayout, {});

public bool hasField(AType s, str fieldName){
    return true; // TODO
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