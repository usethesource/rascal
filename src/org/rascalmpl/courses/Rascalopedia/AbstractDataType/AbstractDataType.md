---
title: Abstract Data Type
---

#### Synopsis

A definition of a data type where the interface, in terms of initial creation and further operations on the data, is clearly separated from its implementation details "behind" its interface.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

An [Abstract Data Type](http://en.wikipedia.org/wiki/Abstract_data_type) is a mathematical description of a structure that can be implemented in various ways.  Well-known examples are [stack](http://en.wikipedia.org/wiki/Stack_(data_structure)) and [tree](http://en.wikipedia.org/wiki/Tree_(data_structure)). For instance, a stack data type can be characterized by `empty` (the empty stack), two functions `push` and `pop` and axioms that define them. At the implementation level, a stack can be implemented using a list, array or something else. In object-oriented programming abstract data types are a way of [Information Hiding](https://en.wikipedia.org/wiki/Information_hiding), namely to hide the implementation of a data-structure from its programming interface.

Abstract data types are **not to be confused** with [Algebraic Data Types](https://en.wikipedia.org/wiki/Algebraic_data_type), although you could use the latter to create the former. Both are abbreviated with "ADT". 
Abstract Data Types are not as important for Rascal programmers as Algebraic Data Types are,
but the builtin lists, sets, relations and maps can be seen as Abstract Data Types. This is because
you as a programmer do not have to understand how they are implemented under-the-hood.

#### Examples


*  The run-time stack of a programming language interpreter.
*  A search tree.
*  An ontology.

#### Benefits

#### Pitfalls

