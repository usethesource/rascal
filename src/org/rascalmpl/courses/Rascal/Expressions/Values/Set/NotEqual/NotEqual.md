# Set NotEqual

.Synopsis
Not equal operator on set values.

.Index
!=

.Syntax
`Exp~1~ != Exp~2~`

.Types


| `_Exp~1~_`    |  `_Exp~2~_`    | `_Exp~1~_ != _Exp~2~_`  |
| --- | --- | --- |
| `set[_T~1~_]` |  `set[_T~2~_]` | `bool`                |


.Function

.Details

.Description
Yields `true` if both arguments are unequal sets and `false` otherwise.

.Examples
```rascal-shell
{1, 2, 3} != {3, 2, 1};
{1, 2, 3} != {1, 2};
```

.Benefits

.Pitfalls

