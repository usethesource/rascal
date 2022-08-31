# ListRelation Transitive Closure

.Synopsis
Transitive closure on binary list relation values.

.Index
+

.Syntax
`Exp +`

.Types


|`Exp`               | `Exp +`            |
| --- | --- |
| `lrel[_T~1~_, _T~2~_]` | `lrel[_T~1~_, _T~2~_]`  |


.Function

.Details

.Description
Returns the transitive closure of a binary listrelation.
Transitive closure is defined by repeated composition of a relation.
If we define for a given relation R:

*  R^1^ = R
*  R^2^ = R o R
*  R^3^ = R o R^2^
*  ...


then the transitive closure R+ can be defined as

*  R+ = R^1^ + R^2^ + R^3^ + ...


.Examples
```rascal-shell
[<1,2>, <2,3>, <3,4>]+;
```

.Benefits

.Pitfalls

