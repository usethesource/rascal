# ListRelation FieldSelection

.Synopsis
Select a field (column) from a list relation value.

.Index
.

.Syntax
`_Exp_ . _Name_`

.Types


|                                        |                   |
| --- | --- |
|`_Exp_`                                 | `_Exp_ . _Name_`  |
|
| `lrel[_T~1~_ _L~1~_, _T~2~_ _L~2~_, ... ]` | `list[_T~i~_]`     |


.Function

.Details

.Description
_Exp_ should evaluate to a list relation that has an _i_-th field label _L_~i~ that is identical to _Name_.
Return a list with all values of that field.
_Name_ stands for itself and is not evaluated.

.Examples
```rascal-shell
lrel[str street, int nm] R = [<"abc", 1>, <"abc", 2>, <"def", 4>, <"def", 5>];
R.street;
```

.Benefits

.Pitfalls

