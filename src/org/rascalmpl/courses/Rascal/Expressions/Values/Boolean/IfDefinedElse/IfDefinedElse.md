---
title: "Boolean IfDefinedElse"
keywords: "?"
---

.Synopsis
Test whether expression has a defined value, otherwise provide alternative.

.Syntax
`Exp~1~ ? Exp~2~`

.Types

//

| `Exp~1~` | `Exp~2~` | `Exp~1~ ? Exp~2~` |
| --- | --- | --- |
| `T~1~`   | `T~2~`   |  `T~2~ <: T~1~`  |


.Function

.Description
If no exception is generated during the evaluation of _Exp_~1~, the result of `Exp~1~ ? Exp~2~` is the value of _Exp_~1~.
Otherwise, it is the value of _Exp_~2~.

Also see ((Boolean-IsDefined)) and ((Assignment)).

.Examples
This test can, for instance, be used to handle the case that a certain key value is not in a map:
```rascal-shell,error
T = ("a" : 1, "b" : 2);
```
Trying to access the key `"c"` will result in an error:
```rascal-shell,continue,error
T["c"];
```
Using the `?` operator, we can write:
```rascal-shell,continue,error
T["c"] ? 0;
```
This is very useful, if we want to modify the associated value, but are not sure whether it exists:
```rascal-shell,continue,error
T["c"] ? 0 += 1;
```
Another example using a list:
```rascal-shell,continue,error
L = [10, 20, 30];
L[4] ? 0;
```
It is, however, not possible to assign to index positions outside the list.

.Benefits

.Pitfalls

