@bootstrapParser
module lang::rascal::checker::constraints::PatternWithAction

import List;
import ParseTree;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::syntax::RascalRascal;

//
// Build the constraints 
// 
public ConstraintBase gatherPatternWithActionConstraints(STBuilder st, ConstraintBase cs, PatternWithAction pat) {
    switch(pat) {
        //
        // Given that e has type t1, constrain it to be something that can be bound to pattern p.
        // The type of the PWA is then a replacement type, from pat to t1.
        //
        case `<Pattern p> => <Expression e>` : {
            <cs, t1> = makeFreshType(cs);
            Constraint c1 = TreeIsType(e, e@\loc, t1);
            Constraint c2 = Bindable(p, t1, pat@\loc);
            Constraint c3 = TreeIsType(pat, ReplacementType(p,t1), pat@\loc);
            cs.constraints = cs.constraints + { c1, c2, c3 };
        }
        
        //
        // Given that e has type t1, constrain it to be something that can be bound to pattern p.
        // The type of the PWA is then a replacement type, from pat to t1. All when expressions
        // are of type bool.
        //
        case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
            <cs, t1> = makeFreshType(cs);
            Constraint c1 = TreeIsType(er, er@\loc, t1);
            Constraint c2 = Bindable(p, t1, pat@\loc);
            Constraint c3 = TreeIsType(pat, ReplacementType(p,t1), pat@\loc);
            cs.constraints = cs.constraints + { c1, c2, c3 };
            for (e <- es) cs.constraints = cs.constraints + TreeIsType(e, e@\loc, makeBoolType());
        }
        
        //
        // This is not a replacement 
        //
        case `<Pattern p> : <Statement s>` : {
            cs = cs; // no-op, keeps us from triggering the default...
        }
        
        default : throw "Unhandled case in checkPatternWithAction, <pat>";
    }
    
    return cs;    
}
