# Tuple Subscription

.Synopsis
Retrieve a tuple field by its index position.

.Index
[ ]

.Syntax
`_Exp_~1~ [ _Exp_~2~ ]`

.Types

.Function

.Details

.Description
Subscription retrieves the tuple element with index _Exp_~2~ from the tuple value of _Exp_~1~.

.Examples
Introduce a tuple, assign it to T and retrieve the element with index 0:
```rascal-shell
T = <"mon", 1>;
T[0];
```

.Benefits

.Pitfalls

