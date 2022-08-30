# String Subscription

.Synopsis
Retrieve a substring via its index.

.Index
[ ]

.Syntax
`_Exp_~1~ [ _Exp_~2~ ]`

.Types


|                |            |                          |
| --- | --- | --- |
| `_Exp~1~_`     | `_Exp~2~_` | `_Exp~1~_ [ _Exp~2~_ ]`  |
| `str`         | `int`     | `str`                  |


.Function

.Details

.Description
String subscription uses the integer value of _Exp_~2~ as index in the string value of _Exp_~1~.
The value of _Exp_~2~ should be greater or equal 0 and less than the number of characters in the string.
If this is not the case, the exception `IndexOutOfBounds` is thrown.

.Examples

Introduce a string, assign it to S and retrieve the element with index 1:
```rascal-shell,continue,error
S = "abc";
S[1];
```
Explore an error case:
```rascal-shell,continue,error
S[5];
```

       
