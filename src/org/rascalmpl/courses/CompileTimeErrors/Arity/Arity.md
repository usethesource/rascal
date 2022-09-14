---
title: Arity
---

.Synopsis
The number of arguments of an operator differ from what is required.

.Syntax

.Types

.Function
       
.Usage

.Description
Various operators like [composition]((Rascal:Relation-Composition)), 
[transitive closure]((Rascal:Relation-TransitiveClosure)) and 
[reflexive transitive closure]((Rascal:Relation-ReflexiveTransitiveClosure)) 
expect binary relations or tuples as arguments.

.Examples
This composition is correct:
```rascal-shell
{<1,10>, <2,20>} o {<10,100>, <20, 200>};
```
This is not, since the first argument has arity 3:
```rascal-shell,error
{<1,5,10>, <2,6,20>} o {<10,100>, <20, 200>};
```

These transitive closures are correct:
```rascal-shell
{<1,2>, <2,3>,<4,5>}+
{<1,2>, <2,3>,<4,5>}*
```
But these are incorrect:
```rascal-shell,error
{<1,2,3>, <2,3,4>,<4,5,6>}+
{<1,2,3>, <2,3,4>,<4,5,6>}*
```
.Benefits

.Pitfalls

