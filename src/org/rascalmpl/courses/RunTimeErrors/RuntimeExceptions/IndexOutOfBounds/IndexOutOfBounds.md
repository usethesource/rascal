# Index Out Of Bounds

.Synopsis
Index is out of bounds.

.Syntax

.Types
`data RuntimeException = IndexOutOfBounds(int index)`

.Usage
`import Exception;` (only needed when `IndexOutOfBounds` is used in `catch`)

.Details

.Description
Subscription is possible on various ordered types, including [list]((Rascal:List-Subscription)),
link:{RascaLangl}#Tuple-Subscription[tuple], and
[node]((Rascal:Node-Subscription)).
This error is generated when a subscript is out of bounds for the value that is being subscripted.

Remedies:

* Guard the subscription with a test that the index is within bounds.
* Make your code less dependent on index values. Suggestions:
  ** Use the [index]((Libraries:List-index)) to produce all legal indices of a list. 
     Instead of `for(int i <- [0..size(L)]) { ... }` use `for(int i <- index(L)) { ... }`.
  ** Use a [list slice]((Rascal:List-Slice)) to automate part of the index computation.
*  Catch the `IndexOutOfBounds` yourself, see [try catch]((Rascal:Statements-TryCatch)).


.Examples

Initialize a list `L`:
[source,rascal-shell,continue,error]
----
L = [0, 10, 20, 30, 40];
----
The legal indices are 0, 1, 2, 3, 4, so index 5 gives an error:
[source,rascal-shell,continue,error]
----
L[5];
----
We can catch the `IndexOutOfBounds` error. First import the Rascal exceptions (which are also included in `Prelude`)
and `IO`:
[source,rascal-shell,continue,error]
----
import Exception;
import IO;
try 
  L[5]; 
catch IndexOutOfBounds(msg):
  println("The message is: <msg>");
----


.Benefits

.Pitfalls

