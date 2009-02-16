module RSFCalls

import Relation;
import Set;
import Graph;
import RSF;
import IO;
import Benchmark;

public bool measure(){

	str dir = "src/benchmark/RSF/";
	list[str] names = ["JHotDraw52.rsf", "JDK140AWT.rsf"]; //, "jdk14v2.rsf", "Eclipse202a.rsf"];
	
	for(str name : names){
		map[str, rel[str,str]] values = readRSF(dir + name);
		rel[str,str] CALL = values["CALL"];
		n = size(CALL);
		println("<name>: CALL contains <n> tuples");
	
		res1 = trans(CALL);
		res2 = reachFromTop1(CALL);
		res3 = reachFromTop2(CALL);
		println("Time: trans <res1>, reachFromTop1 <res2>, reachFromTop2 <res3> msec");
    }
    return true;
}

public real trans(rel[str,str] CALL){
	start = currentTimeMillis();
	res = CALL+;
	return currentTimeMillis() - start;
}

public real reachFromTop1(rel[str,str] CALL){
	start = currentTimeMillis();
	res = domainR(CALL+, top(CALL));
	return currentTimeMillis() - start;
}

public real reachFromTop2(rel[str,str] CALL){
	start = currentTimeMillis();
	res = reach(CALL, top(CALL));
	return currentTimeMillis() - start;
}