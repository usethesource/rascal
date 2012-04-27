module demo::lang::turing::l1::cst::Syntax

layout WhiteSpace = [\ \t\n\r]* !>> [\ \t\n\r];

start syntax Program = program: Statement+  statements;
  
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
