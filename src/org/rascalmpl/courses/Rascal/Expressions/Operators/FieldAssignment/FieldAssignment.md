# Field Assignment

.Synopsis
Assignment to a field of a tuple or datatype.

.Index
[ = ]

.Syntax
`Exp~1~ [ Name = Exp~2~ ]`

.Types

.Function

.Details

.Description
_Exp_~1~ should evaluate to a tuple or datatype with a field _Name_; assign the value of _Exp_~2~ to that field

Field assignment applies to all values that have named components like tuples and relations with named elements, data types, and locations. 
Field assignment returns a new value in which the named component has been replaced by a new value.
_Name_ stands for itself and is not evaluated.

.Examples
```rascal-shell
tuple[int key, str val] T = <1, "abc">;
T[val = "def"];
 T;
```

Observe that field assignment creates a new value with an updated field. The old value remains unchanged as can be seen from the unchanged value of T in the above example.

.Benefits

.Pitfalls

