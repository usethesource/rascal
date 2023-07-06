@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}

@synopsis{

Library functions for relations.

}
@description{

The following library functions are defined for relations:
(((TOC)))
}
module Relation


@synopsis{

Return the set of all elements in any tuple in a relation.

}
@examples{

```rascal-shell
import Relation;
carrier({<1,10>, <2,20>});
carrier({<1,10,100,1000>, <2,20,200,2000>});
```
}
public set[&T]  carrier (rel[&T,&T] R)
{
  return R<0> + R<1>;
}

public set[&T]  carrier (rel[&T, &T, &T] R)
{
  return (R<0> + R<1>) + R<2>;
}

public set[&T]  carrier (rel[&T,&T,&T,&T] R)
{
  return  ((R<0> + R<1>) + R<2>) + R<3>;
}

public set[&T]  carrier (rel[&T,&T,&T,&T,&T] R)
{
  return  (((R<0> + R<1>) + R<2>) + R<3>) + R<4>;
}


@synopsis{

A relation restricted to certain element values in tuples.

}
@description{

Returns relation `R` restricted to tuples with elements in set `S`.

}
@examples{

```rascal-shell
import Relation;
carrierR({<1,10>, <2,20>, <3,30>}, {10, 1, 20});
```
}
public rel[&T,&T] carrierR (rel[&T,&T] R, set[&T] S)
{
  return { <V0, V1> | <&T V0, &T V1> <- R, V0 in S, V1 in S };
}

public rel[&T,&T,&T] carrierR (rel[&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2> | <&T V0, &T V1, &T V2> <- R, V0 in S, V1 in S, V2 in S };
}

public rel[&T,&T,&T,&T] carrierR (rel[&T,&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2, V3> | <&T V0, &T V1, &T V2, &T V3> <- R, V0 in S, V1 in S, V2 in S, V3 in S };
}

public rel[&T,&T,&T,&T,&T] carrierR (rel[&T,&T,&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2, V3, V4> | <&T V0, &T V1, &T V2, &T V3, &T V4> <- R, 
                                  V0 in S, V1 in S, V2 in S, V3 in S, V4 in S };
}


@synopsis{

A relation excluding tuples that contain certain element values.

}
@examples{

```rascal-shell
import Relation;
carrierX({<1,10>, <2,20>, <3,30>}, {10, 1, 20});
```
}

@synopsis{

A relation excluded tuples containing certain values.

}
@description{

Returns relation `R` excluding tuples with some element in `S`.

}
@examples{

```rascal-shell
import Relation;
carrierX({<1,10>, <2,20>, <3,30>}, {10, 1, 20});
```
}
public rel[&T,&T] carrierX (rel[&T,&T] R, set[&T] S)
{
  return { <V0, V1> | <&T V0, &T V1> <- R, V0 notin S, V1 notin S };
}

public rel[&T,&T,&T] carrierX (rel[&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2> | <&T V0, &T V1, &T V2> <- R, V0 notin S, V1 notin S, V2 notin S };
}

public rel[&T,&T,&T,&T] carrierX (rel[&T,&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2, V3> | <&T V0, &T V1, &T V2, &T V3> <- R, V0 notin S, V1 notin S, V2 notin S, V3 notin S };
}

public rel[&T,&T,&T,&T,&T] carrierX (rel[&T,&T,&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2, V3, V4> | <&T V0, &T V1, &T V2, &T V3, &T V4> <- R, 
                                  V0 notin S, V1 notin S, V2 notin S, V3 notin S, V4 notin S };
}


@synopsis{

Complement of a relation.

}
@description{

Given a relation `R` a new relation `U` can be constructed that contains
all possible tuples with element values that occur at corresponding tuple positions in `R`.
The function `complement` returns the complement of `R` relative to `U`, in other words: `U - R`.

}
@examples{

```rascal-shell
import Relation;
```
Declare `R` and compute corresponding `U`:
```rascal-shell,continue
R = {<1,10>, <2, 20>, <3, 30>};
U = domain(R) * range(R);
```
Here is the complement of `R` computed in two ways:
```rascal-shell,continue
U - R;
complement({<1,10>, <2, 20>, <3, 30>});
```
}
public rel[&T0, &T1] complement(rel[&T0, &T1] R)
{
  return (domain(R) * range(R)) - R;
}

