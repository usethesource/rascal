@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::doc::HTMLIsland

syntax Water
	= Char
	;

lexical Char
	= ![\<]
	| WLT
	;

syntax WLT
	= [\<] !>> "code class=\"rascal\"" !>> "span class=\"rascal\""
	;
	

lexical Content
	= ![\<]+
	| LT
	;

lexical LT
	= [\<] !>> "/span\>" !>> "/code\>"
	;
	
lexical Begin
	= [\<] "code class=\"rascal\"" [\>]
	;
	
lexical End
	= [\<] "/code" [\>]
	; 

lexical IBegin
	= [\<] "span class=\"rascal\"" [\>]
	;
	
lexical IEnd
	= [\<] "/span" [\>]
	;
	
