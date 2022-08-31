# String LessThanOrEqual

.Synopsis
Less than or equal operator on string values.

.Index
<=

.Syntax
`Exp~1~ <= Exp~2~`

.Types


| `_Exp~1~_` | `_Exp~2~_` | `_Exp~1~_ <= _Exp~2~_`  |
| --- | --- | --- |
| `str`     |  `str`    | `bool`                |


.Function

.Details

.Description
Yields `true` if the string value of _Exp_~1~ is lexicographically less
than the string value of _Exp_~2~ or if both string are equal, and `false` otherwise.

.Examples
```rascal-shell
"abc" <= "abc";
"abc" <= "abcdef";
"abc" <= "defghi";
"abc" <= "a";
```

.Benefits

.Pitfalls

