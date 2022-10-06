@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module RSF::RSFCalls

import Relation;
import util::Math;
import Set;
import  analysis::graphs::Graph;
import lang::rsf::IO;
import IO;
import util::Benchmark;

value main() = measure();

public bool measure(){
  return measure(["JHotDraw52.rsf", "JDK140AWT.rsf", "JWAM16FullAndreas.rsf", "jdk14v2.rsf", "Eclipse202a.rsf"]);
}

public bool measureOne(){
   return measure(["JHotDraw52.rsf"]);
}

public bool measure(list[str] names){

	loc p = |benchmarks:///RSF/|;
	
	for(str name <- names){
		map[str, rel[str,str]] values = readRSF(p[path= p.path + name]);
		rel[str,str] CALL = values["CALL"];
		n = size(CALL);
		println("<name>: CALL contains <n> tuples");
		nTop = size(top(CALL));
		println("<name>: size top <nTop>");
	
		time0 = realTime();
		res1 = trans(CALL);          time1 = realTime();
		res2  = reachFromTop1(CALL); time2 = realTime();
		res3 = reachFromTop2(CALL);  time3 = realTime();
		
		d1 = time1 - time0;
		d2 = time2 - time1;
		d3 = time3 - time2;
		
		println("Time (msec): trans <d1>, reachFromTop1 <d2>, reachFromTop2 <d3>");
		
		size1 = size(res1); size2= size(res2); size3 = size(res3);
		println("Size (elms): trans <size1>, reachFromTop1 <size2>, reachFromTop2 <size3>");
		if(res2 != res3){
			println("***\> res2 != res3");
		}
		
    }
    return true;
}

public rel[str,str] trans(rel[str,str] CALL){
	return CALL+;
}

public set[str] reachFromTop1(rel[str,str] CALL){
    set[str] tp = top(CALL);
	return tp + range(domainR(CALL+, tp));
}

public set[str] reachFromTop2(rel[str,str] CALL){
	return reach(CALL, top(CALL));
}
