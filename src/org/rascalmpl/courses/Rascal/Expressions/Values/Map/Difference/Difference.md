---
title: "Map Difference"
keywords: "-"
---

.Synopsis
The difference between two maps.

.Syntax
`Exp~1~ - Exp~2~`

.Types


| `Exp~1~`             |  `Exp~2~`             | `Exp~1~ - Exp~2~`                             |
| --- | --- | --- |
| `map[TK~1~, TV~1~]` |  `map[TK~2~, TV~2~]` | `map[lub(TK~1~,TK~2~),lub(TK~1~,TK~2~)]`   |


.Function

.Description
The result is the difference of the two map values of _Exp_~1~ and _Exp_~2~,
i.e. a map with all pairs in _Exp_~1~ that do have a key that does not occur in _Exp_~2~.

.Examples
```rascal-shell
("apple": 1, "pear": 2) - ("banana": 3, "apple": 4);
```

.Benefits

.Pitfalls

