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
|`tuple[ T~1~ L~1~, T~2~ L~2~, ... ]` |  `L~i~` | `T~i~`         |


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

