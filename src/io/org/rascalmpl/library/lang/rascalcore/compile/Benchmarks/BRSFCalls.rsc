@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::rascalcore::compile::Benchmarks::BRSFCalls

import Exception;
import Relation;
import util::Math;
import Set;
import  analysis::graphs::Graph;
import lang::rsf::IO;
import IO;

public value main(){
  return measure([/*"JHotDraw52.rsf", "JDK140AWT.rsf", */"JWAM16FullAndreas.rsf.xz", "jdk14v2.rsf.xz" /*, "Eclipse202a.rsf.xz"*/]);
}

public bool measureOne(){
   return measure(["JHotDraw52.rsf"]);
}

public bool measure(list[str] names){

	loc p = |compressed+std:///experiments/Compiler/Benchmarks/|;
	
	for(str name <- names){
		map[str, rel[str,str]] values = readRSF(p[path= p.path + name]);
		rel[str,str] CALL = values["CALL"];
		n = size(CALL);
		println("<name>: CALL contains <n> tuples");
		nTop = size(top(CALL));
		
		res1 = trans(CALL);          
		res2  = reachFromTop1(CALL); 
		res3 = reachFromTop2(CALL); 
		
    }
    return true;
}

public rel[str,str] trans(rel[str,str] CALL){
	return CALL+;
}

public set[str] reachFromTop1(rel[str,str] CALL){
    set[str] t = top(CALL);
	return t + range(domainR(CALL+, t));
}

public set[str] reachFromTop2(rel[str,str] CALL){
	return reach(CALL, top(CALL));
}
