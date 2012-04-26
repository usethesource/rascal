module demo::lang::turing::l1::cst::Syntax

start syntax Program = program: {Statement EOL}+  statements;

lexical EOL
  = [\n]
  | [\r][\n]
  | [\r] !>> [\n]
  ; 
  
syntax Statement
	= jumpAlways: "J_" Number num 
	| jumpSet: "J1" Number num 
	| jumpUnset: "J0" Number num 
	| writeSet: "W1"
	| writeUnset: "W0" 
	| moveForward: "MF"
	| moveBackward: "MB" 
	;
	
lexical Number = [0-9]+ !>> [0-9];
