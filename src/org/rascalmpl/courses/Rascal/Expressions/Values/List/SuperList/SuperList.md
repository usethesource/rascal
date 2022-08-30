# List SuperList

.Synopsis
The super list operator on lists.

.Index
>=

.Syntax
`_Exp_~1~ >= _Exp_~2~`

.Types


|                |                 |                         |
| --- | --- | --- |
| `_Exp~1~_`     |  `_Exp~2~_`     | `_Exp~1~_ >= _Exp~2~_`  |
| `list[_T~1~_]` |  `list[_T~2~_]` | `bool`                |


.Function

.Details

.Description
Yields `true` if the value of _Exp_~2~ is equal to or a sublist of the value of _Exp_~1~,  and `false` otherwise.

.Examples
```rascal-shell
[1, 2, 3, 4] >= [1, 2, 3];
[1, 2, 3, 4] >= [1, 2, 3, 4];
[1, 2, 3, 4] >= [1, 2, 3];
[1, 2, 3, 4, 5] >= [1, 3, 5]
```

.Benefits

.Pitfalls

