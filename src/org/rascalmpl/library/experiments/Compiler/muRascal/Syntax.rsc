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
lexical FConst = fconst: ( [A-Za-z][A-Za-z0-9_]* ) \ Keywords; // [_][A-Za-z0-9]+;

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

syntax Function =     
              preFunction:	"function" Identifier name "[" Integer scopeId "," Integer nformal "," {Identifier ","}* locals "]"
                            "{" (Exp ";")+ body "}"
			;

syntax Exp  =
			  muLab: 				Label id
			| muFun: 				"fun" FConst id
			| muFun: 				"fun" FConst id >> ":" ":" Integer scope // nested functions and closures
			| muConstr: 			"cons" FConst id
			
		    | muLoc: 				Identifier id >> ":" ":" Integer pos
			| muVar: 				Identifier id >> ":" ":" Integer scope >> ":" ":" Integer pos
			
			// call-by-reference: uses of variables that refer to a value location in contrast to a value
			| preLocDeref:  		"deref" Identifier id
			| muVarDeref:   		"deref" Identifier id >> ":" ":" Integer scope >> ":" ":" Integer pos
			
			> muCallPrim: 			"prim" "(" String name "," {Exp ","}+ args ")"
			
			| preSubscript: 		"get" Exp lst "[" Exp index "]"
			> muCall: 				Exp exp1 "(" {Exp ","}* args ")"
			> muReturn: 			"return"  Exp
			> muReturn: 			"return"
			
		 	> preAssignLoc:			Identifier id "=" Exp exp
		 	| preAssignSubscript:	"set" Exp lst "[" Exp index "]" "=" Exp exp
			> muAssign: 			Identifier id >> ":" ":" Integer scope >> ":" ":" Integer pos "=" Exp exp
			
			// call-by-reference: assignment 
			| preAssignLocDeref: 	"deref" Identifier id "=" Exp exp
			> muAssignVarDeref:  	"deref" Identifier id >> ":" ":" Integer scope >> ":" ":" Integer pos "=" Exp exp
			
		
			| muIfelse: 			"if" "(" Exp exp1 ")" "{" (Exp ";")* thenPart "}" "else" "{" (Exp ";")* elsePart "}"
			| muWhile: 				"while" "(" Exp cond ")" "{" (Exp ";")* body "}" 
			
			| muCreate:     		"create" "(" Exp fun  ")"
			| muCreate: 			"create" "(" Exp fun "," {Exp ","}+ args ")"
			
			| muInit: 				"init" "(" Exp coro ")"
			| muInit: 				"init" "(" Exp coro "," {Exp ","}+ args ")"
			
			| muNext:   			"next" "(" Exp coro ")"
			| muNext:   			"next" "(" Exp coro "," {Exp ","}+ args ")"
			
			| muHasNext: 			"hasNext" "(" Exp coro ")"	
			
			| muYield: 				"yield"  Exp exp 
			> muYield: 				"yield"
			
			// call-by-reference: expressions that return a value location
			| preLocRef:     		"ref" Identifier id
			| muVarRef:      		"ref" Identifier id >> ":" ":" Integer scope >> ":" ":" Integer pos
			
			| bracket				"(" Exp exp ")"
			;

keyword Keywords = 
              "module" | "function" | "return" | "get" | "set" |
			  "prim" | "if" | "else" |  "while" |
              "create" | "init" | "next" | "yield" | "hasNext" |
              "type" |
              "ref" | "deref" |
              "fun" | "cons"
             ;
             
// Syntactic features that will be removed by the preprocessor. 
            
syntax Exp =
              preIntCon:	Integer txt
            | preStrCon:	String txt
            | preTypeCon:   "type" String txt
			| preVar: 		Identifier id
			| preIfthen:    "if" "(" Exp exp1 ")" "{" (Exp ";")* thenPart "}"
			| preList:		"[" {Exp ","}* exps "]"
		
			| preAssignLocList:
							"[" Identifier id1 "," Identifier id2 "]" "=" Exp exp
			;
