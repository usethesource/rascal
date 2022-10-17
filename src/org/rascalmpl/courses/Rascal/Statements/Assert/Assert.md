---
title: Assert
keywords:
  - assert

---

#### Synopsis

An executable assertion.

#### Syntax

*  `assert Exp~1~`
*  `assert Exp~1~ : Exp~2~`

#### Types


| `Exp~1~` | `Exp~2~`  |
| --- | --- |
| `bool`    | `str`      |


#### Function

#### Description

An assert statement may occur everywhere where a declaration is allowed. It has two forms:

An assert statement consists of a Boolean expression _Exp_~1~ and an optional string expression _Exp_~2~
that serves as a identifying message for this assertion. 

When _Exp_~1~ evaluates to `false`, an `AssertionFailed` exception is thrown.

#### Examples

```rascal-shell,error
assert 1==2 : "is never true";
int div(int x, int y) {
  assert y != 0 : "y must be non-zero";
  return x / y;
}
div(4,0);
```

#### Benefits

#### Pitfalls

