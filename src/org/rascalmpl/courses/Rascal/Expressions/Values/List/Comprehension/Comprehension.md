# List Comprehension

.Synopsis
A list comprehension generates a list value.

.Index
[ | ]

.Syntax
`[ Exp~1~, Exp~2~, ... | Gen~1~, Gen~2~, ... ]`

.Types


| `_Exp~1~_` | `_Exp~2~_` | ... | `[ _Exp~1~_, _Exp~2~_, ... \| _Gen~1~_, _Gen~2~_, ... ]`  |
| --- | --- | --- | --- | --- |
| `_T~1~_`   | `_T~2~_`   | ... | `list[ lub( _T~1~_, _T~2~_, ... ) ]`                   |


.Function

.Details

.Description
A list comprehension consists of a number of contributing expressions _Exp_~1~, _Exp_~2~, ... and a number of
generators _Gen_~1~, _Gen_~2~, _Gen_~3~, ... that are evaluated as described in ((Expressions-Comprehensions)).

.Examples
Computing a list of squares of the numbers from 0 to 10 that are divisible by 3:
```rascal-shell
[n * n | int n <- [0 .. 10], n % 3 == 0];
```
But we can also include the relevant `n` in the resulting list:
```rascal-shell
[n, n * n | int n <- [0 .. 10], n % 3 == 0];
```

.Benefits

.Pitfalls

