---
title: "Value GreaterThanOrEqual"
keywords: ">="
---

.Synopsis
Greater than or equal operator on values.

.Syntax
`Exp~1~ >= Exp~2~`

.Types


| `Exp~1~` | `Exp~2~` | `Exp~1~ >= Exp~2~` |
| --- | --- | --- |
| `value`   |  `value`  | `bool`               |


.Function

.Details

.Description
By brute force, a total less than operator between two values _V_~1~ and _V_~2~ of arbitrary types _T_~1~ and _T_~2~ is defined:

*  If the types _T~1~_ and _T~2~_ can be compared then _V~1~_ less than _V~2~_ is used.

*  Otherwise values are ordered according their type name, for instance, `int` is smaller than `list`, and `map` is smaller than `rel`.


Greater than or equal yields `true` if the value of _Exp_~2~ is strictly less
than (according to the ordering defined above) the value of _Exp_~1~ or if both values are equal, and `false` otherwise.

.Examples

Introduce two variables `X`, `Y` and `Z` and force them to be of type `value`:
```rascal-shell,continue
value X = "def";
value Y = "abc";
value Z = 3.14;
```
Now compare `X` and `Y`:
```rascal-shell,continue
X >= Y;
```
and `X` and `Z`:
```rascal-shell,continue
X >= Z;
```

.Benefits

.Pitfalls

