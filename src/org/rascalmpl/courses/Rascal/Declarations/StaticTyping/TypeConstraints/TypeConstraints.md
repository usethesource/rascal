---
title: "Type Constraints"
keywords: "<:,&"
---

#### Synopsis

Type constraints restrict the acceptable type for parameters.

#### Syntax

`& Name <: Type`

#### Types

#### Function

#### Description

Constraints can be imposed on the actual types to which a type parameter may be bound. 
This is expressed by a subtype constraint which expresses that 
actual types bound to _Name_ should be a subtype of _Type_. 

#### Examples

Here is the definition of the absolute value function `abs` from the ((Number)) library:
```rascal
public &T <: num abs(&T <: num N)
{
	return N >= 0 ? N : -N;
}
```
The argument `N` is constrained to be at most of type `num`.
```rascal-shell
import util::Math;
abs(-3);
abs(-3.5);
```

Here is an example from the ((Values-Node)) library:
```rascal
&T <: node setAnnotations(&T <: node x, map[str, value] annotations);
```
(we don't give the body of this function since it has been implemented in Java).
`setAnnotations` takes a value of any type that is at most `node` and adds annotations to it.
This makes it possible to set annotations on any ((Algebraic Data Type)).

```rascal-shell
import Node;
nd = "f"(10, "abc");
```
First we apply `setAnnotations` to a node value:
```rascal-shell,continue
setAnnotations(nd, ("color" : "red", "size" : "large"));
```
Next, we introduce a new data type `Suite`:
```rascal-shell,continue
data Suite = hearts() | diamonds() | clubs() | spades();
st = diamonds();
```
And apply `setAnnotations` to a value of type `Suite`:
```rascal-shell,continue
setAnnotations(st, ("player" : "Hossein", "gain" : "120"));
```

#### Benefits

#### Pitfalls

