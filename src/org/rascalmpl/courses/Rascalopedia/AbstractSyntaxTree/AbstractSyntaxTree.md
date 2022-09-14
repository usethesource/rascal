---
title: Abstract Syntax Tree
---

.Synopsis
Representation of the abstract syntactic structure of a program.

.Syntax

.Types

.Function
       
.Usage

.Description
A ((ParseTree-ParseTree)) is a detailed and very precise represention of the concrete syntactic structure of a program.
It may even be so detailed that it contains every space, comment and parenthesis in the original source text.
In many cases a less detailed representation is sufficient and an abstract syntax tree (or AST for short) is used.

.Examples

For the input sentence

![]((example-text.png))


the parse tree (left) and abstract syntax tree (right) may look as follows:



![]((parse-ast.png))


Note that the parse tree on the left did not preserve the spaces in the original text but there
are parse tree formats (including the one used by Rascal) that preserve all textual information.

.Benefits

.Pitfalls

