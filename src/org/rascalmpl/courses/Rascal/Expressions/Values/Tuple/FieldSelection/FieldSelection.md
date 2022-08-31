#  Tuple FieldSelection

.Synopsis
Select a field from a tuple by its field name.

.Index
.

.Syntax
`Exp . Name`

.Types


| `Exp`                                 | `Name` | `Exp . Name` |
| --- | --- | --- |
|`tuple[ _T~1~_ _L~1~_, _T~2~_ _L~2~_, ... ]` |  `_L~i~_` | `_T~i~_`         |


.Function

.Details

.Description
Field selection applies to tuples with named elements.
_Exp_ should evaluate to a tuple with field _Name_ and returns the value of that field.
_Name_ stands for itself and is not evaluated.

.Examples
```rascal-shell
tuple[int key, str val] T = <1, "abc">;
T.val;
```

.Benefits

.Pitfalls

