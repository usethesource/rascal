module Relation

import Set;

// carrier

public set[&T]  carrier (rel[&T,&T] R)
@doc{carrier -- all elements in any tuple in a binary relation}
{
  return R<0> + R<1>;
}

public set[&T]  carrier (rel[&T,&T,&T] R)
@doc{carrier -- all elements in any tuple in a ternary relation}
{
  return (R<0> + R<1>) + R<2>;
}

public set[&T]  carrier (rel[&T,&T,&T,&T] R)
@doc{carrier -- all elements in any tuple in a quaternary relation}
{
  return  ((R<0> + R<1>) + R<2>) + R<3>;
}

public set[&T]  carrier (rel[&T,&T,&T,&T,&T] R)
@doc{carrier -- all elements in any tuple in a quinary relation}
{
  return  (((R<0> + R<1>) + R<2>) + R<3>) + R<4>;
}

// carrierR

public rel[&T,&T] carrierR (rel[&T,&T] R, set[&T] S)
@doc{carrierR -- all elements in any tuple in binary relation R restricted to elements of S}
{
  return { <V0, V1> | <&T V0, &T V1> : R, V0 in S, V1 in S };
}

public rel[&T,&T,&T] carrierR (rel[&T,&T,&T] R, set[&T] S)
@doc{carrierR -- all elements in any tuple in ternary relation R restricted to elements of S}
{
  return { <V0, V1, V2> | <&T V0, &T V1, &T V2> : R, V0 in S, V1 in S, V2 in S };
}

public rel[&T,&T,&T,&T] carrierR (rel[&T,&T,&T,&T] R, set[&T] S)
@doc{carrierR -- all elements in any tuple in quaterny relation R restricted to elements of S}
{
  return { <V0, V1, V2, V3> | <&T V0, &T V1, &T V2, &T V3> : R, V0 in S, V1 in S, V2 in S, V3 in S };
}

public rel[&T,&T,&T,&T,&T] carrierR (rel[&T,&T,&T,&T,&T] R, set[&T] S)
@doc{carrierR -- all elements in any tuple in quinary relation R restricted to elements of S}
{
  return { <V0, V1, V2, V3, V4> | <&T V0, &T V1, &T V2, &T V3, &T V4> : R, 
                                  V0 in S, V1 in S, V2 in S, V3 in S, V4 in S };
}

// carrierX

public rel[&T,&T] carrierX (rel[&T,&T] R, set[&T] S)
@doc{carrierX -- binary relation excluded tuples with some element in S}
{
  return { <V0, V1> | <&T V0, &T V1> : R, V0 notin S, V1 notin S };
}

public rel[&T,&T,&T] carrierX (rel[&T,&T,&T] R, set[&T] S)
@doc{carrierX -- ternary relation excluded tuples with some element in SS}
{
  return { <V0, V1, V2> | <&T V0, &T V1, &T V2> : R, V0 notin S, V1 notin S, V2 notin S };
}

public rel[&T,&T,&T,&T] carrierX (rel[&T,&T,&T,&T] R, set[&T] S)
@doc{carrierX -- quaterny relation excluded tuples with some element in S}
{
  return { <V0, V1, V2, V3> | <&T V0, &T V1, &T V2, &T V3> : R, V0 notin S, V1 notin S, V2 notin S, V3 notin S };
}

public rel[&T,&T,&T,&T,&T] carrierX (rel[&T,&T,&T,&T,&T] R, set[&T] S)
@doc{carrierX -- quinary relation excluded tuples with some element in S}
{
  return { <V0, V1, V2, V3, V4> | <&T V0, &T V1, &T V2, &T V3, &T V4> : R, 
                                  V0 notin S, V1 notin S, V2 notin S, V3 notin S, V4 notin S };
}

// complement

public rel[&T0, &T1] complement(rel[&T0, &T1] R)
@doc{complement -- complement of binary relation}
{
  return (domain(R) * range(R)) - R;
}

public rel[&T0, &T1, &T2] complement(rel[&T0, &T1, &T2] R)
@doc{complement -- complement of ternary relation}
{
  return {<V0, V1, V2> | &T0 V0 : R<0>, &T1 V1 : R<1>,  &T2 V2 : R<2>, <V0, V1, V2> notin R};
}

public rel[&T0, &T1, &T2, &T3] complement(rel[&T0, &T1, &T2, &T3] R)
@doc{complement -- complement of quaternary relation}
{
  return {<V0, V1, V2, V3> | &T0 V0 : R<0>, &T1 V1 : R<1>,  &T2 V2 : R<2>, &T3 V3 : R<3>, <V0, V1, V2, V3> notin R};
}

public rel[&T0, &T1, &T2, &T3, &T4] complement(rel[&T0, &T1, &T2, &T3, &T4] R)
@doc{complement -- complement of quinary relation}
{
  return {<V0, V1, V2, V3, V4> | &T0 V0 : R<0>, &T1 V1 : R<1>,  &T2 V2 : R<2>, &T3 V3 : R<3>, 
                                 &T4 V4 : R<4>, <V0, V1, V2, V3, V4> notin R};
}

// domain

public set[&T0] domain (rel[&T0,&T1] R)
@doc{domain -- first element of each tuple in binary relation}
{
  return R<0>;
}

