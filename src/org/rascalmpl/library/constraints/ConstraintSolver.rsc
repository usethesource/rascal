@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module constraints::ConstraintSolver

import constraints::Constraint;

public Constraints solveConstraints(Constraints constraints) {
    // First, try to unify the constraints
    
    // Second, split any that are composites (for instance, 
    // f(t1,t2) = f(t3,t4) splits to t1 = t3, t2 = t4).
    
    // Finally, no more steps can be applied, so return the current
    // set of constraints.
    return constraints;
}

public bool areConstraintsSolved(Constraints constraints) {
    // Check to see if we have any failure conditions, for instance,
    // if we have t1 = t2, t1 = t3, and t2 =/= t3
    
    return true;
}
