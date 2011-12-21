@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::types::TypeExceptions

import Exception;
import lang::rascal::types::Types;

data RuntimeException =
      UnexpectedRType(RType t1)
    | UnexpectedRTypes(RType t1, RType t2)
    | UnimplementedRType(RType t1)
    | CannotCalculateBindings(RType t1, RType t2, loc l)
    ;
