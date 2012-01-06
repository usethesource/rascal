/*****************************/
/* DEPRECATED                */
/* Use util::Benchmark       */
/* DO NOT EDIT               */
/*****************************/

module Time

@doc{Return current time in nanoseconds since the epoch.}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int getNanoTime();

@doc{Return current time in milliseconds since the epoch.}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int getMilliTime();
