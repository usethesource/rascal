@bootstrapParser
module rascal::checker::constraints::PatternWithAction

import List;
import ParseTree;
import rascal::types::Types;
import rascal::scoping::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

//
// Generate the constraints for the PatternWithAction productions. With the first two productions, the
// expression after the => can be of an arbitrary type, but this type 1) must be compatible with the
// pattern p before the =>, and 2) must be compatible with the tree node, reachable within the visited
// structure (in the case of a visit), or with the switch expression (in the case of a switch/case),
// since it will replace it. We need to do this double check to avoid subtle problems. An example of
// this is if one visits an ADT with am int field. A value pattern would match this field, and a num
// would be a subtype of this pattern, but if we replace the int with a num we voilate the ADT type.
//
// TODO: Add typing rules!
// 
public ConstraintBase gatherPatternWithActionConstraints(STBuilder st, ConstraintBase cs, PatternWithAction pat) {
    switch(pat) {
        case `<Pattern p> => <Expression e>` : {
            <cs, t1> = makeFreshType(cs);
            Constraint c1 = TreeIsType(e, e@\loc, t1);
            Constraint c2 = TreeIsType(pat, pat@\loc, ReplacementType(p,t1));
            cs.constraints = cs.constraints + { c1, c2 };
        }
        
        case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
            <cs, t1> = makeFreshType(cs);
            Constraint c1 = TreeIsType(e, e@\loc, t1);
            Constraint c2 = TreeIsType(pat, pat@\loc, ReplacementType(p,t1));
            cs.constraints = cs.constraints + { c1, c2 };
            for (e <- es) cs.constraints = cs.constraints + TreeIsType(e, e@\loc, makeBoolType());
        }
        
        case `<Pattern p> : <Statement s>` : {
            <cs, t1> = makeFreshType(cs);
            Constraint c1 = TreeIsType(s, s@\loc, makeStatementType(t1));
            Constraint c2 = TreeIsType(pat, pat@\loc, NoReplacementType(p,t1));
            cs.constraints = cs.constraints + { c1, c2 };
        }
        
        default : throw "Unhandled case in checkPatternWithAction, <pat>";
    }
    
    return cs;    
}