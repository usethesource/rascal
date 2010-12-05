@bootstrapParser
module rascal::checker::constraints::Catch

import List;
import ParseTree;
import rascal::types::Types;
import rascal::scoping::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

//
// Gather constraints for catch clauses in exception handlers.
//
public ConstraintBase gatherCatchConstraints(SymbolTable symbolTable, ConstraintBase constraintBase, Catch c) {
    switch(c) {
        // For a catch with a body but no binding to the thrown value, we don't need to generate any
        // constraints. We do provide an empty body so we don't trigger the default below.
        case `catch : <Statement b>` :
            ; 
        
        // For a catch that does bind the throw value, the only constraint needed is to ensure that
        // the pattern provided matches RuntimeException, since we cannot catch arbitrary values.
        case `catch <Pattern p> : <Statement b>` :
            constraintBase.constraints = constraintBase.constraints + BindsRuntimeException(p,p@\loc);    
        
        default: throw "Unhandled catch syntax in gatherCatchConstraints: <c>";
    }
    
    return constraintBase;
}

