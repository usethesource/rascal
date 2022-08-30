# List notin

.Synopsis
Negated membership test on lists.

.Index
notin

.Syntax
`_Exp_~1~ notin _Exp_~2~`

.Types

//

|====
| `_Exp~1~_`           |  `_Exp~2~_`      | `_Exp~1~_ notin _Exp~2~_` 

| `_T~1~_`  <: `_T~2~_` |  `list[_T~2~_]`  | `bool`                  
|====

.Function

.Details

.Description
Yields `true` if the value of _Exp_~1~ does not occur as element in the value of _Exp_~2~ and `false` otherwise. 
The type of _Exp_~1~ should be compatible with the element type of _Exp_~2~.

.Examples
```rascal-shell
4 notin [1, 2, 3];
2 notin [1, 2, 3];
```

.Benefits

.Pitfalls

