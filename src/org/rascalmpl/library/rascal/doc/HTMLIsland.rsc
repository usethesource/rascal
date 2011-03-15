module rascal::doc::HTMLIsland

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
	
