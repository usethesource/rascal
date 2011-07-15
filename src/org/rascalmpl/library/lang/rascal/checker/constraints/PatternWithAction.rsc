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
module lang::rascal::checker::constraints::PatternWithAction

import List;
import ParseTree;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::syntax::RascalRascal;

//
// TODO: For consistency, factor the cases out into functions.
//
public ConstraintBase gatherPatternWithActionConstraints(STBuilder st, ConstraintBase cb, PatternWithAction pat) {
    switch(pat) {
        //
        //    p : tp        e : te      te <: tp
        // ------------------------------------------
        //               p => e : tp
        //
        case (PatternWithAction) `<Pattern p> => <Expression e>` : {
            te = typeForLoc(cb, e@\loc);
            tp = typeForLoc(cb, p@\loc);
            cb.constraints = cb.constraints + SubtypeOf(te, tp, U(), pat@\loc);
            cb = addConstraintForLoc(cb, pat@\loc, tp);
        }
        
        //
        //    p : tp        e : te      esi : bool | esi <- es         te <: tp
        // --------------------------------------------------------------------------
        //               p => e when es : tp
        //
        case (PatternWithAction) `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
            te = typeForLoc(cb, e@\loc);
            tp = typeForLoc(cb, p@\loc);
            cb.constraints += SubtypeOf(te, tp, U(), pat@\loc);
            for (esi <- es) cb.constraints += ConstrainType(typeForLoc(cb, esi@\loc), makeBoolType(), pat@\loc); 
            cb = addConstraintForLoc(cb, pat@\loc, tp);            
        }
        
        //
        //    p : tp        
        // ----------------
        //   p : s : void
        //
        case (PatternWithAction) `<Pattern p> : <Statement s>` : {
            cb = addConstraintForLoc(cb, pat@\loc, makeVoidType());
        }    
        
        default : throw "Unhandled case in checkPatternWithAction, <pat>";
    }
    
    return cb;    
}
