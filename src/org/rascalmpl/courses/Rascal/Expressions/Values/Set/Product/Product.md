# Set Product

.Synopsis
The product of two set values.

.Index
*

.Syntax
`Exp~1~ * Exp~2~`

.Types


| `_Exp~1~_`    |  `_Exp~2~_`    | `_Exp~1~_ * _Exp~2~_`  |
| --- | --- | --- |
| `set[_T~1~_]` |  `set[_T~2~_]` | `rel[_T~1~_,_T~2~_]`   |


.Function

.Details

.Description
Yields a relation resulting from the product of the values of _Exp_~1~ and _Exp_~2~. It contains a tuple for each combination of values from both arguments.

.Examples
```rascal-shell
{1, 2, 3} * {4, 5, 6};
```
A card deck can be created as follows:
```rascal-shell
{"clubs", "hearts", "diamonds", "spades"} * {1,2,3,4,5,6,7,8,9,10,11,12,13};
```

.Benefits

.Pitfalls

