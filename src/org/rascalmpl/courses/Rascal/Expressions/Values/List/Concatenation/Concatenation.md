# List Concatenation

.Synopsis
Concatenate two lists.

.Index
+

.Syntax
`_Exp_~1~ + _Exp_~2~`

.Types

//

|                |                 |                             |
| --- | --- | --- |
| `_Exp~1~_`     |  `_Exp~2~_`     | `_Exp~1~_ + _Exp~2~_`       |
| `list[_T~1~_]` |  `list[_T~2~_]` | `list[lub(_T~1~_,_T~2~_)]`  |



.Function

.Details

.Description

The `+` operator concatenates the elements of the two lists in order of appearance. 

Note that the same operator is overloaded for ((List-Insert)) and ((List-Append)).

.Examples

```rascal-shell
[1, 2, 3] + [4, 5, 6];
[] + [1]
[1] + []
[1] + [2] + [3]
```

And overloaded usage for insert and append looks like:
```rascal-shell
1 + []
[] + 1
```

.Benefits

.Pitfalls

