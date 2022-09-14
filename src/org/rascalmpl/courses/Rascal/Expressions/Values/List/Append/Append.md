---
title: "List Append"
keywords: "+"
---

.Synopsis
Append an element at the end of a list

.Syntax

.Types

//

| `Exp~1~`     |  `Exp~2~`     | `Exp~1~ + Exp~2~`       |
| --- | --- | --- |
| `list[T~1~]` |  `T~2~`       | `list[lub(T~1~,T~2~)]`  |


.Function

.Details

.Description

The operator `+` appends an element at the end of a list. The `+` is one of those ((Operators)) which are overloaded. It can also mean ((List-Insert)) or ((List-Concatenation)) for example.

.Examples

```rascal-shell
[] + 1;
[1] + 2;
```

.Benefits:

.Pitfalls:

* If both operands of `+` are a list, then it acts as ((List-Concatenation)) 

This is concatenation:
```rascal-shell,continue
[1] + [2]
```

To append a list to a list, use extra brackets:
```rascal-shell,continue
[1] + [[2]]
```

