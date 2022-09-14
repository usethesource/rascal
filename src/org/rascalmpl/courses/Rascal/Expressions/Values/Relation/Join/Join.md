---
title: "Relation Join"
keywords: "join"
---

.Synopsis
Join two relation values.

.Syntax
`Exp~1~ join Exp~2~`

.Types


| `Exp~1~`                            |  `Exp~2~`                            | `Exp~1~ join Exp~2~`                                             |
| --- | --- | --- |
| `rel[ T~11~, T~12~, T~13~, ... ]` |  `rel[ T~21~, T~22~, T~23~, ... ]` | `rel[ T~11~, T~12~, T~13~, ..., T~21~, T~22~, T~23~, ... ]`  |


.Function

.Details

.Description
Relation resulting from the natural join of the relation values of the two arguments.
This relation contains tuples that are the result from concatenating the elements from both arguments.

.Examples
```rascal-shell
{<1,2>, <10,20>} join {<2,3>};
{<1,2>} join {3, 4};
{<1,2>, <10,20>} join {<2,3>, <20,30>};
```

.Benefits

.Pitfalls

