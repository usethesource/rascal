module lang::java::m3::TypeSymbol

extend analysis::m3::TypeSymbol;

data Bound 
  = \super(list[TypeSymbol] bound)
  | \extends(list[TypeSymbol] bound)
  | \unbounded()
  ;
  
data TypeSymbol 
  = \class(loc decl, list[TypeSymbol] typeParameters)
  | \interface(loc decl, list[TypeSymbol] typeParameters)
  | \enum(loc decl)
  | \method(loc decl, list[TypeSymbol] typeParameters, TypeSymbol returnType, list[TypeSymbol] parameters)
  | \constructor(loc decl, list[TypeSymbol] parameters)
  | \typeParameter(loc decl, Bound upperbound) 
  | \typeArgument(loc decl)
  | \wildcard(Bound bound)
  | \capture(Bound bound, TypeSymbol wildcard)
  | \intersection(list[TypeSymbol] types)
  | \union(list[TypeSymbol] types)
  | \object()
  | \int()
  | \float()
  | \double()
  | \short()
  | \boolean()
  | \char()
  | \byte()
  | \long()
  | \void()
  | \null()
  | \array(TypeSymbol component, int dimension)
  | \typeVariable(loc decl)
  | \module(loc decl)
  ;  
  
default bool subtype(TypeSymbol s, TypeSymbol t) = s == t;

default TypeSymbol lub(TypeSymbol s, TypeSymbol t) = s == t ? s : object();  
