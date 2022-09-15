---
title: Tuple NotEqual
keywords:
  - !=

---

#### Synopsis

Not equal operator on tuple values.

#### Syntax

`Exp~1~ != Exp~2~`

#### Types


| `Exp~1~`                      |  `Exp~2~`                      | `Exp~1~ != Exp~2~`  |
| --- | --- | --- |
| `tuple[ T~11~, T~12~, ... ]` |  `tuple[ T~21~, T~22~, ... ]` | `bool`                |


#### Function

#### Description

Yields `true` if both tuples are not identical and `false` otherwise.

#### Examples

```rascal-shell
<1, "abc", true> != <1, "abc">;
<1, "abc", true> != <1, "abc", true>;
```

#### Benefits

#### Pitfalls

