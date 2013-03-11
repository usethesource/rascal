@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module lang::oberon0::\syntax::Layout

lexical Layout 
	= whitespace: [\t-\n\r\ ] 
	| Comment 
	;

layout Layouts = Layout* 
	!>> [\t-\n \r \ ] 
	!>> "(*" 
	;

lexical Comment 
	= @category="Comment"  "(*" CommentChar* "*)" 
	;

lexical CommentChar 
	= ![*] 
	| [*] !>> [)] 
	;

