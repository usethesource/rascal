---
title: UnsupportedOperation
---

#### Synopsis

Attempt to apply a operation to a value for which the operation is not defined.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

This error is generated when an unsupported operation is applied to (a combination of) values.
There can be many causes for this as illustrated below.

Remedies: 

*  Change the operation to another operations that is supported on the given values.
*  Change the values so that the operation can be applied to them.
*  Rewrite the expression compleye to get the effect you intended.

#### Examples

```rascal-shell,error
L = [1,2,3];
```
Division is not supported on lists:
```rascal-shell,continue,error
[1, 2, 3] / 4;
```
Combined multiplication and assignment is not supported either:
```rascal-shell,continue,error
L *= 3;
```
Taking the time from a date-only value is not supported:
```rascal-shell,continue,error
$2010-07-15$.justTime;
```
Calling an integer as a function is not supported:
```rascal-shell,continue,error
17(3, "abc");
```

#### Benefits

#### Pitfalls

