---
title: IllegalQualifiedDeclaration
---

#### Synopsis

Qualified names cannot be declared.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

When the same name is declared in different modules, a qualified name can be used to refer to a specific version of that name.
This error is generated when a qualified name is used in a declaration.

Remedy: remove the qualification.

#### Examples

Using a qualified name gives an error:
```rascal-shell,error
data M::D = d();
```
Without the qualification, this is correct:
```rascal-shell,continue,error
data D = d();
```

#### Benefits

#### Pitfalls

