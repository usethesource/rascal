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
@contributor{Davy Landman - Davy.Landman@cwi.nl - CWI}

@doc{
#### Synopsis

Functions for time measurement and benchmarking.

#### Description

The `Benchmark` library provides the following functions:
(((TOC)))
}
module util::Benchmark

@synopsis{Write a JVM heap dump to a file.}

@description{
* The file parameter has to be of the `file` scheme.
* The live parameter restricts the dump to only live objects.
}
@javaClass{org.rascalmpl.library.util.Benchmark}
java void heapDump(loc file, bool live=true);

@synopsis{Returns the free memory of the current JVM}
@description{
This returns the number of bytes that can be allocated
still against the current result of ((getTotalMemory)).
}
@javaClass{org.rascalmpl.library.util.Benchmark}
java int getFreeMemory();

@synopsis{Returns the current total memory allocated by the current JVM}
@description{
This returns the number of bytes currently allocated for use by the JVM.
The number can change over time but it's never higher than ((getMaxMemory))`
}
@javaClass{org.rascalmpl.library.util.Benchmark}
java int getTotalMemory();

@synopsis{Returns the maximum amount of memory that is available to the current JVM}
@javaClass{org.rascalmpl.library.util.Benchmark}
java int getMaxMemory();

@synopsis{Returns the amount of memory that is currently in use by the programs running on this JVM}
int getUsedMemory() = getTotalMemory() - getFreeMemory();

@synopsis{Returns the amount of memory that is yet available, in principle, on the current JVM}
int getMaxFreeMemory() = getMaxMemory() - getUsedMemory();

@synopsis{CPU time in nanoseconds (10^-9^ sec)}
@description{
* Current cpu time in __nanoseconds__ (10^-9^ sec) since the start of the thread that runs the code that calls this function.
* This number has nanoseconds resolution, but not necessarily nanosecond accuracy.
}
@examples{
We use the `fac` function described in [Factorial]((Recipes:Basic-Factorial)) as example:
```rascal-shell
import util::Benchmark;
import demo::basic::Factorial;
```
Here we measure time by using separate calls to `cpuTime` before and after a call to `fac`.
```rascal-shell,continue
before = cpuTimeNow();
fac1(50);
cpuTimeNow() - before;
```

See also ((cpuTimeOf)) for a more convenient way of measuring the time spent during a block of code.
}
@pitfalls{
* The timings shown above may be significantly influenced by the documentation compilation process
}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int cpuTimeNow();

@javaClass{org.rascalmpl.library.util.Benchmark}
@synopsis{Returns wall clock time in _milliseconds_ since the Unix epoch}
@description{
Returns the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
}
@pitfalls{
   * The actual accuracy of the time may be not as good as a millisecond. This depends on OS and hardware specifics.
   * Note that the resolution is _milliseconds_ here, while ((cpuTimeNow)) produces nanosecond resolution.
}
public java int realTimeNow();

