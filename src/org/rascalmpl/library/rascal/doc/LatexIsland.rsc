module rascal::doc::LatexIsland

syntax Water
	= lex ![\\]
	| lex WBackslash
	;

syntax WBackslash
	= [\\]
	# "begin{rascal}"
	# "irascal{"
	;
	

syntax Content
	= lex ![\\{}]+
	| [{] Content* [}]
	| lex [\\][{}]
	| Backslash
	| "\\\\begin{rascal}"
	| "\\\\end{rascal}"
	| "\\\\irascal{"
	| "\\\\}"
	;

syntax Backslash
	= lex [\\]
	# [{}]
	# "end{rascal}"
	;
	
syntax Begin
	= lex [\\] "begin{rascal}"
	;
	
syntax End
	= lex [\\] "end{rascal}"
	; 

syntax IBegin
	= lex [\\] "irascal{"
	;
	
syntax IEnd
	= lex "}"
	;
	
