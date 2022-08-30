# Map in

.Synopsis
Membership test on the keys of a map.

.Index
in

.Syntax
`_Exp_~1~ in _Exp_~2~`

.Types

| `_Exp~1~_`           |  `_Exp~2~_`         | `_Exp~1~_ in _Exp~2~_`  |
| --- | --- | --- |
| `_T~1~_`  <: `_TK_`  |  `map[_TK_, _TV_]` | `bool`                |


.Function

.Details

.Description
Yields `true` if the value of _Exp_~1~ occurs as key in the map value of _Exp_~2~ and `false` otherwise. 
The type of _Exp_~1~ should be compatible with the key type _TK_ of _Exp_~2~.

.Examples
```rascal-shell
"pear" in ("apple": 1, "pear": 2);
"pineapple" in ("apple": 1, "pear": 2);
```

.Benefits

.Pitfalls

