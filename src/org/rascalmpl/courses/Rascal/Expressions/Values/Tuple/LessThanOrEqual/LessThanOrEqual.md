# Tuple LessThanOrEqual

.Synopsis
Less than or equal operator on tuple values.

.Index
<=

.Syntax
`_Exp_~1~ <= _Exp_~2~`

.Types


|====
| `_Exp~1~_`                      |  `_Exp~2~_`                      | `_Exp~1~_ <= _Exp~2~_` 

| `tuple[ _T~11~_, _T~12~_, ... ]` |  `tuple[ _T~21~_, _T~22~_, ... ]` | `bool`               
|====

.Function

.Details

.Description
Yields `true` if 

*  both tuples are equal, or
*  the left-most element in the tuple value of _Exp~1~_ that differs from the corresponding element in the tuple 
value of _Exp_~2~ is less than that element in _Exp_~2~.


Otherwise the result if `false`.

.Examples
```rascal-shell
<1, "abc", true> <= <1, "abc", true>;
<1, "abc", true> <= <1, "def", true>;
```

.Benefits

.Pitfalls

