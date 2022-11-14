---
title: UndeclaredType
---

#### Synopsis

Use of a type that has not been declared.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

A type has to be declared before it can be used.
This error is generated when an undeclared type is used.

Remedies:

*  Rename the type name.
*  Declare the type.
*  Import a module that declares the type. (Did you import all library modules?)

#### Examples

Using the undeclared type `myint` gives an error:
```rascal-shell,error
myint incr(myint n) = n + 1;
```
The solkution is to first declares `myint` (here as an alias for `int`):
```rascal-shell,error
alias myint = int;
myint incr(myint n) = n + 1;
incr(3);
```

#### Benefits

#### Pitfalls

