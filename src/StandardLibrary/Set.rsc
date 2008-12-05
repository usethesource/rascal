module Set

public &T arb(set[&T] S)
throws empty_set(str msg)
  @primitive{"Set.arb"}


public int size(set[&T] S)
  @primitive{"Set.size"}
  
  public list[&T] toList(set[&T] S)  
  @primitive{"List.toList"}

  public map[&T, &U] toMap(set[tuple[&T, &U]] S)
  throws non_unique_domain(str msg)
  @primitive{"Set.toMap"}
  
public str toString(set[&T] S)
  @primitive{"Set.toString"}


public rel[&T] toRel(set[&T] S)
   @primitive{"Set.toRel"}

public &T reducer(set[&T] S, &T F (&T,&T), &T unit){
  &T result = unit;
  for(&T E : S){
    result = F(result, E);
  }
  return result;
}

public set[&T] mapper(set[&T] S, &T F (&T,&T)){
  return {F(E) | &T E : S};
}

public &T min(set[&T] S)
  @doc{Minimum of a set}
{
  &T result = arb(S);
  for(&T E : S){
    result = min(result, E);
  }
  return result;
}

public &T max(set[&T] R)
  @doc{Maximum of a set}
{
  &T result = arb(R);
  for(&T E : R){
    result = max(result, E);
  }
  return result;
}

public &T sum(set[&T] S, &T zero)
  @doc{Sum elements of a Set: sum}
{
  return reducer(S, +, zero);
}

public &T multiply(set[&T] S, &T unity)
  @doc{Multiply elements of a Set}
{
  return reducer(S, *, unity);
}

public &T average(set[&T] S, &T zero)
  @doc{Average of elements of a set}
{
  return sum(S, zero)/size(R);
}

%% TODO

%% Powerset: power0
%%public set[set[&T]] power0(set[&T] R)
%% throw unimplemented("power0")

%% Powerset: power1
%%public set[set[&T]] power1(set[&T] R)
%%  throw unimplemented("power0")

%% --- Maps --------------------------------------------------

public bool ==(map[&T, &U] M1, map[&