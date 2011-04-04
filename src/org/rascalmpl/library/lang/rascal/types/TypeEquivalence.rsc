@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::types::TypeEquivalence

import lang::rascal::types::Types;
import lang::rascal::types::TypeExceptions;
import lang::rascal::types::SubTypes;

//
// Returns true when two types are comparable. Two types are comparable if they are
// the same type, or if one is a subtype of the other. The equality check is built
// in to the subtype check, if t1 == t2 then t1 <: t2. This matches the definition
// of comparable given in PDB.
//
public bool comparable(RType t1, RType t2) {
    return subtypeOf(t1,t2) || subtypeOf(t2,t1);
}

//
// Check to see if two types are equivalent. Two types are equivalent if they are both
// subtypes of the other. This matches the definition of equivalent given in PDB.
//
public bool equivalent(RType t1, RType t2) {
    return subtypeOf(t1,t2) && subtypeOf(t2,t1);
}

//
// TODO: Add LOTS of tests!
//
