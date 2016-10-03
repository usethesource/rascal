# ArgumentMismatch

.Synopsis
The called signature does not match any defined function.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

A function has a name and a signature (the names and types of its arguments). 
This error is reported when a call of a function cannot be associated with a function declaration.

Remedies:

*  Modify the call so that the arguments match the function declaration.
*  Write a new definition for a function with the same name, that matches the argument types in the call.

.Examples
Define a function `triple` that multiplies its argument by 3:
[source,rascal-shell,error]
----
int triple(int x) = 3 * x;
----
It works fine:
[source,rascal-shell,continue,error]
----
triple(5)
----
Unless it is called with an argument of a wrong type:
[source,rascal-shell,continue,error]
----
triple([1,2,3])
----
We can define a new version of `triple` function that accepts lists:
[source,rascal-shell,continue,error]
----
list[int] triple(list[int] L) = [3 * x | x <- L];
triple([1,2,3]);
----

.Benefits

.Pitfalls

