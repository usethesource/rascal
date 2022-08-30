# List Equal

.Synopsis
Equality on lists.

.Index
==

.Syntax
`_Exp_~1~ == _Exp_~2~`

.Types

//

|                |                 |                         |
| --- | --- | --- |
| `_Exp~1~_`     |  `_Exp~2~_`     | `_Exp~1~_ == _Exp~2~_`  |
| `list[_T~1~_]` |  `list[_T~2~_]` | `bool`                |


.Function

.Details

.Description
Yields `true` if both arguments are equal lists and `false` otherwise.

.Examples
```rascal-shell
[1, 2, 3] == [1, 2, 3];
[1, 2, 3] == [3, 2, 1];
```

.Benefits

.Pitfalls

