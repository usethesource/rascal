@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Vadim Zaytsev - vadim@grammarware.net - UvA}


@synopsis{

Library functions for list relations.

}
@description{

The following library functions are defined for list relations :
(((TOC)))
}
module ListRelation

import List;


@synopsis{

Return the list of all elements in any tuple in a list relation.

}
@examples{

```rascal-shell
import ListRelation;
carrier([<1,10>, <2,20>]);
carrier([<1,10,100,1000>, <2,20,200,2000>]);
```
}
public list[&T]  carrier (lrel[&T,&T] R)
	= [V0, V1 | <&T V0, &T V1> <- R];

public list[&T]  carrier (lrel[&T,&T,&T] R)
	= [V0, V1, V2 | <&T V0, &T V1, &T V2> <- R];

public list[&T]  carrier (lrel[&T,&T,&T,&T] R)
	= [V0, V1, V2, V3 | <&T V0, &T V1, &T V2, &T V3> <- R];

public list[&T]  carrier (lrel[&T,&T,&T,&T,&T] R)
	= [V0, V1, V2, V3, V4 | <&T V0, &T V1, &T V2, &T V3, &T V4> <- R];


@synopsis{

A list relation restricted to certain element values in tuples.

}
@description{

Returns list relation `R` restricted to tuples with elements in set `S`.

}
@examples{

```rascal-shell
import ListRelation;
carrierR([<1,10>, <2,20>, <3,30>], {10, 1, 20});
```
}
public lrel[&T,&T] carrierR (lrel[&T,&T] R, set[&T] S)
	= [ <V0, V1> | <&T V0, &T V1> <- R, V0 in S, V1 in S ];

public lrel[&T,&T,&T] carrierR (lrel[&T,&T,&T] R, set[&T] S)
	= [ <V0, V1, V2> | <&T V0, &T V1, &T V2> <- R, V0 in S, V1 in S, V2 in S ];

public lrel[&T,&T,&T,&T] carrierR (lrel[&T,&T,&T,&T] R, set[&T] S)
	= [ <V0, V1, V2, V3> | <&T V0, &T V1, &T V2, &T V3> <- R, V0 in S, V1 in S, V2 in S, V3 in S ];

public lrel[&T,&T,&T,&T,&T] carrierR (lrel[&T,&T,&T,&T,&T] R, set[&T] S)
	= [ <V0, V1, V2, V3, V4> | <&T V0, &T V1, &T V2, &T V3, &T V4> <- R, 
								V0 in S, V1 in S, V2 in S, V3 in S, V4 in S ];


@synopsis{

A list relation excluding tuples containing certain values.

}
@description{

Returns list relation `R` excluding tuples with some element in `S`.

}
@examples{

```rascal-shell
import ListRelation;
carrierX([<1,10>, <2,20>, <3,30>], {10, 1, 20});
```
}
public lrel[&T,&T] carrierX (lrel[&T,&T] R, set[&T] S)
	= [ <V0, V1> | <&T V0, &T V1> <- R, V0 notin S, V1 notin S ];

public lrel[&T,&T,&T] carrierX (lrel[&T,&T,&T] R, set[&T] S)
	= [ <V0, V1, V2> | <&T V0, &T V1, &T V2> <- R, V0 notin S, V1 notin S, V2 notin S ];

public lrel[&T,&T,&T,&T] carrierX (lrel[&T,&T,&T,&T] R, set[&T] S)
	= [ <V0, V1, V2, V3> | <&T V0, &T V1, &T V2, &T V3> <- R, V0 notin S, V1 notin S, V2 notin S, V3 notin S ];

public lrel[&T,&T,&T,&T,&T] carrierX (lrel[&T,&T,&T,&T,&T] R, set[&T] S)
	= [ <V0, V1, V2, V3, V4> | <&T V0, &T V1, &T V2, &T V3, &T V4> <- R, 
								V0 notin S, V1 notin S, V2 notin S, V3 notin S, V4 notin S ];


@synopsis{

Complement of a list relation.

}
@description{

Given a list relation `R` a new relation `U` can be constructed that contains
all possible tuples with element values that occur at corresponding tuple positions in `R`.
The function `complement` returns the complement of `R` relative to `U`, in other words: `U - R`.

}
@examples{

```rascal-shell
import ListRelation;
```

Declare `R` and compute corresponding `U`:

```rascal-shell,continue
R = [<1,10>, <2, 20>, <3, 30>];
U = domain(R) * range(R);
```

Here is the complement of `R` computed in two ways:
```rascal-shell,continue
U - R;
complement([<1,10>, <2, 20>, <3, 30>]);
```
}
public lrel[&T0, &T1] complement(lrel[&T0, &T1] R)
	= size(R) < 2 ? []
	: dup([<V0, V1> | &T0 V0 <- R<0>, &T1 V1 <- R<1>, <V0, V1> notin R]);
 // Was:  (domain(R) * range(R)) - R;

public lrel[&T0, &T1, &T2] complement(lrel[&T0, &T1, &T2] R)
	= dup([<V0, V1, V2> | &T0 V0 <- R<0>, &T1 V1 <- R<1>,  &T2 V2 <- R<2>, <V0, V1, V2> notin R]);

