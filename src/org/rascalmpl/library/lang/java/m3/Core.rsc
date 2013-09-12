module lang::java::m3::Core

extend lang::java::m3::TypeSymbol;
import lang::java::m3::Registry;
import lang::java::m3::AST;

extend analysis::m3::Core;

import analysis::graphs::Graph;
import analysis::m3::Registry;

import IO;
import String;
import Relation;
import Set;
import Map;
import Node;
import List;

import util::FileSystem;

data Modifiers
	= \private()
	| \public()
	| \protected()
	| \friendly()
	| \static()
	| \final()
	| \synchronized()
	| \transient()
	| \abstract()
	| \native()
	| \volatile()
	| \strictfp()
	| \deprecated()
	| \annotation(loc \ann)
  	;

anno rel[loc from, loc to] M3@extends;            // classes extending classes and interfaces extending interfaces
anno rel[loc from, loc to] M3@implements;         // classes implementing interfaces
anno rel[loc from, loc to] M3@methodInvocation;   // methods calling each other (including constructors)
anno rel[loc from, loc to] M3@fieldAccess;        // code using data (like fields)
anno rel[loc from, loc to] M3@typeDependency;     // using a type literal in some code (types of variables, annotations)
anno rel[loc from, loc to] M3@methodOverrides;    // which method override which other methods

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
java void setEnvironmentOptions(set[loc] classPathEntries, set[loc] sourcePathEntries);

void setEnvironmentOptions(loc project) {
    setEnvironmentOptions(getPaths(project, "class") + find(project, "jar"), getPaths(project, "java"));
}

M3 composeJavaM3(M3 m1, M3 m2) {
  m1 = composeM3(m1, m2);
  
  m1@extends += m2@extends;
  m1@implements += m2@implements;
  m1@methodInvocation += m2@methodInvocation;
  m1@fieldAccess += m2@fieldAccess;
  m1@typeDependency += m2@typeDependency;
  m1@methodOverrides += m2@methodOverrides;
  
  return m1;
}

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
java M3 createM3FromFile(loc file, str javaVersion = "1.7");

M3 createM3FromProject(loc project, str javaVersion = "1.7") {
    setEnvironmentOptions(project);
    result = (m3(project.authority) | composeJavaM3(it, createM3FromFile(f, javaVersion = javaVersion)) | loc f <- find(project, "java"));
    registerProject(project.authority, result);
    return result;
}

private set[loc] getPaths(loc dir, str suffix) { 
   bool containsFile(loc d) = isDirectory(d) ? (x <- d.ls && x.extension == suffix) : false;
   return find(dir, containsFile);
}

bool isCompilationUnit(loc entity) = entity.scheme == "java+compilationUnit";
bool isPackage(loc entity) = entity.scheme == "java+package";
bool isClass(loc entity) = entity.scheme == "java+class";
bool isMethod(loc entity) = entity.scheme == "java+method" || entity.scheme == "java+constructor";
bool isParameter(loc entity) = entity.scheme == "java+parameter";
bool isVariable(loc entity) = entity.scheme == "java+variable";
bool isField(loc entity) = entity.scheme == "java+field";
bool isInterface(loc entity) = entity.scheme == "java+interface";

set[loc] files(rel[loc, loc] containment) 
  = {e.lhs | tuple[loc lhs, loc rhs] e <- containment, isCompilationUnit(e.lhs)};

rel[loc, loc] declaredMethods(M3 m, set[Modifiers] checkModifiers = {}) {
    declaredClasses = classes(m@containment);
    methodModifiersMap = toMap(m@modifiers);
    
    return {e | tuple[loc lhs, loc rhs] e <- domainR(m@containment, declaredClasses), isMethod(e.rhs), checkModifiers <= (methodModifiersMap[e.rhs]? ? methodModifiersMap[e.rhs] : {}) };
}

rel[loc, loc] declaredFields(M3 m, set[Modifiers] checkModifiers = {}) {
    declaredClasses = classes(m@containment);
    methodModifiersMap = toMap(m@modifiers);
    
    return {e | tuple[loc lhs, loc rhs] e <- domainR(m@containment, declaredClasses), isField(e.rhs), checkModifiers <= (methodModifiersMap[e.rhs]? ? methodModifiersMap[e.rhs] : {}) };
}

rel[loc, loc] declaredFieldsX(M3 m, set[Modifiers] checkModifiers = {}) {
    declaredClasses = classes(m@containment);
    methodModifiersMap = toMap(m@modifiers);
    
    return {e | tuple[loc lhs, loc rhs] e <- domainR(m@containment, declaredClasses), isField(e.rhs), isEmpty(checkModifiers & (methodModifiersMap[e.rhs]? ? methodModifiersMap[e.rhs] : {})) };
} 
 
rel[loc, loc] declaredTopTypes(M3 m)  
  = {e | tuple[loc lhs, loc rhs] e <- m@containment, isCompilationUnit(e.lhs), isClass(e.rhs) || isInterface(e.rhs)}; 

rel[loc, loc] declaredSubTypes(M3 m) 
  = {e | tuple[loc lhs, loc rhs] e <- m@containment, isClass(e.rhs)} - declaredTopTypes(rels);

set[loc] classes(M3 m) =  {e | e <- range(m@containment), isClass(e)};
set[loc] packages(M3 m) = {e | e <- domain(m@containment), isPackage(e)};
set[loc] variables(M3 m) = {e | e <- range(m@containment), isVariable(e)};
set[loc] parameters(M3 m)  = {e | e <- range(m@containment), isParameter(e)};
set[loc] fieldDecls(M3 m) = {e | e <- range(m@containment), isField(e)};

set[loc] elements(M3 m, loc parent) = { e | <parent, e> <- m@containment };

set[loc] fields(M3 m, loc class) = { e | e <- elements(m, class), isField(e) };
set[loc] methods(M3 m, loc class) = { e | e <- elements(m, class), isMethod(e) };
set[loc] nestedClasses(M3 m, loc class) = { e | e <- elements(m, class), isClass(e) };