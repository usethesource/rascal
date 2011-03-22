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
