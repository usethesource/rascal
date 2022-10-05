---
title: ASF+SDF
---

#### Synopsis

Rascal explained for ASF+SDF programmers.

#### Syntax

#### Types

#### Function

#### Description

Rascal is the successor of the specification language ASF+SDF that is part of the [ASF+SDF Meta-Environment](http://www.meta-environment.org).

What are the differences between ASF+SDF and Rascal? What are the commonalities?

*  Rascal has all the high level features of ASF+SDF and some more. 
  Regarding functionality, old ASF+SDF specifications could, in principle, be transformed into Rascal programs using a conversion tool.
  We do not provide such a tool since it turns out to be better to redesign your specification from scratch to profit most
  from all the new features in Rascal.
  
*  Rascal uses its own syntax definition notation that is richer than SDF. It also uses its own parser generation and parsing technology.
  Parser generation is currently somewhat slower than for SDF, the generated parsers are, however, 
  substantially faster than SDF-based parsers.
  SDF specification can be automatically converted to Rascal,
  but here again, manual conversion leads to better results.

*  Like in ASF+SDF, Rascal has modules that introduce a namespace scope for variables and functions, which can be either private or public.     
  Modules can have type parameters as in SDF, which are instantiated by import statements.

*  In Rascal, patterns and variables in concrete syntax may optionally be quoted and escaped, and explicit declaration of the top non-terminal 
  is supported to solve ambiguity.

*  Rascal does not support rewrite rules. Instead pattern-directed function definition and invocation can be used.

*  Unlike ASF+SDF, Rascal has native, efficient, implementations for lists, sets, relations and maps.

*  Unlike ASF+SDF, Rascal can be used without parsing or concrete syntax, supporting for example regular expressions and abstract data types.

*  Rascal has native support for functions, which have a fixed syntax, always return a value and have a body consisting of imperative control flow statements. Adding a function will not trigger the need for regenerating parse tables as is the case in the ASF+SDF implementation. Function types can be polymorphic in their parameters and also allow functions as arguments to implement reusable algorithms.

*  The imperative nature of Rascal allows you to factor out common code and nest conditionals, unlike in ASF+SDF where alternative control flow paths have to be encoded by enumerating equations with non-overlapping conditions.

*  Rascal is an imperative language, which natively supports I/O and other side-effects without the workarounds that are needed in ASF+SDF to achieve this.

*  Rascal has native support for traversals, instead of the add-on it used to be in ASF+SDF. The visit statement is comparable to a traversal function in ASF+SDF, and is as type-safe as the traversal function, but it is more programmeable.

*  Instead of accumulator values of traversal functions in ASF+SDF, Rascal simply supports lexically scoped variables that can be updated using assignments.

*  Rascal natively supports specific expressions and datatypes for relational calculus, all borrowed directly from RScript.

*  When programming using Rascal functions, instead of rules, the control flow of a program becomes easily traceable and debuggable. It is simply like stepping through well structured code.

*  Rascal is based on a Java interpreter, or a Java run-time when compiled. So the code is more portable.

*  Rascal is supported by a modern, Eclipse-based, IDE, unlike the simple IDE of the ASF+SDF Meta-Environment.
  IDE services like keyword highlighting, outlining, and more are available for the Rascal programmer.

#### Examples

#### Benefits

#### Pitfalls