@synopsis{Return nanoseconds clock time of the JVM's high resolution clock.}
@description{
   See `System.nanoTime` Java documentation. An excerpt:

> Returns the current value of the running Java Virtual Machine's 
> high-resolution time source, in nanoseconds.
> This method can only be used to measure elapsed time and is
> not related to any other notion of system or wall-clock time.
> The value returned represents nanoseconds since some fixed but
> arbitrary <i>origin</i> time (perhaps in the future, so values
> may be negative).  The same origin is used by all invocations of
> this method in an instance of a Java virtual machine; other
> virtual machine instances are likely to use a different origin.
     
> This method provides nanosecond precision, but not necessarily
> nanosecond resolution (that is, how frequently the value changes).
}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int getNanoTimeNow();

@synopsis{Synonym for ((realTimeNow))}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int getMilliTimeNow();

@synopsis{Measure the exact running time of a block of code, using ((cpuTimeNow)).}
public int cpuTimeOf(void () block) {
   int then = cpuTimeNow();
   block();
   return cpuTimeNow() - then;
}

@synopsis{System time in nanoseconds (10^-9^ sec).}
@description{
Returns the CPU time that the current thread has executed in system mode in nanoseconds.

* Current system time in nanoseconds (10^-9^ sec) since the start of the thread that runs the code that calls this function.
* The returned value is of nanoseconds precision but not necessarily nanoseconds accuracy.
* CPU time is the number of CPU cycles times the OS-registered clock speed.
* The other [CPU time]((cpuTimeNow)), next to [System time]((systemTimeNow)) is spent in [User time]((userTimeNow)).
}
@examples{
We use the `fac` function described in [Factorial]((Recipes:Basic-Factorial)) as example:

```rascal-shell
import util::Benchmark;
import demo::basic::Factorial;
```

Here we measure time by using separate calls to `sytemTime` before and after a call to `fac`.

```rascal-shell,continue
before = systemTimeNow();
fac1(50);
systemTimeNow() - before;
```
}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int systemTimeNow();

@synopsis{Measure the exact running time of a block of code, using ((systemTimeNow)).}
@examples{
 ```rascal-shell
import util::Benchmark;
import demo::basic::Factorial;
systemTimeOf(
   void() { 
      fac1(50); 
   } 
);
```
}
public int systemTimeOf(void () block) {
   int then = systemTimeNow();
   block();
   return systemTimeNow() - then;
}

@javaClass{org.rascalmpl.library.util.Benchmark}
@synopsis{User time in nanoseconds (10^-9^ sec)}
@description{
Returns the CPU time that the current thread has executed in user mode in nanoseconds.

* The returned value is of nanoseconds precision but not necessarily nanoseconds accuracy.
* As distinguished from ((DateTime-now)) which returns the wall clock time since the Unix epoch.
* CPU time is the number of CPU cycles times the OS-registered clock speed.
* The other [CPU time]((cpuTimeNow)), next to [user time]((userTimeNow)) is spent in [system time]((systemTimeNow)).
}
@examples{
We use the `fac` function described in [Factorial]((Recipes:Basic-Factorial)) as example:

```rascal-shell
import util::Benchmark;
import demo::basic::Factorial;
```
Here we measure time by using separate calls to `userTime` before and after a call to `fac`.
```rascal-shell,continue
before = userTimeNow();
fac1(50);
userTimeNow() - before;
```

}
public java int userTimeNow();

@synopsis{Measure the exact running time of a block of code in nanoseconds, doc combined with previous function.}
@example{
The code to be measured can also be passed as a function parameter to `userTime`:
```rascal-shell
import util::Benchmark;
import demo::basic::Factorial;
userTimeOf(
   void() {
      fac1(50); 
   } 
);
```
}
public int userTimeOf(void () block) {
   int then = userTimeNow();
   block();
   return userTimeNow() - then;
}

@synopsis{Measure the exact running time of a block of code in milliseconds, doc included in previous function.}
@pitfalls{
* watch out this is measured in milliseconds, not nanoseconds
}
public int realTimeOf(void () block) {
   int then = realTimeNow();
   block();
   return realTimeNow() - then;
}

@synopsis{Utility to measure and compare the execution time a set of code blocks}
@description{

Given is a map that maps strings (used as label to identify each case) to void-closures that execute the code to be benchmarked.
An optional `duration` argument can be used to specify the function to perform the actual measurement. By default the function ((realTimeOf)) is used. A map of labels and durations is returned.
}
@examples{
We use the `fac` function described in [Factorial]((Recipes:Basic-Factorial)) as example:
```rascal-shell
import util::Benchmark;
import demo::basic::Factorial;
```

We measure two calls to the factorial function with arguments `100`, respectively, `200` 
(using by default ((realTimeNow)) that returns milliseconds):
```rascal-shell,continue
benchmark(
   ("fac100" : void() {
                  fac1(100);
               }, 
   "fac200" :  void() {
                  fac1(200);
               }) 
   );
```

We can do the same using ((userTimeNow)) that returns nanoseconds:
```rascal-shell,continue
benchmark( 
   ("fac100" : void() {
                  fac1(100);
            }, 
   "fac200" : void() {
                  fac1(200);
            })
   , userTimeOf);
```
}
public map[str,num] benchmark(map[str, void()] Cases) {
	return benchmark(Cases, realTimeOf);
}

public map[str,num] benchmark(map[str, void()] Cases, int (void ()) duration)
{
	measurements = ();
	for (str Name <- Cases) {
		measurements[Name] = duration(Cases[Name]);
	}
	
	return measurements;
}



@ynopsis{"Force" a JVM garbage collection.}
@description{
This function tries to trigger a garbage collection. It may be useful to call this function
just before measuring the efficiency of a code block, in order to factor out previous effects
on the heap.
}
@benefits{
* This helps avoiding to restart the JVM, and optionally warming it up, for each individual measurement.
* Long running terminal ((RascalShell-REPL))s can be rejuvenated on demand by a call to ((gc)).
}
@pitfalls{
* Although a GC cycle is triggered by this function, it guarantees nothing about the effect of this cycle in terms of completeness or precision in removing garbage from the heap.
* GC only works for real garbage. So if there is an unrelated accidental memory leak somewhere, it may better to start a fresh JVM to measure the current functionality under scrutiny.
}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int gc();
