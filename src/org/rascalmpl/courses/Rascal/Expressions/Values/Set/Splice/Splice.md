# Set Splice

.Synopsis
Splice the elements of a set in an enclosing set.

.Index
*

.Syntax

.Types


|        |           |             |                                          |
| --- | --- | --- | --- |
|`_Exp_` | `_Exp~1~_`|  `_Exp~n~_` | `{_Exp~1~_, ..., _Exp_, ..., _Exp~n~_}`  |
|`_T_`   | `_T~1~_`  |  `_T~n~_`   | `set[lub(_T~1~_, ..., _T_, ...,_T~n~_)]`     |


.Function
       
.Usage

.Details

.Description
The operator `*` splices the elements of a set in an enclosing set.

.Examples

Consider the following set in which the set `{10, 20, 30}` occurs as set element. It has as type `set[value]`:
```rascal-shell,continue
{1, 2, {10, 20, 30}, 3, 4};
```
The effect of splicing the same set element in the enclosing set gives a flat list of type `set[int]`:
```rascal-shell,continue
{1, 2, *{10, 20, 30}, 3, 4};
```
The same example can be written as:
```rascal-shell,continue
S = {10, 20, 30};
{1, 2, *S, 3, 4};
```

.Benefits

.Pitfalls

