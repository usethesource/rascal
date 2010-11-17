@bootstrapParser
module rascal::checker::constraints::PatternWithAction

import List;
import ParseTree;
import rascal::checker::Types;
import rascal::checker::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

public RType checkPatternWithAction(PatternWithAction pat) {
    switch(pat) {
        case `<Pattern p> => <Expression e>` : {
            if (checkForFail( { p@rtype, e@rtype } )) return collapseFailTypes( { p@rtype, e@rtype } );
            RType boundType = bindInferredTypesToPattern(p@rtype, p);
            if (isFailType(boundType)) return boundType;
            if (!subtypeOf(e@rtype,boundType)) return makeFailType("Type of pattern, <prettyPrintType(boundType)>, and action expression, <prettyPrintType(e@rtype)>, must be identical.", pat@\loc); 
            return p@rtype; 
        }
        
        case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
            set[RType] whenTypes = { e@rtype | e <- es };
            if (checkForFail( whenTypes + p@rtype + er@rtype )) return collapseFailTypes( whenTypes + p@rtype + er@rtype );
            RType boundType = bindInferredTypesToPattern(p@rtype, p);
            if (isFailType(boundType)) return boundType;
            if (!subtypeOf(er@rtype,boundType)) return makeFailType("Type of pattern, <prettyPrintType(boundType)>, and action expression, <prettyPrintType(er@rtype)>, must be comparable.", pat@\loc); 
            return p@rtype; 
        }
        
        case `<Pattern p> : <Statement s>` : {
            RType stmtType = getInternalStatementType(s@rtype);
            if (checkForFail( { p@rtype, stmtType })) return collapseFailTypes( { p@rtype, stmtType });
            RType boundType = bindInferredTypesToPattern(p@rtype, p);
            if (isFailType(boundType)) return boundType;
            return stmtType;
        }
    }
    
    throw "Unhandled case in checkPatternWithAction, <pat>";    
}