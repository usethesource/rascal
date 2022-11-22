---
title: List Concatenation
keywords:
  - "+"

---

#### Synopsis

Concatenate two lists.

#### Syntax

`Exp~1~ + Exp~2~`

#### Types

//

| `Exp~1~`     |  `Exp~2~`     | `Exp~1~ + Exp~2~`       |
| --- | --- | --- |
| `list[T~1~]` |  `list[T~2~]` | `list[lub(T~1~,T~2~)]`  |



#### Function

#### Description

The `+` operator concatenates the elements of the two lists in order of appearance. 

Note that the same operator is overloaded for ((List-Insert)) and ((List-Append)).

#### Examples

```rascal-shell
[1, 2, 3] + [4, 5, 6];
[] + [1]
[1] + []
[1] + [2] + [3]
```

And overloaded usage for insert and append looks like:
```rascal-shell
1 + []
[] + 1
```

#### Benefits

#### Pitfalls

