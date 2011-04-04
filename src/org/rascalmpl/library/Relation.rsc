@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module Relation

import Set;

@doc{All elements in any tuple in a binary relation}
public set[&T]  carrier (rel[&T,&T] R)
{
  return R<0> + R<1>;
}

@doc{All elements in any tuple in a ternary relation}
public set[&T]  carrier (rel[&T,&T,&T] R)
{
  return (R<0> + R<1>) + R<2>;
}

@doc{All elements in any tuple in a quaternary relation}
public set[&T]  carrier (rel[&T,&T,&T,&T] R)
{
  return  ((R<0> + R<1>) + R<2>) + R<3>;
}

@doc{All elements in any tuple in a quinary relation}
public set[&T]  carrier (rel[&T,&T,&T,&T,&T] R)
{
  return  (((R<0> + R<1>) + R<2>) + R<3>) + R<4>;
}

@doc{Binary relation restricted to tuples with elements in a set S}
public rel[&T,&T] carrierR (rel[&T,&T] R, set[&T] S)
{
  return { <V0, V1> | <&T V0, &T V1> <- R, V0 in S, V1 in S };
}

@doc{All elements in any tuple in ternary relation R restricted to elements of S}
public rel[&T,&T,&T] carrierR (rel[&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2> | <&T V0, &T V1, &T V2> <- R, V0 in S, V1 in S, V2 in S };
}

@doc{All elements in any tuple in quaterny relation R restricted to elements of S}
public rel[&T,&T,&T,&T] carrierR (rel[&T,&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2, V3> | <&T V0, &T V1, &T V2, &T V3> <- R, V0 in S, V1 in S, V2 in S, V3 in S };
}

@doc{All elements in any tuple in quinary relation R restricted to elements of S}
public rel[&T,&T,&T,&T,&T] carrierR (rel[&T,&T,&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2, V3, V4> | <&T V0, &T V1, &T V2, &T V3, &T V4> <- R, 
                                  V0 in S, V1 in S, V2 in S, V3 in S, V4 in S };
}

@doc{Binary relation excluded tuples with some element in S}
public rel[&T,&T] carrierX (rel[&T,&T] R, set[&T] S)
{
  return { <V0, V1> | <&T V0, &T V1> <- R, V0 notin S, V1 notin S };
}

@doc{Ternary relation excluded tuples with some element in SS}
public rel[&T,&T,&T] carrierX (rel[&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2> | <&T V0, &T V1, &T V2> <- R, V0 notin S, V1 notin S, V2 notin S };
}

@doc{Quaterny relation excluded tuples with some element in S}
public rel[&T,&T,&T,&T] carrierX (rel[&T,&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2, V3> | <&T V0, &T V1, &T V2, &T V3> <- R, V0 notin S, V1 notin S, V2 notin S, V3 notin S };
}

@doc{Quinary relation excluded tuples with some element in S}
public rel[&T,&T,&T,&T,&T] carrierX (rel[&T,&T,&T,&T,&T] R, set[&T] S)
{
  return { <V0, V1, V2, V3, V4> | <&T V0, &T V1, &T V2, &T V3, &T V4> <- R, 
                                  V0 notin S, V1 notin S, V2 notin S, V3 notin S, V4 notin S };
}

@doc{Complement of binary relation}
public rel[&T0, &T1] complement(rel[&T0, &T1] R)
{
  return (domain(R) * range(R)) - R;
}

@doc{Complement of ternary relation}
public rel[&T0, &T1, &T2] complement(rel[&T0, &T1, &T2] R)
{
  return {<V0, V1, V2> | &T0 V0 <- R<0>, &T1 V1 <- R<1>,  &T2 V2 <- R<2>, <V0, V1, V2> notin R};
}

@doc{Complement of quaternary relation}
public rel[&T0, &T1, &T2, &T3] complement(rel[&T0, &T1, &T2, &T3] R)
{
  return {<V0, V1, V2, V3> | &T0 V0 <- R<0>, &T1 V1 <- R<1>,  &T2 V2 <- R<2>, &T3 V3 <- R<3>, <V0, V1, V2, V3> notin R};
}

@doc{Complement of quinary relation}
public rel[&T0, &T1, &T2, &T3, &T4] complement(rel[&T0, &T1, &T2, &T3, &T4] R)
{
  return {<V0, V1, V2, V3, V4> | &T0 V0 <- R<0>, &T1 V1 <- R<1>,  &T2 V2 <- R<2>, &T3 V3 <- R<3>, 
                                 &T4 V4 <- R<4>, <V0, V1, V2, V3, V4> notin R};
}

@doc{First element of each tuple in binary relation}
public set[&T0] domain (rel[&T0,&T1] R)
{
  return R<0>;
}

@doc{First element of each tuple in ternary relation}
public set[&T0] domain (rel[&T0,&T1,&T2] R)
{
  return R<0>;
  }

@doc{First element of each tuple in quaterny relation}
public set[&T0] domain (rel[&T0,&T1,&T2,&T3] R)
{
  return R<0>;
}

@doc{First element of each tuple in quinary relation}
public set[&T0] domain (rel[&T0,&T1,&T2,&T3,&T4] R)
{
  return R<0>;
}

