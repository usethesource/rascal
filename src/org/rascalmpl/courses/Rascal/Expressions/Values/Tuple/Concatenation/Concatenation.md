---
title: "Tuple Concatenation"
keywords: "+"
---

#### Synopsis

Concatenate two tuple values.

#### Syntax

`Exp~1~ + Exp~2~`

#### Types


| `Exp~1~`                      |  `Exp2_`                      | `Exp~1~ > Exp2_`                                 |
| --- | --- | --- |
| `tuple[ T~11~, T~12~, ... ]` |  `tuple[ T~21~, T~22~, ... ]` | `tuple[ T~11~, T~12~, ..., T~21~, T~22~, ... ]` |


#### Function

#### Description

Returns a tuple consisting of the concatenation of the tuple elements of _Exp_~1~ and _Exp_~2~.

#### Examples

```rascal-shell
<"abc", 1, 2.5> + <true, "def">;
```

#### Benefits

#### Pitfalls

