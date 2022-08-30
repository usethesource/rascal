# Set Difference

.Synopsis
The difference between two sets.

.Index
-

.Syntax
`_Exp_~1~ - _Exp_~2~`

.Types


| `_Exp~1~_`    |  `_Exp~2~_`     | `_Exp~1~_ - _Exp~2~_`        |
| --- | --- | --- |
| `set[_T~1~_]` |  `set[_T~2~_]`  | `set[lub(_T~1~_,_T~2~_)]`    |
| `set[_T~1~_]` |  `_T~2~_`       | `set[lub(_T~1~_,_T~2~_)]`    |



.Function

.Details

.Description
If both _Exp_~1~ and _Exp_~2~ have a set as value, the result is the difference of these two set values. 
If _Exp_~2~ does not have a set as value, it is first converted to a set before the difference is computed.
The difference is computed by removing all elements of the second set from the first set.

.Examples
```rascal-shell
{1, 2, 3, 4} - {1, 2, 3};
{1, 2, 3, 4} - {3};
{1, 2, 3, 4} - 3;
{1, 2, 3, 4} - {5, 6, 7};
```

.Benefits

.Pitfalls

