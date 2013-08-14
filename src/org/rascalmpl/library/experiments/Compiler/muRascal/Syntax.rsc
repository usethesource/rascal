module experiments::Compiler::muRascal::Syntax

layout LAYOUTLIST
  = LAYOUT* !>> [\t-\n \r \ ] !>> "//" !>> "/*";

lexical LAYOUT
  = Comment 
  | [\t-\n \r \ ];
    
lexical Comment
  = "/*" (![*] | [*] !>> [/])* "*/" 
  | "//" ![\n]* [\n]; 

//layout Whitespace = [\ \t\n]*;

lexical Identifier = id: ( [A-Za-z][A-Za-z0-9_]* ) \ Keywords;
lexical Integer =  [0-9]+;
lexical Label = label: [$][A-Za-z0-9]+;
lexical FConst = fconst: [_][A-Za-z0-9]+;

lexical StrChar = 
			  NewLine: [\\] [n] 
            | Tab: [\\] [t] 
            | Quote: [\\] [\"] 
            | Backslash: [\\] [\\] 
            | Decimal: [\\] [0-9] [0-9] [0-9] 
            | Normal: ![\n\t\"\\] 
            ;

lexical String = [\"] StrChar* [\"];

start syntax Module =
			  preMod: 		"module" Identifier name Function* functions
			;

syntax NameDecl = 
			  preDecl: 		Identifier name ":" Integer pos
			;

syntax Function =     
              preFunction:	"function" Identifier name "[" Integer scope "," Integer nformal "," {NameDecl ","}* names "]" "{" {Exp ";"}+ body "}"
			;

syntax Exp  = 
			  muCon: 		Integer n
			| muCon: 		String s
			| muLab: 		Label id
			| muFun: 		FConst id
			| muConstr: 	FConst
			
		    | muLoc: 		Identifier id ":" Integer pos
			| muVar: 		Identifier id ":" Integer scope ":" Integer pos
			| muCall: 		Exp exp1 "(" {Exp ","}* args ")"
			
			> muCallPrim: 	"prim" "(" String name "," {Exp ","}+ args ")"
			| muReturn: 	"return"
			| muReturn: 	"return" Exp exp
			
		 	| preAssignLoc:	Identifier id "=" Exp exp
			> muAssign: 	Identifier id >> ":" Integer scope >> ":" Integer pos "=" Exp exp
			> muAssignRef: 	"@" Identifier id >> ":" Integer scope >> ":" Integer pos "=" Exp exp
			
			| preIfthen:    "if" "(" Exp exp1 ")" "{" {Exp ";"}* thenPart "}"
			| muIfelse: 	"if" "(" Exp exp1 ")" "{" {Exp ";"}* thenPart "}" "else" "{" {Exp ";"}* elsePart "}"
			| muWhile: 		"while" "(" Exp cond ")" "{" {Exp ";"}* body "}" 
			
			| muCreate: 	"create" "(" Identifier fname "," {Exp ","}+ exargsps ")"
			> muCreate: 	"create" "(" Exp coro ")"
			
			| muInit: 		"init" "(" Exp coro ")"
			| muInit: 		"init" "(" Exp coro "," {Exp ","}* args ")"
			
			| muNext:   	"next" "(" Exp coro ")"
			| muNext:   	"next" "(" Exp coro "," {Exp ","}+ args ")"
			
			| muHasNext: 	"hasNext" "(" Exp coro ")"	
			
			| muYield: 		"yield"
			| muYield: 		"yield"  Exp exp
			
			| bracket "(" Exp exp ")"
			;

keyword Keywords = 
              "module" | "function" | "return" |
			  "prim" | "if" | "then" | "else" | "fi" |  "while" |
              "create" | "init" | "next" | "yield" | "hasNext" |
             ;
             
// Syntactic features that will be removed by the preprocessor. 
            
syntax Exp =
			   preVar: Identifier id
			
			 ;