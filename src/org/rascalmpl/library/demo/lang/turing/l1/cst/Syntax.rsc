module demo::lang::turing::l1::cst::Syntax

start syntax Program = program: {Statement EOL}+  statements;

lexical EOL
  = [\n]
  | [\r][\n]
  | [\r] !>> [\n]
  ; 
  
syntax Statement
	= jumpAlways: "J_" LineNumber num 
	| jumpSet: "J1" LineNumber num 
	| jumpUnset: "J0" LineNumber num 
	| writeSet: "W1"
	| writeUnset: "W0" 
	| moveForward: "MF"
	| moveBackward: "MB" 
	;
	
lexical LineNumber = [0-9]+ !>> [0-9];
