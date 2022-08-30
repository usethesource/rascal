# Value NotEqual

.Synopsis
Not equal operator on values.

.Index
!=

.Syntax
`_Exp_~1~ != _Exp_~2~`

.Types


| `_Exp~1~_`   | `_Exp~2~_` | `_Exp~1~_ != _Exp~2~_`  |
| --- | --- | --- |
| `value`     |  `value`  | `bool`                |


.Function

.Details

.Description
Yields `true` if both arguments are not identical and `false` otherwise.

.Examples

Introduce two variables `X`, `Y` and `Z` and force them to be of type `value`:
```rascal-shell,continue
value X = "abc";
value Y = "abc";
value Z = 3.14;
```
Now compare `X` and `Y` for inequality:
```rascal-shell,continue
X != Y;
```
and `X` and `Z`:
```rascal-shell,continue
X != Z;
```

.Benefits

.Pitfalls

