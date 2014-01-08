@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::types::TypeInstantiation

import List;
import Set;
import IO;
import Node;
import Type;
import ParseTree;

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::TypeExceptions;

public alias Bindings = map[str varName, Symbol varType];

// TODO: Add support for bags if we ever get around to supporting them...
// TODO: Add support for overloaded types if they can make it to here (this is
// usually invoked on specific types that are inside overloads)
public Bindings match(Symbol r, Symbol s, Bindings b) {
	// Strip off labels and aliases
	if (\label(_, lt) := r) return match(lt, s, b);
	if (\label(_, rt) := s) return match(r, rt, b);
	if (\alias(_,_,lt) := r) return match(lt, s, b);
	if (\alias(_,_,rt) := s) return match(r, rt, b);

	// The simple case: if the receiver is a basic type or a node 
	// (i.e., has no internal structure), just do a comparability
	// check. The receiver obviously does not contain a parameter.
	if (arity(r) == 0 && comparable(s,r)) return b;
	
	// Handle parameters
	if (isTypeVar(r)) {
		varName = getTypeVarName(r);
		varBound = getTypeVarBound(r);
		
		if (varName in b) {
			lubbed = lub(s, b[varName]);
			if (!subtype(lubbed, varBound))
				throw invalidMatch(varName, lubbed, varBound);
			b[varName] = lubbed;
		} else {
			b[varName] = s;
		}
		
		return b;
	}
		
	// For sets and relations, check to see if the "contents" are
	// able to be matched to one another
	if ( isSetType(r) && isSetType(s) ) {
		if ( isRelType(r) && isVoidType(getSetElementType(s)) ) {
			return match(getSetElementType(r), \tuple([\void() | idx <- index(getRelFields(r))]), b);
		} else {	
			return match(getSetElementType(r), getSetElementType(s), b);
		}
	}
		
	// For lists and list relations, check to see if the "contents" are
	// able to be matched to one another
	if ( isListType(r) && isListType(s) ) {
		if ( isListRelType(r) && isVoidType(getListElementType(s)) ) {
			return match(getListElementType(r), \tuple([\void() | idx <- index(getListRelFields(r))]), b);
		} else {
			return match(getListElementType(r), getListElementType(s), b);
		}
	}
		
	// For maps, match the domains and ranges
	if ( isMapType(r) && isMapType(s) )
		return match(getMapFieldsAsTuple(r), getMapFieldsAsTuple(s), b);
	
	// For reified types, match the type being reified
	if ( isReifiedType(r) && isReifiedType(s) )
		return match(getReifiedType(r), getReifiedType(s), b);

	// For ADTs, try to match parameters when the ADTs are the same
	if ( isADTType(r) && isADTType(s) && getADTName(r) == getADTName(s) && size(getADTTypeParameters(r)) == size(getADTTypeParameters(s))) {
		rparams = getADTTypeParameters(r);
		sparams = getADTTypeParameters(s);
		for (idx <- index(rparams)) b = match(rparams[idx], sparams[idx], b);
		return b;
	}
			
	// For constructors, match when the constructor name, ADT name, and arity are the same, then we can check params
	if ( isConstructorType(r) && isConstructorType(s) && getADTName(r) == getADTName(s)) {
		b = match(getConstructorArgumentTypesAsTuple(r), getConstructorArgumentTypesAsTuple(s), b);
		return match(getConstructorResultType(r), getConstructorResultType(s), b);
	}
	
	// For functions, match the return types and the parameter types
	if ( isFunctionType(r) && isFunctionType(s) ) {
		b = match(getFunctionArgumentTypesAsTuple(r), getFunctionArgumentTypesAsTuple(s), b);
		return match(getFunctionReturnType(r), getFunctionReturnType(s), b);
	}
	
	// For tuples, check the arity then match the item types
	if ( isTupleType(r) && isTupleType(s) && getTupleFieldCount(r) == getTupleFieldCount(s) ) {
		rfields = getTupleFieldTypes(r);
		sfields = getTupleFieldTypes(s);
		for (idx <- index(rfields)) b = match(rfields[idx], sfields[idx], b);
		return b;
	}
	
	throw invalidMatch(r, s);
}

private void invalidInstantiation() {
	throw "Binding violates parameter bounds!";
}

@doc{Instantiate type parameters found inside the types.}
public Symbol instantiate(Symbol t:\void(), Bindings bindings) = t;
public Symbol instantiate(\label(str x, Symbol t), Bindings bindings) = \label(x, instantiate(t,bindings));
public Symbol instantiate(\set(Symbol et), Bindings bindings) = \set(instantiate(et,bindings));
public Symbol instantiate(\rel(list[Symbol] ets), Bindings bindings) = \rel([ instantiate(et,bindings) | et <- ets ]);
public Symbol instantiate(\tuple(list[Symbol] ets), Bindings bindings) = \tuple([ instantiate(et,bindings) | et <- ets ]);
public Symbol instantiate(\list(Symbol et), Bindings bindings) = \list(instantiate(et,bindings));
public Symbol instantiate(\lrel(list[Symbol] ets), Bindings bindings) = \lrel([ instantiate(et,bindings) | et <- ets ]);
public Symbol instantiate(\map(Symbol md, Symbol mr), Bindings bindings) = \map(instantiate(md,bindings), instantiate(mr,bindings));
public Symbol instantiate(\bag(Symbol et), Bindings bindings) =\bag(instantiate(et,bindings));
public Symbol instantiate(\parameter(str s, Symbol t), Bindings bindings) = bindings[s] when s in bindings && subtype(bindings[s],t);
public Symbol instantiate(\parameter(str s, Symbol t), Bindings bindings) = invalidInstantiation() when s in bindings && !subtype(bindings[s],t);
public Symbol instantiate(\parameter(str s, Symbol t), Bindings bindings) = \parameter(s,t) when s notin bindings;
public Symbol instantiate(\adt(str s, list[Symbol] ps), Bindings bindings) = \adt(s,[instantiate(p,bindings) | p <- ps]);
public Symbol instantiate(Symbol::\cons(Symbol a, str name, list[Symbol] ps), Bindings bindings) = Symbol::\cons(instantiate(a,bindings), name, [instantiate(p,bindings) | p <- ps]);
public Symbol instantiate(\alias(str s, list[Symbol] ps, Symbol at), Bindings bindings) = \alias(s, [instantiate(p,bindings) | p <- ps], instantiate(at,bindings));
public Symbol instantiate(Symbol::\func(Symbol rt, list[Symbol] ps), Bindings bindings) = Symbol::\func(instantiate(rt,bindings),[instantiate(p,bindings) | p <- ps]);
//public Symbol instantiate(\var-func(Symbol rt, list[Symbol] ps, Symbol va), Bindings bindings) = \var-func(instantiate(rt,bindings),[instantiate(p,bindings) | p <- ps],instantiate(va,bindings));
public Symbol instantiate(\reified(Symbol t), Bindings bindings) = \reified(instantiate(t,bindings));
public default Symbol instantiate(Symbol t, Bindings bindings) = t;