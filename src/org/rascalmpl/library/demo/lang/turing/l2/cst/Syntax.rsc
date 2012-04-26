module demo::lang::turing::l2::cst::Syntax

extend demo::lang::turing::l1::cst::Syntax;

syntax Statement 
	= label: "L" Name name
	| @foldable loop: "REP" Number count "{" EOL 
		{Statement EOL}+ body EOL
		"}"
	| jumpAlwaysLabel: "J_" Name name 
	| jumpSetLabel: "J1" Name name 
	| jumpUnsetLabel: "J0" Name name 
	;
	
lexical Name = ([a-zA-Z] [a-zA-Z0-9]*) !>> [a-zA-Z0-9];