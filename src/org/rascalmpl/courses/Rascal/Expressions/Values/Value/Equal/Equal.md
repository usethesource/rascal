---
title: Value Equal
keywords:
  - "=="

---

#### Synopsis

Equal operator on values.

#### Syntax

`Exp~1~ == Exp~2~`

#### Types


| `Exp~1~`   | `Exp~2~` | `Exp~1~ == Exp~2~`  |
| --- | --- | --- |
| `value`     |  `value`  | `bool`                |


#### Function

#### Description

Yields `true` if both arguments are identical and `false` otherwise.

#### Examples

```rascal-shell
```
Introduce two variables `X`, `Y` and `Z` and force them to be of type `value`:
```rascal-shell,continue
value X = "abc";
value Y = "abc";
value Z = 3.14;
```
Now compare `X` and `Y` for equality:
```rascal-shell,continue
X == Y;
```
and `X` and `Z`:
```rascal-shell,continue
X == Z;
```

#### Benefits

#### Pitfalls

