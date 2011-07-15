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
// The type of a case is either the type of the associated PatternWithAction
// or, in the case of default, void.
//
public ConstraintBase gatherCaseConstraints(STBuilder st, ConstraintBase cb, Case c) {
    switch(c) {
        case `case <PatternWithAction p>` :
            cb = addConstraintForLoc(cb, c@\loc, typeForLoc(cb, p@\loc));

        case `default : <Statement b>` :
            cb = addConstraintForLoc(cb, c@\loc, makeVoidType());
        
        default :
            throw "Unexpected case syntax <c>";
    }
    
    return cb;
}
