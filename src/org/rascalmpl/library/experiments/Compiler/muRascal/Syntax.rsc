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

lexical Identifier = id: ( [_^@]?[A-Za-z][A-Za-z0-9_]* ) \ Keywords;
lexical Integer =  [0-9]+;
lexical Label = label: [$][A-Za-z][A-Za-z0-9]+ \ Keyword;
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
              preFunction:	"function" FunNamePart* funNames Identifier name "[" Integer nformals "," {Identifier ","}* locals "]"
                            "{" (Exp ";")+ body "}"
			;
			
syntax FunNamePart = FConst id >> "::" "::" Integer nformals >> "::" "::";
syntax ModNamePart = Identifier id >> "::" "::";

syntax Exp  =
			  muLab: 					Label id
			
			// non-nested, named functions inside or ouside a given module
			| preFunNN:             	ModNamePart modName FConst id >> "::" "::" Integer nformals
			// nested functions inside a current module
			| preFunN:              	FunNamePart+ funNames FConst id >> "::" "::" Integer nformals
			
			| muConstr: 				"cons" FConst id
			
		    | muLoc: 					Identifier id >> ":" ":" Integer pos
			
			// call-by-reference: uses of variables that refer to a value location in contrast to a value
			| preLocDeref:  			"deref" Identifier id
			| preVarDeref:   			"deref" FunNamePart+ funNames Identifier id
			
			> muCallPrim: 				"prim" "(" String name ")"
			| muCallPrim:               "prim" "(" String name "," {Exp ","}+ args ")"
			| muCallMuPrim: 			"muprim" "(" String name "," {Exp ","}+ args ")"
			
			| muMulti:                  "multi" "(" Exp exp ")"
			| muOne:                    "one" "(" {Exp ","}+ exps ")"
			| muAll:                    "all" "(" {Exp ","}+ exps ")"
			
			| preSubscriptArray: 		"get_array" Exp ar "[" Exp index "]"
			| preSubscriptList: 		"get_list" Exp lst "[" Exp index "]"
			| preSubscriptTuple: 		"get_tuple" Exp tup "[" Exp index "]"
			> muCall: 					Exp exp1 "(" {Exp ","}* args ")"
			> muReturn: 				"return"  Exp exp
			> muReturn: 				"return"
			
			| left preAddition:			Exp lhs "+"   Exp rhs
			
			| left preSubtraction:		Exp lhs "-"   Exp rhs
			| left preDivision:         Exp lhs "/"   Exp rhs
			| left preModulo:           Exp lhs "mod" Exp rhs
		    | left prePower :           Exp lhs "pow" Exp rhs
			> non-assoc preLess:		Exp lhs "\<"  Exp rhs
			| non-assoc preLessEqual:	Exp lhs "\<=" Exp rhs
			| non-assoc preEqual:		Exp lhs "=="  Exp rhs
			| non-assoc preNotEqual:	Exp lhs "!="  Exp rhs
			| non-assoc preGreater:		Exp lhs "\>"  Exp rhs
			| non-assoc preGreaterEqual:Exp lhs "\>=" Exp rhs
			
			> left preAnd:				Exp lhs "&&" Exp rhs
			> left preOr:               Exp lhs "||" Exp rhs
			| non-assoc preIs:			Exp lhs [\ ]<< "is" >>[\ ] Identifier typeName
			
		 	> preAssignLoc:				Identifier id "=" Exp exp
		 	| preAssignSubscriptArray:	"set_array" Exp ar "[" Exp index "]" "=" Exp exp
			> preAssign: 				FunNamePart+ funNames Identifier id "=" Exp exp
			
			// call-by-reference: assignment 
			| preAssignLocDeref: 		"deref" Identifier id "=" Exp exp
			> muAssignVarDeref:  		"deref" FunNamePart+ funNames Identifier id "=" Exp exp
			
		
			| muIfelse: 				(Label label ":")? "if" "(" Exp exp1 ")" "{" (Exp ";")* thenPart "}" "else" "{" (Exp ";")* elsePart "}"
			| muWhile: 					(Label label ":")? "while" "(" Exp cond ")" "{" (Exp ";")* body "}" 
			
			| muTypeSwitch:				"typeswitch" "(" Exp exp ")" "{" (TypeCase ";")+ cases "default" ":" Exp default ";" "}"
			
			| muCreate:     			"create" "(" Exp fun  ")"
			| muCreate: 				"create" "(" Exp fun "," {Exp ","}+ args ")"
			
			| muInit: 					"init" "(" Exp coro ")"
			| muInit: 					"init" "(" Exp coro "," {Exp ","}+ args ")"
			
			| muNext:   				"next" "(" Exp coro ")"
			| muNext:   				"next" "(" Exp coro "," {Exp ","}+ args ")"
			
			| muHasNext: 				"hasNext" "(" Exp coro ")"	
			
			| muYield: 					"yield"  Exp exp 
			> muYield: 					"yield"
			
			// call-by-reference: expressions that return a value location
			| preLocRef:     			"ref" Identifier id
			| preVarRef:      			"ref" FunNamePart+ funNames Identifier id
			
			| bracket					"(" Exp exp ")"
			;
			
syntax TypeCase = muTypeCase: 			"case" Identifier id ":" Exp exp ;		

keyword Keywords = 
              "module" | "function" | "return" | 
              "get_array" | "get_list" | "get_tuple" |
              "set_array" |
			  "prim" | "muprim" | "if" | "else" |  "while" |
              "create" | "init" | "next" | "yield" | "hasNext" |
              "type" |
              "ref" | "deref" |
              "fun" | "cons" | "is" | "mod" | "pow" |
              "typeswitch" | "default" | "case" |
              "multi" | "one" | "all"
             ;
             
// Syntactic features that will be removed by the preprocessor. 
            
syntax Exp =
              preIntCon:				Integer txt
            | preStrCon:				String txt
            | preTypeCon:   			"type" String txt
			| preVar: 					Identifier id
			// *local* variables of functions used inside their closures and nested functions
			| preVar: 					FunNamePart+ funNames Identifier id 
			
			| preIfthen:    			"if" "(" Exp exp1 ")" "{" (Exp ";")* thenPart "}"
			| preAssignLocList:			"[" Identifier id1 "," Identifier id2 "]" "=" Exp exp
			> preList:					"[" {Exp ","}* exps "]"
			;
