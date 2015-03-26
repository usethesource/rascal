module lang::java::patterns::JavaToMicroPatterns

import IO;
import Set;
import Map;
import String;
import Relation;
import analysis::patterns::Micro;
import lang::java::m3::AST;
import lang::java::m3::Core;


rel[loc entity, MicroPattern pattern] findMicroPatterns(M3 model, set[Declaration] asts) {
	model = model[@ASTs = asts];
	return { <e, p> |e <- classes(model) + interfaces(model), <p,bool(M3,loc) f> <- detectors(), f(model, e)};
}


private rel[MicroPattern, bool(M3, loc)] detectors() = {
	<designator(), isDesignator>,
	<taxonomy(), isTaxonomy>,
	<joiner(), isJoiner>,
	<pool(), isPool>,
	<functionPointer(), isFunctionPointer>,
	<functionObject(), isFunctionObject>,
	<cobolLike(), isCobolLike>,
	<stateless(), isStateless>,
	<commonState(), isCommonState>,
	<immutable(), isImmutable>,
	<restrictedCreation(), isRestrictedCreation>,
	<sampler(), isSampler>,
	<box(), isBox>,
	<compoundBox(), isCompoundBox>,
	<canopy(), isCanopy>,
	<record(), isRecord>,
	<dataManager(), isDataManager>,
	<sink(), isSink>,
	<outline(), isOutline>,
	<trait(), isTrait>,
	<stateMachine(), isStateMachine>,
	<pureType(), isPureType>,
	<augmentedType(), isAugmentedType>,
	<pseudoClass(), isPseudoClass>,
	<implementor(), isImplementor>,
	<overrider(), isOverrider>,
	<extender(), isExtender>
};


anno set[Declaration] M3@ASTs;

@memo
private map[loc, Declaration] methodASTs(M3 m)
	= toMapUnique({<d@decl, d> | /Declaration d := m@ASTs, d is method || d is constructor });

@memo
private Declaration methodAST(M3 m, loc method) {
	return methodASTs(m)[method];
}

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

@memo
rel[loc from, loc to] flattenedExtends(M3 m) = m@extends+;

@memo
private set[loc] instanceFields(M3 m, loc e)
	= { f | f <- fields(m,e), !(<f,final()> in m@modifiers) };


private Expression dropArrayAccess(arrayAccess(a,_)) = dropArrayAccess(a);
default Expression dropArrayAccess(Expression e) = e;

@memo
rel[loc lhs, loc src] assignments(M3 m, loc method) {
	rel[loc lhs, loc src] result = {};
	visit(methodAST(m, method)) {
		case a:assignment(arrayAccess(t,_),_,_) :  result += <dropArrayAccess(t)@decl, a@src>; 
		case a:assignment(t,_,_) :  result += <t@decl, a@src>; 
	}
	return result;
}

@memo
private set[loc] constructors(M3 m, loc c)
	= { con | con <- elements(m, c), con.scheme == "java+constructor" };
	
private bool isAbstract(M3 m, loc e) = <e, abstract()> in m@modifiers;

@memo 
private set[loc] abstractMethods(M3 m, loc c)
	= { met | met <- methods(m,c), isAbstract(m, met) };

@memo
private set[loc] staticFinalFields(M3 m, loc c)
	= { f | f <- fields(m,c), <f, static()> in m@modifiers, <f, final()> in m@modifiers };
	
@memo
private set[loc] publicMethods(M3 m, loc c)
	= { met | met <- methods(m,c), <met, \public()> in m@modifiers };
	
