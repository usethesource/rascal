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
             | Root
             | Single(TypeOf T)
             | Set(set[TypeOf] Ts) 
             | Subtypes(TypeSet subs)
             | Supertypes(TypeSet supers)
             | Union(TypeSet lhs, TypeSet rhs)
             | Intersection(TypeSet lhs, TypeSet rhs);
     
// these optimize the computations on solutions by applying algebraic simplifications
rule root       Set({typeof(Object)})            => Root;
rule empty      Set({})                          => EmptySet;        
rule single     Single(TypeOf T)                 => Set({T});
rule rootsub    Subtypes(Root)                   => Universe;
rule rootsup    Supertypes(Root)                 => EmptySet;
rule subuni     Subtypes(Universe)               => Universe;
rule nestedsubs Subtypes(Subtypes(TypeSet x))    => Subtypes(x);
rule supuni     Supertypes(Universe)             => Universe;
rule nestedsups Supertypes(Supertypes(TypeSet x))=> Supertypes(x);
rule emptyinter Intersection(EmptySet,TypeSet _) => EmptySet;
rule uniinterl  Intersection(Universe,TypeSet x) => x;
rule uniinterr  Intersection(TypeSet x,Universe) => x;
rule uniunionl  Union(Universe,TypeSet _)        => Universe;
rule uniunionr  Union(TypeSet _,Universe)        => Universe;
rule emptyunil  Union(EmptySet, TypeSet x)       => x;
rule emptyunir  Union(TypeSet x,EmptySet)        => x;

// final mappings to actual union and intersection
rule realunion Union(Set(TypeSet s1),Set(TypeSet s2))           => Set(s1 + s2);
rule realinter Intersection(Set(TypeSet s1),Set(TypeSet s2))    => Set(s1 & s2);

