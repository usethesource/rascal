@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module lang::oberon0::\syntax::Expressions

import lang::oberon0::\syntax::Layout;
import lang::oberon0::\syntax::Lexical;

syntax Expression 
	= nat: Natural value
	| \true: "TRUE"
	| \false: "FALSE"
	| lookup: Ident var 
	| bracket Bracket: "(" Expression exp ")"
	| not: "~" Expression exp
	> 
	left (
		mul: Expression lhs "*" Expression rhs
		| div: Expression lhs "DIV" Expression rhs
		| \mod: Expression lhs "MOD" Expression rhs
		| amp: Expression lhs "&" Expression rhs
	) 
	>
	left (
		pos: "+" Expression exp
		| neg: "-" Expression exp
		| add: Expression lhs "+" Expression rhs
		| sub: Expression lhs "-" Expression rhs
		| or: Expression lhs "OR" Expression rhs
	)
	> 
	non-assoc (
		eq: Expression lhs "=" Expression rhs
		| neq: Expression lhs "#" Expression rhs
		| lt: Expression lhs "\<" Expression rhs
		| leq: Expression lhs "\<=" Expression rhs
		| gt: Expression lhs "\>" Expression rhs
		| geq: Expression lhs "\>=" Expression rhs
	)
	;

keyword Keywords 
	= "DIV" 
	| "MOD" 
	| "OR" 
	| "TRUE" 
	| "FALSE"
	; 
	

	
