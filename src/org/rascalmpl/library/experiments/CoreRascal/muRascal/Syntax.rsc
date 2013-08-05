module experiments::CoreRascal::muRascal::Syntax

layout Whitespace = [\ \t\n]*;
lexical Identifier = ( [a-z][a-z0-9]* ) \ Keywords;
lexical Integer = [0-9]+;
lexical Label = [$][A-Za-z0-9]+;
lexical FConst = [_][A-Za-z0-9]+;

syntax Exp  = 
			  nil: "nil"
			| \true: "true"
			| \false: "false"
			| number: Integer n
			| var: Identifier id >> ":" ":" Integer scope >> ":"  ":" Integer pos
			| fconst: FConst id
			| label: Label id
			
			| right lambda: "lambda" "(" Identifier id ")" "{" Exp exp "}"
			| left apply: Exp exp1 "(" Exp exp2 ")"
			> assign: Identifier id >> ":" ":" Integer scope >> ":" ":" Integer pos ":=" Exp exp
			| ifelse: "if" Exp exp1 "then" Exp exp2 "else" Exp exp3 "fi"
			| \while: "while" "(" Exp cond ")" "{" Exp body "}" 
			
			| labeled: Label label ":" Exp exp
			| create: "create" "(" Exp exp ")"
			| resume: "resume" "(" Exp exp1 "," Exp exp2 ")"
			| yield: "yield" "(" Exp exp ")"
			| hasNext: "hasNext" "(" Exp exp ")"
			
			| lst: "[" { Exp "," }* exps "]" // lists

			| Y: "Y" "(" Exp exp ")" // Y-combinator
			
			| \block: "{" {Exp ";"}+ exps "}" // block expression

			| \throw: "throw" "(" Exp exp ")"
			| \try: "try" "{" Exp body "}" Catch catch 
			
			| bracket "(" Exp exp ")"
			;

syntax Catch = \catch: "catch" Identifier id ":" Exp body ;	
		
keyword Keywords = "true" | "false" | "nil" | 
				   "var" |
				   "lambda" | "if" | "then" | "else" | 
				   "while" |
                   "create" | "resume" | "yield" | "hasNext" |
                   "Y" |
                   "throw" | "try" | "catch";