---
title: UnitializedVariable
---

#### Synopsis

Use of a variable that has not been initialized.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

A [variable]((Rascal:Declarations-Variable)) has to be initialized before it can be used.
This error is generated when an uninitialzed variable is used.

Remedy: assign a value to the variable before its use:

#### Examples

Using the uninitialized variable `x` gives an error:

```rascal-shell,error
x
```

This can be avoided by first initializing `x` by an ((Statements-Assignment)):
```rascal-shell
x = 3;
x + 5;
```

Or it can be avoided by declaring `x` using a ((Declarations-Variable)):
```rascal-shell
int x = 3;
x + 5;
```


#### Benefits

#### Pitfalls

