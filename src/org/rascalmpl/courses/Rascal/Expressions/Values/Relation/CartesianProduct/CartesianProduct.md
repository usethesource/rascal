# Relation CartesianProduct

.Synopsis
Cartesian product of two relation values.

.Index
*

.Syntax
`_Exp_~1~ * _Exp_~2~`

.Types


|====
|`_Exp~1~_`      | `_Exp~2~_`     | `_Exp~1~_ * _Exp~2~_`  

| `set[_T~1~_]`  | `set[_T~2~_]`  | `rel[_T~1~_, _T~2~_]`  
|====

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

