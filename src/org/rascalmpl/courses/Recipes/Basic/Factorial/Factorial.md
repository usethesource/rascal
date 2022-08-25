# Factorial

.Synopsis
Compute the factorial function.

.Syntax

.Types

.Function

.Details

.Description

.Examples
The http://en.wikipedia.org/wiki/Factorial[factorial]
of a number N is defined as `N * (N-1) * (N-2) * ... * 1`.
Here is the Rascal version:
[source,rascal]
----
include::{LibDir}demo/basic/Factorial.rsc[tags=module]
----
          
<1> `fac` is defined using a conditional expression to distinguish cases.
<2> `fac2` distinguishes cases using pattern-based dispatch ([Rascal Functions]((Rascal:Concepts-Function))).
    Here the case for `0` is defined.
<3> Here all other cases for `fac2` are defined (as indicated by the `default` keyword).
<4> `fac3` shows a more imperative implementation of factorial.

Here is how to use `fac`:

[source,rascal-shell]
----
import demo::basic::Factorial;
fac(47);
----

NOTE: Indeed, Rascal supports arbitrary length numbers.
 
Here is an example of `fac2`:
[source,rascal-shell,continue]
----
fac2(47);
----

.Benefits

.Pitfalls

