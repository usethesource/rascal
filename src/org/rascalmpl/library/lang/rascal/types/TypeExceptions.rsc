@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::types::TypeExceptions

import Exception;
import lang::rascal::types::AbstractType;

data RuntimeException =
      UnexpectedRType(Symbol t1)
    | UnexpectedRTypes(Symbol t1, Symbol t2)
    | UnimplementedRType(Symbol t1)
    | CannotCalculateBindings(Symbol t1, Symbol t2, loc l)
    | invalidMatch(str varName, Symbol typeLub, Symbol typeBound)
    | invalidMatch(Symbol targetType, Symbol sourceType)
    ;
