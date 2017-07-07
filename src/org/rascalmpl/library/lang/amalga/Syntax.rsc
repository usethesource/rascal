module lang::amalga::Syntax

extend lang::amalga::commonSyntax;
extend lang::amalga::executionSyntax;

start syntax Program = "module" Id name Definition* definitions ExecutionScenario+ executionScenarios;
   
syntax Definition 
	= @Foldable func: "function" Id functionName "(" {Parameter ","}* params ")" ":" Expression+ body "end"
	| var:  Type typ Id id "=" Exp exp
	| prim: "primitive" Type return Id id "(" {Parameter ","}* params ")" ";"
  	;

syntax Exp 
	= @Foldable condIf:  "if" Exp cond Exp then "else" Exp elsePart "end"
	| @Foldable forLoop: "for" "x" ":" Exp xRange "," "y" ":" Exp yRange "," "c" ":" Exp colorRange Exp body 
	| newImage: Id id "[" "x" ":" Exp xRange "," "y" ":" Exp yRange "," "c" ":" Exp colorRange "]" "=" Exp body 
	| implicitLoop: Exp name "-\>" Exp body
	| block: "do" ":" Exp+ body "od"
	> ite: Exp cond "?" Exp then ":" Exp else
	;

syntax Parameter = Type type Exp name;