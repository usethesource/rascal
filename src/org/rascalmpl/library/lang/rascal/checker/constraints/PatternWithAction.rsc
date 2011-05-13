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
// Build the constraints 
// 
public ConstraintBase gatherPatternWithActionConstraints(STBuilder st, ConstraintBase cb, PatternWithAction pat) {
    switch(pat) {
        //
        // Given that e has type t1, constrain it to be something that can be bound to pattern p.
        // The type of the PWA is then a replacement type, from pat to t1.
        //
        case `<Pattern p> => <Expression e>` : {
            te = typeForLoc(cb, e@\loc);
            cb.constraints = cb.constraints + Bindable(p, te, U(), pat@\loc);
            cb = addConstraintForLoc(cb, pat@\loc, ReplacementType(p, te));
        }
        
        //
        // Given that e has type t1, constrain it to be something that can be bound to pattern p.
        // The type of the PWA is then a replacement type, from pat to t1. All when expressions
        // are of type bool.
        //
        case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
            te = typeForLoc(cb, er@\loc);
            cb.constraints = cb.constraints + Bindable(p, te, U(), pat@\loc);
            cb = addConstraintForLoc(cb, pat@\loc, ReplacementType(p, te));
            for (e <- es) cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), e@\loc); 
        }
        
        //
        // No constraints are issued on p or s for types. The type of the PWA is a replacement
        // type with just pat, no t1. Inserts inside the statement block are handled elsewhere. 
        //
        case `<Pattern p> : <Statement s>` :
            cb = addConstraintForLoc(cb, pat@\loc, NoReplacementType(p));
        
        default : throw "Unhandled case in checkPatternWithAction, <pat>";
    }
    
    return cb;    
}
