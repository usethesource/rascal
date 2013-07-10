module experiments::m3::Facts

import experiments::m3::AST;

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

public rel[loc, loc] declaredMethods(rel[loc, loc] containment) {
	return {e | tuple[loc lhs, loc rhs] e <- containment, isClass(e.lhs), isMethod(e.rhs)};
}

public rel[loc, loc] declaredMethodsWithModifier(rel [loc, loc] containment, rel[loc, Modifiers] modifiers, set[Modifiers] checkModifiers) {
	rel[loc, loc] methods = declaredMethods(containment);
	map[loc, set[Modifiers]] methodModifiers = toMap(domainR(modifiers, range(methods)));
	set[loc] resultSet = {};
	for (loc method <- methodModifiers) {
		if (checkModifiers <= methodModifiers[method])
			resultSet += method;
	}
	return rangeR(methods, resultSet);
}

public rel[loc, loc] declaredTopTypes(rel[loc, loc] containment) {
	return {e | tuple[loc lhs, loc rhs] e <- rels, isCompilationUnit(e.lhs), isClass(e.rhs) || isInterface(e.rhs)}; 
}

public rel[loc, loc] declaredSubTypes(rel[loc, loc] containment) {
	return {e | tuple[loc lhs, loc rhs] e <- rels, isClass(e.rhs)} - declaredTopTypes(rels);
}

public rel[loc, loc] extends(rel[loc, loc] inheritance) {
	return {e | tuple[loc lhs, loc rhs] e <- inheritance, e.lhs.scheme == e.rhs.scheme};
}

public rel[loc, loc] implements(rel[loc, loc] inheritance) {
	return {e | tuple[loc lhs, loc rhs] e <- inheritance, e.lhs.scheme != e.rhs.scheme};
}

public set[loc] classes(rel[loc, loc] containment) {
	return {e.rhs | tuple[loc lhs, loc rhs] e <- containment, isClass(e.rhs)};
}

public set[loc] packages(rel[loc, loc] containment) {
	return {e.rhs | tuple[loc lhs, loc rhs] e <- containment, isPackage(e.rhs)};
}

public set[loc] variables(rel[loc, loc] containment) {
	return {e.rhs | tuple[loc lhs, loc rhs] e <- containment, isVariable(e.rhs)};
}

public set[loc] parameters(rel[loc, loc] containment) {
	return {e.rhs | tuple[loc lhs, loc rhs] e <- containment, isParameter(e.rhs)};
}

public set[loc] fieldDecls(rel[loc, loc] containment) {
	return {e.rhs | tuple[loc lhs, loc rhs] e <- containment, isField(e.rhs)};
}
