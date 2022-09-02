# Squares

.Synopsis
Print a list of squares

.Syntax

.Types

.Function

.Details

.Description

.Examples
How can we print a list of squares? Here is a solution:
```rascal
include::{LibDir}demo/basic/Squares.rsc[tags=module]
```
           
<1> The [IO]((Library:Prelude-IO)) module is imported since we want to print things using `println`.

<2> [String]((Rascal:Values-String)) interpolation is used several times.
    Here the value of `N` is inserted in the header message.

<3> The values of `I` and `I * I` are inserted in each line that is printed.

<4> Define an alternative implementation `squareTemplate` that is based on string templates 
    and returns a string value instead of printing the results itself.

Here is how `square` can be used:
```rascal-shell
import demo::basic::Squares;
squares(9);
```

`squaresTemplate` gives a similar result but now as a string:
```rascal-shell,continue
squaresTemplate(9);
```

To get a truly identical result we have to import the [IO]((Library:Prelude-IO)) module 
and print the value of `squaresTemplate`:
```rascal-shell,continue
import IO;
println(squaresTemplate(9));
```


.Benefits

.Pitfalls

