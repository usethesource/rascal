# UndeclaredKeywordParameter

.Synopsis
A function is called with a keyword parameter that was not declared in the function's declaration.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
Rascal functions may have keyword parameters. This error is generated when a function call uses an undeclared keyword parameter.

Remedies:

*  Rename the keyword parameter in the call.
*  Add a new keyword parameter to the function.

.Examples
[source,rascal-shell,error]
----
int incr(int n, int delta=1) = n + delta;
----
Calling `incr` with a wrong keyword parameter gives an error:
[source,rascal-shell,continue,error]
----
incr(3, diff=5);
----
This can be fixed by using the correct name for the keyword parameter:
[source,rascal-shell,continue,error]
----
incr(3, delta=5);
----

.Benefits

.Pitfalls

