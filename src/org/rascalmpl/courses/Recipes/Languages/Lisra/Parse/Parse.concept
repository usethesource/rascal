# Parse

.Synopsis
Parsing a Lisp expression.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

Given the Lisp <<Lisra-Syntax>>, we can now apply it to parse textual Lisp expressions
and convert them to the runtime representation `Lval`.

[source,rascal]
----
include::{LibDir}demo/lang/Lisra/Parse.rsc[tags=module]
----

                
<1> First we define the actual `parse` function: it takes a string as argument and returns an `Lval`.
   It proceeds in two steps:
   **  First the text is parsed using `parse(#LispExp, txt)`. The result is parse tree.
   **  Next, the auxiliary function `build` is used to transform the parse tree to an `Lval`.

<2> Function `build` is defined in cases, to handle the various parse tree forms.
    Fortunately, we do not have to spell out the details of the parse tree, but we can use concrete
    patterns instead (see <<Concrete Patterns>>, below).
 
   The right-hand sides deserve some attention. Here the argument `il` is a _parse tree_ (!!) that represents an integer literal.
   We first convert it to a string using string interpolation (`"<il>"`) and then convert it to an integer.

<3> The text of the atom is reconstructed in a similar fashion.

<4> The concrete list elements in `lst` are converted one-by-one using build and are then used to
    create a new `List` value.

.Concrete Patterns
****
We use concrete patterns in these definitions. For instance, the argument pattern 
[source,rascal]
----
(LispExp)`<IntegerLiteral il>`
----
says:

*  Match something of type `LispExp`.
*  It should be an `IntegerLiteral` and bind it to a variable `il`.

More precisely, the text between backquotes should be a string that can be parsed according to the non-terminal
that precedes it (`LispExp` in this example). This is illustrated by the list case where the parentheses appear in the concrete pattern:
[source,rascal]
----
(LispExp)`( <LispExp* lst> )`
----
****

.Examples
[source,rascal-shell]
----
import demo::lang::Lisra::Parse;
import demo::lang::Lisra::Runtime;
parse("1");
parse("x");
parse("(+ 5 7)");
----

.Benefits

.Pitfalls

