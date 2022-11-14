---
title: ModuleNameMismatch
---

#### Synopsis

Module name and file name are different.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

A Rascal module has a name (the name that follows the `module` keyword) and it is stored in a file.
The (enforced) convention is that the name of the module (say `MyModule`) and the name of the file should be the same
(except fo the Rascal extension `.rsc`). Module `MyModule` should be stored in the file `MyModule.rsc`.
This error signals that this convention is violated.

Remedies:

*  Rename the module.
*  Rename the file.

#### Examples

#### Benefits

#### Pitfalls

