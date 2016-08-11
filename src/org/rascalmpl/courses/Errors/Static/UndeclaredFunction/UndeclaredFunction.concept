# UndeclaredFunction

.Synopsis
A function is called that has not been declared.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
All functions, constructors and variables have to be declared before they can be used.
This error is generated when this rule is violated.

Remedies for functions:

*  Declare the function.
*  Declare the function as constructor of an (existing or new) [Rascal:AlgebraicDataType].
*  Import a module that declares the function (Did you import all necessary library modules?)

Remedies for variables:

*  Declare the variable.

.Examples
Calling the undeclared function `triple` gives an error:
[source,rascal-shell,error]
----
triple(5)
----
We can remedy this by declaring the function:
[source,rascal-shell,continue,error]
----
int triple(int n) = 3 * n;
triple(5)
----

Calling the library function `size` gives an error if the proper library (in this case: `List`) is not imported
[source,rascal-shell,error]
----
size([20, 1, 77]);
----
The solution is:
[source,rascal-shell]
----
import List;
size([20, 1, 77]);
----
Another solution is to import the complete Rascal library at once:
[source,rascal-shell]
----
import Prelude;
size([20, 1, 77]);
----

Using an undeclared variable gives an error:
[source,rascal-shell,error]
----
n + 1;
----
A variable is introduced by just assigning to it (with or without its expected type):
[source,rascal-shell]
----
n = 3;
n + 1;
----
Or equivalenty (with an expected type):
[source,rascal-shell]
----
int n = 3;
n + 1;
----

.Benefits

.Pitfalls

