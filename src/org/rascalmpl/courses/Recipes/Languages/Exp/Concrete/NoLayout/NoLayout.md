# No Layout

.Synopsis
A version of Exp based on concrete syntax.

.Syntax

.Types

.Function

.Details

.Description
We describe howto write a grammar for Exp and how to use it to implement an evaluator.


.Examples
Here is the grammar for Exp:
[source,rascal]
----
include::{LibDir}demo/lang/Exp/Concrete/NoLayout/Syntax.rsc[tags=module]
----

Notes:

<1> Defines a lexical syntax rule for IntegerLiterals; they consist of one or more digits.
<2> Defines the alternatives for Exp. The keyword `start` means that this is a start symbol of the grammar.
<3> Defines alternative #1: an `IntegerLiteral`.
<4> Defines alternative #2: parentheses. The `|` says that this alternative has the same priority as the previous one.
    The keyword `bracket` marks this as an alternative that defines parentheses.
<5> Defines alternative #3: multiplication. The `>` says that the previous rule has a higher priority than the current one.
    The keyword `left` marks this as a left-associative rule.
<6> Defines alternative #4: addition. The `>` says again that the previous rule has a higher priority than the current one.
    The keyword `left` marks this as a left-associative rule.


Now that the grammar is in place we want to use it to build an evaluator. Here is how:
[source,rascal]
----
include::{LibDir}demo/lang/Exp/Concrete/NoLayout/Eval.rsc[tags=module]
----

Notes:

<1> We import [Rascal:ParseTree] because we will need the `parse` function below.
<2> The main function `eval` that evaluates an expression as string to an integer. It proceeds in two steps:
    **  `parse(#Exp, txt)` parses the given `txt` according to non-terminal `Exp` as defined by the grammar.
        The result is a parse tree.
    **  This parse tree is given to another eval function that will reduce the tree to an integer.
<3> Converts an IntegerLiteral to an integer. Let's dissect this further:
    **  The `Exp` preceding the concrete pattern, unambiguously defines the type of the pattern.
        This is good practice to avoid ambiguities.
    **  `<IntegerLiteral l>` matches an IntegerLiteral and binds it (a parse tree fragment) to variable `l`.
    **  In the function body, `toInt("<l>")`, the parse tree fragment is inserted in a string -- effectively unparsing it --
        and that string is converted to an integer.
<4> Handle the multiplication case.
<5> Handle the addition case.
<6> Handles the case of parentheses.


What remains, is to check that `eval` works as expected.
[source,rascal-shell]
----
import demo::lang::Exp::Concrete::NoLayout::Syntax;
import ParseTree;
----
Just checking that `parse` returns a sort of parse tree:
[source,rascal-shell,continue]
----
parse(#Exp, "2+3");
----
You will see such parse trees only once, unless you are a researcher in parsing ;-)
Here is a demonstration of `eval`:
[source,rascal-shell,continue]
----
import demo::lang::Exp::Concrete::NoLayout::Eval;
eval("2+3");
eval("2+3*4");
eval("(2+3)*4");
----


.Benefits

.Pitfalls

