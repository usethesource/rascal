@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascal::syntax::Reject

import Grammar;

@doc{Produces all symbols that appear on the right-hand side of a reject (-)}
public set[Symbol] rejectedSymbols(Grammar gr) {
  return { s | /diff(_,_,p) := gr, prod(syms, _,_) <- p, s <- syms};
}
