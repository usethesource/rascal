@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module demo::lang::logic::ast::Propositions

extend demo::lang::logic::ast::Booleans;
extend lang::std::Whitespace;
extend lang::std::Layout;
extend lang::std::Comment;

data Formula 
  = id(str x)
  ;
 
