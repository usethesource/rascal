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
module lang::rascal::checker::constraints::Visit

import List;
import ParseTree;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::syntax::RascalRascal;

//
// Gather constraints over the visit (statement/expression).
//
public ConstraintBase gatherVisitConstraints(STBuilder stBuilder, ConstraintBase cb, Visit v) {
    if (`visit (<Expression se>) { <Case+ cs> }` := v || `<Strategy st> visit (<Expression se>) { <Case+ cs> }` := v) {
        // Step 1: The expression is of arbitrary type, look it up
        te = typeForLoc(cb, se@\loc);
        
        // Step 2: The result of the visit is of the same type as the visited expression
        cb = addConstraintForLoc(cb, v@\loc, te);
        
        // Step 3: Each case should be a type bindable to something reachable in the visited expression.
        for (c <- cs) cb.constraints = cb.constraints + CaseIsReachable(typeForLoc(cb, c@\loc), te, U(), c@\loc);
    }
    
    return cb;
}
