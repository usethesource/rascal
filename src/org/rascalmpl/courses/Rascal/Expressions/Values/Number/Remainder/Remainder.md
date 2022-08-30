# Number Remainder

.Synopsis
Remainder of two integer values.

.Index
%

.Syntax
`_Exp_~1~ % _Exp_~2~`

.Types


| `_Exp~1~_`  |  `_Exp~2~_` | `_Exp~1~_ % _Exp~2~_`  |
| --- | --- | --- |
| `int`      |  `int`     | `int`                |


.Function

.Details

.Description
Yields the remainder when dividing the of _Exp_~1~ by the value of _Exp_~2~.

.Examples
```rascal-shell
12 % 5
12 % 6
```

.Benefits

.Pitfalls
Remainder is only defined on integers:
```rascal-shell,error
13.5 % 6
```

