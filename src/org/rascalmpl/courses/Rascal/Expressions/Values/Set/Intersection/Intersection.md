# Set Intersection

.Synopsis
Intersection of two sets.

.Index
&

.Syntax
`_Exp_~1~ & _Exp_~2~`

.Types


|               |                  |                            |
| --- | --- | --- |
| `_Exp~1~_`    |  `_Exp~2~_`      | `_Exp~1~_ & _Exp~2~_`      |
| `set[_T~1~_]` |  `set[_T~2~_]`   | `set[lub(_T~1~_,_T~2~_)]`  |


.Function

.Details

.Description
Returns the intersection of the two set values of _Exp_~1~ and _Exp_~2~.
The intersection consists of the common elements of both sets.

.Examples
```rascal-shell
{1, 2, 3, 4, 5} & {4, 5, 6};
```

.Benefits

.Pitfalls

