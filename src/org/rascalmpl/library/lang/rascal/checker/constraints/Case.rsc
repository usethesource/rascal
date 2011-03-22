@bootstrapParser
module lang::rascal::checker::constraints::Case

import List;
import ParseTree;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::syntax::RascalRascal;

//
// Gather constraints over individual cases.
//
// TODO: Add type rules!
//
public ConstraintBase gatherCaseConstraints(STBuilder st, ConstraintBase cs, Case c) {
    switch(c) {
        case `case <PatternWithAction p>` : {
            <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
            Constraint c1 = TreeIsType(p,p@\loc,t1);
            Constraint c2 = PWAResultType(t1,t2,p@\loc);
            Constraint c3 = TreeIsType(c,c@\loc,CaseType(t1,t2));
            cs.constraints = cs.constraints + { c1, c2, c3 };
        }
            
        case `default : <Statement b>` : {
            <cs,t1> = makeFreshType(cs);
            Constraint c1 = TreeIsType(b,b@\loc,makeStatementType(t1));
            Constraint c2 = TreeIsType(c,c@\loc,DefaultCaseType(t1));
            cs.constraints = cs.constraints + { c1, c2 };
        }
        
        default :
            throw "Unexpected case syntax <c>";
    }
    
    return cs;
}
