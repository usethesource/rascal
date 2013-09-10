@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bas Basten - Bas.Basten@cwi.nl (CWI)}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl (CWI)}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::java::m3::JDT

import IO;
import String;
import Relation;
import Set;
import Map;
import Node;
import analysis::m3::AST;
import analysis::m3::Registry;

import lang::java::m3::JavaM3;
import List;

private set[loc] crawl(loc dir, str suffix) {
	res = {};
  	for(str entry <- listEntries(dir)){
    	loc sub = dir + entry;   
      	if(isDirectory(sub)) {
        	res += crawl(sub, suffix);
      	} else {
          	if(endsWith(entry, suffix)) { 
            	res += {sub}; 
			}
		}
	};
	return res;
}

private bool containsFileWithExtension(loc dir, str suffix) {
	bool result = false;
	for (str entry <- listEntries(dir)) {
		loc sub = dir + entry;
		if (endsWith(entry, suffix)) {
			return true;
		} else {
			if (isDirectory(sub)) {
				result = containsFileWithExtension(sub, suffix);
			}
		}
	}
	return result;
}

public set[loc] getPaths(loc dir, str suffix) {
	res = {};
	for (str entry <- listEntries(dir)) {
	    loc sub = dir + entry;
	    if (isDirectory(sub) && containsFileWithExtension(sub, suffix)) {
	    	res += {sub};
	    }
	}
	return res;
}

@javaClass{org.rascalmpl.library.lang.java.m3.internal.JDT}
@reflect
public java void setEnvironmentOptions(set[loc] classPathEntries, set[loc] sourcePathEntries);

public void setEnvironmentOptions(loc project) {
	setEnvironmentOptions(getPaths(project, ".class") + crawl(project, ".jar"), getPaths(project, ".java"));
}

@doc{Creates AST from a file}
@javaClass{org.rascalmpl.library.lang.java.m3.internal.JDT}
@reflect
public java Declaration createAstFromFile(loc file, bool collectBindings, str javaVersion = "1.7");

@doc{Creates ASTs from a project}
public set[Declaration] createAstsFromProject(loc project, bool collectBindings, str javaVersion = "1.7" ) {
   setEnvironmentOptions(project);
   return { createAstFromFile(f, collectBindings, javaVersion = javaVersion) | loc f <- crawl(project, ".java") };
}

@javaClass{org.rascalmpl.library.lang.java.m3.internal.JDT}
@reflect
public java M3 createM3FromFile(loc file, str javaVersion = "1.7");

public M3 createM3FromProject(loc project, str javaVersion = "1.7") {
	setEnvironmentOptions(project);

	M3 result = m3(project.authority);
	for (loc f <- crawl(project, ".java")) {
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
	}
        registerProject(project.authority, result);
	return result;
}

@resolver{java}
loc resolveJava(loc name) = resolveM3(name);
