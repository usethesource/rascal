---
title: "Relation Composition"
keywords: "o"
---

.Synopsis
Composition of two relation values.

.Syntax
`Exp~1~ o Exp~2~`

.Types


|`Exp~1~`            | `Exp~2~`           | `Exp~1~ o Exp~2~`  |
| --- | --- | --- |
| `rel[T~1~, T~2~]` | `rel[T~2~, T~3~]` | `rel[T~1~, T~3~]`  |


.Function

.Details

.Description
Returns the composition of two binary relations.

.Examples
```rascal-shell
import Relation;
{<1,10>, <2,20>, <3,15>} o {<10,100>, <20,200>};
```

.Benefits

.Pitfalls
We use the letter `o` as operator and this may conflict other defined names.

