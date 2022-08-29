# Empty Map

.Synopsis
Illegal operation on an empty map.

.Syntax

.Types 
`data RuntimeException = EmptyMap();`
       
.Usage
`import Exception;` (only needed when `EmptyMap` is used in `catch`)

.Details

.Description
Rascal provides many operations and functions on maps, see [map values]((Rascal:Values-Map))
and [map functions]((Libraries:Prelude-Map)).
This error is generated when a function or operations cannot handle the empty map case.

Remedies: 

*  Guard the function or operation with a test on the empty map ([isEmpty]((Libraries:Map-isEmpty))) and 
  take alternative action in that case.
*  Catch the `EmptyMap` yourself, see [try catch]((Rascal:Statements-TryCatch)).

.Examples

Import the `Map` library and introduce `M` with an empty map as value:
```rascal-shell,error
import Map;
M = ();
```
Trying to get an arbitrary value from it gives an error:
```rascal-shell,continue,error
getOneFrom(M);
```
We can also catch the `EmptyMap` error. First import the Rascal exceptions (which are also included in `Prelude`)
and `IO`:
```rascal-shell,continue,error
import Exception;
import IO;
try 
  println(getOneFrom(M)); 
catch EmptyMap(): 
  println("Cannot use getOneFrom on empty map");
```

.Benefits

.Pitfalls

