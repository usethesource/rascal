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
private rel[Symbol,Symbol] reachableTypes = {};

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
    
    grammar = ( config.store[uid].rtype : config.grammar[uid] | int uid <- config.grammar, 
   	  																config.store[uid].rtype in types );
   
    productions = { p.def is label ? <p.def.symbol, Symbol::prod(p.def.symbol, p.def.name, p.symbols, p.attributes)> 
	                               : <p.def, Symbol::prod(p.def, "", p.symbols, p.attributes)> 
	                                     | /Production p:prod(_,_,_) := grammar };
   	
   	starts = { config.store[uid].rtype | int uid <- config.starts, config.store[uid].rtype in types };
   	
   	activeLayouts = { \type | \type <- types, Symbol::layouts(_) := \type };
   	if(!isEmpty(activeLayouts)) {
   		activeLayout = getOneFrom(activeLayouts);
   	}
   	computeReachableTypes();
}

// Extract the declared grammar from a type checker configuration

public map[Symbol,Production] getGrammar() {
	map[Symbol,Production] definitions =   ( nonterminal : \layouts(grammar[nonterminal]) | nonterminal <- grammar ) 
										 + ( Symbol::\start(nonterminal) : \layouts(Production::choice(Symbol::\start(nonterminal),
																					   				   { Production::prod(Symbol::\start(nonterminal), [ Symbol::\label("top", nonterminal) ],{}) })) 
																				| nonterminal <- starts );
	if(<str n, Symbol def> <- typeRel, Symbol::\layouts(_) := def) {
 		definitions = reify(def,definitions);
 	}
 	definitions = definitions + (Symbol::\layouts("$default$"):Production::choice(Symbol::\layouts("$default$"),{Production::prod(Symbol::\layouts("$default$"),[],{})}));
 	definitions = definitions + (Symbol::\empty():Production::choice(Symbol::\empty(),{Production::prod(Symbol::\empty(),[],{})}));
 	
 	//println("getGrammar returns:\n----------");
 	//println("(");
 	//for(s <- definitions) println("<s>: <definitions[s]>,");
 	//println(")\n----------");
 	
 	return definitions;
}

// Extract all declared symbols from a type checker configuration

public map[Symbol,Production] getDefinitions() {
   	// Collect all symbols
   	set[Symbol] symbols = types + carrier(constructors) + carrier(productions) + domain(grammar);
   	
   	//println("getDefinitions:");
   	//for(Symbol s <- symbols){
   	//    println("\t<s>");
   	//}
    
   	// and find their definitions
   	//iprintln(TreeDefinitions);
   	map[Symbol,Production] definitions  = (() | reify(symbol, it) | Symbol symbol <- symbols);
 
 	//iprintln(definitions);
 	
 	return definitions;
}

// Extract the reachability relation for all types. This information can be used by generated code for
// - descendant match
// - visit

private void computeReachableTypes(){
	definitions = getDefinitions();
	//for(d <- definitions){println("\t<d>: <definitions[d]>"); }
	rel[Symbol,Symbol] containment = {};
	for(parent <- definitions){
	    //println("parent = <parent>");
	    prod_choice = definitions[parent];
	    for(Production alt <- prod_choice.alternatives){
	    	//println("alt = <alt>, usedSymbols = <usedSymbols(alt)>");
			for(sym <- usedSymbols(alt)){
		 		for(sym2 <- contains(sym)){
		 			containment += <sym, sym2>;
		 		}
		 		//println("add \<<sym>, <contains(sym)>\>");
				for(sym2 <- sym + contains(sym)){
					containment += <parent, sym2>;
				}
				//println("add \<<parent>, <sym + contains(sym)>\>");
			}
		}
	}
	//println("containment+ ="); for(elm <- containment+) println("\t<elm>");
	reachableTypes = containment+;
}

// Extract the reachable types for a given type

set[Symbol] getReachableTypes(Symbol patternType){
	patternType = regulars(patternType, activeLayout);	// make sure that concrete list types have layout inserted
	//println("getReachableTypes: <patternType>");
	res = patternType + contains(patternType) + {sym1 | <sym1, sym2> <- reachableTypes, subtype(sym2, patternType)};
	if(adt("Tree",[]) in res){
		res += \value();
	}
	//println("res: <res>");
	return res;
}

// Find symbols used in Productions

private set[Symbol] usedSymbols(Production::\cons(Symbol def, list[Symbol] symbols, list[Symbol] kwTypes, map[str, value(map[str,value])] kwDefaults, set[Attr] attributes)) {
    if(size(kwDefaults) != 0){
    	println("*************** kwDefaults = <kwDefaults>");
    }
	return filterSymbols(symbols + kwTypes);
}	

private set[Symbol]  usedSymbols(Production::\func(Symbol def, list[Symbol] symbols, list[Symbol] kwTypes, map[str, value(map[str,value])] kwDefaults, set[Attr] attributes)) {
    if(size(kwDefaults) != 0){
    	println("*************** kwDefaults = <kwDefaults>");
    }
	return filterSymbols(symbols + kwTypes);
}

