# Variable Declaration Pattern

.Synopsis
Variable declaration in abstract pattern.

.Syntax

.Types

.Function

.Details

.Description

A variable declaration
[source,rascal,subs="quotes"]
----
_Type_ _Var_
----
can be used as abstract pattern.
A variable declaration introduces a new variable _Var_ that matches any value of the given type _Type_.
That value is assigned to _Var_ when the whole match succeeds.

The scope of this variable is the outermost expression in which the pattern occurs
or the enclosing <<If>>, <<While>>, or <<Do>> if the pattern occurs in the test expression of those statements.

.Examples
Let's first perform a match that succeeds:
[source,rascal-shell,error]
----
str S := "abc";
----
and now we attempt to inspect the value of `S`:
[source,rascal-shell,continue,error]
----
S;
----

As mentioned above: `S` is only bound in the scope of the match expression!
Let's explore how bindings work in an if statement:
[source,rascal-shell]
----
import IO;
if(str S := "abc")
   println("Match succeeds, S == \"<S>\"");
----

.Benefits

.Pitfalls

