@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@bootstrapParser

module lang::rascal::grammar::definition::Layout

import lang::rascal::syntax::RascalRascal;

public set[Production] \layouts(set[Production] prods, str layoutName) {
  return top-down-break visit (prods) {
    case prod(list[Symbol] lhs,Symbol rhs,attrs(list[Attr] as)) => prod(intermix(lhs, layoutName),rhs,attrs(as)) 
      when start(_) !:= rhs, \lex() notin as  
    case prod(list[Symbol] lhs,Symbol rhs,\no-attrs()) => prod(intermix(lhs, layoutName),rhs,\no-attrs()) 
      when start(_) !:= rhs
  }
}  

private list[Symbol] intermix(list[Symbol] syms, str layoutName) {
  if (syms == []) return syms;
  return tail([\layouts(layoutName), s | s <- syms]);
}
