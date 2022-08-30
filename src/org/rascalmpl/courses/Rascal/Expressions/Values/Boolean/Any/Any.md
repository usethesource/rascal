# Boolean Any

.Index
any

.Synopsis
Any combination of argument values is true.

.Syntax
`any ( _Exp_~1~, _Exp_~2~, ... )`

.Types

//


|            |            |     |                                    |
| --- | --- | --- | --- |
| `_Exp~1~_` | `_Exp~2~_` | ... | `any ( _Exp~1~_, _Exp~2~_, ... )`  |
|`bool`     | `bool`    | ... | `bool`                           |


.Function

.Details

.Description
Yields `true` when at least one combination of values of _Exp_~i~ is true.

.Examples
```rascal-shell
any(int n <- [1 .. 10], n % 2 == 0);
```

.Benefits

.Pitfalls

