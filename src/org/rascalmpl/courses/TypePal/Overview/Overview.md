# Overview

.Synopsis
An overview of TypePal

.Description

TypePal is a declarative framework that operates on a model of the program to be type checked that consisting of 

* _facts_ that some source code fragment has a known type, e.g., an integer literal is of type integer,
  or that its type is equal to the type of another source code fragment, e.g., the type of the expression in parentheses `( e )` is equal to the type of `e`.
* _calculators_ that compute the type of a source code fragment and create new facts;  
  this computation may also need the types of one or more other source code fragments,
  e.g., computing the type of the addition operator is based on the types of its operands.
* _requirements_ that are imposed on a source code fragment by the type system being used; 
  a requirement may need the types of one or more other source code fragments, 
  e.g., if the expression on the right-hand side of an assignment has type integer,
  then the type of the variable on the left-hand side should be compatible with the type integer.

  
We call this model the _TModel_ (for "Type Model") of the program: facts describe elementary observations and equalities between types,
calculators compute new type facts, and requirements impose restrictions on the types of program fragments. 
A requirement or calculator may detect a type violation and will then generate an error message. 
When a requirement is satisfied or a calculator computes a new type, 
this leads to the creation of new facts that may trigger the computation of other requirements and calculators.

Technically, TypePal uses _scope graphs_ for expressing definition and use of names (including their role, scope, name space, and visibility),
and _constraints_ to describe facts, requirements and calculators. 
These constraints are such that they can express either type checking or type inference, or a mixture thereof.
Under the hood, these constraints are solved in an efficient, data-driven, fashion.

TypePal is highly parameterized and can be adapted to specific type checking needs.
