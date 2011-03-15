module rascal::doc::LatexIsland

syntax Water
	= Char
	;

syntax Char
	= lex ![\\]
	| lex WBackslash
	;

syntax WBackslash
	= [\\]
	# "begin{rascal}"
	# "rascal{"
	;
	

syntax Content
	= lex ![\\{}]+
	| "{" Content* "}"
	| lex [\\][{}]
	| lex Backslash
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
	= lex [\\] "rascal{"
	;
	
syntax IEnd
	= lex "}"
	;
	
