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
module experiments::m3::JDT

import IO;
import String;
import Map;
import Node;
import experiments::m3::AST;

private list[loc] getProject(loc dir, str suffix) {
							  res = [];
  for(str entry <- listEntries(dir)){
      loc sub = dir + entry;   
      if(isDirectory(sub)) {
          res += getProject(sub, suffix);
      } else {
	          if(endsWith(entry, suffix)) { 
	             res += [sub]; 
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

@javaClass{org.rascalmpl.library.experiments.m3.internal.JDT}
public java void setEnvironmentOptions(set[loc] classPathEntries, set[loc] sourcePathEntries);

public void setEnvironmentOptions(loc project) {
	    setEnvironmentOptions(getPaths(project, ".class") + getPaths(project, ".jar") +
	    {
	    	|file:///Users/shahi/eclipse/plugins|,
			|file:///Users/shahi/eclipse/plugins/org.eclipse.core.runtime.compartibility.registery_3.5.101.v20130108-163257|,
			|file:///Users/shahi/eclipse/plugins/org.apache.ant_1.8.3.v201301120609/lib|
	    }, getPaths(project, ".java"));
}

@doc{Creates AST from a file}
@javaClass{org.rascalmpl.library.experiments.m3.internal.JDT}
@reflect
public java AstNode createAstFromFile(loc file, bool collectBindings, str projectName);

public AstNode createAstFromFile(loc file, bool collectBindings, str projectName) 
	= createAstFromFile(file, collectBindings, projectName);

@doc{Creates ASTs from a project}
public set[AstNode] createAstsFromProject(loc project, bool collectBindings) {
   setEnvironmentOptions(project);
	 	  return { createAstFromFile(f, collectBindings, project.authority) | loc f <- getProject(project, ".java") };
	}
