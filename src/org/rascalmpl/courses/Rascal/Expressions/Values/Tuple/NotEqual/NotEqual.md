# Tuple NotEqual

.Synopsis
Not equal operator on tuple values.

.Index
!=

.Syntax
`_Exp_~1~ != _Exp_~2~`

.Types


|====
| `_Exp~1~_`                      |  `_Exp~2~_`                      | `_Exp~1~_ != _Exp~2~_` 

| `tuple[ _T~11~_, _T~12~_, ... ]` |  `tuple[ _T~21~_, _T~22~_, ... ]` | `bool`               
|====

.Function

.Details

.Description
Yields `true` if both tuples are not identical and `false` otherwise.

.Examples
```rascal-shell
<1, "abc", true> != <1, "abc">;
<1, "abc", true> != <1, "abc", true>;
```

.Benefits

.Pitfalls

