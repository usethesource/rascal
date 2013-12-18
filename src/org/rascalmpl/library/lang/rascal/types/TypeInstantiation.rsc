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

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::TypeExceptions;

public alias Bindings = map[str varName, Symbol varType];

public Bindings defaultMatch(Symbol t, Symbol m, Bindings bindings) {
	if (isFunctionType(t) && isFunctionType(m)) {
		// Our subtyping rules don't work with uninstantiated or partially
		// instantiated function types, so just perform a sanity check here
		// to make sure each type is comparable. If not, fall through, the
		// logic below will throw if appropriate.
		tTypes = [ getFunctionReturnType(t), *getFunctionArgumentTypes(t) ];
		mTypes = [ getFunctionReturnType(m), *getFunctionArgumentTypes(m) ];
		
		if (size(tTypes) == size(mTypes) && false notin {comparable(tTypes[idx],mTypes[idx]) | idx <- index(tTypes)}) {
			return bindings;
		}
	}
	
	if (!comparable(m,t))
		throw invalidMatch(t, m);
		
	return bindings;
}

public default Bindings match(Symbol t, Symbol m, Bindings bindings) {
	return defaultMatch(t,m,bindings);
}

public Bindings match(\label(_,Symbol t), Symbol m, Bindings bindings) {
	return match(t,m,bindings);
}

public Bindings match(Symbol t:\set(Symbol et), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	return match(et,getSetElementType(m),bindings);
}

public Bindings match(Symbol t:\rel(list[Symbol] ets), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	return match(\tuple(ets),getSetElementType(m),bindings);
}

public Bindings match(Symbol t:\tuple(list[Symbol] ets), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	list[Symbol] mts = getTupleFields(m);
	for (idx <- index(ets)) bindings = match(ets[idx],mts[idx],bindings);
	return bindings;
}

public Bindings match(Symbol t:\list(Symbol et), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	return match(et,getListElementType(m),bindings);
}

public Bindings match(Symbol t:\lrel(list[Symbol] ets), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	return match(\tuple(ets),getListElementType(m),bindings);
}

public Bindings match(Symbol t:\map(Symbol md, Symbol mr), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	return match(mr,getMapRangeType(m),match(md,getMapDomainType(m),bindings));
}

public Bindings match(Symbol t:\bag(Symbol et), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	return match(et,getBagElementType(m),bindings);
}

public Bindings match(Symbol t:\parameter(str s, Symbol tb), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	if (s in bindings) {
		lubbed = lub(m,bindings[s]);
		if (!subtype(lubbed, tb))
			throw invalidMatch(s,lubbed,tb);
		bindings[s] = lubbed;
	} else {
		bindings[s] = m;
	}
	return bindings;
}

public Bindings match(Symbol t:\adt(str s, list[Symbol] ps), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	mps = getADTTypeParameters(m);
	for (idx <- index(ps)) bindings = match(ps[idx],mps[idx],bindings);
	return bindings;
}
 
public Bindings match(Symbol t:Symbol::\cons(Symbol a, str name, list[Symbol] ps), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	bindings = match(a, getConstructorResultType(m), bindings);
	mps = getConstructorArgumentTypes(m);
	for (idx <- index(ps)) bindings = match(ps[idx],mps[idx],bindings);
	return bindings;	
}

public Bindings match(Symbol t:\alias(str s, list[Symbol] ps, Symbol at), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	return match(at,m,bindings);
}

public Bindings match(Symbol t:Symbol::\func(Symbol r, list[Symbol] ps), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	if (!isOverloadedType(m)) {
		bindings = match(\tuple(ps), \tuple(getFunctionArgumentTypes(m)), bindings);
		bindings = match(r, getFunctionReturnType(m), bindings); 
	} else {
		; // TODO: How do we handle overloaded types here?
	}
	return bindings;	
}

//public Bindings match(Symbol t:\var-func(Symbol r, list[Symbol] ps, Symbol va), Symbol m, Bindings bindings) {
//	bindings = defaultMatch(t,m,bindings);
//}

public Bindings match(Symbol t:\reified(Symbol r), Symbol m, Bindings bindings) {
	bindings = defaultMatch(t,m,bindings);
	return match(r,getReifiedType(m),bindings);
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