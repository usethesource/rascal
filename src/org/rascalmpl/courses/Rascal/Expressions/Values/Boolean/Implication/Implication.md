---
title: Boolean Implication
keywords:
  - ==>

---

#### Synopsis

The _implication_ operator on Boolean values.

#### Syntax

`Exp~1~ ==> Exp~2~`

#### Types

//

| `Exp~1~` | `Exp~2~`  | `Exp~1~ ==> Exp~2~`  |
| --- | --- | --- |
| `bool`       | `bool`         | `bool`  |


#### Function

#### Description

The _implication_ operator on Boolean values defined as follows:

| `Exp~1~` | `Exp~2~`  | `Exp~1~ ==> Exp~2~`  |
| --- | --- | --- |
| `true`       | `true`         | `true`  |
| `true`       | `false`         | `false`  |
| `false`       | `true`         | `true`  |
| `false`       | `false`         | `true`  |


Boolean operators have _short circuit_ semantics:  only those operands are evaluated that are needed to compute the result. In the case of the `==>` operator, the result is `true` if `Exp~1~` evaluates to `false`, otherwise `Exp~2~` is evaluated to determine the result.

#### Examples

```rascal-shell
false ==> true;
```


#### Benefits

#### Pitfalls

