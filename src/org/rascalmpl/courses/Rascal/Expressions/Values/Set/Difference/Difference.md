---
title: Set Difference
keywords:
  - -

---

#### Synopsis

The difference between two sets.

#### Syntax

`Exp~1~ - Exp~2~`

#### Types


| `Exp~1~`    |  `Exp~2~`     | `Exp~1~ - Exp~2~`        |
| --- | --- | --- |
| `set[T~1~]` |  `set[T~2~]`  | `set[lub(T~1~,T~2~)]`    |
| `set[T~1~]` |  `T~2~`       | `set[lub(T~1~,T~2~)]`    |



#### Function

#### Description

If both _Exp_~1~ and _Exp_~2~ have a set as value, the result is the difference of these two set values. 
If _Exp_~2~ does not have a set as value, it is first converted to a set before the difference is computed.
The difference is computed by removing all elements of the second set from the first set.

#### Examples

```rascal-shell
{1, 2, 3, 4} - {1, 2, 3};
{1, 2, 3, 4} - {3};
{1, 2, 3, 4} - 3;
{1, 2, 3, 4} - {5, 6, 7};
```

#### Benefits

#### Pitfalls

