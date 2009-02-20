module benchmark::RSF::RSFCallsError

import Relation;
import Set;
import Graph;
import RSF;
import IO;
import Benchmark;

public bool measure(){

	str dir = "src/benchmark/RSF/";
//	list[str] names = ["JHotDraw52.rsf", "JDK140AWT.rsf", "JWAM16FullAndreas.rsf", "jdk14v2.rsf", "Eclipse202a.rsf"];
 	list[str] names = ["JHotDraw52.rsf"];
	
	for(str name : names){
		map[str, rel[str,str]] values = readRSF(dir + name);
		rel[str,str] CALL = values["CALL"];
		n = size(CALL);
		println("<name>: CALL contains <n> tuples");
	
		<time1, res1> = trans(CALL);
		<time2, res2> = reachFromTop1(CALL);
		<time3, res3> = reachFromTop2(CALL);
		println("Time: trans <time1>, reachFromTop1 <time2>, reachFromTop2 <time3> msec");
    }
    return true;
}

public tuple[real, rel[str,str]]trans(rel[str,str] CALL){
	start = currentTimeMillis();
	res = CALL+;
	return <currentTimeMillis() - start, res>;
}

public tuple[real, rel[str,str]] reachFromTop1(rel[str,str] CALL){
	start = currentTimeMillis();
	res = domainR(CALL+, top(CALL));
	return <currentTimeMillis() - start, res>;
}

public tuple[real,res[str,str]] reachFromTop2(rel[str,str] CALL){
	start = currentTimeMillis();
	res = reach(CALL, top(CALL));
	return <currentTimeMillis() - start, res>;
}