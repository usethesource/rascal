# Empty Set

.Synopsis
Illegal operation on an empty set.

.Syntax

.Types
`data RuntimeException = EmptySet();`
       
.Usage
`import Exception;` (only needed when `EmptySet` is used in `catch`)

.Details

.Description
Rascal provides many operations and functions on sets, see [set values]((Rascal:Values-Set))
and [set functions]((Libraries:Prelude-Set)).
This error is generated when a function or operations cannot handle the empty set.

Remedies:

*  Guard the function or operation with a test on the empty set ([isEmpty]((Libraries:Set-isEmpty))) and 
  take alternative action in that case.
*  Catch the `EmptySet` yourself, see [try catch]((Rascal:Statements-TryCatch)).

.Examples

Import the `Set` library and introduce `S` with an empty set as value:
```rascal-shell,continue,error
import Set;
S = {};
```
Taking an element from an empty set gives an error:
```rascal-shell,continue,error
getOneFrom(S);
```
We can also catch the `EmptySet` error. First import the Rascal exceptions (which are also included in `Prelude`)
and `IO`:
```rascal-shell,continue,error
import Exception;
import IO;
try 
  println(getOneFrom(S)); 
catch EmptySet(): 
  println("Cannot apply getOneFrom to empty set");
```

.Benefits

.Pitfalls

