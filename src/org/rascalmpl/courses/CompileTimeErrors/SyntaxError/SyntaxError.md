---
title: SyntaxError
---

#### Synopsis

Text in a module or entered via the command line violates the Rascal syntax.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

This error is generated when a text is expected to be Rascal but does not comply with the Rascal syntax.

Remedy: Correct your text and check for the following:

*  All parentheses are balanced: `(...)`, `[...]`, `{...}`, `<...>`, `/.../`.
*  All multi-line comments are balanced: `/*...*/`.
*  All semi-colons are present.
*  All tests in `if`, `for`, `while` statement are surrounded by `(...)`.


When you are completely desparate and cannot find the syntax error, the following _binary search_ strategy may help:

*  Surround all text in the module (except the module header) with `/*` and `*/`. Now your module should parse. If not the syntax error is in the module header.
*  Move the upper comment symbol `/*` to the middle of the file. Now there are two possibilities:
   ** The module parses. The top part is correct and the syntax error is in the bottom part. 
      Move the `/*` marker to the middle of the bottom part and repeat.
   ** The module does not parse. The syntax error is in the top part.
      Move the `/*` marker to the middle of the top part and repeat.

#### Examples

#### Benefits

#### Pitfalls

