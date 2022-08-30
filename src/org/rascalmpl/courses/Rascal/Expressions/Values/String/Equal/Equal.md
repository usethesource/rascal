# String Equal

.Synopsis
Equality operator on string values.

.Index
==

.Syntax
`_Exp_~1~ == _Exp_~2~`

.Types


| `_Exp~1~_` | `_Exp~2~_` | `_Exp~1~_ == _Exp~2~_`  |
| --- | --- | --- |
| `str`     |  `str`    | `bool`                |


.Function

.Details

.Description
Yields `true` if both arguments are identical and `false` otherwise.

.Examples
```rascal-shell
"abc" == "abc";
"abc" == "defghi";
```

.Benefits

.Pitfalls

