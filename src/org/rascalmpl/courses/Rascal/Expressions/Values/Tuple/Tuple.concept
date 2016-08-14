# Tuple

.Synopsis
Tuple values.

.Index
< >

.Syntax
`< _Exp_~1~, _Exp_~2~, ... >`

.Types


|====
| `_Exp~1~_`  | `_Exp~2~_`  |  ...  | `< _Exp~1~_, _Exp~2~_, ... >` 

| `_T~1~_`    |  _T~2~_     | ...   | `tuple[_T~1~_, _T~2~_, ... ]` 
|====

.Function

.Details

.Description
A tuple is a sequence of elements with the following properties:

*  Each element in a tuple (may) have a different type.

*  Each element of a tuple may have a label that can be used to select that element of the tuple.

*  Each tuple is fixed-width, i.e., has the same number of elements.


Tuples are represented by the type `tuple[_T_~1~ _L_~1~, _T_~2~ _L_~2~, ...]`, 
where _T_~1~, _T_~2~, ... are arbitrary types and _L_~1~, _L_~2~, ... are optional labels. 

The following operators are provided for tuples:
loctoc::[1]

.Examples
[source,rascal-shell]
----
tuple[str first, str last, int age] P = <"Jo","Jones",35>;
P.first;
P.first = "Bo";
----

.Benefits

.Pitfalls

