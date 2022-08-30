# Set StrictSuperSet

.Synopsis
Strict superset operator on set values.

.Index
>

.Syntax
`_Exp_~1~ > _Exp_~2~`

.Types


| `_Exp~1~_`    |  `_Exp~2~_`    | `_Exp~1~_ > _Exp~2~_`  |
| --- | --- | --- |
| `set[_T~1~_]` |  `set[_T~2~_]` | `bool`               |


.Function

.Details

.Description
Yields `true` if the value of _Exp_~1~ is a strict superset of the value of _Exp_~2~, and `false` otherwise.

.Examples
```rascal-shell
{1, 2, 3, 4} > {3, 2, 1};
{1, 2, 3, 4} > {4, 3, 2, 1};
```

.Benefits

.Pitfalls