private str name(loc l) {
	if (isMethod(l)) {
		if (/^<n:[^\(]*>\(/ := l.file) {
			return n;
		}
		throw "Could not find name";
	}
	return l.file;
}

// TODO: correctly handle external interfaces
@doc{A class/interface with absolutely no members (transitivly, except for Object). }
private bool isDesignator(M3 m, loc e) 
	= (isClass(e) || isInterface(e))
	&& m@containment[e] == {}
	&& m@containment[flattenedExtends(m)[e] - Object] == {}
	;

private set[loc] removeConstuctors(set[loc] ll)
	= { l | l <- ll, l.scheme != "java+constructor" };

// TODO: correctly handle external interfaces
@doc{An empty interface/class extending a non empty class or a single non empty interface}
private bool isTaxonomy(M3 m, loc e) 
	= 
	( isInterface(e) 
		&&  m@containment[e] == {}
		&& size(m@extends[e]) == 1
		&& m@containment[flattenedExtends(m)[e]] != {}
	)
	||
	( isClass(e)
		&& m@implements[e] == {}
		&& removeConstuctors(m@containment[e]) == {}
		&& removeConstuctors(m@containment[flattenedExtends(m)[e]]) != {}
	)
	;

@doc{an emtpy interface/class extending/implementing more than one interface}
private bool isJoiner(M3 m, loc e) 
	= 
	( isInterface(e) 
		&& m@containment[e] == {}
		&& size(m@extends[e]) > 1
	)
	||
	( isClass(e)
		&& removeConstuctors(m@containment[e]) == {}
		&& size(m@implements[e]) > 1
	)
	;
	
// TODO: unclear from paper if interfaces with only fields should be counted
@doc{An class that only has final static fields, no methods, no constructors}
private bool isPool(M3 m, loc e)
	= isClass(e)
	&& methods(m, e) == {}
	&& all(f <- fields(m, e), <f, static()> in m@modifiers && <f,final()> in m@modifiers)
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
bool isFunctionObject(M3 m, loc e)
	= isClass(e)
	&& {meth} := removeConstuctors(methods(m, e))
	&& fields(m,e) != {}
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
	&& all(f <- fields(m, e), <f, static()> in m@modifiers && <f, final()> in m@modifiers)
	;
	

@doc{Classes with no instance fields, and at least one static non final field}
private bool isCommonState(M3 m, loc e)
	= isClass(e)
	&& methods(m,e) != {}
	&& all(loc f <- fields(m, e), <f,static()> in m@modifiers)
	&& any(loc f <- fields(m, e), !(<f,final()> in m@modifiers))
	;
	

@doc{A class whose instance fields (more than 1) are only mutated by the constructors}
private bool isImmutable(M3 m, loc e)
	= isClass(e)
	&& set[loc] fs := instanceFields(m,e)
	&& size(fs) > 1
	&& any(loc met <- constructors(m, e), (fs & assignments(m, met)<lhs>) != {})
	&& !any(loc met <- methods(m, e), met.scheme == "java+method" && (fs & assignments(m, met)<lhs>) != {})
	;
	
	
@doc{A class with one instance field assigned once in the constructor}
private bool isCanopy(M3 m, loc e)
	= isClass(e)
	&& {f} := instanceFields(m,e)
	&& all(loc c <- constructors(m, e), size(assignments(m, c)[f]) == 1)
	&& !(f in {*(assignments(m, met)<lhs>) | loc met <- methods(m, e), met.scheme == "java+method"})
	;

@doc{A class with no public constructors and at least on static field of the same type of the class}
private bool isRestrictedCreation(M3 m, loc e)
	= isClass(e)
	&& !any(loc c <- constructors(m, e), !(<c, \public()> in m@modifiers))
	&& any(loc f <- fields(m, e), <f, static()> in m@modifiers && <f, e> in m@typeDependency)
	;

@doc{A class with at least one public constructor and at least one static field of the same type of the class}
private bool isSampler(M3 m, loc e)
	= isClass(e)
	&& any(loc c <- constructors(m, e), <c, \public()> in m@modifiers)
	&& any(loc f <- fields(m, e), <f, static()> in m@modifiers && <f, e> in m@typeDependency)
	;

// TODO: unclear about Object methods
@doc{A Class with one instance field, which is mutated by one of the static/instance methods of the class}	
private bool isBox(M3 m, loc e)
	= isClass(e)
	&& {f} := instanceFields(m,e)
	&& { *(assignments(m, met)[f]) | met <- methods(m, e) - constructors(m,e) } != {}
	;

@doc{Variant of box with one non-primitive field, and one or more primitive fields}	
private bool isCompoundBox(M3 m, loc e)
	= isClass(e)
	&& {f} := { f | f <- instanceFields(m,e), t <- m@typeDependency[f], t.scheme != "java+primitiveType" }
	&& set[loc] otherFields := instanceFields(m,e) - {f}
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

@doc{Only getters and setters}
private  bool isDataManager(M3 m, loc e)
	= isClass(e)
	&& set[loc] fs := instanceFields(m,e)
	&& fs != {}
	&& !(\public() in m@modifiers[fs])
	&& meths := methods(m,e) - constructors(m,e)
	&& (size(meths) > 0 | it &&  (startsWith(name(meth), "set") || startsWith(name(meth), "get") || isObjectMethod(meth)) | meth <- meths)
	;
	
@doc{Methods do not call any other methods, based on the example given in the paper, the Object methods do not count.}
private  bool isSink(M3 m, loc e)
	= isClass(e)
	&& methods(m,e) != {}
	&& !any(met <- m@methodInvocation[methods(m,e)], !isObjectMethod(met))
	;

@doc{Abstract class where two or more declared methods invoke at least one abstract method of the current class }
private bool isOutline(M3 m, loc e)
	= isClass(e)
	&& isAbstract(m,e)
	&& size(domain(m@methodInvocation & (methods(m,e) * abstractMethods(m,e)))) > 2
	;
	
@doc{Abtract classes with no instance fields, at least one abstract method}
private bool isTrait(M3 m, loc e)
	= isClass(e)
	&& isAbstract(m,e)
	&& instanceFields(m,e) == {}
	&& abstractMethods(m, e) != {}
	;
		
@doc{Interfaces with parameter less methods}
private bool isStateMachine(M3 m, loc e)
	= isInterface(e)
	&& fields(m,e) == {}
	&& all(met <- methods(m, e), endsWith(met.file, "()"))
	;
	
@doc{Classes with no implementations details, everything is abstract, or interfaces without any static definitions}
private bool isPureType(M3 m, loc e)
	= (isClass(e) || isInterface(e))
	&& fields(m,e) == {}
	&& methods(m,e) != {}
	&& (isInterface(e) || methods(m,e) == abstractMethods(m,e))
	;

@memo
private map[loc, set[loc]] usedTypes(M3 m, set[loc] where) 
	= toMap(invert((where * where) o m@typeDependency));	

@doc{Like pure type, except that there are 3 or more static final fields/methods of the same type}
private bool isAugmentedType(M3 m, loc e)
	= (isClass(e) || isInterface(e))
	&& methods(m,e) != {}
	&& (
		( isInterface(e) 
			&& uf := usedTypes(m, fields(m,e))
			&& any(loc t <- uf, size(uf[t]) >= 3)
		)
		||
		( isClass(e) 
			&& uf := usedTypes(m, staticFinalFields(m,e))
			&&  any(loc t <- uf, size(uf[t]) >= 3)
		)
	)
	;

@doc{An abstract class with only abstract methods, no instance fields or methods, static fields and methods are allowed}
private bool isPseudoClass(M3 m, loc e)
	= isClass(e)
	&& isAbstract(m,e)
	&& all(u <- (methods(m,e) - abstractMethods(m,e)) + fields(m, e), <u, static()> in m@modifiers)
	;
	
// unsure if this should be for (super classes)+
@doc{A class where all the public methods are implementing an abstract method of the super class }
private bool isImplementor(M3 m, loc e)
	= isClass(e)
	&& !isAbstract(m, e)
	&& {base} := m@extends[e]
	&& size(publicMethods(m,e) - constructors(m,e)) > 0
	&& baseMethods := abstractMethods(m, base)
	&& baseMethods & m@methodOverrides[publicMethods(m,e) - constructors(m,e)] == baseMethods
	&& size(publicMethods(m,e) - constructors(m,e)) == size(baseMethods)
	;
		
@doc{A class where all the public methods are overriding non abstract methods of the super class}
private bool isOverrider(M3 m, loc e) 
	= isClass(e)
	&& {base} := m@extends[e]
	&& size(publicMethods(m,e) - constructors(m,e)) > 0
	&& baseMethods := ((methods(m, base) - abstractMethods(m, base)) - constructors(m,e))
	&& baseMethods & m@methodOverrides[publicMethods(m,e) - constructors(m,e)] == baseMethods
	&& size(publicMethods(m,e) - constructors(m,e)) == size(baseMethods)
	;
	
@doc{A class which extends another class but does not override any of it's methods}
private bool isExtender(M3 m, loc e)
	= isClass(e)
	&& m@extends[e] != {}
	&& m@methodOverrides[methods(m,e) - constructors(m,e)] == {}
	&& (methods(m,e) - constructors(m,e)) != {}
	;