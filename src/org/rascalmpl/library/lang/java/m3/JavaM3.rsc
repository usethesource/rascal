module lang::java::m3::JavaM3

extend analysis::m3::Core;
extend lang::java::m3::TypeSymbol;

import analysis::graphs::Graph;
import Relation;

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
	| \annotation(loc \anno)
  	;

anno rel[loc from, loc to] M3@extends;    // sub-typing relation between classes and interfaces
anno rel[loc from, loc to] M3@implements;
anno rel[loc from, loc to] M3@methodInvocation;   // methods calling each other (including constructors)
anno rel[loc from, loc to] M3@fieldAccess;        // code using data (like fields)
anno rel[loc from, loc to] M3@typeDependency;     // using a type literal in some code (types of variables, annotations)
anno rel[loc from, loc to] M3@methodOverrides;


public rel[loc from, loc to] getDeclaredTypeHierarchy(M3 model) {
  set[loc] allClasses = {e | e <- domain(model@declarations), e.scheme == "java+class" || e.scheme == "java+interface"};
  set[loc] classesWithoutParent = allClasses - carrier(model@extends);
  rel[loc, loc] typeHierarchy = model@extends;
  classesWithoutParent += top(invert(typeHierarchy));
  loc javaObject = |java+class:///java/lang/Object|;
  for (loc topClass <- classesWithoutParent) {
    javaObject.authority = topClass.authority;
    typeHierarchy += <topClass, javaObject>;
  }
  
  return typeHierarchy;
}
