module lang::amalga::executionSyntax

syntax ExecutionScenario = "exec" Id id "{" Execution+ commands "}";

syntax Execution 
	= executionResult: Type typ Id id "=" ExecutionCommand exp  ";"
	| call: ExecutionCommand exp ";"
	| asert: "assert" Exp expression ";"
	;

syntax ExecutionCommand 
	= loadImage: "loadImage""(" String url ")"
	| run: "run" "(" Id id "(" {Id ","}* params ")" "," {Exp ","}* params ")"
	| viewImage: "renderImage""(" Id image")"
	| saveOutput: "save""(" Id output "," String fullPath ")"	
	;