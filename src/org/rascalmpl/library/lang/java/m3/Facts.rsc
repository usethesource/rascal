module lang::java::m3::Facts

import lang::java::m3::Core;
import Prelude;

public bool isCompilationUnit(loc entity) = entity.scheme == "java+compilationUnit";
public bool isPackage(loc entity) = entity.scheme == "java+package";
public bool isClass(loc entity) = entity.scheme == "java+class";
public bool isMethod(loc entity) = entity.scheme == "java+method" || entity.scheme == "java+constructor";
public bool isParameter(loc entity) = entity.scheme == "java+parameter";
public bool isVariable(loc entity) = entity.scheme == "java+variable";
public bool isField(loc entity) = entity.scheme == "java+field";
public bool isInterface(loc entity) = entity.scheme == "java+interface";

public set[loc] files(rel[loc, loc] containment) 
  = {e.lhs | tuple[loc lhs, loc rhs] e <- containment, isCompilationUnit(e.lhs)};


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

public rel[loc, loc] declaredTopTypes(rel[loc, loc] containment)  
  = {e | tuple[loc lhs, loc rhs] e <- containment, isCompilationUnit(e.lhs), isClass(e.rhs) || isInterface(e.rhs)}; 

public rel[loc, loc] declaredSubTypes(rel[loc, loc] containment) 
  = {e | tuple[loc lhs, loc rhs] e <- containment, isClass(e.rhs)} - declaredTopTypes(rels);

public rel[loc, loc] extends(rel[loc, loc] inheritance) 
  = {e | tuple[loc lhs, loc rhs] e <- inheritance, e.lhs.scheme == e.rhs.scheme};

public rel[loc, loc] implements(rel[loc, loc] inheritance) 
  = {e | tuple[loc lhs, loc rhs] e <- inheritance, e.lhs.scheme != e.rhs.scheme};

public set[loc] classes(rel[loc, loc] containment) =  {e | e <- range(containment), isClass(e)};
public set[loc] packages(rel[loc, loc] containment) = {e | e <- domain(containment), isPackage(e)};
public set[loc] variables(rel[loc, loc] containment) = {e | e <- range(containment), isVariable(e)};
public set[loc] parameters(rel[loc, loc] containment)  = {e | e <- range(containment), isParameter(e)};
public set[loc] fieldDecls(rel[loc, loc] containment) = {e | e <- range(containment), isField(e.rhs)};