private set[Symbol] usedSymbols(Production::\choice(Symbol def, set[Production] alternatives)) =
	{ *usedSymbols(alt) | alt <- alternatives };
	
private set[Symbol] usedSymbols(Production::prod(Symbol def, list[Symbol] symbols, set[Attr] attributes)) = 
	filterSymbols(symbols);

private set[Symbol] usedSymbols(Production::regular(Symbol def)) = 
	filterSymbols([symbol]);

private set[Symbol] usedSymbols(Production::\priority(Symbol def, list[Production] choices)) =
	{ *usedSymbols(c) | c <- choices };
	
private set[Symbol] usedSymbols(Production::\associativity(Symbol def, Associativity \assoc, set[Production] alternatives)) =
	{ *usedSymbols(a) | a <- alternatives };
	

private Symbol simplify(Symbol symbol){
	if(conditional(sym1, _) := symbol) return simplify(sym1);
	if(label(_, sym1) := symbol) return simplify(sym1);
	return symbol;
}

private bool ignore(Symbol symbol){
	switch(symbol){
		case \lit(_): 		 return true;
		case \cilit(_):		 return true;
		case \char-class(_): return true;
		case \layouts(_):	 return true;
		default:
							 return false;
       }
}

private set[Symbol] filterSymbols(list[Symbol] symbols) =
	{symbol1 | symbol <- symbols, symbol1 := simplify(symbol), !ignore(symbol1)};	

// Find directly dependent Symbols for a given Symbol

// From Type.rsc

private set[Symbol] contains(Symbol::\label(str name, Symbol symbol)) = filterSymbols([symbol]);

private set[Symbol] contains(Symbol::\set(Symbol symbol)) = filterSymbols([symbol]);
	
private set[Symbol] contains(Symbol::\rel(list[Symbol] symbols)) = filterSymbols(symbols);
	
private set[Symbol] contains(Symbol::\lrel(list[Symbol] symbols)) = filterSymbols(symbols);

private set[Symbol] contains(Symbol::\tuple(list[Symbol] symbols)) = filterSymbols(symbols);
	
private set[Symbol] contains(Symbol::\list(Symbol symbol)) = filterSymbols([symbol]);

private set[Symbol] contains(Symbol::\map(Symbol from, Symbol to)) = filterSymbols([from, to]);

private set[Symbol] contains(Symbol::\bag(Symbol symbol)) = filterSymbols([symbol]);

private set[Symbol] contains(Symbol::\adt(str name, list[Symbol] parameters)) = {};
	
private set[Symbol] contains(Symbol::\cons(Symbol \adt, str name, list[Symbol] parameters)) = filterSymbols(parameters);

private set[Symbol] contains(Symbol::\alias(str name, list[Symbol] parameters, Symbol aliased)) = filterSymbols([aliased]);

private set[Symbol] contains(Symbol::\func(Symbol ret, list[Symbol] parameters)) =	filterSymbols(parameters);
	
private set[Symbol] contains(Symbol::\var-func(Symbol ret, list[Symbol] parameters, Symbol varArg)) =	filterSymbols(parameters + varArg);

private set[Symbol] contains(Symbol::\reified(Symbol symbol)) = filterSymbols([symbol]);

private set[Symbol] contains(Symbol::\parameter(str name, Symbol bound)) = {};

// Parse tree constructors from ParseTree.rsc

private set[Symbol] contains(Symbol::\start(Symbol symbol)) = filterSymbols([symbol]);

private set[Symbol] contains(Symbol::\parameterized-sort(str name, list[Symbol] parameters)) = filterSymbols(parameters);

private set[Symbol] contains(Symbol::\parameterized-lex(str name, list[Symbol] parameters)) = filterSymbols(parameters);

private set[Symbol] contains(Symbol::\opt(Symbol symbol)) = filterSymbols([symbol]);

private set[Symbol] contains(Symbol::\iter(Symbol symbol)) = \char-class(_) := symbol ? {} : filterSymbols([symbol]);

private set[Symbol] contains(Symbol::\iter-star(Symbol symbol)) = \char-class(_) := symbol ? {} : filterSymbols([symbol]);

private set[Symbol] contains(Symbol::\iter-seps(Symbol symbol, list[Symbol] separators)) = filterSymbols([symbol]);	// Note: we ignore the separators

private set[Symbol] contains(Symbol::\iter-star-seps(Symbol symbol, list[Symbol] separators)) = filterSymbols([symbol]);

private set[Symbol] contains(Symbol::\alt(set[Symbol] alternatives)) = filterSymbols(toList(alternatives));

private set[Symbol] contains(Symbol::\seq(list[Symbol] symbols)) = filterSymbols(symbols);

private set[Symbol] contains(Symbol::\conditional(Symbol symbol, set[Condition] conditions)) = contains(symbol);

private default set[Symbol] contains(Symbol symbol){
	//println("default: <symbol>");
	return {};
}

// 
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