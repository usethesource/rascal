module Relation

public &T arb(rel[&T] R)
throws empty_relation(str msg)
  @primitive{"Rel.arb"}

public int size(rel[&T] R)
  @primitive{"Rel.size"}

public str toString(rel[&T] R)
  @primitive{"Rel.toString"}

%% Note: in rel[&T], the type variable &T refers 
%% to the tuple type of the relation.

public list[&T] toList(rel[&T] R)
  @primitive{"Rel.toList"}
  
public set[&T] toSet(rel[&T] R)
  @primitive{"Rel.toSet"}

public map[&T] toMap(rel[tuple[&T]] S)
  throws non_unique_domain(str msg)
  @primitive{"Ret.toMap"}

public rel[&T] mapper(rel[&T] R, &T F (&T,&T)){
  return {F(E) | &T E : R};
}

public rel[&T1, &T2] *(set[&T1] R, set[&T2] S)
  @doc{Carthesian product of two sets}
{
  return {<X, Y> | &T1 X : R, &T2 Y : S};
}

public rel[&T1, &T3] compose(rel[&T1, &T2] R,
                                 rel[&T2, &T3] S)
  @doc{Compose two relations}
{
   return {<X, Z> | <&T1 X, &T2 Y1>: R, 
                    <&T2 Y2, &T3 Z>: S, Y1 == Y2};
}

public rel[&T, &T] id(set[&T] S)
  @doc{Identity relation}
{
  return { <X, X> | &T X : S};
}

public rel[&T2, &T1] invert (rel[&T1, &T2] R)
  @doc{Inverse of relation}
{
  return { <Y, X> | <&T1 X, &T2 Y> : R };
}


public rel[&T1, &T2] complement(rel[&T1, &T2] R)
  @doc{Complement of relation}
{
  return (domain(R) * range(R)) - R;
}

public set[&T1] domain (rel[&T1,&T2] R)
  @doc{Domain of relation}
{
  return { X | <&T1 X, &T2 Y> : R };
}

public set[&T1] range (rel[&T1,&T2] R)
  @doc{Range of relation}
{
  return { Y | <&T1 X, &T2 Y> : R };
}

public set[&T]  carrier (rel[&T,&T] R)
  @doc{Carrier of relation}
{
  return domain(R) + range(R);
}

public rel[&T1,&T2] domainR (rel[&T1,&T2] R, set[&T1] S)
  @doc{Domain Restriction of a relation}
{
  return { <X, Y> | <&T1 X, &T2 Y> : R, X in S };
}

public rel[&T1,&T2] rangeR (rel[&T1,&T2] R, set[&T2] S)
  @doc{range Restriction of a relation}
{
  return { <X, Y> | <&T1 X, &T2 Y> : R, Y in S };
}

public rel[&T,&T] carrierR (rel[&T,&T] R, set[&T] S)
  @doc{Carrier restriction of a relation}
{
  return { <X, Y> | <&T X, &T Y> : R, X in S, Y in S };
}

public rel[&T1,&T2] domainX (rel[&T1,&T2] R, set[&T1] S)
  @doc{Domain exclusion of a relation}
{
  return { <X, Y> | <&T1 X, &T2 Y> : R, X notin S };
}

public rel[&T1,&T2] rangeX (rel[&T1,&T2] R, set[&T2] S)
  @doc{Range exclusion of a relation}
{
  return { <X, Y> | <&T1 X, &T2 Y> : R, Y notin S };
}

public rel[&T,&T] carrierX (rel[&T,&T] R, set[&T] S)
  @doc{Carrier exclusion of a relation}
{
  return { <X, Y> | <&T1 X, &T2 Y> : R, 
                    !(X in S), !(Y in S) };
}
