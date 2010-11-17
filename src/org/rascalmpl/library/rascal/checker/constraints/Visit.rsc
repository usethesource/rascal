@bootstrapParser
module rascal::checker::constraints::Visit

import List;
import ParseTree;
import rascal::checker::Types;
import rascal::checker::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

//
// TODO: Extract common code in each case into another function
//
// TODO: Any way to verify that types of visited sub-parts can properly be types
// of subparts of the visited expression?
//
// TODO: Put checks back in, taken out for now since they are adding useless "noise"
//
public RType checkVisit(Visit v) {
    switch(v) {
        case `visit (<Expression se>) { <Case+ cs> }` : {
            set[RType] caseTypes = { c@rtype | c <- cs };
            if (checkForFail( caseTypes + se@rtype )) return collapseFailTypes(caseTypes + se@rtype);
            RType caseLubType = lubSet(caseTypes);
            //if (subtypeOf(caseLubType, se@rtype)) 
                return se@rtype;
            //else
                //return makeFailType("Visit cases must all be subtypes of the type of the visited expression",v@\loc); 
        }
        
        case `<Strategy st> visit (<Expression se>) { <Case+ cs> }` : {
            set[RType] caseTypes = { c@rtype | c <- cs };
            if (checkForFail( caseTypes + se@rtype )) return collapseFailTypes(caseTypes + se@rtype);
            RType caseLubType = lubSet(caseTypes);
            //if (subtypeOf(caseLubType, se@rtype))
                return se@rtype;
            //else
                //return makeFailType("Visit cases must all be subtypes of the type of the visited expression",v@\loc); 
        }       
    }
}