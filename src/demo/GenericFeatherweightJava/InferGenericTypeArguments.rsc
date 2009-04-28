module demo::GenericFeatherweightJava::InferGenericTypeArguments

import demo::GenericFeatherweightJava::TypeConstraints;
import demo::GenericFeatherweightJava::Types;
import demo::GenericFeatherweightJava::AbstractSyntax;
import demo::GenericFeatherweightJava::Extract;

public map[TypeOf var, TypeSet possibles] solveConstraints() {
  constraints = { c | name <- ClassTable, c <- extract(name) };
  subtypes = { <a,b> | subtype(t1, t2) <- constraints };
  supertypes = subtypes<1,0>;

  with 
    map[TypeOf var, TypeSet possibles] estimates = initialEstimates(constraints);
  solve {
     estimates = ( v:Intersection(subtypes[v],estimates[v])    | v <- estimates );
     estimates = ( v:Intersection(estimates[v], supertypes[v]) | v <- estimates );
  }

  return estimates;
}

map[TypeOf, TypeSet] initialEstimates(set[Constraint] constraints) {
   map[TypeOf, TypeSet] result = ();

   visit (constraints) { 
     case typeof(t:typeLit(x,y)) : result[t] = Single(t);
     case typeof(typeVar(x), expr) : result[t] = Universe; // Single(bound); // TODO: find bound!
      // initialize result on Universe
     case TypeOf t : result[t] = Universe; 
   };

   return result;
}