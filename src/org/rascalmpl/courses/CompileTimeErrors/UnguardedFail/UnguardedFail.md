# UnguardedFail

.Synopsis
Use of `fail` statement outside a condtional context.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
A [fail]((Rascal:Statements-Fail)) statement is only allowed inside conditional statements.
This error is generated when `fail` is used outside a conditional context.

Remedies:

*  Surround the `fail` statement by a conditional conditional statement.
*  Replace the `fail` statement by a [Rascal:Throw] statement.
*  replace the `fail` statement by a [Rascal:Return] statement.

.Examples
Here is a correct (albeit not very useful) use of `fail` where the pattern match `int N := 35` acts as guard:
[source,rascal-shell]
----
if(int N := 35){ if(N > 10) fail; }
----
Any condition (non only one using pattern matching) can act as guard:
[source,rascal-shell,continue]
----
if(true) { fail; }
----
An error occurs when `fail` is used outside a conditional context:
[source,rascal-shell,error]
----
fail;
----

.Benefits

.Pitfalls

