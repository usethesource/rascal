---
title: Action
---

#### Synopsis

Actions are functions that are called when parse trees are constructed (right after parsing).

#### Syntax

#### Types

#### Function

#### Description

A so-called ((Action)) is a normal rascal ((Function Declarations)) that overloads a ((SyntaxDefinition)). 
A ((Syntax Definition)), very similar to ((Algebraic Data Type)) definitions, defines a constructor for a parse tree node. 
This constructor is the default function, and when it is overloaded by a non-default function this overloaded function will be tried first. 
You can overload any labeled ((Syntax Definition)) using the name of an alternative.

For example:
```rascal
syntax A = a: B  C;

public A a(B b, C c) {
  return f(b, c);
}
```
In this example ((Action)) function the a is replaced by whatever A the `f` function returns. 

((Action))s are executed every time a parse tree is constructed:

*  Right after parsing.
*  On the way back from a visit statement.
*  When a ((Concrete Syntax)) expression is executed.
*  When ((Parse Trees)) are constructed "manually".


They can be used as a ((Disambiguation)) method, using the `filter` statement, as in:
```rascal
syntax E = id: Id i;
set[Id] types = {};

public E id(Id i) {
  if (i in types) 
    filter; // remove this parse tree and all its parents up to the first amb node
  else 
    fail; // just build the parse tree "E = id: Id i", by defaulting to the constructor
} 
```
#### Examples

#### Benefits

#### Pitfalls

