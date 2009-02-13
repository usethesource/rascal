module RSFCalls

import Relation;
import Set;
import RSF;
import IO;
import Benchmark;

public bool measure(){

	str dir = "src/benchmark/RSF/";
	str name = "JHotDraw52.rsf";
	list[str] names = ["JHotDraw52.rsf", "JDK140AWT.rsf", "jdk14v2.rsf", "Eclipse202a.rsf"];
	
	for(str name : names){
		map[str, rel[str,str]] values = readRSF(dir + name);
		rel[str,str] CALL = values["CALL"];
		n = size(CALL);
		println("<name>: CALL contains <n> tuples");
	
		start = currentTimeMillis();
		trans = CALL+;
		used = currentTimeMillis() - start;
		println("Time: <used> millis");
    }
    return true;
}