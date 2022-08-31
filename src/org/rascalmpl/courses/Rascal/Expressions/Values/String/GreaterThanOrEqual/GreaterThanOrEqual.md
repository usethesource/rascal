# String GreaterThanOrEqual

.Synopsis
Greater than or equal operator on string values.

.Index
>=

.Syntax
`Exp~1~ >= Exp~2~`

.Types


| `Exp~1~` | `Exp~2~` | `Exp~1~ >= Exp~2~`  |
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

