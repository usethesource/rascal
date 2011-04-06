@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module demo::Fun::Fun-syntax;

// TODO
import Patterns;
import Modules;

// TODO
//imports 
//	EmbedSortAsPatternAndVar[Exp] 
//	EmbedSortAsPatternAndVar[Var]

start syntax Module; // TODO

syntax Var = "prime" "(" Var ")"
			| [a-z]+
			# [a-z]
			;

syntax Exp = Var
			| "fn" Var "=>" Exp
			| Exp Exp
			;
