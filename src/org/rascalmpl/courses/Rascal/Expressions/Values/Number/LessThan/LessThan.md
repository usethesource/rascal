# Number LessThan

.Synopsis
Less than operator on numeric values.

.Index
<

.Syntax
`Exp~1~ < Exp~2~`

.Types


| `Exp~1~`  |  `Exp~2~` | `Exp~1~ < Exp~2~`  |
| --- | --- | --- |
| `int`      |  `int`     | `bool`               |
| `int`      |  `real`    | `bool`               |
| `real`     |  `real`    | `bool`               |


.Function

.Details

.Description
Yields `true` if the value of Exp~1~ is numerically less than the value of Exp~2~, and `false` otherwise.

.Examples
```rascal-shell
13 < 12
12 < 13
13.5 < 12
12.5 < 13
```

.Benefits

.Pitfalls

