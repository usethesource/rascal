---
title: Syntax
---

#### Synopsis

The textual syntax of Lisp.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

The first step in defining Lisp's textual format, we define a grammar for it:

```rascal
include::{LibDir}demo/lang/Lisra/Syntax.rsc[tags=module]
```

                
`Whitespace` defines the characters that can be ignored between tokens.

`IntegerLiteral` defines integer constants. In a first approximation `[0-9]` is enough.
However, to ensure that the longest possible sequence of digits is used, the `!>> [0-9]` part
ensures that an integer cannot be followed by another digit.

`AtomExp` defines a Lisp symbol that may contain a wide range of characters (except layout and digits).

The main syntactic concept is a `LispExp` that may be an `IntegerLiteral`, `AtomExp` or a list
of `LispExp`s surrouned by parentheses.

#### Examples

This grammar is demonstrated in ((Lisra-Parse)).

#### Benefits

#### Pitfalls

