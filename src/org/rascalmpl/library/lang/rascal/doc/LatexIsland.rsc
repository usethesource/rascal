@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::doc::LatexIsland

start syntax Document
	= PreB Snippet TailB
	| PreI Snippet TailI
	| Stuff
	;

syntax Snippet
	= Content+ 
	;

lexical ContentLex
	= ![\\{}]+
	| [\\][{}]
	;
	
syntax Content
	= "{" Content* "}"
    | CBS
	| ContentLex
	;

syntax CBS
	= [\\] !>> [{}]
	;


lexical PreB
	= Char* [\\] "begin{rascal}"
	;
	
syntax TailB
	= MidBI Snippet TailI
	| MidBB Snippet TailB
	| PostB
	;
	
lexical MidBI
	= [\\] "end{rascal}" Char* [\\] "rascal{"
	;

lexical MidBB
	= [\\] "end{rascal}" Char* [\\] "begin{rascal}"
	;

lexical PreI
	= Char* [\\] "rascal{"
	;	

syntax TailI
	= MidII Snippet TailI
	| MidIB Snippet TailB
	| PostI
	;

lexical MidII
	= "}" Char* [\\] "rascal{"
	;

lexical MidIB
	= "}" Char* [\\] "begin{rascal}"
	;



lexical PostB
	= [\\] "end{rascal}" Char*
	;

lexical PostI
	= "}" Char*
	;

lexical Stuff
	= Char*
	;

	
lexical Char
	= ![\\]
	| BS
	;

lexical BS
	= [\\] !>> "begin{rascal}" !>> "rascal{"
	;

