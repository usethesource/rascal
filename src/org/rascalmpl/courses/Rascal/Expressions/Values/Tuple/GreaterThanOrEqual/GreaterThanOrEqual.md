---
title: "Tuple GreaterThanOrEqual"
keywords: ">="
---

.Synopsis
Greater than or equal operator on tuple values.

.Syntax
`Exp~1~ >= Exp~2~`

.Types


| `Exp~1~`                      |  `Exp~2~`                      | `Exp~1~ >= Exp~2~`  |
| --- | --- | --- |
| `tuple[ T~11~, T~12~, ... ]` |  `tuple[ T~21~, T~22~, ... ]` | `bool`                |


.Function

.Details

.Description
Yields `true` if 

*  both tuples are equal, or
*  the left-most element in the tuple value of _Exp~1~_ that differs from the corresponding element in the tuple 
value of _Exp_~2~ is greater than that element in _Exp_~2~.


Otherwise the result if `false`.

.Examples
```rascal-shell
<1, "abc", true> > <1, "abc", true>;
<1, "def", true> > <1, "abc", true>;
```

.Benefits

.Pitfalls

