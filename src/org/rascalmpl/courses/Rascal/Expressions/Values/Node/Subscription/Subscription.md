# Node Subscription

.Synopsis
Retrieve an argument of a node via its index.

.Index
[ ]

.Syntax
`Exp~1~ [ Exp~2~ ]`

.Types


| `_Exp~1~_`     | `_Exp~2~_` | `_Exp~1~_ [ _Exp~2~_ ]`  |
| --- | --- | --- |
| `node`        | `int`     | `value`                 |


.Function

.Details

.Description
Node subscription uses the integer value of _Exp_~2~ as index in the argument list of the node value of _Exp_~1~.
The value of _Exp_~2~ should be greater or equal 0 and less than the number of arguments of the node.
If this is not the case, the exception `IndexOutOfBounds` is thrown.

.Examples
```rascal-shell,error
```
Introduce a node, assign it to F and retrieve the various arguments:
```rascal-shell,continue,error
F = "f"(1, "abc", false);
F[0]
F[1]
F[2]
```
Explore an error case:
```rascal-shell,continue,error
F[3];
```

       
