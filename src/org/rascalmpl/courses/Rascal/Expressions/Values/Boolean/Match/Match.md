---
title: Boolean Match
keywords:
  - ":="

---

#### Synopsis

Match a pattern against an expression.

#### Syntax

`Pat := Exp`

#### Types

//

| `Pat`      | `Exp` |`Pat := Exp` |
| --- | --- | --- |
| ((Patterns)) | `value` | `bool`         |


#### Function

#### Description

See ((Patterns)) for a complete description of all possible patterns on the left-hand
side of the match operator. This expression returns true _while_ the pattern matches.
* if the pattern is unitary, it can match only in one way, and it does then `true` is returned once.
* if the pattern is non-unitary, it could match in multiple ways, then every time the expression is evaluated it will return true and bind its variables in a different way.

#### Examples

```rascal-shell
123 := 456;
[10, *n, 50] := [10, 20, 30, 40, 50];
{10, *int n, 50} := {50, 40, 30, 30, 10};
```
#### Benefits

#### Pitfalls

