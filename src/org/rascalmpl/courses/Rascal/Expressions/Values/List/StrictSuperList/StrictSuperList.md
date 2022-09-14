---
title: "List StrictSuperList"
keywords: ">"
---

#### Synopsis

The strict super list operator on lists.

#### Syntax

`Exp~1~ > Exp~2~`

#### Types

//

| `Exp~1~`     |  `Exp~2~`     | `Exp~1~ > Exp~2~`  |
| --- | --- | --- |
| `list[T~1~]` |  `list[T~2~]` | `bool`               |


#### Function

#### Description

Yields `true` if the value of Exp~2~ is a strict sublist of the value of Exp~1~,  and `false` otherwise.

#### Examples

```rascal-shell
[1, 2, 3, 4] > [1, 2, 3];
[1, 2, 3, 4] > [1, 2, 3, 4];
[1, 2, 3, 4] > [1, 2, 3];
[1, 2, 3, 4, 5] > [1, 3, 5]
```

#### Benefits

#### Pitfalls

