@bootstrapParser
module rascal::checker::constraints::Case

import List;
import ParseTree;
import rascal::checker::Types;
import rascal::checker::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

//
// Check individual cases
//
public RType checkCase(Case c) {
    switch(c) {
        case `case <PatternWithAction p>` : {
            
            // If insert is used anywhere in this case pattern, find the type being inserted and
            // check to see if it is correct.
            // TODO: This will only check the first insert. Need to modify logic to handle all
            // insert statements that are in this visit, but NOT in a nested visit. It may be
            // easiest to mark visit boundaries during the symbol table construction, since that
            // is done in a top-down manner.
            RType caseType = getCasePatternType(c);
            set[RType] failures = { };
            top-down-break visit(p) {
                case (Expression) `<Label l> <Visit v>` : 0; // no-op
                
                case (Statement) `<Label l> <Visit v>` : 0; // no-op
                
                case Statement ins : `insert <DataTarget dt> <Statement s>` : {
                    RType stmtType = getInternalStatementType(s@rtype);
                    if (! subtypeOf(stmtType, caseType)) {
                        failures += makeFailType("Type of insert, <prettyPrintType(stmtType)>, does not match type of case, <prettyPrintType(caseType)>", s@\loc);
                    }
                } 
            }
            RType retType = (size(failures) == 0) ? p@rtype : collapseFailTypes(failures);
            return retType;
        }
        
        case `default : <Statement b>` : {
            return getInternalStatementType(b@rtype);
        }
    }
}