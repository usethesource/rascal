---
title: "List Subscription"
keywords: "[,]"
---

.Synopsis
Retrieve a list element via its index.

.Syntax
`Exp~1~ [ Exp~2~ ]`

.Types

//

| `Exp~1~`     | `Exp~2~` | `Exp~1~ [ Exp~2~ ]` |
| --- | --- | --- |
| `list[T~1~]` | `int`     | `T~1~`              |


.Function

.Details

.Description
List subscription uses the integer value of _Exp_~2~ as index in the list value of _Exp_~1~.
The value of _Exp_~2~ should be greater or equal 0 and less than the number of elements in the list.
If this is not the case, the exception `IndexOutOfBounds` is thrown.

.Examples

Introduce a list, assign it to L and retrieve the element with index 1:
```rascal-shell,continue,error
L = [10, 20, 30];
L[1];
```
Explore an error case:
```rascal-shell,continue,error
L[5];
```

.Benefits

.Pitfalls

