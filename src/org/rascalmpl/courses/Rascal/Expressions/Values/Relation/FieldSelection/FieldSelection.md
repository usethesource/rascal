# Relation FieldSelection

.Synopsis
Select a field (column) from a relation value.

.Index
.

.Syntax
`_Exp_ . _Name_`

.Types


|====
|`_Exp_`                                | `_Exp_ . _Name_` 

| `rel[_T~1~_ _L~1~_, _T~2~_ _L~2~_, ... ]` | `set[_T~i~_]`     
|====

.Function

.Details

.Description
_Exp_ should evaluate to a relation that has an _i_-th field label _L_~i~ that is identical to _Name_.
Return a set with all values of that field.
_Name_ stands for itself and is not evaluated.

.Examples
```rascal-shell
rel[str street, int nm] R = {<"abc", 1>, <"abc", 2>, <"def", 4>, <"def", 5>};
R.street;
```

.Benefits

.Pitfalls

