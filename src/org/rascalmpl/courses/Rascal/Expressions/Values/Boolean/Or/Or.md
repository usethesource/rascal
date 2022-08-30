# Boolean Or

.Synopsis
The _or_ operator on Boolean values.

.Index
||

.Syntax
`_Exp_~1~ || _Exp_~2~`

.Types

//

| `_Exp~1~_` | `_Exp~2~_`  | `_Exp~1~_ \|\| _Exp~2~_`  |
| --- | --- | --- | --- | --- |
| `bool`    | `bool`     | `bool`                |


.Function

.Details

.Description
The _or_ operator on Boolean values defined as follows:

| `_Exp~1~_` | `_Exp~2~_`  | `_Exp~1~_ \|\| _Exp~2~_`  |
| --- | --- | --- | --- | --- |
| `true`    | `true`     | `true`                |
| `true`    | `false`    | `true`                |
| `false`   | `true`     | `true`                |
| `false`   | `false`    | `false`               |


Boolean operators have _short circuit_ semantics:  only those operands are evaluated that are needed to compute the result. In the case of the `||` operator, the result is `true` if `_Exp_~1~` evaluates to `true`, otherwise `_Exp_~2~` is evaluated to determine the result.

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

