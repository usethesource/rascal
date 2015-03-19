module lang::java::patterns::JavaToMicroPatterns

import analys::patterns::Micro;
import lang::java::m3::AST;
import lang::java::m3::Core;


@memo
rel[loc from, loc to] flattenedExtends(M3) = m@extends+;

private loc Object = |java+class:///java/lang/Object|;

// TODO: correctly handle external interfaces
@doc{A class/interface with absolutely no members (transitivly, except for Object). }
private bool isDesignator(M3 m, loc e) 
	= (isClass(e) || isInterface(e))
	&& m@containment[e] == {}
	&& m@containment[flattenedExtends(m)[e] - Object] == {}
	;

private set[loc] removeConstuctors(set[loc] ll)
	= { l | l <- ll, l.scheme != "java+constuctor" };

// TODO: correctly handle external interfaces
@doc{An empty interface/class extending a non empty class or a single non empty interface}
private bool isTaxonomy(M3 m, loc e) 
	= 
	( isInterface(e) 
		&&  m@containment[e] == {}
		&& {_} := m@extends[e]
		&& m@containment[flattenedExtends(m)[e]] != {}
	)
	||
	( isClass(e)
		&& m@implements[e] == {}
		&& removeConstuctors(m@containment[c]) == {}
		&& removeConstuctors(m@containment[flattenedExtends(m)[c]]) != {}
	)
	;

@doc{an emtpy interface/class extending/implementing more than one interface}
private bool isJoiner(M3 m, loc e) 
	= 
	( isInterface(e) 
		&& m@containment[e] == {}
		&& {_,_,*_} := m@extends[e]
	)
	||
	( isClass(e)
		&& removeConstuctors(m@containment[e]) == {}
		&& {_,_,*_} := m@implements[e]
	)
	;
	
// TODO: unclear from paper if interfaces with only fields should be counted
@doc{An class that only has final static fields, no methods, no constructors}
private bool isPool(M3 m, loc e)
	= isClass(e)
	&& methods(m, e) == {}
	&& all(f <- fields(m, e), {static(), final(), *_} := m@modifiers[f])
	;

@doc{Classes with no fields, and only a single public instance method}
private bool isFunctionPointer(M3 m, loc e) 
	= isClass(e)
	&& {meth} := m@containment[e]
	&&  meth.scheme == "java+method"
	&& \public() in m@modifiers[meth]
	&& !(static() in m@modifiers[meth])
	;