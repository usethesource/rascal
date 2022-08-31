# Set Comprehension

.Synopsis
A set comprehension generates a set value.

.Index
{ | }

.Syntax
`{ Exp~1~, Exp~2~, ... | Gen~1~, Gen~2~, ... }`

.Types


| `_Exp~1~_` | `_Exp~2~_` | ... | `{ _Exp~1~_, _Exp~2~_, ... \| _Gen~1~_, _Gen~2~_, ... }`  |
| --- | --- | --- | --- | --- |
| `_T~1~_`   | `_T~2~_`   | ... | `set[ lub( _T~1~_, _T~2~_, ... ) ]`                    |


.Function

.Details

.Description
A set comprehension consists of a number of contributing expressions _Exp_~1~, _Exp_~2~, ... and a number of
generators _Gen_~1~, _Gen_~2~, _Gen_~3~, ... that are evaluated as described in ((Expressions-Comprehensions)).

.Examples
```rascal-shell
{ N * N | int N <- [0 .. 10]};
{ N * N | int N <- [0 .. 10], N % 3 == 0};
```

.Benefits

.Pitfalls

