# Literal Pattern

.Synopsis
Literal in abstract pattern.

.Syntax

.Types

.Function

.Details

.Description

A literal of one of the basic types <<Values-Boolean>>, <<Values-Integer>>, <<Values-Real>>, <<Values-Number>>, <<Values-String>>, <<Values-Location>>, or <<Values-DateTime>>
can be used as abstract pattern.
A literal pattern matches with a value that is identical to the literal.

.Examples
A literal pattern matches with a value that is equal to it:
[source,rascal-shell]
----
123 := 123
"abc" := "abc"
----
A literal pattern does not match with a value that is not equal to it:
[source,rascal-shell]
----
123 := 456
"abc" := "def"
----
If the type of the literal pattern is *incomparable* to the subject's type, a static type error is produced
to announce that the match is guaranteed to fail:
[source,rascal-shell,error]
----
123 := "abc";
----
However, a literal pattern can be used to filter among other values:
[source,rascal-shell]
----
value x = "abc";
123 := x;
x = 123;
123 := x;
----


.Benefits

.Pitfalls

