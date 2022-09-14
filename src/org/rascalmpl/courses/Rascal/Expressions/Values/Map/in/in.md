---
title: "Map in"
keywords: "in"
---

.Synopsis
Membership test on the keys of a map.

.Syntax
`Exp~1~ in Exp~2~`

.Types

| `Exp~1~`           |  `Exp~2~`         | `Exp~1~ in Exp~2~`  |
| --- | --- | --- |
| `T~1~`  <: `TK`  |  `map[TK, TV]` | `bool`                |


.Function

.Description
Yields `true` if the value of Exp~1~ occurs as key in the map value of Exp~2~ and `false` otherwise. 
The type of _Exp_~1~ should be compatible with the key type _TK_ of _Exp_~2~.

.Examples
```rascal-shell
"pear" in ("apple": 1, "pear": 2);
"pineapple" in ("apple": 1, "pear": 2);
```

.Benefits

.Pitfalls

