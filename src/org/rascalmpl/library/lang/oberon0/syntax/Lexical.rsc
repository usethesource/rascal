@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module lang::oberon0::\syntax::Lexical

lexical Ident 
	= id: ([A-Za-z] !<< [a-zA-Z][a-zA-Z0-9]* !>> [A-Za-z0-9]) \ Keywords 
	;
	
lexical Natural 
	= [0-9]+  !>> [0-9]
	;


