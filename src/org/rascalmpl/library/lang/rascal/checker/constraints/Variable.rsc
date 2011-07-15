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
module lang::rascal::checker::constraints::Variable

import ParseTree;
import IO;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::checker::Annotations;
import lang::rascal::checker::TreeUtils;
import lang::rascal::syntax::RascalRascal;

//
// NOTE: Variable declarations are either part of the toplevel variable declaration or the
// local variable declaration statement, both of which are typed declarations. Untyped
// declarations, of the form name = expression, are actually assignment statements, with
// name an arbitrary assignable (tuples, fields, etc), and are handled in the statement
// logic, not here.
// 
public ConstraintBase gatherVariableConstraints(STBuilder st, ConstraintBase cb, Variable v) {
    if ((Variable) `<Name n>` := v || (Variable)`<Name n> = <Expression _>` := v) {
        // We don't know the defined type of n, so give it a fresh type of tv
        // and indicate that tv is defined by whatever is in the symbol table
        // for the name at this location.
        <cb, tv> = makeFreshType(cb);
        cb = addConstraintForLoc(cb, n@\loc, tv);
        if (n@\loc in st.itemUses<0>)
            cb.constraints = cb.constraints + DefinedBy(tv,st.itemUses[n@\loc],n@\loc);
        else
            cb.constraints = cb.constraints + ConstrainType(tv, makeFailType("No definition for this variable found",n@\loc), n@\loc);

        if ((Variable) `<Name _> = <Expression e>` := v) {
            // Ensure that the type of the expression e is assignable to n; this should then
            // yield result type tr, which is not necessarily the same as te or tv.
            te = typeForLoc(cb, e@\loc);
            <cb, tr> = makeFreshType(cb);
            cb.constraints = cb.constraints + Assignable(tv, te, tr, v@\loc);
        }

        return cb;        
    }
    
    throw "Invalid variable syntax <v>";
}
