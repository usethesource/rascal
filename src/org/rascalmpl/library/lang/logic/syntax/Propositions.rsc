@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::logic::\syntax::Propositions

extend lang::logic::\syntax::Booleans;
extend lang::std::Id;

syntax Formula 
  = id: Id name \ "true" \ "false";
