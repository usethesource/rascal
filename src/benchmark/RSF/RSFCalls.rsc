module RSFCalls

import Relation;
import Set;
import Graph;
import RSF;
import IO;
import Benchmark;

public bool measure(){

	str dir = "src/benchmark/RSF/";
	list[str] names = ["JHotDraw52.rsf", "JDK140AWT.rsf", "JWAM16FullAndreas.rsf", "jdk14v2.rsf", "Eclipse202a.rsf"];
// 	list[str] names = ["JHotDraw52.rsf"];
	
	for(str name : names){
		map[str, rel[str,str]] values = readRSF(dir + name);
		rel[str,str] CALL = values["CALL"];
		n = size(CALL);
		println("<name>: CALL contains <n> tuples");
	
		time0 = currentTimeMillis();
		res1 = trans(CALL);          time1 = currentTimeMillis() - time0;
		res2  = reachFromTop1(CALL); time2 = currentTimeMillis() - time1;
		res3 = reachFromTop2(CALL);  time3 = currentTimeMillis() - time2;
		
		println("Time (msec): trans <time1>, reachFromTop1 <time2>, reachFromTop2 <time3>");
		
		size1 = size(res1); size2= size(res2); size3 = size(res3);
		println("Size (elms): trans <size1>, reachFromTop1 <size2>, reachFromTop2 <size3>");
		if(res2 != res3){
			println("***> res2 != res3");
		}
		
    }
    return true;
}

public rel[str,str] trans(rel[str,str] CALL){
	return CALL+;
}

public set[str] reachFromTop1(rel[str,str] CALL){
    top = top(CALL);
	return top + range(domainR(CALL+, top));
}

public set[str] reachFromTop2(rel[str,str] CALL){
	return reach(CALL, top(CALL));
}