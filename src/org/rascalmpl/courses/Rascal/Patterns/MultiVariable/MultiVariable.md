---
title: MultiVariable Pattern
keywords:
  - "*"
  - multivariable
  - multi-variable
  - splice
---

#### Synopsis

Multi-variable (also known as Splice-variable) in abstract pattern.

#### Syntax

```rascal
* Var

* Type Var
```

#### Types

#### Function

#### Description

A multi-variable as nested in a ((Patterns-List)) or ((Patterns-Set)) pattern matches zero
or more elements of said container. Optionally the *element type* of the multi-variable may be specified, in that case only elements that match the specific type are bound to the variable.

#### Examples

```rascal-shell
import IO;
```
Using lists:
```rascal-shell,continue
if([10, *N, 50] := [10, 20, 30, 40, 50])
   println("Match succeeds, N == <N>");
```
the above is equivalent with:
```rascal-shell,continue
if([10, *int N, 50] := [10, 20, 30, 40, 50])
   println("Match succeeds, N == <N>");
```
Using sets:
```rascal-shell,continue
if({10, *S, 50} := {50, 40, 30, 30, 10})
   println("Match succeeds, S == <S>");
```
the above is equivalent with:
```rascal-shell,continue
if({10, *int S, 50} := {50, 40, 30, 30, 10})
   println("Match succeeds, S == <S>");
```

#### Benefits

#### Pitfalls

In older versions of Rascal the type of a multi-variable had to be a list or set type.

