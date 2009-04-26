module demo::GenericFeatherweightJava::TypeConstraints
import demo::GenericFeatherweightJava::GFJ;

// these are the constraint variables
data TypeOf = typeof(Expr expr) 
            | typeof(Method method) 
            | typeof(Name fieldName) 
            | typeof(Type typeId) 
            | typeof(Type var, Expr expr);  

// these are the constraints
data Constraint = eq(TypeOf a, TypeOf b) 
                | subtype(TypeOf a, TypeOf b) 
                | subtype(TypeOf a, set[TypeOf] alts);

// these are the sets of possible solutions for a constraint variable
data TypeSet = Universe
             | EmptySet
             | Single(TypeOf T)
             | Set(set[TypeOf] Ts) 
             | Subtypes(TypeSet subs)
             | Supertypes(TypeSet supers)
             | Union(TypeSet lhs, TypeSet rhs)
             | Intersection(TypeSet lhs, TypeSet rhs);
     
// these optimize the computations on solutions by applying algebraic simplifications
rule empty      Set({})                          => EmptySet;        
rule object     Subtypes(Single(typeof(Object))) => Universe;
rule subuni     Subtypes(Universe)               => Universe;
rule nestedsubs Subtypes(Subtypes(x))            => Subtypes(x);
rule supuni     Supertypes(Universe)             => Universe;
rule nestedsups Supertypes(Supertypes(x))        => Supertypes(x);
rule emptyinter Intersection(EmptySet,_)         => EmptySet;
rule uniinterl  Intersection(Universe,x)         => x;
rule uniinterr  Intersection(x,Universe)         => x;
rule uniunionl  Union(Universe,_)                => Universe;
rule uniunionr  Union(_,Universe)                => Universe;
rule emptyunil  Union(EmptySet,x)                => x;
rule emptyunir  Union(x,EmptySet)                => x;
