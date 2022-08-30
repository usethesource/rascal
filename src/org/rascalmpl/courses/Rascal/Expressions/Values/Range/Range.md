# Range

.Synopsis
Numeric range of values.

.Index
[ .. ]

.Syntax

*  `[ _Exp~1~_ .. _Exp~3~_ ]`
*  `[ _Exp~1~_, _Exp~2~_ .. _Exp~3~_ ]`

.Types

.Function

.Details

.Description
Ranges are a shorthand for describing lists of integers from 
_Exp_~1~ up to (exclusive) _Exp_~3~ with increments of 1.
When _Exp_~2~ is present it is taken as the second element of the list
and _Exp_~2~ - _Exp_~1~ is used as increment for the subsequent list elements.

A range with integer expressions is identical to a list ((List-Slice)).
However, a range may also contain numeric expressions that are not integers.

.Examples
```rascal-shell
[1 .. 10];
[1, 3 .. 10];
[0.5, 3.2 .. 10];
[1, -2 .. -10];
```

.Benefits
Ranges are mostly used to loop over ranges of integers.

.Pitfalls
In some cases ranges are empty where one could have expected at least one element:
```rascal-shell
[1, 3 .. -10];
```

