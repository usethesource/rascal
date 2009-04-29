module demo::GenericFeatherweightJava::TypeConstraints

import demo::GenericFeatherweightJava::AbstractSyntax;
import demo::GenericFeatherweightJava::Types;

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
             | Single(Type T)
             | Set(set[Type] Ts) 
             | Subtypes(TypeSet subs)
             | Supertypes(TypeSet supers)
             | Union(TypeSet lhs, TypeSet rhs)
             | Intersection(TypeSet lhs, TypeSet rhs);
     
// these optimize the computations on solutions by applying algebraic simplifications
rule root       Set({Object})                    => Root;
rule empty      Set({})                          => EmptySet;        
rule single     Single(Type   T)                 => Set({T});
rule rootsub    Subtypes(Root)                   => Universe;
rule rootsup    Supertypes(Root)                 => EmptySet;
rule subuni     Subtypes(Universe)               => Universe;
rule nestedsubs Subtypes(Subtypes(TypeSet x))    => Subtypes(x);
rule supuni     Supertypes(Universe)             => Universe;
rule nestedsups Supertypes(Supertypes(TypeSet x))=> Supertypes(x);
rule emptyinter Intersection(EmptySet, TypeSet _)=> EmptySet;
rule emptyinter Intersection(TypeSet _, EmptySet)=> EmptySet;
rule uniinterl  Intersection(Universe,TypeSet x) => x;
rule uniinterr  Intersection(TypeSet x,Universe) => x;
rule uniunionl  Union(Universe,TypeSet _)        => Universe;
rule uniunionr  Union(TypeSet _,Universe)        => Universe;
rule emptyunil  Union(EmptySet, TypeSet x)       => x;
rule emptyunir  Union(TypeSet x,EmptySet)        => x;

// final implementations
rule realinter  Intersection(Set(set[Type] t1), Set(set[Type] t2)) => Set(t1 & t2);
rule realunion         Union(Set(set[Type] t1), Set(set[Type] t2)) => Set(t1 + t2);

