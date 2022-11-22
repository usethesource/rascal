---
title: Exp
---

#### Synopsis

A demo of _hello world_ simplicity for syntax definition and language definition.
It illustrates how to define concrete and abstract syntax and how to use concrete and abstract patterns to evaluate expressions.

#### Syntax

#### Types

#### Function

#### Description

Our sample language Exp contains the following elements:

*  Integer constants, e.g., `123`.
*  A multiplication operator, e.g., `3*4`.
*  An addition operator, e.g., `3+4`.
*  Multiplication is left-associative and has precedence over addition.
*  Addition is left-associative.
*  Parentheses can be used to override the precedence of the operators.

We demonstrate _three different designs_ for implementing this language. Each alternative
has its benefits and pitfalls. You can compare them to learn about the trade-offs.

* ((Exp-Abstract))
* ((Exp-Concrete))
* ((Exp-Combined))

#### Examples

These are example sentences that are part of the ((Exp)) language:
*  `123`
*  `2+3+4`
*  `2+3*4`
*  `(2+3)*4`

Because you can guess what they should mean (their semantics), you should
be able to follow the interpreter code that implements this semantics.

#### Benefits

* Exp is nice and simple and can show you core ideas of language design and implementation

#### Pitfalls

* Some more concepts are needed when a language grows beyond simple arithmetic expressions.
