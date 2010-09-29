module Benchmark

import IO;

@doc{Current time in nanoseconds since the start of the thread that runs the code that calls this function.}
@javaImport{import java.lang.System;}
@javaClass{org.rascalmpl.library.Benchmark}
public int java userTime();

@doc{Current time in nanoseconds since the start of the thread that runs the code that calls this function.}
@javaImport{import java.lang.System;}
@javaClass{org.rascalmpl.library.Benchmark}
public int java systemTime();

@doc{Current time in nanoseconds since the start of the thread that runs the code that calls this function.}
@javaImport{import java.lang.System;}
@javaClass{org.rascalmpl.library.Benchmark}
public int java cpuTime();

@doc{Current time in milliseconds since the start of the thread that runs the code that calls this function.}
@javaImport{import java.lang.System;}
@javaClass{org.rascalmpl.library.Benchmark}
public int java realTime();

@doc{Measure and report the execution time of name:void-closure pairs}
public void benchmark(map[str, void()] Cases)
{
	measurements = ();
	for (str Name <- Cases) {
		measurements[Name] = duration(Cases[Name]);
	}
	
	println(measurements);
}

@doc{Measure the exact running time of a block of code}
public int userTime(void () block) {
   int now = userTime();
   block();
   return userTime() - now;
}

@doc{Measure the exact running time of a block of code}
public int cpuTime(void () block) {
   int now = cpuTime();
   block();
   return cpuTime() - now;
}

@doc{Measure the exact running time of a block of code}
public int systemTime(void () block) {
   int now = systemTime();
   block();
   return systemTime() - now;
}

@doc{Measure the exact running time of a block of code}
public int realTime(void () block) {
   int now = realTime();
   block();
   return realTime() - now;
}