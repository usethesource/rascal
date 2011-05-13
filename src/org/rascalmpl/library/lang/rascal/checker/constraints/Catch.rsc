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
// TODO: Should accurately calculate the pattern type (as accurately
// as possible statically, at least)
//
public ConstraintBase gatherCatchConstraints(STBuilder stBuilder, ConstraintBase cb, Catch c) {
    if (`catch <Pattern p> : <Statement b>` := c)
        cb.constraints = cb.constraints + BindsRuntimeException(p, U(), p@\loc);
    return cb;
}

