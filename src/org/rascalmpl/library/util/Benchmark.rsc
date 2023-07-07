@license{
   (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Davy Landman - Davy.Landman@cwi.nl - CWI}


@synopsis{

Functions for time measurement and benchmarking.

}
@description{

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
```rascal-shell
import util::Benchmark;
int fac(int n) {
  if (n <= 1) return 1;
  else return n * fac(n - 1);
}
```
Here we measure time by using separate calls to `cpuTime` before and after a call to `fac`.
```rascal-shell,continue
before = cpuTime();
fac(50);
cpuTime() - before;
```

See also ((cpuTimeOf)) for a more convenient way of measuring the time spent during a block of code.
}
@pitfalls{
* The timings shown above may be significantly influenced by the documentation compilation process
}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int cpuTime();

@javaClass{org.rascalmpl.library.util.Benchmark}
@synopsis{Returns wall clock time in _milliseconds_ since the Unix epoch}
@description{
Returns the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
}
@pitfalls{
   * The actual accuracy of the time may be not as good as a millisecond. This depends on OS and hardware specifics.
   * Note that the resolution is _milliseconds_ here, while ((cpuTime)) produces nanosecond resolution.
}
public java int realTime();

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
public java int getNanoTime();

@synopsis{Synonym for ((realTime))}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int getMilliTime();

@synopsis{Measure the exact running time of a block of code, using ((cpuTime)).}
public int cpuTimeOf(void () block) {
   int then = cpuTime();
   block();
   return cpuTime() - then;
}

@synopsis{System time in nanoseconds (10^-9^ sec).}
@description{
Returns the CPU time that the current thread has executed in system mode in nanoseconds.

* Current system time in nanoseconds (10^-9^ sec) since the start of the thread that runs the code that calls this function.
* The returned value is of nanoseconds precision but not necessarily nanoseconds accuracy.
* CPU time is the number of CPU cycles times the OS-registered clock speed.
* The other [CPU time]((cpuTime)), next to [System time]((systemTime)) is spent in [User time]((userTime)).
}
@examples{

```rascal-shell,continue
import util::Benchmark;
```

Here we measure time by using separate calls to `sytemTime` before and after a call to `fac`.

```rascal-shell,continue
before = systemTime();
fac(50);
systemTime() - before;
```
}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int systemTime();

@synopsis{Measure the exact running time of a block of code, using ((systemTime)).}
@examples{
 ```rascal-shell,continue
import util::Benchmark;
int fac(int n) = n <= 1 ? 1 : n * fac(n - 1);
systemTimeOf(
   void() { 
      fac(50); 
   } 
);
```
}
public int systemTimeOf(void () block) {
   int then = systemTime();
   block();
   return systemTime() - then;
}

@javaClass{org.rascalmpl.library.util.Benchmark}
@synopsis{User time in nanoseconds (10^-9^ sec)}
@description{
Returns the CPU time that the current thread has executed in user mode in nanoseconds.

* The returned value is of nanoseconds precision but not necessarily nanoseconds accuracy.
* As distinguished from ((DateTime-now)) which returns the wall clock time since the Unix epoch.
* CPU time is the number of CPU cycles times the OS-registered clock speed.
* The other [CPU time]((cpuTime)), next to [user time]((userTime)) is spent in [system time]((systemTime)).
}
@examples{

```rascal-shell
import util::Benchmark;
int fac(0) = 1;
default int fac(int n) = n * fac(n - 1);
```
Here we measure time by using separate calls to `userTime` before and after a call to `fac`.
```rascal-shell,continue
before = userTime();
fac(50);
userTime() - before;
```

}
public java int userTime();

@synopsis{Measure the exact running time of a block of code in nanoseconds, doc combined with previous function.}
@example{
The code to be measured can also be passed as a function parameter to `userTime`:
```rascal-shell,continue
import util::Benchmark;
userTimeOf(
   void() {
      fac(50); 
   } 
);
```
}
public int userTimeOf(void () block) {
   int then = userTime();
   block();
   return userTime() - then;
}

@synopsis{Measure the exact running time of a block of code in milliseconds, doc included in previous function.}
@pitfalls{
* watch out this is measured in milliseconds, not nanoseconds
}
public int realTimeOf(void () block) {
   int then = realTime();
   block();
   return realTime() - then;
}

@synopsis{Utility to measure and compare the execution time a set of code blocks}
@description{

Given is a map that maps strings (used as label to identify each case) to void-closures that execute the code to be benchmarked.
An optional `duration` argument can be used to specify the function to perform the actual measurement. By default the function ((realTimeOf)) is used. A map of labels and durations is returned.
}
@examples{
```rascal-shell
import util::Benchmark;
int fac(int n) {
  if (n <= 1) return 1;
  else return n * fac(n - 1);
}
```

We measure two calls to the factorial function with arguments `100`, respectively, `200` 
(using by default ((realTime)) that returns milliseconds):
```rascal-shell,continue
benchmark(
   ("fac100" : void() {
                  fac(100);
               }, 
   "fac200" :  void() {
                  fac(200);
               }) 
   );
```

We can do the same using ((userTime)) that returns nanoseconds:
```rascal-shell,continue
benchmark( 
   ("fac100" : void() {
                  fac(100);
            }, 
   "fac200" : void() {
                  fac(200);
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
* Long running terminals can be rejuvenated on demand by a call to ((gc)).
}
@pitfalls{
* Although a GC cycle is triggered by this function, it guarantees nothing about the effect of this cycle in terms of completeness or precision in removing garbage from the heap.
* GC only works for real garbage. So if there is an unrelated accidental memory leak somewhere, it may better to start a fresh JVM to measure the current functionality under scrutiny.
}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java void gc();
