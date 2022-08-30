# Node GreaterThan

.Synopsis
Greater than operator on node values.

.Index
>

.Syntax
`_Exp_~1~ > _Exp_~2~`

.Types


|            |             |                        |
| --- | --- | --- |
| `_Exp~1~_` |  `_Exp~2~_` | `_Exp~1~_ > _Exp~2~_`  |
| `node`    |  `node`    | `bool`               |


.Function

.Details

.Description
Comparison on nodes is defined by a lexicographic ordering. Node `_N_ = _F_(_N_~1~, ..., _N_~n~)` is greater than node 
`_N_ = _G_(_M_~1~, ..., _M_~m~)` when:
*  _N_ is not equal to _M_, and
*  _F_ is lexicographically greater than _G_, or _F_ is equal to _G_ and `_n_ > _m_`.

.Examples
```rascal-shell
"g"(3) > "f"(10, "abc");
"f"(10, "abc") > "f"(10);
```

.Benefits

.Pitfalls

