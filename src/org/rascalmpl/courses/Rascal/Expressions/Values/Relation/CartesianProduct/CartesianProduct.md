# Relation CartesianProduct

.Synopsis
Cartesian product of two relation values.

.Index
*

.Syntax
`Exp~1~ * Exp~2~`

.Types


|`Exp~1~`      | `Exp~2~`     | `Exp~1~ * Exp~2~`   |
| --- | --- | --- |
| `set[T~1~]`  | `set[T~2~]`  | `rel[T~1~, T~2~]`   |


.Function

.Details

.Description
Returns a binary relation that is the http://en.wikipedia.org/wiki/Cartesian_product[Cartesian product] of two sets.

.Examples
```rascal-shell
{1, 2, 3} * {9};
{1, 2, 3} * {10, 11};
```

.Benefits

.Pitfalls

