# Follow Declaration

.Synopsis
A conditional <<Symbol>>, constraining the characters that can immediately follow a symbol in the input source text.

.Index
>> !>>

.Syntax

*  `_Symbol_ >> _constraint_` 
*  `_Symbol_ !>> _constraint_`


where a _constraint_ is any character class, a literal or a keyword non-terminal <<SyntaxDefinition-Symbol>>.

.Types

.Function

.Details

.Description

Using `!>>`, the parser will not accept the _Symbol_ if it is immediately followed by the _terminal_ in the input string. If the end of the symbol coincides with end-of-file, the constraint will always succeed and the symbol is accepted.

.Examples

.Benefits

.Pitfalls