public rel[&T0, &T1, &T2] complement(rel[&T0, &T1, &T2] R)
{
  return {<V0, V1, V2> | &T0 V0 <- R<0>, &T1 V1 <- R<1>,  &T2 V2 <- R<2>, <V0, V1, V2> notin R};
}

public rel[&T0, &T1, &T2, &T3] complement(rel[&T0, &T1, &T2, &T3] R)
{
  return {<V0, V1, V2, V3> | &T0 V0 <- R<0>, &T1 V1 <- R<1>,  &T2 V2 <- R<2>, &T3 V3 <- R<3>, <V0, V1, V2, V3> notin R};
}

public rel[&T0, &T1, &T2, &T3, &T4] complement(rel[&T0, &T1, &T2, &T3, &T4] R)
{
  return {<V0, V1, V2, V3, V4> | &T0 V0 <- R<0>, &T1 V1 <- R<1>,  &T2 V2 <- R<2>, &T3 V3 <- R<3>, 
                                 &T4 V4 <- R<4>, <V0, V1, V2, V3, V4> notin R};
}


@synopsis{

Domain of a  relation: a set consisting of the first element of each tuple.

}
@examples{

```rascal-shell
import Relation;
domain({<1,10>, <2,20>});
domain({<"mon", 1>, <"tue", 2>});
```
}
public set[&T0] domain (rel[&T0,&T1] R)
{
  return R<0>;
}

public set[&T0] domain (rel[&T0,&T1,&T2] R)
{
  return R<0>;
  }

public set[&T0] domain (rel[&T0,&T1,&T2,&T3] R)
{
  return R<0>;
}

public set[&T0] domain (rel[&T0,&T1,&T2,&T3,&T4] R)
{
  return R<0>;
}


@synopsis{

Relation restricted to certain domain elements.

}
@description{

Restriction of a relation `R` to tuples with first element in `S`.

}
@examples{

```rascal-shell
import Relation;
domainR({<1,10>, <2,20>, <3,30>}, {3, 1});
```
}
public rel[&T0,&T1] domainR (rel[&T0,&T1] R, set[&T0] S)
{
  return { <V0, V1> | <&T0 V0, &T1 V1> <- R, V0 in S };
}

rel[&T, &U] domainR(rel[&T, &U] R, bool(&T) accept)
  = { <t,u> | <t,u> <- R, accept(t)};

public rel[&T0,&T1,&T2] domainR (rel[&T0,&T1,&T2] R, set[&T0] S)
{
  return { <V0, V1, V2> | <&T0 V0, &T1 V1, &T2 V2> <- R, V0 in S };
}

public rel[&T0,&T1,&T2,&T3] domainR (rel[&T0,&T1,&T2,&T3] R, set[&T0] S)
{
  return { <V0, V1, V2, V3> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3> <- R, V0 in S };
}

public rel[&T0,&T1,&T2,&T3,&T4] domainR (rel[&T0,&T1,&T2,&T3,&T4] R, set[&T0] S)
{
  return { <V0, V1, V2, V3, V4> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> <- R, V0 in S };
}


@synopsis{

Relation excluding certain domain values.

}
@description{

Relation `R` excluded tuples with first element in `S`.

}
@examples{

```rascal-shell
import Relation;
domainX({<1,10>, <2,20>, <3,30>}, {3, 1});
```
}
public rel[&T0,&T1] domainX (rel[&T0,&T1] R, set[&T0] S)
{
  return { <V0, V1> | <&T0 V0, &T1 V1> <- R, V0 notin S };
}

public rel[&T0,&T1,&T2] domainX (rel[&T0,&T1,&T2] R, set[&T0] S)
{
  return { <V0, V1, V2> | <&T0 V0, &T1 V1, &T2 V2> <- R, V0 notin S };
}

public rel[&T0,&T1,&T2,&T3] domainX (rel[&T0,&T1,&T2,&T3] R, set[&T0] S)
{
  return { <V0, V1, V2, V3> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3> <- R, V0 notin S };
}

public rel[&T0,&T1,&T2,&T3,&T4] domainX (rel[&T0,&T1,&T2,&T3,&T4] R, set[&T0] S)
{
  return { <V0, V1, V2, V3, V4> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> <- R, V0 notin S };
}


