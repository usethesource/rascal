# String LessThan

.Synopsis
Less than operator on string values.

.Index
<

.Syntax
`_Exp_~1~ < _Exp_~2~`

.Types


|            |            |                        |
| --- | --- | --- |
| `_Exp~1~_` | `_Exp~2~_` | `_Exp~1~_ < _Exp~2~_`  |
| `str`     |  `str`    | `bool`                |


.Function

.Details

.Description
Yields `true` if the string value of _Exp_~1~ is strictly lexicographically less
than the string value of _Exp_~2~, and `false` otherwise.

.Examples
```rascal-shell
"abc" < "abcdef";
"abc" < "defghi";
"abc" < "a";
```

.Benefits

.Pitfalls

