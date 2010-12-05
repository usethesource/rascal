@bootstrapParser
module rascal::checker::constraints::StringTemplate

import List;
import ParseTree;
import rascal::types::Types;
import rascal::scoping::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

//
// Gather constraints for string templates, used inside string interpolation
// expressions and/or patterns.
//
// TODO: Need to see how to best use these inside patterns. We don't currently
// gather constraints on patterns, but instead bind them top-down once we have
// a candidate subject type or we have enough information to identify the
// constructor at the top.
//
public ConstraintBase gatherStringTemplateConstraints(SymbolTable symbolTable, ConstraintBase constraintBase, StringTemplate s) {
    // For all the different types of string templates, we only need to constrain the overall type
    // and the type of any generators, conditions, etc. The body is made up of either string
    // templates or expressions, and these are either constrained elsewhere (string templates)
    // or can be any type, meaning they are unconstrained, so we leave the type to be whatever
    // it is determined as from expression constraint generation.
    switch(s) {
        case `for (<{Expression ","}+ gens>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            for (gen <- gens) 
                constraintBase.constraints = constraintBase.constraints + TreeIsType(gen,gen@\loc,makeBoolType());
            constraintBase.constraints = constraintBase.constraints + TreeIsType(s,s@\loc,makeStrType());
        }

        case `if (<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            for (cond <- conds)
                constraintBase.constraints = constraintBase.constraints + TreeIsType(conds,cond@\loc,makeBoolType());
            constraintBase.constraints = constraintBase.constraints + TreeIsType(s,s@\loc,makeStrType());
        }

        case `if (<{Expression ","}+ conds>) { <Statement* preThen> <StringMiddle bodyThen> <Statement* postThen> } else { <Statement* preElse> <StringMiddle bodyElse> <Statement* postElse> }` : {
            for (cond <- conds)
                constraintBase.constraints = constraintBase.constraints + TreeIsType(conds,cond@\loc,makeBoolType());
            constraintBase.constraints = constraintBase.constraints + TreeIsType(s,s@\loc,makeStrType());
        }

        case `while (<Expression cond>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            constraintBase.constraints = constraintBase.constraints + TreeIsType(cond,cond@\loc,makeBoolType());
            constraintBase.constraints = constraintBase.constraints + TreeIsType(s,s@\loc,makeStrType());
        }

        case `do { <Statement* pre> <StringMiddle body> <Statement* post> } while (<Expression cond>)` : {
            constraintBase.constraints = constraintBase.constraints + TreeIsType(cond,cond@\loc,makeBoolType());
            constraintBase.constraints = constraintBase.constraints + TreeIsType(s,s@\loc,makeStrType());
        }
        
        default : throw "Unexpected string template syntax at location <s@\loc>, no match"; 
    }

    return constraintBase;
}