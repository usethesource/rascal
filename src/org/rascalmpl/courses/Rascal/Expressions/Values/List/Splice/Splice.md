# List Splice

.Synopsis
Splice the elements of a list in an enclosing list.

.Index
*

.Syntax

.Types

//


|        |           |             |                                          |
| --- | --- | --- | --- |
|`_Exp_` | `_Exp~1~_`|  `_Exp~n~_` | `[_Exp~1~_, ..., _Exp_, ..., _Exp~n~_]`  |
|`_T_`   | `_T~1~_`  |  `_T~n~_`   | `list[lub(_T~1~_, ..., _T_, ...,_T~n~_)]`     |


.Function
       
.Usage

.Details

.Description
The operator `*` splices the elements of a list in an enclosing list.

.Examples

Consider the following list in which the list `[10, 20, 30]` occurs as list element. It has as type `list[value]`:
```rascal-shell
[1, 2, [10, 20, 30], 3, 4];
```
The effect of splicing the same list element in the enclosing list gives a flat list of type `list[int]`:
```rascal-shell,continue
[1, 2, *[10, 20, 30], 3, 4];
```
The same example can be written as:
```rascal-shell,continue
L = [10, 20, 30];
[1, 2, *L, 3, 4];
```

.Benefits
in which nested lists are handled.

.Pitfalls

