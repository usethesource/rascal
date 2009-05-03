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
     for (TypeOf v <- estimates, c <- constraints) {
        switch(c) {
          case subtype(v, typeof(Type t)) : {
            println("estimates for <v> are now: ", estimates[v]);
            println("\t will intersect with: ", t);
            println("\t to produce: ",  Intersection(estimates[v], Subtypes(Single(t))));
            estimates[v] = Intersection(estimates[v], Subtypes(Single(t)));
          }
          case eq(v, typeof(Type t)) : {
            estimates[v] = Single(t);
          }
        } 
     }
    
     println("--- computed estimates are:");
     printEstimates(estimates);
     println("--- resolving final subtype queries");
     estimates = innermost visit(estimates) {
        case Subtypes(Set({s, set[Type] rest})) => 
             Union(Single(s), Union(Set(subtypes[s]), Subtypes(Set({rest}))))
     };
     printEstimates(estimates);
    
  }

  return estimates;
}

public void printEstimates(map[TypeOf var, TypeSet possibles] estimates) {
  for (key <- estimates) {
    val = estimates[key];
    println("<key> = <val>");
  }
}

public map[TypeOf, TypeSet] initialEstimates(set[Constraint] constraints) {
   map[TypeOf, TypeSet] result = ();

   visit (constraints) { 
     case t:typeof(u:typeLit(x,y))   : ; // nothing
     case t:typeof(typeVar(x), expr) : result[t] = Single(Object); // Single(bound); // TODO: find bound!
      // initialize result on Universe
     case TypeOf t : result[t] = Universe; 
   };

   return result;
}