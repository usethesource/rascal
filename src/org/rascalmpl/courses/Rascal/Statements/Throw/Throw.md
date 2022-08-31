# Throw

.Synopsis

Throw any value as an exception up the call stack.

.Index
throw

.Syntax
`throw Exp`

.Types

.Function

.Details

.Description

A throw statement causes the immediate abortion of the execution of the current function with _Exp_ \'s value as exception value.
The exception can be caught by a ((Try Catch)) statement in the current function or in one of its callers.
If the exception is not caught, the execution of the Rascal program is terminated. The following rules apply:

*  The static type of _Exp_ should be `RuntimeException`, see [RuntimeException]((Libraries:Exception-RuntimeException)).

*  The Rascal program may contain data declarations that extend the type `RuntimeException`.

.Examples
Here is a a variant of string concatenation for ball haters:
```rascal-shell,error
str conc(str x, str y){ if("ball" in {x, y}) throw "I hate balls"; return x + y; }
conc("fairy", "tale");
conc("foot", "ball");
```


.Benefits

.Pitfalls

