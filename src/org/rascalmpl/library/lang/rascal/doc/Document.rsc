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
module lang::rascal::doc::Document

import ParseTree;

start syntax Document
	= Chunk*
	;

layout WS
	=
	;

syntax Chunk
	= Snippet
	| Water
	;

syntax Water
	= ...
	;

lexical Block
	= Begin Content+ End
	;

lexical Inline
	= IBegin Content+ IEnd
	;

syntax Snippet
	= Block
	| Inline
	;


public str expand(Document doc, loc l, str(Tree, loc) formatBlock, str(Tree, loc) formatInline) {
	result = "";
	top-down-break visit (doc) {
		case Block b: result += formatBlock(b, l);
		case Inline i: result += formatInline(i, l);
		case Water w: result += "<w>";
	}
	return result;
}

