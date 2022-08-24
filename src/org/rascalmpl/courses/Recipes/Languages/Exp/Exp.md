# Exp

.Synopsis
The _hello world_ of syntax definition and language definition.
It illustrates how to define concrete and abstract syntax and how to use concrete and abstract patterns to evaluate expressions.

.Syntax

.Types

.Function

.Details

.Description
Our sample language Exp contains the following elements:

*  Integer constants, e.g., `123`.
*  A multiplication operator, e.g., `3*4`.
*  An addition operator, e.g., `3+4`.
*  Multiplication is left-associative and has precedence over addition.
*  Addition is left-associative.
*  Parentheses can be used to override the precedence of the operators.

.Examples

*  `123`
*  `2+3+4`
*  `2+3*4`
*  `(2+3)*4`

.Benefits

.Pitfalls

