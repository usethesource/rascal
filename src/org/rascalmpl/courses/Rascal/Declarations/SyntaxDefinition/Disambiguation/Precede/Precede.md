# Precede Declaration

.Synopsis
A conditional ((Symbol)), constraining the characters that can immediately precede a symbol in the input source text.

.Index
<< !<<

.Syntax

*  `_constraint_ << _Symbol_` 
*  `_constraint_ !<< _Symbol_`


where a _constraint_ is any character class, a literal or a keyword non-terminal ((SyntaxDefinition-Symbol)).

.Types

.Function

.Details

.Description

Using `!<<`, the parser will not accept the _Symbol_ if it is immediately preceded by the _terminal_ in the input string. If the start of the symbol coincides with start of the inout, the constraint will always succeed and the symbol is accepted.


.Examples

.Benefits

.Pitfalls

