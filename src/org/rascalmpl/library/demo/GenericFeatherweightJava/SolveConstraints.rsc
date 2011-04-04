@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module demo::GenericFeatherweightJava::SolveConstraints

import demo::GenericFeatherweightJava::TypeConstraints;
import demo::GenericFeatherweightJava::Types;
import demo::GenericFeatherweightJava::AbstractSyntax;
import demo::GenericFeatherweightJava::Extract;
import IO;

public map[TypeOf var, TypeSet possibles] solveConstraints() {
  set[Constraint] constraints = { c | name <- ClassTable, c <- extract(name) };
  map[TypeOf var, TypeSet possibles] estimates = initialEstimates(constraints);
  
  solve (estimates) {
     for (TypeOf v <- estimates, subtype(v, typeof(Type t)) <- constraints)
       estimates[v] = Intersection({estimates[v], Subtypes(Single(t))});
  }

  set[Type] types = { }; visit(constraints) { case Type t : types += {t}; };
  rel[Type,Type] subtypes = { <u,t> | t <- types, u <- types, subtype((), t, u) };
  
  println("--- computed estimates are:");
  printEstimates(estimates);
  println("--- resolving final subtype queries");
  estimates = innermost visit(estimates) {
     case Subtypes(Set({s, set[Type] rest})) => 
          Union({Single(s), Set(subtypes[s]), Subtypes(Set({rest}))})
  };
  printEstimates(estimates);
    
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
     case eq(TypeOf v, typeof(Type t)) : result[v] = Single(t);
     case t:typeof(typeVar(x), expr)   : result[t] = Single(Object); // Single(bound); // TODO: find bound!
     case t:typeof(u:typeLit(x,y))     : ; // skip 
     case TypeOf t                     : result[t] = Universe; // default 
   };

   return result;
}
