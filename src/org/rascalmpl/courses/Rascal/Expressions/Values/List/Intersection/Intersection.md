# List Intersection

.Synopsis
Intersection of two lists.

.Index
&

.Syntax
`Exp~1~ & Exp~2~`

.Types

//

| `_Exp~1~_`     |  `_Exp~2~_`      | `_Exp~1~_ & _Exp~2~_`       |
| --- | --- | --- |
| `list[_T~1~_]` |  `list[_T~2~_]`  | `list[lub(_T~1~_,_T~2~_)]`  |


.Function

.Details

.Description
Returns the intersection of the two list values of  _Exp_~1~ and _Exp_~2~, i.e.,
the list value of _Exp_~1~ with all elements removed that do not occur in the list value of _Exp_~2~.

.Examples
```rascal-shell
[1, 2, 3, 4, 5] & [4, 5, 6];
```

.Benefits

.Pitfalls