@doc{Restriction of a binary relation to tuples with first element in S}
public rel[&T0,&T1] domainR (rel[&T0,&T1] R, set[&T0] S)
{
  return { <V0, V1> | <&T0 V0, &T1 V1> <- R, V0 in S };
}

@doc{Restriction of a ternary relation to tuples with first element in S}
public rel[&T0,&T1,&T2] domainR (rel[&T0,&T1,&T2] R, set[&T0] S)
{
  return { <V0, V1, V2> | <&T0 V0, &T1 V1, &T2 V2> <- R, V0 in S };
}

@doc{Restriction of a quaterny relation to tuples with first element in S}
public rel[&T0,&T1,&T2,&T3] domainR (rel[&T0,&T1,&T2,&T3] R, set[&T0] S)
{
  return { <V0, V1, V2, V3> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3> <- R, V0 in S };
}

@doc{Restriction of a quinary relation to tuples with first element in S}
public rel[&T0,&T1,&T2,&T3,&T4] domainR (rel[&T0,&T1,&T2,&T3,&T4] R, set[&T0] S)
{
  return { <V0, V1, V2, V3, V4> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> <- R, V0 in S };
}

@doc{Binary relation excluded tuples with first element in S}
public rel[&T0,&T1] domainX (rel[&T0,&T1] R, set[&T0] S)
{
  return { <V0, V1> | <&T0 V0, &T1 V1> <- R, V0 notin S };
}

@doc{Ternary relation excluded tuples with first element in SS}
public rel[&T0,&T1,&T2] domainX (rel[&T0,&T1,&T2] R, set[&T0] S)
{
  return { <V0, V1, V2> | <&T0 V0, &T1 V1, &T2 V2> <- R, V0 notin S };
}

@doc{Quaterny relation excluded tuples with first element in S}
public rel[&T0,&T1,&T2,&T3] domainX (rel[&T0,&T1,&T2,&T3] R, set[&T0] S)
{
  return { <V0, V1, V2, V3> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3> <- R, V0 notin S };
}

@doc{Quinary relation excluded tuples with first element in S}
public rel[&T0,&T1,&T2,&T3,&T4] domainX (rel[&T0,&T1,&T2,&T3,&T4] R, set[&T0] S)
{
  return { <V0, V1, V2, V3, V4> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> <- R, V0 notin S };
}

@doc{Identity relation for a set}
public rel[&T, &T] ident (set[&T] S)
{
  return {<V, V> | V <- S};
}

@doc{Inverse the tuples in a binary relation}
public rel[&T1, &T0] invert (rel[&T0, &T1] R)
{
  return R<1, 0>;
}

@doc{Inverse the tuples in a ternary relation}
public rel[&T2, &T1, &T0] invert (rel[&T0, &T1, &T2] R)
{
  return R<2, 1, 0>;
}

@doc{Inverse the tuples in a quaterny relation}
public rel[&T3, &T2, &T1, &T0] invert (rel[&T0, &T1, &T2, &T3] R)
{
  return R<3, 2, 1, 0>;
}

@doc{Inverse the tuples in a quinary relation}
public rel[&T4, &T3, &T2, &T1, &T0] invert (rel[&T0, &T1, &T2, &T3, &T4] R)
{
  return R<4, 3, 2, 1, 0>;
}

@doc{All but the first element of each tuple in the given binary relation}
public set[&T1] range (rel[&T0,&T1] R)
{
  return R<1>;
}

@doc{All but the first element of each tuple in the given ternary relation}
public rel[&T1,&T2] range (rel[&T0,&T1, &T2] R)
{
  return R<1,2>;
}

@doc{All but the first element of each tuple in the given quaterny relation}
public rel[&T1,&T2,&T3] range (rel[&T0,&T1,&T2,&T3] R)
{
  return R<1,2,3>;
}

@doc{All but the first element of each tuple in the given quinary relation}
public rel[&T1,&T2,&T3,&T4] range (rel[&T0,&T1,&T2,&T3,&T4] R)
{
  return R<1,2,3,4>;
}

@doc{Restriction of a binary relation to tuples with second element in S}
public rel[&T0,&T1] rangeR (rel[&T0,&T1] R, set[&T2] S)
{
  return { <V0, V1> | <&T0 V0, &T1 V1> <- R, V1 in S };
}

@doc{Binary relation excluded tuples with second element in S}
public rel[&T0,&T1] rangeX (rel[&T0,&T1] R, set[&T2] S)
{
  return { <V0, V1> | <&T0 V0, &T1 V1> <- R, V1 notin S };
}

@doc{Compute a distribution: count how many times events are mapped to which bucket}
public map[&T, int] distribution(rel[&U event, &T bucket] input) {
  map[&T,int] result = ();
  for (<&U event, &T bucket> <- input) {
    result[bucket]?0 += 1;
  }
  
  return result;
}

@doc{Make sets of elements in the domain that relate to the same element in the range}
public set[set[&U]] groupDomainByRange(rel[&U dom, &T ran] input) {
   return ( i : (input<ran, dom>)[i] | i <- input.ran )<1>;
}

@doc{Make sets of elements in the range that relate to the same element in the domain}
public set[set[&T]] groupRangeByDomain(rel[&U dom, &T ran] input) {
   return ( i : input[i] | i <- input.dom )<1>;
}
