---
title: "Relation CartesianProduct"
keywords: "*"
---

#### Synopsis

Cartesian product of two relation values.

#### Syntax

`Exp~1~ * Exp~2~`

#### Types


|`Exp~1~`      | `Exp~2~`     | `Exp~1~ * Exp~2~`   |
| --- | --- | --- |
| `set[T~1~]`  | `set[T~2~]`  | `rel[T~1~, T~2~]`   |


#### Function

#### Description

Returns a binary relation that is the [Cartesian product](http://en.wikipedia.org/wiki/Cartesian_product) of two sets.

#### Examples

```rascal-shell
{1, 2, 3} * {9};
{1, 2, 3} * {10, 11};
```

#### Benefits

#### Pitfalls