@synopsis{

Make sets of elements in the domain that relate to the same element in the range.

}
@examples{

```rascal-shell
import Relation;
legs = {<"bird", 2>, <"dog", 4>, <"human", 2>, <"spider", 8>, <"millepede", 1000>, <"crab", 8>, <"cat", 4>};
groupDomainByRange(legs);
```
}
public set[set[&U]] groupDomainByRange(rel[&U dom, &T ran] input) {
   return ( i : (input<ran, dom>)[i] | i <- input.ran )<1>;
}


@synopsis{

Make sets of elements in the range that relate to the same element in the domain.

}
@description{

```rascal-shell
import Relation;
skins = {<"bird", "feather">, <"dog", "fur">, <"tortoise", "shell">, <"human", "skin">, <"fish", "scale">, <"lizard", "scale">, <"crab", "shell">, <"cat", "fur">};
groupRangeByDomain(skins);
```
}
public set[set[&T]] groupRangeByDomain(rel[&U dom, &T ran] input) {
   return ( i : input[i] | i <- input.dom )<1>;
}


@synopsis{

The identity relation.

}
@description{

The identity relation for set `S`.

}
@examples{

```rascal-shell
import Relation;
ident({"mon", "tue", "wed"});
```
}
public rel[&T, &T] ident (set[&T] S)
{
  return {<V, V> | V <- S};
}


@synopsis{

Invert the tuples in a relation.

}
@examples{

```rascal-shell
import Relation;
invert({<1,10>, <2,20>});
```
}
public rel[&T1, &T0] invert (rel[&T0, &T1] R)
{
  return R<1, 0>;
}

public rel[&T2, &T1, &T0] invert (rel[&T0, &T1, &T2] R)
{
  return R<2, 1, 0>;
}

public rel[&T3, &T2, &T1, &T0] invert (rel[&T0, &T1, &T2, &T3] R)
{
  return R<3, 2, 1, 0>;
}

public rel[&T4, &T3, &T2, &T1, &T0] invert (rel[&T0, &T1, &T2, &T3, &T4] R)
{
  return R<4, 3, 2, 1, 0>;
}


@synopsis{

The range (i.e., all but the first element of each tuple) of a relation.

}
@examples{

```rascal-shell
import Relation;
range({<1,10>, <2,20>});
range({<"mon", 1>, <"tue", 2>});
```
}
public set[&T1] range (rel[&T0,&T1] R)
{
  return R<1>;
}

public rel[&T1,&T2] range (rel[&T0,&T1, &T2] R)
{
  return R<1,2>;
}

public rel[&T1,&T2,&T3] range (rel[&T0,&T1,&T2,&T3] R)
{
  return R<1,2,3>;
}

public rel[&T1,&T2,&T3,&T4] range (rel[&T0,&T1,&T2,&T3,&T4] R)
{
  return R<1,2,3,4>;
}


@synopsis{

Relation restricted to certain range values.

}
@description{

Restriction of binary relation `R` to tuples with second element in set `S`.

}
@examples{

```rascal-shell
import Relation;
rangeR({<1,10>, <2,20>, <3,30>}, {30, 10});
```
}
public rel[&T0,&T1] rangeR (rel[&T0,&T1] R, set[&T2] S)
{
  return { <V0, V1> | <&T0 V0, &T1 V1> <- R, V1 in S };
}


@synopsis{

Relation excluding certain range values.

}
@description{

Restriction of binary relation `R` to tuples with second element not in set `S`.

}
@examples{

```rascal-shell
import Relation;
rangeX({<1,10>, <2,20>, <3,30>}, {30, 10});
```
}
public rel[&T0,&T1] rangeX (rel[&T0,&T1] R, set[&T2] S)
{
  return { <V0, V1> | <&T0 V0, &T1 V1> <- R, V1 notin S };
}


@synopsis{

Indexes a binary relation as a map

}
@description{

Converts a binary relation to a map of the domain to a set of the range.

}
@examples{

```rascal-shell
import Relation;
index({<1,10>, <2,20>, <3,30>, <30,10>});
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[&K, set[&V]] index(rel[&K, &V] R);
