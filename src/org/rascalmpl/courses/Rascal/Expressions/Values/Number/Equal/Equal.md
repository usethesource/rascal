# Number Equal

.Index
==

.Synopsis
Equality operator on numeric values.

.Syntax
`Exp~1~ == Exp~2~`

.Types


| `_Exp~1~_`  |  `_Exp~2~_` | `_Exp~1~_ == _Exp~2~_`   |
| --- | --- | --- |
| `int`      |  `int`     | `bool`                 |
| `int`      |  `real`    | `bool`                 |
| `real`     |  `real`    | `bool`                 |


.Function

.Details

.Description
Yields `true` if the value of both arguments is numerically equal, and `false` otherwise.

.Examples
```rascal-shell
12 == 12
12 == 12.0
12 == 13
12 == 13.0
3.14 == 3.14
3.14 == 3
```

.Benefits

.Pitfalls

