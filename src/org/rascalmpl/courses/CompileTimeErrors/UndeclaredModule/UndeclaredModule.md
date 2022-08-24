# UndeclaredModule

.Synopsis
Reference to a non-existing module.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
This can, for example, occur in a qualified name.

Remedies:

*  Check that the offending module name is spelled correctly.
*  Create a module of that name.

.Examples
Referring to the non-existing module `M` gives errors:
[source,rascal-shell,error]
----
M::x = 3;
M::f(3);
----

.Benefits

.Pitfalls