public lrel[&T0, &T1, &T2, &T3] complement(lrel[&T0, &T1, &T2, &T3] R)
	= dup([<V0, V1, V2, V3> | &T0 V0 <- R<0>, &T1 V1 <- R<1>,  &T2 V2 <- R<2>, &T3 V3 <- R<3>, <V0, V1, V2, V3> notin R]);

public lrel[&T0, &T1, &T2, &T3, &T4] complement(lrel[&T0, &T1, &T2, &T3, &T4] R)
	= dup([<V0, V1, V2, V3, V4> | &T0 V0 <- R<0>, &T1 V1 <- R<1>,  &T2 V2 <- R<2>, &T3 V3 <- R<3>, 
                                 &T4 V4 <- R<4>, <V0, V1, V2, V3, V4> notin R]);


@synopsis{

Domain of a list relation: a list consisting of the first element of each tuple, uniquely.

}
@description{

The domain can be seen as all possible inputs of the relation image operation. The
result contains elements (or tuples) in the order of appearance of the original relation,
but all occurences after the first occurrence of an element have been removed.

}
@examples{

```rascal-shell
import ListRelation;
domain([<1,10>, <2,20>]);
domain([<"mon", 1>, <"tue", 2>]);
```
}
public list[&T0] domain(lrel[&T0,&T1]             R) = dup(R<0>);
public list[&T0] domain(lrel[&T0,&T1,&T2]         R) = dup(R<0>);
public list[&T0] domain(lrel[&T0,&T1,&T2,&T3]     R) = dup(R<0>);
public list[&T0] domain(lrel[&T0,&T1,&T2,&T3,&T4] R) = dup(R<0>);


@synopsis{

List relation restricted to certain domain elements.

}
@description{

Restriction of a list relation `R` to tuples with first element in `S`.

}
@examples{

```rascal-shell
import ListRelation;
domainR([<1,10>, <2,20>, <3,30>], {3, 1});
```
}
public lrel[&T0,&T1] domainR (lrel[&T0,&T1] R, set[&T0] S)
	= [ <V0, V1> | <&T0 V0, &T1 V1> <- R, V0 in S ];

public lrel[&T0,&T1,&T2] domainR (lrel[&T0,&T1,&T2] R, set[&T0] S)
	= [ <V0, V1, V2> | <&T0 V0, &T1 V1, &T2 V2> <- R, V0 in S ];

public lrel[&T0,&T1,&T2,&T3] domainR (lrel[&T0,&T1,&T2,&T3] R, set[&T0] S)
	= [ <V0, V1, V2, V3> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3> <- R, V0 in S ];

public lrel[&T0,&T1,&T2,&T3,&T4] domainR (lrel[&T0,&T1,&T2,&T3,&T4] R, set[&T0] S)
	= [ <V0, V1, V2, V3, V4> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> <- R, V0 in S ];

// If the restiction is specified as a list, we take the order of tuples from there
public lrel[&T0,&T1] domainR (lrel[&T0,&T1] R, list[&T0] L)
	= [ <V0, V1> | &T0 V0 <- L, <V0, &T1 V1> <- R];

public lrel[&T0,&T1,&T2] domainR (lrel[&T0,&T1,&T2] R, list[&T0] L)
	= [ <V0, V1, V2> | &T0 V0 <- L, <V0, &T1 V1, &T2 V2> <- R];

public lrel[&T0,&T1,&T2,&T3] domainR (lrel[&T0,&T1,&T2,&T3] R, list[&T0] L)
	= [ <V0, V1, V2, V3> | &T0 V0 <- L, <V0, &T1 V1, &T2 V2, &T3 V3> <- R];

public lrel[&T0,&T1,&T2,&T3,&T4] domainR (lrel[&T0,&T1,&T2,&T3,&T4] R, list[&T0] L)
	= [ <V0, V1, V2, V3, V4> | &T0 V0 <- L, <V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> <- R];



@synopsis{

List relation excluding certain domain values.

}
@description{

List relation `R` excluding tuples with first element in `S`.

}
@examples{

```rascal-shell
import ListRelation;
domainX([<1,10>, <2,20>, <3,30>], {3, 1});
```
}
public lrel[&T0, &T1] domainX (lrel[&T0, &T1] R, set[&T0] S)
	= [ <V0, V1> | <&T0 V0, &T1 V1> <- R, V0 notin S ];

public lrel[&T0, &T1, &T2] domainX (lrel[&T0, &T1, &T2] R, set[&T0] S)
	= [ <V0, V1, V2> | <&T0 V0, &T1 V1, &T2 V2> <- R, V0 notin S ];

public lrel[&T0, &T1, &T2, &T3] domainX (lrel[&T0, &T1, &T2, &T3] R, set[&T0] S)
	= [ <V0, V1, V2, V3> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3> <- R, V0 notin S ];

