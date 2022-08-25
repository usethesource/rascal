# Immutable Values

.Synopsis
Immutable values.

.Syntax

.Types

.Function

.Details

.Description
Values are the basic building blocks of a language and the type of values determines how they may be used.

Rascal is a _value-oriented language_. This means that values are immutable and are always freshly constructed from existing parts.
For instance, replacing an element in a list does not modify the original list but produces a new list that only differs
from the original one in the modified position.

The language also provides variables. A value can be associated with a variable as the result of an explicit assignment statement: during the lifetime of a variable different (immutable) values may be assignment to it. Other ways to associate a value with a variable is by way of function calls (binding of formal parameters to actual values) and as the result of a successful pattern match.

The approach that values are immutable and that variables can be associated with different immutable values during their lifetime avoids
sharing and aliasing problems that exist in many languages. 

.Examples

First we, create a list value and assign it to two variables `L` and `M`.
[source,rascal-shell]
----
L = [1, 2, 3];
M = L;
----
Next we assign a new value to the first element of the list. The effect is that a new list value `[10, 2, 3]` is constructed.
[source,rascal-shell,continue]
----
L[0] = 10;
----
L is now associated with this new value:
[source,rascal-shell,continue]
----
L;
----
But `M` is still associated with the original, unmodified, value.
[source,rascal-shell,continue]
----
M;
----
In pointer-based languages and in object-oriented languages the change to the original value of `L` would also be visible
via `M`.


String values are, like all other values, also immutable. Let's experiment with the [replaceAll]((Libraries:String-replaceAll)) function:
[source,rascal-shell]
----
import String;
replaceAll("abracadabra", "a", "A");
----
Now assign to variables `S` and `T` the string `"abracadabra"` and let's see what happens:
[source,rascal-shell,continue]
----
S = "abracadabra";
T = S;
S = replaceAll("abracadabra", "a", "A");
S;
T;
----

To summarize: all values are immutable and variables can during their lifetime be associated with different immutable values.


.Benefits

*  Immutable values contribute to referential transparence.

.Pitfalls

*  Immutable values maybe less efficient than mutable ones.

