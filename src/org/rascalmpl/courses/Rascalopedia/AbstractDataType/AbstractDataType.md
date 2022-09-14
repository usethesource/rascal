---
title: AbstractDataType
---

.Synopsis
A definition of a data type.

.Syntax

.Types

.Function
       
.Usage

.Description
An [Abstract Data Type](http://en.wikipedia.org/wiki/Abstract_data_type) is a mathematical description of a structure
that can be implemented in various ways. For instance, a stack data type can be characterized by `empty` (the empty stack),
two functions `push` and `pop` and axioms that define them. At the implementation level, a stack
can be implemented using a list, array or something else.

In functional languages, and also in Rascal, abstract datatypes (or ADTs for short)
are used to define new data types. Well-known examples are http://en.wikipedia.org/wiki/Stack_(data_structure)[stack] and http://en.wikipedia.org/wiki/Tree_(data_structure)[tree].

See [Algebraic Data Types]((Rascal:Declarations-AlgebraicDataType)) and 
[Constructors]((Rascal:Values-Constructor)) in the [Rascal Language Reference]((Rascal)).

.Examples

## Abstract Data Types in Daily Life

*  A stack of trays in the local cafetaria: ![]((dispenser.jpg))
   [credit](http://www.thermo-box.co.uk/fimi-food-transport-and-handling-products/self-levelling-heated-and-unheated-plate-and-tray-systems.html)

*  A tree:
   ![]((tree.jpg))
   [credit](http://free-extras.com/images/tree-569.htm)

*  Coral:
   ![]((coral.jpg))
   [credit](http://blog.enn.com/?p=476)


## Abstract Data Types in computer science

*  The run-time stack of a programming language interpreter.
*  A search tree.
*  An ontology.


## Abstract Data Types in Rascal

*  A tree data type:
```rascal
data MyTree = leaf(int n) | tree(str name, MyTree left, MyTree right);
```

.Benefits

.Pitfalls

