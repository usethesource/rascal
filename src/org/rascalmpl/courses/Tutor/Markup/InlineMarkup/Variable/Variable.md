---
title: Variable
---

#### Synopsis

Markup for a variable.

#### Syntax

We use subscript syntax for variable indices. 

* `\VarName`
* `\VarName\~a~`

Watch out, only characters in this class are supported `[aeh-pr-vx0-9()+-]`.

#### Types

#### Function

#### Description

Variables in text and code are represented by ((Italic)) markup. 
They may be followed by one or more subscripts (enclosed by tildes).

#### Examples

* `\Var` gives _Var_.

* `\Var\~1~` gives _Var_~1~.

#### Benefits

#### Pitfalls

* This feature is broken currently. The italics do not work in code fragments and subscripts are broken as well.
* We only support a limited set of characters currently: `[aeh-pr-vx0-9()+-]`