public set[&T0] domain (rel[&T0,&T1,&T2] R)
@doc{domain -- first element of each tuple in ternary relation}
{
  return R<0>;
  }

public set[&T0] domain (rel[&T0,&T1,&T2,&T3] R)
@doc{domain -- first element of each tuple in quaterny relation}
{
  return R<0>;
}

public set[&T0] domain (rel[&T0,&T1,&T2,&T3,&T4] R)
@doc{domain -- first element of each tuple in quinary relation}
{
  return R<0>;
}

// domainR

public rel[&T0,&T1] domainR (rel[&T0,&T1] R, set[&T0] S)
@doc{domainR -- restriction of a binary relation to tuples with first element in S}
{
  return { <V0, V1> | <&T0 V0, &T1 V1> : R, V0 in S };
}

public rel[&T0,&T1,&T2] domainR (rel[&T0,&T1,&T2] R, set[&T0] S)
@doc{domainR -- restriction of a ternary relation to tuples with first element in S}
{
  return { <V0, V1, V2> | <&T0 V0, &T1 V1, &T2 V2> : R, V0 in S };
}

public rel[&T0,&T1,&T2,&T3] domainR (rel[&T0,&T1,&T2,&T3] R, set[&T0] S)
@doc{domainR -- restriction of a quaterny relation to tuples with first element in S}
{
  return { <V0, V1, V2, V3> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3> : R, V0 in S };
}

public rel[&T0,&T1,&T2,&T3,&T4] domainR (rel[&T0,&T1,&T2,&T3,&T4] R, set[&T0] S)
@doc{domainR -- restriction of a quinary relation to tuples with first element in S}
{
  return { <V0, V1, V2, V3, V4> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> : R, V0 in S };
}

// domainX

public rel[&T0,&T1] domainX (rel[&T0,&T1] R, set[&T0] S)
@doc{domainR -- binary relation excluded tuples with first element in S}
{
  return { <V0, V1> | <&T0 V0, &T1 V1> : R, V0 notin S };
}

public rel[&T0,&T1,&T2] domainX (rel[&T0,&T1,&T2] R, set[&T0] S)
@doc{domainR -- ternary relation excluded tuples with first element in SS}
{
  return { <V0, V1, V2> | <&T0 V0, &T1 V1, &T2 V2> : R, V0 notin S };
}

public rel[&T0,&T1,&T2,&T3] domainX (rel[&T0,&T1,&T2,&T3] R, set[&T0] S)
@doc{domainR -- quaterny relation excluded tuples with first element in S}
{
  return { <V0, V1, V2, V3> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3> : R, V0 notin S };
}

public rel[&T0,&T1,&T2,&T3,&T4] domainX (rel[&T0,&T1,&T2,&T3,&T4] R, set[&T0] S)
@doc{domainR -- quinary relation excluded tuples with first element in S}
{
  return { <V0, V1, V2, V3, V4> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> : R, V0 notin S };
}

// invert

public rel[&T1, &T0] invert (rel[&T0, &T1] R)
@doc{invert -- inverse the tuples in a binary relation}
{
  return R<1, 0>;
}

public rel[&T2, &T1, &T0] invert (rel[&T0, &T1, &T2] R)
@doc{invert -- inverse the tuples in a ternary relation}
{
  return R<2, 1, 0>;
}

public rel[&T3, &T2, &T1, &T0] invert (rel[&T0, &T1, &T2, &T3] R)
@doc{invert -- inverse the tuples in a quaterny relation}
{
  return R<3, 2, 1, 0>;
}

public rel[&T4, &T3, &T2, &T1, &T0] invert (rel[&T0, &T1, &T2, &T3, &T4] R)
@doc{invert -- inverse the tuples in a quinary relation}
{
  return R<4, 3, 2, 1, 0>;
}

// range

public set[&T1] range (rel[&T0,&T1] R)
@doc{range -- all but the first element of each tuples in binary relation}
{
  return R<1>;
}

public rel[&T1,&T2] range (rel[&T0,&T1, &T2] R)
@doc{range -- all but the first element of each tuples in ternary relation}
{
  return R<1,2>;
}

public rel[&T1,&T2,&T3] range (rel[&T0,&T1,&T2,&T3] R)
@doc{range -- all but the first element of each tuples in quaterny relation}
{
  return R<1,2,3>;
}

public rel[&T1,&T2,&T3,&T4] range (rel[&T0,&T1,&T2,&T3,&T4] R)
@doc{range -- all but the first element of each tuples in quinary relation}
{
  return R<1,2,3,4>;
}

// rangeR

// TODO: generalize rangeR and rangeX

public rel[&T0,&T1] rangeR (rel[&T0,&T1] R, set[&T2] S)
@doc{rangeR -- restriction of a binary relation to tuples with second element in S}
{
  return { <V0, V1> | <&T0 V0, &T1 V1> : R, V1 in S };
}

public rel[&T0,&T1] rangeX (rel[&T0,&T1] R, set[&T2] S)
 @doc{rangeX -- binary relation excluded tuples with second element in S}
{
  return { <V0, V1> | <&T0 V0, &T1 V1> : R, V1 notin S };
}



