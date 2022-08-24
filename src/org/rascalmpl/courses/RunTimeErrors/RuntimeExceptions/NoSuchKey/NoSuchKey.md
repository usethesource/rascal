# No Such Key

.Synopsis
A map does not contain a requested key.

.Types
`data RuntimeException = NoSuchKey(value v);`
       
.Usage
`import Exception;` (only needed when `NoSuchKey` is used in `catch`)

.Details

.Description
Rascal provides many operations and functions on maps, 
see link:/Rascal#Values-Map[map values] and link:/Libraries#Prelude-Map[map functions].
This error is generated when a function or operation cannot find a requested key value in a map.

Remedies: 

*  Use the 
   link:/Rascal#Boolean-isDefined[isDefined] and 
   link:/Rascal#Boolean-ifDefinedElse[ifDefinedElse] operator to guard a lookup in a map.
*  Catch the `NoSuchKey` yourself, see link:/Rascal#Statements-TryCatch[try catch].

.Examples

Import the `Map` and `IO` libraries and introduce map `M`:
[source,rascal-shell,error]
----
import Map;
import IO;
M = ("a" : 1, "b" : 2);
----
Indexing `M` with a non-existing key gives an error:
[source,rascal-shell,continue,error]
----
M["c"]
----
Use the postfix isDefined operator `?` to test whether the value is defined:
[source,rascal-shell,continue,error]
----
if(M["c"]?) println("defined"); else println("not defined");
----
Or use the binary ifDefinedElse operator `?` to return an alternative value
when the value of `M["c"]` is undefined:
[source,rascal-shell,continue,error]
----
M["c"] ? 3
----
Yet another solution is to use try/catch.
First we import the Rascal exceptions (which are also included in `Prelude`):
[source,rascal-shell,continue,error]
----
import Exception;
try println(M["c"]); catch NoSuchKey(k): println("Key <k> does not exist");
----

.Benefits

.Pitfalls

