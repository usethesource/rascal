module analysis::m3::Facts

import analysis::m3::JavaM3;
import Prelude;

public bool isCompilationUnit(loc entity) {
	if (entity.scheme == "java+compilationUnit")
		return true;
	return false;
}

public bool isPackage(loc entity) {
	if (entity.scheme == "java+package")
		return true;
	return false;
}

public bool isClass(loc entity) {
	if (entity.scheme == "java+class")
		return true;
	return false;
}

public bool isMethod(loc entity) {
	if (entity.scheme == "java+method" || entity.scheme == "java+constructor")
		return true;
	return false;
}

public bool isParameter(loc entity) {
	if (entity.scheme == "java+parameter")
		return true;
	return false;
}

public bool isVariable(loc entity) {
	if (entity.scheme == "java+variable")
		return true;
	return false;
}

public bool isField(loc entity) {
	if (entity.scheme == "java+field")
		return true;
	return false;
}

public bool isInterface(loc entity) {
	if (entity.scheme == "java+interface")
		return true;
	return false;
}

public set[loc] files(rel[loc, loc] containment) {
	return {e.lhs | tuple[loc lhs, loc rhs] e <- containment, isCompilationUnit(e.lhs)};
}

public rel[loc, loc] declaredMethods(M3 m, set[Modifiers] checkModifiers = {}) {
	declaredClasses = classes(m@containment);
	methodModifiersMap = toMap(m@modifiers);
	
	return {e | tuple[loc lhs, loc rhs] e <- domainR(m@containment, declaredClasses), isMethod(e.rhs), checkModifiers <= (methodModifiersMap[e.rhs]? ? methodModifiersMap[e.rhs] : {}) };
}

public rel[loc, loc] declaredFields(M3 m, set[Modifiers] checkModifiers = {}) {
	declaredClasses = classes(m@containment);
	methodModifiersMap = toMap(m@modifiers);
	
	return {e | tuple[loc lhs, loc rhs] e <- domainR(m@containment, declaredClasses), isField(e.rhs), checkModifiers <= (methodModifiersMap[e.rhs]? ? methodModifiersMap[e.rhs] : {}) };
}

public rel[loc, loc] declaredFieldsX(M3 m,set[Modifiers] checkModifiers = {}) {
	declaredClasses = classes(m@containment);
	methodModifiersMap = toMap(m@modifiers);
	
	return {e | tuple[loc lhs, loc rhs] e <- domainR(m@containment, declaredClasses), isField(e.rhs), isEmpty(checkModifiers & (methodModifiersMap[e.rhs]? ? methodModifiersMap[e.rhs] : {})) };
} 

public rel[loc, loc] declaredTopTypes(rel[loc, loc] containment) {
	return {e | tuple[loc lhs, loc rhs] e <- containment, isCompilationUnit(e.lhs), isClass(e.rhs) || isInterface(e.rhs)}; 
}

public rel[loc, loc] declaredSubTypes(rel[loc, loc] containment) {
	return {e | tuple[loc lhs, loc rhs] e <- containment, isClass(e.rhs)} - declaredTopTypes(rels);
}

public rel[loc, loc] extends(rel[loc, loc] inheritance) {
	return {e | tuple[loc lhs, loc rhs] e <- inheritance, e.lhs.scheme == e.rhs.scheme};
}

public rel[loc, loc] implements(rel[loc, loc] inheritance) {
	return {e | tuple[loc lhs, loc rhs] e <- inheritance, e.lhs.scheme != e.rhs.scheme};
}

public set[loc] classes(rel[loc, loc] containment) {
	return {e | e <- range(containment), isClass(e)};
}

public set[loc] packages(rel[loc, loc] containment) {
	return {e | e <- domain(containment), isPackage(e)};
}

public set[loc] variables(rel[loc, loc] containment) {
	return {e | e <- range(containment), isVariable(e)};
}

public set[loc] parameters(rel[loc, loc] containment) {
	return {e | e <- range(containment), isParameter(e)};
}

public set[loc] fieldDecls(rel[loc, loc] containment) {
	return {e | e <- range(containment), isField(e.rhs)};
}
