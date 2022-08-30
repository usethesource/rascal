# DateTime LessThanOrEqual

.Synopsis
Less than or equal operator on datetime values.

.Index
<=

.Syntax
`_Exp_~1~ <= _Exp_~2~`

.Types

//

|                 |                 |                         |
| --- | --- | --- |
| `_Exp~1~_`      | `_Exp~2~_`      | `_Exp~1~_ <= _Exp~2~_`  |
| `datetime`     |  `datetime`    | `bool`                |


.Function

.Details

.Description
Yields `true` if the `datetime` value of _Exp_~1~ is earlier in time than the `datetime` value
of _Exp_~2~ or if the values of _Exp_~1~ and _Exp_~2~ are equal, and `false` otherwise.

.Examples
```rascal-shell
$2010-07-15$ <= $2010-07-15$;
$2011-07-15$ <= $2010-07-14$;
```

.Benefits

.Pitfalls

