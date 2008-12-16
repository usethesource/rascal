module Relation

import Set;

public set[&T]  carrier (rel[&T,&T] R)
@doc{carrier -- all elements in any tuple in a relation}
{
  return domain(R) + range(R);
}

public rel[&T,&T] carrierR (rel[&T,&T] R, set[&T] S)
@doc{carrierR -- all elements in any tuple in relation R restricted to elements of S}
{
  return { <X, Y> | <&T X, &T Y> : R, X in S, Y in S };
}

public rel[&T,&T] carrierX (rel[&T,&T] R, set[&T] S)
@doc{carrierX -- all elements in any tuple in relation R excluded elements of S}
{
  return { <X, Y> | <&T X, &T Y> : R, 
                    !(X in S), !(Y in S) };
}

public rel[&T1, &T2] complement(rel[&T1, &T2] R)
@doc{complement -- complement of relation}
{
  return (domain(R) * range(R)) - R;
}

public rel[&T1, &T3] compose(rel[&T1, &T2] R,
                                 rel[&T2, &T3] S)
  @doc{Compose two relations}
{
   return {<X, Z> | <&T1 X, &T2 Y1>: R, 
                    <&T2 Y2, &T3 Z>: S, Y1 == Y2};
}

/* ----- begin experiment -----

public set[&T1] domain (rel[&T1,&T2] R)
{
  return R<0>;
}

public set[&T1] domain (rel[&T1,&T2,&T3] R)
{
  return R<0>;

public set[&T1] domain (rel[&T1,&T2,&T3,&T4] R)
{
  return R<0>;
}

public set[&T1] range (rel[&T1,&T2] R)
{
  return R<1>;
}

public rel[&T2,&T3] range (rel[&T1,&T2, &T3] R)
{
  return R<1,2>;
}

public rel[&T2,&T3,&T4] range (rel[&T1,&T2,&T3,&T4] R)
{
  return R<1,2,3>;
}

public set[&T]  carrier (rel[&T,&T] R)
@doc{carrier -- all elements in any tuple in a relation}
{
  return R<0> + R<1>
}

public set[&T]  carrier (rel[&T,&T,&T] R)
@doc{carrier -- all elements in any tuple in a relation}
{
  return R<0> + R<1> + R<2>;
}

public set[&T]  carrier (rel[&T,&T,&T,&T] R)
@doc{carrier -- all elements in any tuple in a relation}
{
  return  R<0> + R<1> + R<2> + R<3>;
}

----- end experiment ------- */

public set[&T1] domain (rel[&T1,&T2] R)
{
  return { X | <&T1 X, &T2 Y> : R};
}


public rel[&T1,&T2] domainR (rel[&T1,&T2] R, set[&T1] S)
@doc{domainR -- restriction of a binary relation to tuples with first element in S}
{
  return { <X, Y> | <&T1 X, &T2 Y> : R, X in S };
}

public rel[&T1,&T2] domainX (rel[&T1,&T2] R, set[&T1] S)
@doc{domainX -- binary relation excluded tuples with first element in S}
{
  return { <X, Y> | <&T1 X, &T2 Y> : R, X notin S };
}

public rel[&T, &T] ident(set[&T] S)
@doc{id == identity relation}
{
  return { <X, X> | &T X : S};
}

public rel[&T2, &T1] invert (rel[&T1, &T2] R)
@doc{inverse -- inverse the tuples in a relation}
{
  return { <Y, X> | <&T1 X, &T2 Y> : R };
}

public set[&T1] range (rel[&T1,&T2] R)
@doc{range -- second elements in tuples of binary relation}
{
  return { Y | <&T1 X, &T2 Y> : R };
}

public rel[&T1,&T2] rangeR (rel[&T1,&T2] R, set[&T2] S)
@doc{rangeR -- restriction of a binary relation to tuples with second element in S}
{
  return { <X, Y> | <&T1 X, &T2 Y> : R, Y in S };
}

public rel[&T1,&T2] rangeX (rel[&T1,&T2] R, set[&T2] S)
 @doc{rangeX -- binary relation excluded tuples with second element in S}
{
  return { <X, Y> | <&T1 X, &T2 Y> : R, Y notin S };
}


//%% Note: in rel[&T], the type variable &T refers 
///%% to the tuple type of the relation.

//public list[&T] toList(rel[&T] R)
//  @primitive{"Rel.toList"}
  
//public map[&T] toMap(rel[tuple[&T]] S)
//  throws non_unique_domain(str msg)
//  @primitive{"Ret.toMap"}

//public set[&T] toSet(rel[&T] R)
//  @primitive{"Rel.toSet"}


