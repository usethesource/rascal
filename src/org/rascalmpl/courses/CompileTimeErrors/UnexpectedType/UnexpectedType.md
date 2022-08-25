# UnexpectedType

.Synopsis
A value of a different type was expected.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
This error signals an incompatibility between expected type and actual type.
Some of the situations in which this may occur are
[assert]((Rascal:Statements-Assert)), [variable]((Rascal:Declarations-Variable)),
[solve]((Rascal:Statements-Solve)).

Remedy: adjust the actual type to the expected type.

.Examples
Declaring variable `n` as `int` and assigning it a `str` value gives an error:
[source,rascal-shell,error]
----
int n = "abc";
----
The solution is to assign an `int` value to `n`:
[source,rascal-shell]
----
int n = 123;
----
An `assert` statement expects an argument of type `bool`:
[source,rascal-shell,error]
----
assert 3;
----

.Benefits

.Pitfalls

