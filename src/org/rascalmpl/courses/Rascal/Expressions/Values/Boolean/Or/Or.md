---
title: "Boolean Or"
keywords: "||"
---

.Synopsis
The _or_ operator on Boolean values.

.Syntax
`Exp~1~ || Exp~2~`

.Types

//

| `Exp~1~` | `Exp~2~`  | `Exp~1~ \|\| Exp~2~`  |
| --- | --- | --- | --- | --- |
| `bool`    | `bool`     | `bool`                |


.Function

.Description
The _or_ operator on Boolean values defined as follows:

| `Exp~1~` | `Exp~2~`  | `Exp~1~ \|\| Exp~2~`  |
| --- | --- | --- | --- | --- |
| `true`    | `true`     | `true`                |
| `true`    | `false`    | `true`                |
| `false`   | `true`     | `true`                |
| `false`   | `false`    | `false`               |


Boolean operators have _short circuit_ semantics:  only those operands are evaluated that are needed to compute the result. In the case of the `||` operator, the result is `true` if `Exp~1~` evaluates to `true`, otherwise `Exp~2~` is evaluated to determine the result.

Note that `||` will backtrack over its argument expressions until it can find an evaluation that is `true`, unless there is none.

Variable assignments as a result of matching or generator expressions under a `||` are visible outside the context of the operator, but only if the context is conditional, such as an if-then-else or a for loop. Note that it is statically required that both sides of an `||` introduce the same variable names of the same type.

.Examples
```rascal-shell
import IO;
false || true;
(i <- [1,2,3,4] && i % 2 == 0) || false
for ((i <- [1,2,3,4] && i % 2 == 0) || false) 
  println("true for <i>");
```

.Benefits

.Pitfalls

