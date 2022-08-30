# Relation Composition

.Synopsis
Composition of two relation values.

.Index
o

.Syntax
`_Exp_~1~ o _Exp_~2~`

.Types


|                      |                      |                        |
| --- | --- | --- |
|`_Exp~1~_`            | `_Exp~2~_`           | `_Exp~1~_ o _Exp~2~_`  |
| `rel[_T~1~_, _T~2~_]` | `rel[_T~2~_, _T~3~_]` | `rel[_T~1~_, _T~3~_]`  |


.Function

.Details

.Description
Returns the composition of two binary relations.

.Examples
```rascal-shell
import Relation;
{<1,10>, <2,20>, <3,15>} o {<10,100>, <20,200>};
```

.Benefits

.Pitfalls
We use the letter `o` as operator and this may conflict other defined names.

