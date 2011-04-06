@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module lang::rascal::doc::HTMLIsland

syntax Water
	= Char
	;

syntax Char
	= lex ![\<]
	| lex WLT
	;

syntax WLT
	= [\<]
	# "code class=\"rascal\""
	# "span class=\"rascal\""
	;
	

syntax Content
	= lex ![\<]+
	| LT
	;

syntax LT
	= [\<]
	# "/span\>"
	# "/code\>"
	;
	
syntax Begin
	= lex [\<] "code class=\"rascal\"" [\>]
	;
	
syntax End
	= lex [\<] "/code" [\>]
	; 

syntax IBegin
	= lex [\<] "span class=\"rascal\"" [\>]
	;
	
syntax IEnd
	= lex [\<] "/span" [\>]
	;
	
