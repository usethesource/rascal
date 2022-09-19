---
title: Abstract Syntax Tree
---

#### Synopsis

Representation of the abstract syntactic structure of a program.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

A ((Rascalopedia-ParseTree)) is a detailed and very precise represention of the concrete syntactic structure of a program.
It may even be so detailed that it contains every space, comment and parenthesis in the original source text.
In many cases a less detailed representation is sufficient and an abstract syntax tree (or AST for short) is used.

#### Examples

For the input sentence

![]((ParseTree-example-text.png))


the parse tree (left) and abstract syntax tree (right) may look as follows:



![]((parse-ast.png))


Note that the parse tree on the left did not preserve the spaces in the original text but there
are parse tree formats (including the one used by Rascal) that preserve all textual information.

#### Benefits

* parse trees are nice for grammar debugging, because they are an executing trace of the parsing algorithm.
* parse trees are good for source-to-source transformation, because they do not a priori remove important details such as whitespace indentation and source code comments. A transformation based on parse trees is sometimes called "high fidelity" because of this.

#### Pitfalls

* parse trees are sometimes hard to read in abstract form, due to their inherent low-level of detail and complexity. When confronted with a large one, it's best to first try and minimize the input sentence before trying to debug a parse tree.
