module Benchmark

/*
 * A rudimentary benchmarking framework:
 * - currentTimeMillis
 * - benchmark
 */

import IO;

@doc{Current time in milliseconds since January 1, 1970 GMT.}
@javaImport{import java.lang.System;}
@javaClass{org.meta_environment.rascal.library.Benchmark}
public real java currentTimeMillis();

@doc{Measure and report the execution time of name:void-closure pairs}
public void benchmark(map[str, void()] Cases)
{
	measurements = ();
	for(str Name <- Cases){
		real ctm1 = currentTimeMillis();
		Fun = Cases[Name];
		Fun();
		real ctm2 = currentTimeMillis();
		measurements[Name] = ctm2 - ctm1;
	}
	
	println(measurements);
	
}

