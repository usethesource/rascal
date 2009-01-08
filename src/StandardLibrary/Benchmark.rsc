module Benchmark

import IO;

public double java currentTimeMillis()
@doc{currentTimeMillis -- current time in milliseconds since January 1, 1970 GMT.}
@java-import{import java.lang.System;}
{
	double ctm = System.currentTimeMillis();
	return values.dubble(ctm);
}

public void benchmark(rel[str, void()] Cases)
@doc{benchmark -- measure and report the execution time of <name, void-closure> pairs}
{
	for(<str Name, void() Fun> : Cases){
		double ctm1 = currentTimeMillis();
		#Fun();
		double ctm2 = currentTimeMillis();
		measurements[Name] = ctm2 - ctm1;
	}
	
	println(measurements);
	
}

