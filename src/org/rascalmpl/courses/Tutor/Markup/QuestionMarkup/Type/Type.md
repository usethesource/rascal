---
title: Type
---

#### Synopsis

A question about a Rascal type.

#### Syntax

*  `QType OptName : TypeDescriptor`
*  `QType OptName : QSteps Test Listing`

#### Types

#### Function

#### Description

A type question presents a Rascal expressions and poses a question about its type.

_OptName_ is an optional name of the question (enclosed between `[` and `]`).
If _OptName_ is missing, the question gets a unique number as name.

The desired type is given by a ((TypeDescriptor)).

The first form presents the value generated for the _TypeDescriptor_ and asks about its type.

The second form allows more preparatory steps and also allows adding a listing to the question.

#### Examples

See the effect of the following type questions in the Questions section below.

##  Question 1 

The following question can be paraphrased as: _I give you an arbitrary set of integers, what is its type?_
```rascal
QType: <A:set[int]>
```

##  Question 2 


The following question can be paraphrased as: _I give you an addition of a set of integers, strings or reals and another set of the same type; what is the type of the result?_
```rascal
QType: <A:set[arb[int,str,real]]> + <B:same[A]>
```

#### Benefits

#### Pitfalls

