# UnexpectedKeywordArgumentType

.Synopsis
The actual value of a keyword argument is not compatible with its declared type.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
Functions may be declared to have keyword parameters. Each keyword parameters has a type, a name and a default value.
This error is generated when the declared type and the actual type of the keyword parameter are not compatible.

Remedies:

*  Change the type of the keyword parameter in the call.
*  Change the type of the keyword parameter in the function declaration.

.Examples

Declare `incr` function with keyword parameter `delta` of type `int`:
```rascal-shell,error
int incr(int x, int delta = 1) = n + delta;
```
Erroneous use of `delta` with a string value:
```rascal-shell,continue,error
incr(3, delta="more");
```

.Benefits

.Pitfalls

