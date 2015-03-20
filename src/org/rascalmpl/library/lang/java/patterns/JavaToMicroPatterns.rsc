module lang::java::patterns::JavaToMicroPatterns

import analys::patterns::Micro;
import lang::java::m3::AST;
import lang::java::m3::Core;

anno set[Declaration] M3@ASTs;

@memo
Declaration methodAST(M3 m, loc method) {
	if (/Declaration d := m@ASTs, d is method || d is constructor, d@decl == method) {
		return d;
	}
	throw "Cannot find the method in the AST";
}

private M3 enrichM3(M3 m, set[Declaration] asts) 
	= m[@ASTs = asts];


@memo
rel[loc from, loc to] flattenedExtends(M3 m) = m@extends+;

private loc Object = |java+class:///java/lang/Object|;

@memo
private bool isObjectMethod(loc f) {
	if (f.scheme != "java+method") {
		return false;
	}
	switch (f.file) {
		case "toString()" : return true;
		case "clone()": return true;
		case "hashCode()": return true;
		case "finalize()": return true;
		case "equals(java.lang.Object)": return true;
		default: return false;
	}
}

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

// TODO: unclear if constructors are allowed
@doc{Classes with no fields, and only a single public instance method}
private bool isFunctionPointer(M3 m, loc e) 
	= isClass(e)
	&& {meth} := m@containment[e]
	&& meth.scheme == "java+method"
	&& \public() in m@modifiers[meth]
	&& !(static() in m@modifiers[meth])
	;
	
@doc{Like function pointers, but with instance fields and possible constructors }
private bool isFunctionObject(M3 m, loc e)
	= isClass(e)
	&& {meth} := removeConstuctors(methods(m, e))
	&& meth.scheme == "java+method"
	&& !(static() in m@modifiers[fields(m, e)])
	&& \public() in m@modifiers[meth]
	&& !(static() in m@modifiers[meth])
	;

private set[loc] removeNestedUnits(set[loc] ll)
	= { l | l <- ll, l.scheme != "java+class", l.scheme != "java+interface" };

@doc{A class with a single static method and one or more static fields, no instance methods and fields}
private bool isCobolLike(M3 m, loc e)
	= isClass(e)
	&& {meth} := removeConstuctors(methods(m, e))
	&& meth.scheme == "java+method"
	&& static() in m@modifiers[meth]
	&& \public() in m@modifiers[meth]
	&& all(f <- fields(m,e), <f, static()> in m@modifiers)
	;
	
@doc{Classes with static/instance methods and all the fields are static final}
private bool isStateless(M3 m, loc e)
	= isClass(e)
	&& methods(m,e) != {}
	&& all(f <- fields(m, e), {static(), final(), *_} := m@modifiers[f])
	;
	

@doc{Classes with no instance fields, and at least one static non final field}
private bool isCommonState(M3 m, loc e)
	= isClass(e)
	&& methods(m,e) != {}
	&& all(f <- fields(m, e), <f,static()> in m@modifiers)
	&& any(f <- fields(m, e), !(<f,final()> in m@modifiers))
	;
	

private set[loc] instanceFields(M3 m, loc e)
	= { f | f <- fields(m,e), !(<f,final()> in m@modifiers) };

private rel[loc lhs, loc src] assignments(M3 m, loc method)
	= { <t@decl, a@src> | /a:assignment(t, _, _) := methodAST(m, method)};

@doc{A class whose instance fields (more than 1) are only mutated by the constructors}
private bool isImmutable(M3 m, loc e)
	= isClass(e)
	&& set[loc] fs := instanceFields(m,e)
	&& size(fs) > 1
	&& any(met <- constructors(m, e), (fs & assignments(m, met)<lhs>) != {})
	&& !any(met <- methods(m, e), met.scheme == "java+method" && (fs & assignments(m, met)<lhs>) != {})
	;
	
	
@doc{A class with one instance field assigned once in the constructor}
private bool isCanopy(M3 m, loc e)
	= isClass(e)
	&& {f} := instanceFields(m,e)
	&& all(c <- constructors(m, e), {_} := assignments(m, c)[f])
	&& !any(met <- methods(m, e), met.scheme == "java+method" && !(f in assignments(m, met)<lhs>))
	;

@doc{A class with no public constructors and at least on static field of the same type of the class}
private bool isRestrictedCreation(M3 m, loc e)
	= isClass(e)
	&& !any(c <- constructors(m, e), !(<c, \public()> in m@modifiers))
	&& any(f <- fields(m, e), <f, static()> in m@modifiers && <f, e> in m@typeDependency)
	;

@doc{A class with at least one public constructor and at least one static field of the same type of the class}
private bool isSampler(M3 m, loc e)
	= isClass(e)
	&& any(c <- constructors(m, e), <c, \public()> in m@modifiers)
	&& any(f <- fields(m, e), <f, static()> in m@modifiers && <f, e> in m@typeDependency)
	;

// TODO: unclear about Object methods
@doc{A Class with one instance field, which is mutated by one of the static/instance methods of the class}	
private bool isBox(M3 m, loc e)
	= isClass(e)
	&& {f} := instanceFields(m,e)
	&& { *(assignments(m, met)[f]) | met <- removeConstuctors(methods(m, e)) } != {}
	;

@doc{Variant of box with one non-primitive field, and one or more primitive fields}	
private bool isCompoundBox(M3 m, loc e)
	= isClass(e)
	&& {f} := { f | f <- instanceFields(m,e), t <- m@typeDepedency[f], t.scheme != "java+primitiveType" }
	&& set[loc] otherFields := { fp | fp <- instanceFields(m,e), t <- m@typeDepedency[fp], t.scheme == "java+primitiveType" }
	&& { *(assignments(m, met)[f]) | met <- removeConstuctors(methods(m, e)) } != {}
	&& all(fp <- otherFields, { *(assignments(m, met)[fp]) | met <- removeConstuctors(methods(m, e)) } != {})
	;

@doc{All instance fields are public, no declared methods}
private bool isRecord(M3 m, loc e)
	= isClass(e)
	&& all(f <- fields(m,e), <f, \public()> in m@modifiers)
	&& !(static() in m@modifiers[fields(m,e)])
	&& !any(met <- methods(m,e), met.scheme =="java+method" && !isObjectMethod(met))


	
	;