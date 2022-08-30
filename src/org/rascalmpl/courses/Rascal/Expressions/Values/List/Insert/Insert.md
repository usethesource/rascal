# List Insert

.Synopsis
add an element in front of a list

.Index
+

.Syntax

.Types

//

| `_Exp~1~_`     |  `_Exp~2~_`     | `_Exp~1~_ + _Exp~2~_`       |
| --- | --- | --- |
| `_T~1~_`       |  `list[_T~2~_]` | `list[lub(_T~1~_,_T~2~_)]`  |


.Function

.Details

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

