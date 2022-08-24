# Relation ReflexiveTransitiveClosure

.Synopsis
The reflexive transitive closure of a binary relation.

.Index
*

.Syntax
`_Exp_ *`

.Types


|====
|`_Exp_`              | `_Exp_ *`           

| `rel[_T~1~_, _T~2~_]` | `rel[_T~1~_, _T~2~_]` 
|====

.Function

.Details

.Description

Reflexive transitive closure is defined by repeated composition of a relation.
If we define for a given relation R:

*  R^0^ = identity relation = `{<a, a>, <b, b> | <a, b> <- R}`
*  R^1^ = R
*  R^2^ = R o R
*  R^3^ = R o R^2^
*  ...


then the reflexive transitive closure R* can be defined in two ways:
(also see <<Relation-TransitiveClosure>>):
*  R* = R^0^ + R^1^ + R^2^ + R^3^ + ...
*  R* = R^0^ + R+


.Examples
[source,rascal-shell]
----
{<1,2>, <2,3>, <3,4>}*;
----

.Benefits

.Pitfalls

