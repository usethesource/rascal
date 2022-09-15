---
title: Node Equal
keywords:
  - ==

---

#### Synopsis

Equal operator on node values.

#### Syntax

`Exp~1~ == Exp~2~`

#### Types

| `Exp~1~`  |  `Exp~2~` | `Exp~1~ == Exp~2~`  |
| --- | --- | --- |
| `node`     |  `node`    | `bool`                |


#### Function

#### Description

Yields `true` if the node names of the values of _Exp_~1~ and _Exp_~2~ are equal and
the children of each node are pairwise equal, otherwise `false`.

#### Examples

```rascal-shell
"f"(1, "abc", true) == "f"(1, "abc", true);
"f"(1, "abc", true) == "f"(1, "def", true);
```

#### Benefits

#### Pitfalls

