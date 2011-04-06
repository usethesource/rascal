@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
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
data TypeSet = Universe()
             | EmptySet()
             | Root()
             | Single(Type T)
             | Set(set[Type] Ts) 
             | Subtypes(TypeSet subs)
             | Supertypes(TypeSet supers)
             | Union(set[TypeSet] args)
             | Intersection(set[TypeSet] args);
     
// these optimize the computations on solutions by applying algebraic simplifications
rule root       Set({Object})                    => Root();
rule empty      Set({})                          => EmptySet();        
rule single     Single(Type   T)                 => Set({T});
rule rootsub    Subtypes(Root)                   => Universe();
rule emptysub   Subtypes(EmptySet)               => EmptySet();
rule rootsup    Supertypes(Root)                 => EmptySet();
rule subuni     Subtypes(Universe)               => Universe();
rule nestedsubs Subtypes(Subtypes(TypeSet x))    => Subtypes(x);
rule supuni     Supertypes(Universe)             => Universe();
rule nestedsups Supertypes(Supertypes(TypeSet x))=> Supertypes(x);

rule interdone  Intersection({TypeSet last})     => last;
rule subinterl  Intersection({Subtypes(TypeSet x), x, set[TypeSet] rest}) => Intersection(Subtypes(x), rest);
rule emptyinter Intersection({EmptySet, set[TypeSet] _}) => EmptySet;
rule uniinterl  Intersection({Universe,set[TypeSet] x}) => Intersection({x});
rule intermerge Intersection({Intersection({set[TypeSet] x}), set[TypeSet] y}) =>
                Intersection({x, y});

rule uniondone  Union({TypeSet last})            => last;
rule uniunionl  Union({Universe,set[TypeSet] _}) => Universe();
rule emptyunil  Union({EmptySet,set[TypeSet] x}) => Union({x});
rule unionmerge Union({Union({set[TypeSet] x}), set[TypeSet] y}) =>
                Union({x, y});

// final implementations
rule realinter  Intersection({Set(set[Type] t1), Set(set[Type] t2), set[TypeSet] rest}) => 
                Intersection({Set(t1 & t2), rest});
rule realunion         Union({Set(set[Type] t1), Set(set[Type] t2), set[TypeSet] rest}) => 
                       Union({Set(t1 + t2), rest});

