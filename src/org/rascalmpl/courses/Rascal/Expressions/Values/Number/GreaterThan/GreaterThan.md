# Number GreaterThan

.Index
>

.Synopsis
Greater than operator on numeric values.

.Syntax
`_Exp_~1~ > _Exp_~2~`

.Types


| `_Exp~1~_`  |  `_Exp~2~_` | `_Exp~1~_ > _Exp~2~_`   |
| --- | --- | --- |
| `int`      |  `int`     | `bool`                |
| `int`      |  `real`    | `bool`                |
| `real`     |  `real`    | `bool`                |


.Function

.Details

.Description
Yields `true` if the value of _Exp~1~_ is numerically greater than the value of _Exp~2~_, and `false` otherwise.

.Examples
```rascal-shell
13 > 12
12 > 13
13.5 > 12
12.5 > 13
```

.Benefits

.Pitfalls

