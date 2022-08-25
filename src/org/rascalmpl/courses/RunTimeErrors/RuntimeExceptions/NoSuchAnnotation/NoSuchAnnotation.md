# No Such Annotation

.Synopsis
Annotation without a value.

.Types
`data RuntimeException = NoSuchAnnotation(str label);`
       
.Usage
`import Exception;` (only needed when `NoSuchAnnotation` is used in `catch`)


.Function
       
.Usage

.Details

.Description
An [annotation]((Rascal:Declarations-Annotation)) can be associated with any node value
(including any [algebraic data type]((Rascal:Declarations-AlgebraicDataType))).
This error is generated when the value of an annotation is requested but has not been defined.

Remedies:

*  Ensure that the annotation value is properly initialized for all relevant parts of the node value. 
*  Use the 
   [isDefined]((Rascal:Boolean-isDefined)) and 
   [ifDefinedElse]((Rascal:Boolean-ifDefinedElse)) operators to check whether the annotation value 
   is set and act accordingly.
*  Catch the `NoSuchAnnotation yourself`, see [try catch]((Rascal:Statements-TryCatch)).

.Examples

INFO: Eliminate the double [red]##Error: Error:##

[source,rascal-shell,error]
----
data Fruit = apple(int n) | orange(int n);
anno str Fruit @ quality;
piece = orange(13);
piece@quality;
----
Use the unary postfix operator isDefined `?` to check whether the `quality` annotation is set:
[source,rascal-shell,continue,error]
----
piece@quality?;
----
Use the ternary operator ifDefinedElse `?` to compute an alternative value when the `quality` annotation is not set:
[source,rascal-shell,continue,error]
----
piece@quality ? "no quality value";
----
We can also catch the `NoSuchAnnotation` error. First import the Rascal exceptions (which are also included in `Prelude`)
and `IO`:
[source,rascal-shell,continue,error]
----
import Exception;
import IO;
try piece@quality; catch NoSuchAnnotation(l): println("No such annotation: <l>");
----
Finally, we can just assign a value to the `quality` annotation:
[source,rascal-shell,continue,error]
----
piece@quality = "excellent";
piece@quality;
----

.Benefits

.Pitfalls

WARNING: Using white space around the `@` confuses the Rascal parser

