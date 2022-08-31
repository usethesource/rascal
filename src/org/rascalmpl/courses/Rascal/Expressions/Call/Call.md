# Call

.Synopsis
Function call.

.Index
( )

.Syntax
`Name ( Exp~1~, Exp~2~, ... )`

.Types

//

| `Exp~1~`  | `Exp~2~` | ... | `Name ( Exp~1~, Exp~2~, ... )`  |
| --- | --- | --- | --- |
| `T~1~`    | `T~2~`   | ... | Determined by _Name_, _T~i~_ and function declarations  |


.Function

.Details

.Description
First, the actual parameter expressions _Exp_~i~ are evaluated resulting in values _V_~i~.
Based on _Name_ and the argument types _T_~i~, the identity of the function to be called is determined.

The values _V_~i~ are bound to the formal parameter names of the 
declared functions and the function body is executed.
The value returned by the function is used as value of the function call.


A _constructor call_ has identical syntax to that of a function call, see ((Values-Constructor)),

See ((Function)) for more details about function declarations.

NOTE: Describe keyword parameters.

.Examples

First declare a function `square` with argument _n_ that returns _n^2_:
```rascal-shell,continue
int square(int n) { return n * n; }
```

Next call `square`. This results in the following steps:

* Based on the name `square` and the int argument 12 we identify the function to be called
  (= the function `square` we just defined).
* Compute the value of the actual parameter (= 12).
* Bind the formal parameter `n` to the actual value 12.
* Execute the body of `square`.
* The return value of square is the vale of the call:

```rascal-shell,continue
square(12);
```

.Benefits

.Pitfalls

