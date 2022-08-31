# List Product

.Synopsis
Compute the product of two lists.

.Index
*

.Syntax
`Exp~1~ * Exp~2~`

.Types


| `Exp~1~`     |  `Exp~2~`     | `Exp~1~ * Exp~2~`          |
| --- | --- | --- |
| `list[T~1~]` |  `list[T~2~]` | `list[tuple[T~1~,T~2~]]`   |


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

