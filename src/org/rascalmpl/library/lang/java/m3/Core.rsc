module lang::java::m3::Core

extend lang::java::m3::TypeSymbol;
extend lang::java::m3::Registry;
extend analysis::m3::Core;

import analysis::graphs::Graph;
import analysis::m3::AST;
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

@javaClass{org.rascalmpl.library.lang.java.m3.internal.JDT}
@reflect
public java void setEnvironmentOptions(set[loc] classPathEntries, set[loc] sourcePathEntries);

public void setEnvironmentOptions(loc project) {
    setEnvironmentOptions(getPaths(project, "class") + find(project, "jar"), getPaths(project, "java"));
}

@javaClass{org.rascalmpl.library.lang.java.m3.internal.JDT}
@reflect
public java M3 createM3FromFile(loc file, str javaVersion = "1.7");

public M3 createM3FromProject(loc project, str javaVersion = "1.7") {
    setEnvironmentOptions(project);

    M3 result = m3(project.authority);
    for (loc f <- find(project, "java")) {
        M3 model = createM3FromFile(f, javaVersion = javaVersion);
        result@declarations += model@declarations;
        result@uses += model@uses;
        result@containment += model@containment;
        result@extends += model@extends;
        result@implements += model@implements;
        result@methodInvocation += model@methodInvocation;
        result@fieldAccess += model@fieldAccess;
        result@typeDependency += model@typeDependency;
        result@documentation += model@documentation;
        result@modifiers += model@modifiers;
        result@messages += model@messages;
        result@names += model@names;
        result@methodOverrides += model@methodOverrides;
        result@types += model@types;
    }
        
    registerProject(project.authority, result);
    return result;
}



private set[loc] getPaths(loc dir, str suffix) { 
   bool containsFile(loc d) = isDirectory(d) ? (x <- d.ls && x.extension == suffix) : false;
   return find(dir, containsFile);
}