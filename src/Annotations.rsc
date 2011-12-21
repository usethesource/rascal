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
module lang::rascal::checker::Annotations

import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import ParseTree;

//
// Annotation for linking symbol table items to trees.
//
anno set[ItemId] Tree@nameIds;

