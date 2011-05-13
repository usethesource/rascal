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
module lang::rascal::checker::constraints::Case

import List;
import ParseTree;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::syntax::RascalRascal;

//
// Gather constraints over individual cases. The type of the pattern with
// action should be either a replacement type or a no replacement type. We
// link this in here as the overall type of the case. This can then be used
// to check the actual type of a switch or a visit this case is included
// within.
//
public ConstraintBase gatherCaseConstraints(STBuilder st, ConstraintBase cb, Case c) {
    switch(c) {
        case `case <PatternWithAction p>` :
            cb = addConstraintForLoc(cb, c@\loc, CaseType(typeForLoc(cb, p@\loc)));
            
        case `default : <Statement b>` :
            cb = addConstraintForLoc(cb, c@\loc, DefaultCaseType());
        
        default :
            throw "Unexpected case syntax <c>";
    }
    
    return cb;
}
