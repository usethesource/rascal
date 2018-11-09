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
Rascal provides many operations and functions on sets, see link:/Rascal#Values-Set[set values]
and link:/Libraries#Prelude-Set[set functions].
This error is generated when a function or operations cannot handle the empty set.

Remedies:

*  Guard the function or operation with a test on the empty set (link:/Libraries#Set-isEmpty[isEmpty]) and 
  take alternative action in that case.
*  Catch the `EmptySet` yourself, see link:/Rascal#Statements-TryCatch[try catch].

.Examples

Import the `Set` library and introduce `S` with an empty set as value:
[source,rascal-shell,continue,error]
----
import Set;
S = {};
----
Taking an element from an empty set gives an error:
[source,rascal-shell,continue,error]
----
getOneFrom(S);
----
We can also catch the `EmptySet` error. First import the Rascal exceptions (which are also included in `Prelude`)
and `IO`:
[source,rascal-shell,continue,error]
----
import Exception;
import IO;
try 
  println(getOneFrom(S)); 
catch EmptySet(): 
  println("Cannot apply getOneFrom to empty set");
----

.Benefits

.Pitfalls

