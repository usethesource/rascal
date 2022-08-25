# Interactive Box Height

.Synopsis
Control the height of a box with user input.

.Syntax

.Types

.Function

.Details

.Description

*  A text entry field to enter numbers.
*  A box, whose height is controlled by the numer entered in the text field.

.Examples
Here is a solution:
[source,rascal]
----
include::{LibDir}demo/vis/Higher.rsc[tags=module]
----

                
The auxiliary function `intInput` checks that a strings consists solely of digits.
Function `higher` can be understood as follows:

*  A local variable `H` is used to maintain the current height.
*  It returns a vertical concatenation of two elements: a [Rascal:textfield] and a [Rascal:box].
*  The textfield has three arguments:
**  A string that is the initial value of the text field.
**  A call back function `void(str s){H = toInt(s);}` that is called when text entry is complete:
     argument `s` is the text entered and the effect is to convert that text to a number and assign it to `H`.
**  Function `intInput` that checks that only numbers are entered.
**  The box has
**  a fixed width
**  it is not vertically resizable.
**  It has a vertical size that that depends on an anonymous function `vsize(num(){return H;})` that returns the value of `H`. 
**  Its color is red.


Rendering this figure:
[source,rascal-figure,width=,height=,file=h1.png]
----
import demo::vis::Higher;
render(higher());
----
gives


![]((h1.png))


Unfortunately we cannot show the interaction here, so run this example from the `demo` directory and watch how the height of the box changes when you enter a new number in the text field.

.Benefits

.Pitfalls

