module experiments::Compiler::ReductionWithEvalCtx::Syntax

layout Whitespace = [\ \t\n]*;
lexical Identifier = ( [a-z][a-z0-9]* ) \ Keywords;
lexical Integer = [0-9]+;
lexical Label = [$][A-Za-z0-9]+;
lexical Constant = [_][A-Za-z0-9]+;

//@doc{The lambda expression part}
syntax Exp  = 
			  nil: "nil"
			| \true: "true"
			| \false: "false"
			| number: Integer n
			| id: Identifier id
			| right lambda: "lambda" "(" Identifier id ")" "{" Exp exp "}"
			| left apply: Exp exp1 "(" Exp exp2 ")"
			> left ( 
					 add: Exp exp1 "+" Exp exp2
				   | minus: Exp exp1 "-" Exp exp2 
				   )
			| non-assoc ( 
						  eq: Exp exp1 "==" Exp exp2
						| less: Exp exp1 "\<" Exp exp2
						)
			> assign: Identifier id ":=" Exp exp
			| ifelse: "if" Exp exp1 "then" Exp exp2 "else" Exp exp3 "fi"
			| \while: "while" "(" Exp cond ")" "{" Exp body "}" 
			| bracket "(" Exp exp ")"
			
// @doc{Extension with co-routines}
			| label: Label id
			| labeled: Label label ":" Exp exp
			| create: "create" "(" Exp exp ")"
			| resume: "resume" "(" Exp exp1 "," Exp exp2 ")"
			| yield: "yield" "(" Exp exp ")"
			| hasNext: "hasNext" "(" Exp exp ")"
				
//@doc{Extension with continuations}
			| abort: "abort" "(" Exp exp ")"
			| callcc: "callcc" "(" Exp exp ")"
//@doc{Extension with constants and lists}
			| const: Constant id
			| lst: "[" { Exp "," }* exps "]" // lists
//@doc{Extension with recursion}
			| Y: "Y" "(" Exp exp ")" // Y-combinator
			
			| \block: "{" {Exp ";"}+ exps "}" // block expression
//@doc{Extension with exceptions}			
			| \throw: "throw" "(" Exp exp ")"
			| \try: "try" "{" Exp body "}" Catch catch 
			;

syntax Catch = \catch: "catch" Identifier id ":" Exp body ;	
		
keyword Keywords = "true" | "false" | "nil" | "lambda" | "if" | "then" | "else" | "while" |
                   "create" | "resume" | "yield" | "hasNext" |
                   "abort" | "callcc" | 
                   "Y" |
                   "throw" | "try" | "catch";