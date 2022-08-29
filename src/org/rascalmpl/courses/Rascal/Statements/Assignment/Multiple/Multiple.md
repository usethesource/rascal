# Multiple

.Synopsis
Assign to multiple assignables.

.Index
< > =

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
First the value _Exp_ is determined and should be a tuple of the form `< _V_~1~, _V_~2~, ..., _V_~n~ >`.
Next the assignments `_Assignable_~i~ = _V_~i~` are performed for 1 \<= i \<= n.

.Examples
```rascal-shell
<A, B, C> = <"abc", 2.5, [1,2,3]>;
A;
B;
C;
```

.Benefits

.Pitfalls

