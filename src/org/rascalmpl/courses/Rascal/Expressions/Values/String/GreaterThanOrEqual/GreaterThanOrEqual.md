# String GreaterThanOrEqual

.Synopsis
Greater than or equal operator on string values.

.Index
>=

.Syntax
`_Exp_~1~ >= _Exp_~2~`

.Types


| `_Exp~1~_` | `_Exp~2~_` | `_Exp~1~_ >= _Exp~2~_`  |
| --- | --- | --- |
| `str`     |  `str`    | `bool`                |


.Function

.Details

.Description
Yields `true` if the string value of _Exp_~1~ is lexicographically greater
than the string value of _Exp_~2~ or if both strings are equal, and `false` otherwise.

.Examples
```rascal-shell
"abc" >= "abc";
"abcdef" >= "abc";
"defghi" >= "abcdef";
"a" >= "abc";
```

.Benefits

.Pitfalls

