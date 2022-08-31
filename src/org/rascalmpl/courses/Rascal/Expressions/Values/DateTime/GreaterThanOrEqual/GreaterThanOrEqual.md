# DateTime GreaterThanOrEqual

.Synopsis
Greater than or equal operator on datetime values.

.Index
>=

.Syntax
`Exp~1~ >= Exp~2~`

.Types

//

| `_Exp~1~_`      | `_Exp~2~_`      | `_Exp~1~_ >= _Exp~2~_`  |
| --- | --- | --- |
| `datetime`     |  `datetime`    | `bool`                |


.Function

.Details

.Description
Yields `true` if the `datetime` value of Exp~1~ is later in time than the `datetime` value
of _Exp_~2~ or if both values are equal, and `false` otherwise.

.Examples
```rascal-shell
$2011-07-15$ >= $2010-07-15$;
$2010-07-15$ >= $2010-07-14$;
```

.Benefits

.Pitfalls

