# Node NotEqual

.Synopsis
Not equal operator on node values.

.Index
!=

.Syntax
`Exp~1~ != Exp~2~`

.Types


| `Exp~1~`  |  `Exp~2~` | `Exp~1~ != Exp~2~`  |
| --- | --- | --- |
| `node`     |  `node`    | `bool`                |


.Function

.Details

.Description
Yields `true` if the node names of the values of _Exp_~1~ and _Exp_~2~ are unequal or
any of the children of each node is pairwise unequal, otherwise `true`.

.Examples
```rascal-shell
"f"(1, "abc", true) != "g"(1, "abc", true);
"f"(1, "abc", true) != "f"(1, "abc", true);
```

.Benefits

.Pitfalls

