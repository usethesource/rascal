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
module lang::rascal::checker::constraints::Catch

import List;
import ParseTree;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::syntax::RascalRascal;

//
// Gather constraints for catch clauses in exception handlers.
//
public ConstraintBase gatherCatchConstraints(STBuilder stBuilder, ConstraintBase constraintBase, Catch c) {
    switch(c) {
        // For a catch with a body but no binding to the thrown value, we don't need to generate any
        // constraints. We do provide an empty body so we don't trigger the default below.
        case `catch : <Statement b>` :
            ; 
        
        // For a catch that does bind the throw value, the only constraint needed is to ensure that
        // the pattern provided matches RuntimeException, since we cannot catch arbitrary values.
        case `catch <Pattern p> : <Statement b>` :
            constraintBase.constraints = constraintBase.constraints + BindsRuntimeException(p,p@\loc);    
        
        default: throw "Unhandled catch syntax in gatherCatchConstraints: <c>";
    }
    
    return constraintBase;
}

