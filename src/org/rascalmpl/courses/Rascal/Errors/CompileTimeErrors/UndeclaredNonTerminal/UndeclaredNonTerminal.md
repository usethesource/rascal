---
title: UndeclaredNonTerminal
---

#### Synopsis

A syntax rule uses an undeclared non-terminal.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

All non-terminals that occur in a [syntax definition]((Rascal:Declarations-SyntaxDefinition))
should be declared in some rule.
This error is generated when this is not the case.

Remedy: declare the offending non-terminal.

#### Examples

Here is an example where the non-terminal `Y` is not declared:
```rascal-shell,error
import ParseTree;
syntax X = "a" Y;
parse(#X, "ab");
```

#### Benefits

#### Pitfalls

The source location of the non-terminal is not yet available, so the error message mentions the module in which this occurs.

