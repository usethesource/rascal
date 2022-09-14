---
title: "Node GreaterThan"
keywords: ">"
---

.Synopsis
Greater than operator on node values.

.Syntax
`Exp~1~ > Exp~2~`

.Types


| `Exp~1~` |  `Exp~2~` | `Exp~1~ > Exp~2~`  |
| --- | --- | --- |
| `node`    |  `node`    | `bool`               |


.Function

.Details

.Description
Comparison on nodes is defined by a lexicographic ordering. Node `N = F(N~1~, ..., N~n~)` is greater than node 
`N = G(M~1~, ..., M~m~)` when:
*  _N_ is not equal to _M_, and
*  _F_ is lexicographically greater than _G_, or _F_ is equal to _G_ and `n > m`.

.Examples
```rascal-shell
"g"(3) > "f"(10, "abc");
"f"(10, "abc") > "f"(10);
```

.Benefits

.Pitfalls

