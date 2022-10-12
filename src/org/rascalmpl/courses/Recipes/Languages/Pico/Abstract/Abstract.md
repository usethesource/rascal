---
title: Abstract
---

#### Synopsis

Abstract syntax for Pico.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

#### Examples

Here is the complete abstract syntax for Pico:

```rascal-include
demo::lang::Pico::Abstract
```

An abstract syntax specification is not necessary for implementing a programming language 
in Rascal. However, sometimes it comes in handy and for this reason we demonstrate an
AST for Pico here. One reason could be that you are using an external parser for a language,
another is that your DSL is only an intermediate format in a longer pipeline.

Notes:

* <1> The types that may occur in a Pico program are either `natural` or `string`.
* <2> Introduce `PicoId` as an alias for Rascal's `str` datatype.
* <3> Define the various data types that constitute an AST for Pico. Observe that the constructor names match the names used in the concrete syntax, e.g., `strCon`, `add`, `ifElseStat`.
* <4> Define an annotation with name `location` and of type `loc` (source code location) for all AST types. This will be used when transforming a parse tree into an abstract syntax tree ((ParseTree-implode)).
* <5> Introduce `Occurrence` as a generic way of describing the location of various items in the AST.

#### Benefits

#### Pitfalls

