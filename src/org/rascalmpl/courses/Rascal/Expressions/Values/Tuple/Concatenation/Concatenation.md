# Tuple Concatenation

.Synopsis
Concatenate two tuple values.

.Index
+

.Syntax
`_Exp_~1~ + _Exp_~2~`

.Types


| `_Exp~1~_`                      |  `_Exp_2_`                      | `_Exp~1~_ > _Exp_2_`                                 |
| --- | --- | --- |
| `tuple[ _T~11~_, _T~12~_, ... ]` |  `tuple[ _T~21~_, _T~22~_, ... ]` | `tuple[ _T~11~_, _T~12~_, ..., _T~21~_, _T~22~_, ... ]` |


.Function

.Details

.Description
Returns a tuple consisting of the concatenation of the tuple elements of _Exp_~1~ and _Exp_~2~.

.Examples
```rascal-shell
<"abc", 1, 2.5> + <true, "def">;
```

.Benefits

.Pitfalls

