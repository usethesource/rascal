module demo::lang::turing::l1::cst::Syntax

layout Standard 
  = WhitespaceOrComment* !>> [\ \t\n\f\r] !>> "//";
  
syntax WhitespaceOrComment 
  = whitespace: Whitespace
  | comment: Comment
  ; 

lexical Whitespace = [\ \t\f]; 

lexical Comment = @category="Comment" "//" ![\n\r]* $;

start syntax Program = program: Statement+  statements;

lexical Statement
	= jump: "J" Condition con LineNumber num [\n\r]+
	| write: "W" TapeLanguage val [\n\r]+
	| move: "M" Direction direction [\n\r]+
	;

lexical Condition 
	= \any: "_" 
	| \set: "1"
	| unset: "0"
	;
	
lexical LineNumber = [0-9]+ !>> [0-9];

lexical TapeLanguage 
	= \set: "1"
	| unset: "0"
	;
	
lexical Direction 
	= forward : "F"
	| backward: "B"
	;