public lrel[&T0, &T1, &T2, &T3, &T4] domainX (lrel[&T0, &T1, &T2, &T3, &T4] R, set[&T0] S)
	= [ <V0, V1, V2, V3, V4> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> <- R, V0 notin S ];



@synopsis{

Make sets of elements in the domain that relate to the same element in the range.

}
@examples{

```rascal-shell
import ListRelation;
legs = [<"bird", 2>, <"dog", 4>, <"human", 2>, <"spider", 8>, <"millepede", 1000>, <"crab", 8>, <"cat", 4>];
groupDomainByRange(legs);
```
}
public list[list[&U]] groupDomainByRange(lrel[&U dom, &T ran] input) 
	= dup([[d | <&U d,r> <- input] | &T r <- input.ran]);


@synopsis{

Make sets of elements in the range that relate to the same element in the domain.

}
@description{

```rascal-shell
import ListRelation;
skins = [<"bird", "feather">, <"dog", "fur">, <"tortoise", "shell">, <"human", "skin">, <"fish", "scale">, <"lizard", "scale">, <"crab", "shell">, <"cat", "fur">];
groupRangeByDomain(skins);
```
}
public list[list[&T]] groupRangeByDomain(lrel[&U dom, &T ran] input)
	= dup([[r | <d,&T r> <- input] | &U d <- input.dom]);
	// the "input[i]" trick used for set-based relations does not work here
	// because [<"a",1>]["a"] does give [1], but [<1,1>][1] does not!


@synopsis{

The identity list relation.

}
@description{

The identity list relation for set `S`.

}
@examples{

```rascal-shell
import ListRelation;
ident(["mon", "tue", "wed"]);
```
}
public lrel[&T, &T] ident (list[&T] S) = [<V, V> | V <- S];


@synopsis{

Invert the tuples in a list relation.

}
@examples{

```rascal-shell
import ListRelation;
invert([<1,10>, <2,20>]);
```
}
public lrel[            &T1,&T0] invert (lrel[&T0,&T1            ] R) = R<1,0>;
public lrel[        &T2,&T1,&T0] invert (lrel[&T0,&T1,&T2        ] R) = R<2,1,0>;
public lrel[    &T3,&T2,&T1,&T0] invert (lrel[&T0,&T1,&T2,&T3    ] R) = R<3,2,1,0>;
public lrel[&T4,&T3,&T2,&T1,&T0] invert (lrel[&T0,&T1,&T2,&T3,&T4] R) = R<4,3,2,1,0>;


@synopsis{

The range is composed of all but the first element of each tuple of a list relation, uniquely.

}
@description{

The range can be seen as all the elements of in all possible images of the relation. The
result contains elements (or tuples) in the order of appearance of the original relation,
but all occurences after the first occurrence of an element have been removed.

}
@examples{

```rascal-shell
import ListRelation;
range([<1,10>, <2,20>]);
range([<"mon", 1>, <"tue", 2>]);
```
}
public list[&T1]             range (lrel[&T0,&T1]             R) = dup(R<1>);
public lrel[&T1,&T2]         range (lrel[&T0,&T1, &T2]        R) = dup(R<1,2>);
public lrel[&T1,&T2,&T3]     range (lrel[&T0,&T1,&T2,&T3]     R) = dup(R<1,2,3>);
public lrel[&T1,&T2,&T3,&T4] range (lrel[&T0,&T1,&T2,&T3,&T4] R) = dup(R<1,2,3,4>);


@synopsis{

List relation restricted to certain range values.

}
@description{

Restriction of binary list relation `R` to tuples with second element in set `S`.

}
@examples{

```rascal-shell
import ListRelation;
rangeR([<1,10>, <2,20>, <3,30>], {30, 10});
```
}
public lrel[&T0,&T1] rangeR (lrel[&T0,&T1] R, set[&T1] S)
	= [ <V0, V1> | <&T0 V0, &T1 V1> <- R, V1 in S ];

// If the restiction is specified as a list, we take the order of tuples from there
public lrel[&T0,&T1] rangeR (lrel[&T0,&T1] R, list[&T1] L)
	= [ <V0, V1> | &T1 V1 <- L, <&T0 V0, V1> <- R ];


@synopsis{

List relation excluding certain range values.

}
@description{

Restriction of binary list relation `R` to tuples with second element not in set `S`.

}
@examples{

```rascal-shell
import ListRelation;
rangeX([<1,10>, <2,20>, <3,30>], {30, 10});
```
}
public lrel[&T0,&T1] rangeX (lrel[&T0,&T1] R, set[&T1] S)
	= [ <V0, V1> | <&T0 V0, &T1 V1> <- R, V1 notin S ];
// Why not?
public lrel[&T0,&T1] rangeX (lrel[&T0,&T1] R, list[&T1] S)
	= [ <V0, V1> | <&T0 V0, &T1 V1> <- R, V1 notin S ];


@synopsis{

Listes a binary list relation as a map

}
@description{

Converts a binary list relation to a map of the domain to a set of the range.

}
@examples{

```rascal-shell
import ListRelation;
index([<1,10>, <2,20>, <3,30>, <30,10>]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[&K, set[&V]] index(lrel[&K, &V] R);


