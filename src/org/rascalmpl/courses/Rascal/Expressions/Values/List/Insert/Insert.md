---
title: "List Insert"
keywords: "+"
---

.Synopsis
add an element in front of a list

.Syntax

.Types

//

| `Exp~1~`     |  `Exp~2~`     | `Exp~1~ + Exp~2~`       |
| --- | --- | --- |
| `T~1~`       |  `list[T~2~]` | `list[lub(T~1~,T~2~)]`  |


.Function

.Description

The `+` operator can insert an element in front of a list. Note that `+` is one of the ((Operators)) that is overloaded, it is also ((List-Concatenation)) and ((List-Append)) for example.

.Examples

```rascal-shell
1 + []
1 + [2]
1 + [2,3]
```

.Benefits

.Pitfalls

*  If the first operand before the `+` is a list, `+` acts as ((List-Concatenation)) and not as ((List-Insert))

This is concatenation:
```rascal-shell,continue
[1] + [2]
```
To insert a list as an element, use extra brackets:
```rascal-shell,continue
[[1]] + [2]
```

