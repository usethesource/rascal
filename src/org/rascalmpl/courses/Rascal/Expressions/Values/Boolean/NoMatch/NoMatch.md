---
title: Boolean NoMatch
keywords:
  - "!:="

---

#### Synopsis

Negated [Boolean Match] operator.

#### Syntax

`Pat !:= Exp`

#### Types

//

| `Pat`     | `Exp` |`Pat !:= Exp` |
| --- | --- | --- |
| [Patterns]  | `value` | `bool`           |


#### Function

#### Description

See ((Patterns)) for a complete description of the possible patterns on the left-hand side. The expression will return true if the pattern can not match in _any_ way. It will use back-tracking in case the pattern is non-unitary to find a possible match, but it will not backtrack again after it has not found a match unless a larger context mandates it.

#### Examples

```rascal-shell
123 !:= 456;
[10, *n, 50] !:= [10, 20, 30, 40];
{10, *n, 50} !:= {40, 30, 30, 10};
```

#### Benefits

#### Pitfalls

