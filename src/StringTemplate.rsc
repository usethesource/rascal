@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::checker::constraints::StringTemplate

import List;
import ParseTree;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::syntax::RascalRascal;

//
// Gather constraints for string templates, used inside string interpolation
// expressions and/or patterns.
//
// TODO: Need to see how to best use these inside patterns. We don't currently
// gather constraints on patterns, but instead bind them top-down once we have
// a candidate subject type or we have enough information to identify the
// constructor at the top.
//
public ConstraintBase gatherStringTemplateConstraints(STBuilder stBuilder, ConstraintBase cb, StringTemplate s) {
    // For all the different types of string templates, we only need to constrain the overall type
    // and the type of any generators, conditions, etc. The body is made up of either string
    // templates or expressions, and these are either constrained elsewhere (string templates)
    // or can be any type, meaning they are unconstrained, so we leave the type to be whatever
    // it is determined as from expression constraint generation.
    switch(s) {
        case `for (<{Expression ","}+ gens>) { <Statement* pre> <StringMiddle body> <Statement* post> }` :
            for (gen <- gens) 
                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, gen@\loc), makeBoolType(), s@\loc);

        case `if (<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post> }` :
            for (cond <- conds)
                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, cond@\loc), makeBoolType(), s@\loc);

        case `if (<{Expression ","}+ conds>) { <Statement* preThen> <StringMiddle bodyThen> <Statement* postThen> } else { <Statement* preElse> <StringMiddle bodyElse> <Statement* postElse> }` :
            for (cond <- conds)
                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, cond@\loc), makeBoolType(), s@\loc);

        case `while (<Expression cond>) { <Statement* pre> <StringMiddle body> <Statement* post> }` :
            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, cond@\loc), makeBoolType(), s@\loc);

        case `do { <Statement* pre> <StringMiddle body> <Statement* post> } while (<Expression cond>)` :
            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, cond@\loc), makeBoolType(), s@\loc);
        
        default : throw "Unexpected string template syntax at location <s@\loc>, no match"; 
    }

    return addConstraintForLoc(cb, s@\loc, makeStrType());
}
