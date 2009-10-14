module benchmark::RSF::RSFCalls

import Relation;
import Integer;
import Set;
import Graph;
import RSF;
import IO;
import Benchmark;

public bool measure(){
  return measure(["JHotDraw52.rsf", "JDK140AWT.rsf", "JWAM16FullAndreas.rsf", "jdk14v2.rsf", "Eclipse202a.rsf"]);
}

public bool measureOne(){
   return measure(["JHotDraw52.rsf"]);
}
public bool measure(list[str] names){

	str dir = "benchmarks/benchmark/RSF/";
	
	for(str name <- names){
		map[str, rel[str,str]] values = readRSF(dir + name);
		rel[str,str] CALL = values["CALL"];
		n = size(CALL);
		println("<name>: CALL contains <n> tuples");
		nTop = size(top(CALL));
		println("<name>: size top <nTop>");
	
		time0 = currentTimeMillis();
		res1 = trans(CALL);          time1 = currentTimeMillis();
		res2  = reachFromTop1(CALL); time2 = currentTimeMillis();
		res3 = reachFromTop2(CALL);  time3 = currentTimeMillis();
		
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
    set[str] top = top(CALL);
	return top + range(domainR(CALL+, top));
}

public set[str] reachFromTop2(rel[str,str] CALL){
	return reach(CALL, top(CALL));
}