@bootstrapParser
module rascal::checker::constraints::Visit

import List;
import ParseTree;
import rascal::types::Types;
import rascal::scoping::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

//
// Gather constraints over the visit (statement/expression).
//
// TODO: Add type rule!
//
public ConstraintBase gatherVisitConstraints(SymbolTable symbolTable, ConstraintBase constraintBase, Visit v) {
    if (`visit (<Expression se>) { <Case+ cs> }` := v || `<Strategy st> visit (<Expression se>) { <Case+ cs> }` := v) {
        <constraintBase, t1> = makeFreshType(constraintBase);
        
        // Step 1: The expression is of arbitrary type
        constraintBase.constraints = constraintBase.constraints + TreeIsType(se,se@\loc,t1);
        
        // Step 2: The result of the visit is of the same type as the visited expression
        constraintBase.constraints = constraintBase.constraints + TreeIsType(v,v@\loc,t1);
        
        // Step 3: Each case should be a case type, like in a switch, and the case pattern
        // should indicate a pattern which can be bound to something reachable from e.
        // TODO: This check may not be complete! We need to verify that this actually
        // always works.
        for (c <- cs) {
            <constraintBase, t2> = makeFreshType(constraintBase);
            Constraint c1 = TreeIsType(c,c@\loc,t2);
            Constraint c2 = CaseIsReachable(t2,t1,c@\loc);
            constraintBase.constraints = constraintBase.constraints + { c1, c2 };
        }
    }
    
    return constraintBase;
}