module demo::GenericFeatherweightJava::InferGenericTypeArguments

import demo::GenericFeatherweightJava::TypeConstraints;
import demo::GenericFeatherweightJava::Types;
import demo::GenericFeatherweightJava::AbstractSyntax;
import demo::GenericFeatherweightJava::Extract;
import IO;

public map[TypeOf var, TypeSet possibles] solveConstraints() {
  set[Constraint] constraints = { c | name <- ClassTable, c <- extract(name) };
  set[Type] types = { }; visit(constraints) { case Type t : types += {t}; };
  rel[Type,Type] subtypes = { <t,u> | t <- types, u <- subtypes, subtype((), t, u) };
  supertypes = subtypes<1,0>;

  with 
    map[TypeOf var, TypeSet possibles] estimates = initialEstimates(constraints);
  solve {
     estimates = ( v:Intersection(Set(subtypes[v]),estimates[v])    | v <- estimates );
     // estimates = ( v:Intersection(estimates[v], Set(supertypes[v])) | v <- estimates );
     println(estimates);
  }

  return estimates;
}

public map[TypeOf, TypeSet] initialEstimates(set[Constraint] constraints) {
   map[TypeOf, TypeSet] result = ();

   visit (constraints) { 
     case t:typeof(u:typeLit(x,y))   : result[t] = Single(u);
     case t:typeof(typeVar(x), expr) : result[t] = Universe; // Single(bound); // TODO: find bound!
      // initialize result on Universe
     case TypeOf t : result[t] = Universe; 
   };

   return result;
}