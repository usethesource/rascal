module demo::GenericFeatherweightJava::SolveConstraints

import demo::GenericFeatherweightJava::TypeConstraints;
import demo::GenericFeatherweightJava::Types;
import demo::GenericFeatherweightJava::AbstractSyntax;
import demo::GenericFeatherweightJava::Extract;
import IO;

public map[TypeOf var, TypeSet possibles] solveConstraints() {
  set[Constraint] constraints = { c | name <- ClassTable, c <- extract(name) };
  set[Type] types = { }; visit(constraints) { case Type t : types += {t}; };
  rel[Type,Type] subtypes = { <t,u> | t <- types, u <- types, subtype((), t, u) };
  supertypes = subtypes<1,0>;

  with 
    map[TypeOf var, TypeSet possibles] estimates = initialEstimates(constraints);
  solve {
     for (TypeOf v <- estimates, subtype(v1,typeof(Type t)) <- constraints) {
        estimates[v] = Intersection(estimates[v], Single(t));
        if (estimates[v] == EmptySet) throw "Buggy empty estimate for <v>";
     }
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