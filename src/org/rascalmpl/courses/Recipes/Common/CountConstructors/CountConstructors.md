---
title: Count Constructors
---

#### Synopsis

Generic function that can count constructors in a value of any algebraic data type.

#### Syntax

#### Types

#### Function

#### Description

In ((ColoredTrees)), we have seen a function that can count the number of red nodes in a `ColoredTree`.
Is it possible to define a function that can count constructors in a value of any algerbaic data type?

We exploit the subtype relation (see [Static Typing]((RascalConcepts:StaticTyping))) 
between [algebraic data types]((Rascal:Declarations-AlgebraicDataType))s 
and the type [node]((Rascal:Values-Node)) to achieve this.

In real applications this becomes relevant when counting, for instance, statement types in programs.

#### Examples

```rascal
include::{LibDir}demo/common/CountConstructors.rsc[tags=module]
```

                
Two data types are introduced `ColoredTree` and `Hand` together
with an example value of each (`CT`, respectively, `H`).

<1> The function `count` is defined.

<2> Introduces an empty map to maintain the frequencies.
<3> Defines a visit of argument `N`; it traverses the complete value of `N`.
<4> Defines the case that we encounter a node and we update its frequency count.
  First the name of the constructor is retrieved (using [getName]((Library:Node-getName))) and then the
  frequency is updated. The [isDefined]((Rascal:Assignment-IsDefined)) operator is used to provide a default value of 0 when
  the name was not yet in the map.
<5> The map `freq` is returned as result.
<6> Defines a variant `countRelevant`; it gets is an extra argument of relevant constructors
names that is used to filter the map that is returned by `count` using [domainR]((Library:Map-domainR)).

```rascal-shell
import demo::common::CountConstructors;
count(CT);
count(H);
countRelevant(H, {"hearts", "spades"});
```

#### Benefits

#### Pitfalls

