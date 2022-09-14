---
title: "Map Composition"
keywords: "o"
---

.Synopsis
Composition of two map values.

.Syntax
`Exp~1~ o Exp~2~`

.Types

|`Exp~1~`            | `Exp~2~`           | `Exp~1~ o Exp~2~`  |
| --- | --- | --- |
| `map[T~1~, T~2~]` | `map[T~2~, T~3~]` | `map[T~1~, T~3~]`  |


.Function

.Description
Returns the composition of two maps.

.Examples
```rascal-shell
import Map;
("one" : 1, "two" : 2) o (1 : 10, 2 : 20);
```

.Benefits

.Pitfalls
We use the letter `o` as operator and this may conflict other defined names.
