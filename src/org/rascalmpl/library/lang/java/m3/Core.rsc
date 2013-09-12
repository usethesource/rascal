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
public java void setEnvironmentOptions(set[loc] classPathEntries, set[loc] sourcePathEntries);

public void setEnvironmentOptions(loc project) {
    setEnvironmentOptions(getPaths(project, "class") + find(project, "jar"), getPaths(project, "java"));
}

public M3 composeJavaM3(M3 m1, M3 m2) {
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
public java M3 createM3FromFile(loc file, str javaVersion = "1.7");

public M3 createM3FromProject(loc project, str javaVersion = "1.7") {
    setEnvironmentOptions(project);
    result = (m3(project.authority) | composeJavaM3(it, createM3FromFile(f, javaVersion = javaVersion)) | loc f <- find(project, "java"));
    registerProject(project.authority, result);
    return result;
}

private set[loc] getPaths(loc dir, str suffix) { 
   bool containsFile(loc d) = isDirectory(d) ? (x <- d.ls && x.extension == suffix) : false;
   return find(dir, containsFile);
}