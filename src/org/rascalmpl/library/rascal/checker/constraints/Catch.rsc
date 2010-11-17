@bootstrapParser
module rascal::checker::constraints::Catch

import List;
import ParseTree;
import rascal::checker::Types;
import rascal::checker::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

//
// Check catch clauses in exception handlers
//
public RType checkCatch(Catch c) {
    switch(c) {
        case `catch : <Statement b>` : {
            return b@rtype;
        }
        
        // TODO: Pull out into own function for consistency
        case `catch <Pattern p> : <Statement b>` : {
            
            if (checkForFail({ p@rtype, getInternalStatementType(b@rtype) }))
                return makeStatementType(collapseFailTypes({ p@rtype, getInternalStatementType(b@rtype) }));
            else {
                RType boundType = bindInferredTypesToPattern(p@rtype, p);
                if (isFailType(boundType)) return makeStatementType(boundType);
                return b@rtype;
            }
        }
    }
}

