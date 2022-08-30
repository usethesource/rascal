# DateTime Equal

.Synopsis
Equality on datetime values.

.Index
==

.Syntax
`_Exp_~1~ == _Exp_~2~`

.Types

//

|====
| `_Exp~1~_`      | `_Exp~2~_`      | `_Exp~1~_ == _Exp~2~_` 

| `datetime`     |  `datetime`    | `bool`               
|====

.Function

.Details

.Description
Yields `true` if both arguments are identical `datetime` values and `false` otherwise.

.Examples
```rascal-shell
$2010-07-15$ == $2010-07-15$;
$2010-07-15$ == $2010-07-14$;
```

.Benefits

.Pitfalls

