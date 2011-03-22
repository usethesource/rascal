module lang::rascal::doc::LatexIsland

start syntax Document
	= PreB Snippet TailB
	| PreI Snippet TailI
	| Stuff
	;

syntax Snippet
	= Content+ 
	;
	
syntax Content
	= lex ![\\{}]+
	| "{" Content* "}"
	| lex [\\][{}]
	| CBS
	;

syntax CBS
	= [\\]
	# [{}]
	;


syntax PreB
	= lex Char* [\\] "begin{rascal}"
	;
	
syntax TailB
	= MidBI Snippet TailI
	| MidBB Snippet TailB
	| PostB
	;
	
syntax MidBI
	= lex [\\] "end{rascal}" Char* [\\] "rascal{"
	;

syntax MidBB
	= lex [\\] "end{rascal}" Char* [\\] "begin{rascal}"
	;

syntax PreI
	= lex Char* [\\] "rascal{"
	;	

syntax TailI
	= MidII Snippet TailI
	| MidIB Snippet TailB
	| PostI
	;

syntax MidII
	= lex "}" Char* [\\] "rascal{"
	;

syntax MidIB
	= lex "}" Char* [\\] "begin{rascal}"
	;



syntax PostB
	= lex [\\] "end{rascal}" Char*
	;

syntax PostI
	= lex "}" Char*
	;

syntax Stuff
	= lex Char*
	;

	
syntax Char
	= lex ![\\]
	| lex BS
	;

syntax BS
	= lex [\\]
	# "begin{rascal}"
	# "rascal{"
	;

