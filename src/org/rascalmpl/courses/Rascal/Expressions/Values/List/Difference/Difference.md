---
title: List Difference
keywords:
  - -

---

#### Synopsis

The difference between two lists.

#### Syntax

`Exp~1~ - Exp~2~`

#### Types

//

| `Exp~1~`     |  `Exp~2~`     | `Exp~1~ - Exp~2~`        |
| --- | --- | --- |
| `list[T~1~]` |  `list[T~2~]` | `list[lub(T~1~,T~2~)]`   |
| `list[T~1~]` |  `T~2~`       | `list[lub(T~1~,T~2~)]`   |



#### Function

#### Description

If both _Exp_~1~ and _Exp_~2~ have a list as value, the result is the difference of these two list values. 
If _Exp_~2~ does not have a list as value, it is first converted to a list before the difference is computed.
The difference is computed by taking the successive elements of the second list and
removing the first occurrence of that element in the first list. 

#### Examples

```rascal-shell
[1, 2, 3, 4] - [1, 2, 3];
[1, 2, 3, 4] - [3];
[1, 2, 3, 4] - 3;
[1, 2, 3, 4] - [5, 6, 7];
[1, 2, 3, 1, 2, 3] - [1];
[1, 2, 3, 1, 2, 3] - [1, 2];
```

#### Benefits

#### Pitfalls

