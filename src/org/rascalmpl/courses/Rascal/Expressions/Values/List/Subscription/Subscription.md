# List Subscription

.Synopsis
Retrieve a list element via its index.

.Index
[ ]

.Syntax
`_Exp_~1~ [ _Exp_~2~ ]`

.Types

//

|====
| `_Exp~1~_`     | `_Exp~2~_` | `_Exp~1~_ [ _Exp~2~_ ]`

| `list[_T~1~_]` | `int`     | `_T~1~_`             
|====

.Function

.Details

.Description
List subscription uses the integer value of _Exp_~2~ as index in the list value of _Exp_~1~.
The value of _Exp_~2~ should be greater or equal 0 and less than the number of elements in the list.
If this is not the case, the exception `IndexOutOfBounds` is thrown.

.Examples

Introduce a list, assign it to L and retrieve the element with index 1:
[source,rascal-shell,continue,error]
----
L = [10, 20, 30];
L[1];
----
Explore an error case:
[source,rascal-shell,continue,error]
----
L[5];
----

.Benefits

.Pitfalls

