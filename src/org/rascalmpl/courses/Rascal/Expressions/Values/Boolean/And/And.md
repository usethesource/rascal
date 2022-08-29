# Boolean And

.Synopsis
Boolean _and_ operator.

.Index
&&

.Syntax
`_Exp_~1~ && _Exp_~2~`

.Types

//

|====
| `_Exp~1~_` | `_Exp~2~_`  | `_Exp~1~_ && _Exp~2~_` 

| `bool`       | `bool`         | `bool` 
|====

.Function

.Details

.Description
The _and_ operator on Boolean values defined as follows:

|====
| `_Exp~1~_` | `_Exp~2~_`  | `_Exp~1~_ && _Exp~2~_` 

| `true`       | `true`         | `true` 
| `true`       | `false`         | `false` 
| `false`       | `true`         | `false` 
| `false`       | `false`         | `false` 
|====

Boolean operators have _short circuit_ semantics:  only those operands are evaluated that are needed to compute the result. In the case of the `&&` operator, the result is `false` if `_Exp_~1~` evaluates to `false`, otherwise `_Exp_~2~` is evaluated to determine the result.

Note that `&&` backtracks over its argument expressions until it can find an evaluation that yields `true` unless there is none. This may happen if the left or right expression is a non-deterministic pattern match or a value generator.

Variable assignments as a result of matching or generator expressions under a `&&` are visible outside the context of the operator, but only if the context is conditional, such as an if-then-else or a for loop. Note that if one of the argument expressions evaluates to false, then no binding is done either.

.Examples
```rascal-shell
true && false;
i <- [1,2,3] && (i % 2 == 0)
import IO;
if (i <- [1,2,3] && (i % 2 == 0))
  println("<i> % 2 == 0");
for (i <- [1,2,3,4] && (i % 2 == 0)) 
  println("<i> % 2 == 0");
```

.Benefits

*  The backtracking `&&` allows one to express searching for a computational solution in concise manner.

.Pitfalls

*  Side effects to global variables or IO in the context of a backtracking `&&` can lead to more effects than you bargained for.

```rascal-shell
import IO;
int i = 0;
bool incr() { i += 1; return true; }
for (int j <- [1,2,3] && incr() && (i % 2 == 0)) 
  println("once true for <j>");
i;
```
