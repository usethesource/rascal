# IsDefined

.Synopsis
Assign but replace if value is not defined.

.Index
? 

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

First the value of _Exp_~1~ is determined and if that is defined it is assigned to _Assignable_. 
Otherwise, the value of _Exp_~2~ is assigned to _Assignable_. 
Values which can be undefined are values in ((Map))s where the key is not set 
or values of ((Annotation Declaration)) which have not been set yet. 

No other values can be used in an undefined state, so the ? operator does not make sense on undefined or uninitialized variables for example.

.Examples
```rascal-shell
M = ("Andy": 1, "Brian" : 2);
```
Using an `isDefined` assignable can we increment a non-existing entry:
```rascal-shell,continue
M["SomebodyElse"] ? 0 += 1;
M["SomebodyElse"];
```
And if we increment an existing entry the ? has no effect:
```rascal-shell,continue
M["Andy"] ? 0 += 1;
M["Andy"]
```

.Benefits

.Pitfalls

