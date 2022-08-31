# List in

.Synopsis
Membership test on list elements.

.Index
in

.Syntax
`Exp~1~ in Exp~2~`

.Types

//

| `_Exp~1~_`           |  `_Exp~2~_`      | `_Exp~1~_ in _Exp~2~_`  |
| --- | --- | --- |
| `_T~1~_`  <: `_T~2~_` |  `list[_T~2~_]`  | `bool`               |


.Function

.Details

.Description
Yields `true` if the value of Exp~1~ occurs as element in the value of Exp~2~ and `false` otherwise. 
The type of _Exp_~1~ should be compatible with the element type of _Exp_~2~.

.Examples
```rascal-shell
2 in [1, 2, 3];
4 in [1, 2, 3];
```

.Benefits

.Pitfalls

