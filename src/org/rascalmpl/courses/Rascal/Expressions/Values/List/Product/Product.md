# List Product

.Synopsis
Compute the product of two lists.

.Index
*

.Syntax
`_Exp_~1~ * _Exp_~2~`

.Types


|                |                 |                                |
| --- | --- | --- |
| `_Exp~1~_`     |  `_Exp~2~_`     | `_Exp~1~_ * _Exp~2~_`          |
| `list[_T~1~_]` |  `list[_T~2~_]` | `list[tuple[_T~1~_,_T~2~_]]`   |


.Function

.Details

.Description
Yields a list of tuples resulting from the product of the values of _Exp_~1~ and _Exp_~2~. 
It contains a tuple for each combination of values from both arguments.

.Examples
```rascal-shell
[1, 2, 3] * [4, 5, 6];
```
Here is a concise way to create a deck of cards:
```rascal-shell
["clubs", "hearts", "diamonds", "spades"] * [1 .. 13];
```

.Benefits

.Pitfalls

