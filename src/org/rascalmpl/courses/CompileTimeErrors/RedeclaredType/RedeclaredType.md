---
title: RedeclaredType
---

.Synopsis
A type with the same name has been declared before.

.Syntax

.Types

.Function
       
.Usage

.Description
Some declarations introduce new type names. Most important are an 
[algebraic data type]((Rascal:Declarations-AlgebraicDataType)) and [alias]((Rascal:Declarations-Alias)).
This error signals that the same type name is used for incompatible purposes.

Remedy: rename one of the type names.

.Examples
```rascal-shell,error
data D = d(int x);
alias D = str;
```

```rascal-shell,error
alias D = int;
alias D = str;
```

.Benefits

.Pitfalls

