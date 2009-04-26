module demo::GenericFeatherweightJava::InferGenericTypeArguments

import demo::GenericFeatherweightJava::TypeConstraints;
import demo::GenericFeatherweightJava::Types;
import demo::GenericFeatherweightJava::GFJ;
import demo::GenericFeatherweightJava::Extract;

public map[TypeOf var, TypeSet possibles] solveConstraints() {
  constraints = { c | name <- ClassTable, c <- extract(name) };
  
  with 
    map[TypeOf var, TypeSet possibles] estimates = initialEstimates(constraints);
  solve {
     estimates = ( v:ps | subtype(t1,t2) <- estimates, 
                   // TODO whatever is needed here 
                   xxxxx                            
                 );
  }

  return estimates;
}

map[TypeOf, TypeSet] initialEstimates(set[Constraint] constraints) {
 return ();
}