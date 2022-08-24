# Quine

.Synopsis
A self-reproducing program.

.Syntax

.Types

.Function

.Details

.Description
A http://en.wikipedia.org/wiki/Quine_(computing)[quine]
is a computer program that takes no input and produces a copy of its own source code.
A quine is also called a _self-replicating_ or _self-reproducing_ program.

At the http://www.nyx.net/~gthompso/quine.htm[Quine Page] you can find hundreds of
quines in many different programming languages.

Learning about quines, is about learning how to quote and escape symbols in strings.


.Examples

[source,rascal]
----
include::{LibDir}demo/basic/Quine.rsc[]
----

                
<1> A remarkable point in the code: the string variable `program` has as value
   the text of the module `Quine` upto here. 
<2> The definition of `program` ends here.
    This string has a mesmerizing amount of escapes to which we will come back in a moment.

<3> The function `quine` prints the string `program` twice, here as is and this produces the program upto icon:callout[1] above.
<4> Here the value of `program` is printed as a string (surrounded with string quotes) in order to reproduce the string value 
    of `program` followed by a semi-colon (`;`).
  
Now here is the catch: we have to be very carefull in handling special characters like quote (`"`) and backslash (`\`) in strings.

Let's do a simple experiment:
[source,rascal-shell]
----
import IO;
str greeting = "\"Good Morning, Dr. Watson\", said Holmes";
println("\"" + greeting + "\"");
----
As you see the quotes inside the string are not escaped and the result is not a legal string.
So what can we do? We escape all dangerous characters in the string before printing it using the [Rascal:escape] function.
It takes a string and a map of characters to be escaped and returns a result in which all escaping has been carried out.
Be aware that in the map, also escaping is needed!
We want to say: escape `"` and replace it by `\"`, but since both `"` and `\` have to be escaped themselves
we have to say: escape `"\""` and replace it by `"\\\""`. The effect is as follows:
[source,rascal-shell,continue]
----
import String;
println("\"" + escape(greeting, ("\"": "\\\"")) + "\"");
----
And indeed, the two quotes are now properly escaped.

This is exactly what happens at image:{images}/4.png[] in the definition of `quine`:
[source,rascal]
----
println("\"" + escape(program, ("\"" : "\\\"", "\\" : "\\\\")) + "\";");
----
We escape `program` and replace `"` by `\"`, and `\` by `\\`.
The mesmerizing amount of `\` characters can be explained due to escaping `"` and `\`.

Now let's put `quine` to the test.
[source,rascal-shell]
----
import demo::basic::Quine;
quine();
----
If you follow this output line-by-line you will see that it
is identical to the original source code of module `Quine`.

.Benefits

.Pitfalls

