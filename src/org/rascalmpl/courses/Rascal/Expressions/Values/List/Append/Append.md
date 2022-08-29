# List Append

.Synopsis
Append an element at the end of a list

.Index
+

.Syntax

.Types

//

|====
| `_Exp~1~_`     |  `_Exp~2~_`     | `_Exp~1~_ + _Exp~2~_`      

| `list[_T~1~_]` |  `_T~2~_`       | `list[lub(_T~1~_,_T~2~_)]` 
|====

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

* If both operands of `+` are a list, then it acts as ((List Concatenation)) 

This is concatenation:
```rascal-shell,continue
[1] + [2]
```

To append a list to a list, use extra brackets:
```rascal-shell,continue
[1] + [[2]]
```

