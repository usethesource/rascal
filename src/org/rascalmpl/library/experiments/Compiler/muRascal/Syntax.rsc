module experiments::Compiler::muRascal::Syntax

layout LAYOUTLIST
  = LAYOUT* !>> [\t-\n \r \ ] !>> "//" !>> "/*";

lexical LAYOUT
  = Comment 
  | [\t-\n \r \ ];
    
lexical Comment
  = @category = "Comment" "/*" (![*] | [*] !>> [/])* "*/" 
  | @category = "Comment" "//" ![\n]* [\n]; 

// lexical Identifier = id: ( [_^@]?[A-Za-z][A-Za-z0-9_]* ) \ Keywords;
lexical Integer =  [0-9]+;
lexical Label = label: [$][A-Za-z][A-Za-z0-9]+  \ Keywords;
lexical FConst = ( [A-Z][A-Z0-9_]* )    \ Keywords;
lexical MConst = ( [A-Z][A-Za-z0-9_]* ) \ Keywords;
lexical TConst = @category = "IType" ( [a-z][a-z]* )        \ Keywords;

lexical IId = ( [i][A-Z][A-Za-z0-9_]* )      \ Keywords;
lexical RId = ( [r][A-Z][A-Za-z0-9_]* )      \ Keywords;
lexical MId = 
			  ( [a-h j-q s-z][A-Za-z0-9_]* ) \ Keywords
			| ( [ir][a-z][A-Za-z0-9_]* )    \ Keywords
			;

lexical Identifier = 
			    fvar: FConst var
			  |	@category = "IValue" ivar: IId var
              | @category = "Reference" rvar: RId var
              | mvar: MId var
              ; 

lexical StrChar = 
			  @category = "Constant" NewLine: [\\] [n] 
            | @category = "Constant" Tab: [\\] [t] 
            | @category = "Constant" Quote: [\\] [\"] 
            | @category = "Constant" Backslash: [\\] [\\] 
            | @category = "Constant" Decimal: [\\] [0-9] [0-9] [0-9] 
            | @category = "Constant" Normal: ![\n\t\"\\]
            ;

lexical String = @category = "Constant" [\"] StrChar* [\"];

start syntax Module =
			  preMod: 		"module" MConst name TypeDeclaration* types Function* functions
			;
			
syntax TypeDeclaration = preTypeDecl: "declares" String sym;

syntax Function =     
                preFunction:  "function"  FunNamePart* funNames FConst name "[" Integer nformals "," {Identifier!fvar ","}* locals "]"
                              "{" (Exp ";")+ body "}"
              | preCoroutine: "coroutine" FunNamePart* funNames FConst name "[" Integer nformals "," {Identifier!fvar ","}* locals "]"
                              "{" (Exp ";")+ body "}"
			;
			
syntax FunNamePart = FConst id >> "::" "::" Integer nformals >> "::" "::";
syntax ModNamePart = MConst id >> "::" "::";

syntax Exp  =
			  muLab: 					Label id
			
			// non-nested, named functions inside or ouside a given module
			| preFunNN:             	ModNamePart modName FConst id >> "::" "::" Integer nformals
			// nested functions inside a current module
			| preFunN:              	FunNamePart+ funNames FConst id >> "::" "::" Integer nformals
			
			| muConstr: 				"cons" FConst id
			
			// call-by-reference: uses of variables that refer to a value location in contrast to a value
			| preLocDeref:  			"deref" Identifier!fvar!ivar!mvar id
			| preVarDeref:   			"deref" FunNamePart+ funNames Identifier!fvar!ivar!mvar id
			
			> muCallPrim: 				"prim" "(" String name ")"
			| muCallPrim:               "prim" "(" String name "," {Exp ","}+ args ")"
			| muCallMuPrim: 			"muprim" "(" String name "," {Exp ","}+ args ")"
			
			| muMulti:                  "multi" "(" Exp exp ")"
			| muOne:                    "one" "(" {Exp ","}+ exps ")"
			| muAll:                    "all" "(" {Exp ","}+ exps ")"
			
			> muCall: 					Exp!muReturn!muYield!muExhaust exp1 "(" {Exp ","}* args ")"
			
			> muReturn: 				"return"  Exp exp
			| muReturn:                 "return" "(" Exp exp "," {Exp ","}+ exps ")"
			> muReturn: 				"return" !>> "("
			
			| left preAddition:			Exp lhs "+"   Exp rhs
			
			| left preSubtraction:		Exp lhs "-"   Exp rhs
			| left preDivision:         Exp lhs "/"   Exp rhs
			| left preMultiplication:   Exp lhs "*"   Exp rhs
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
			| non-assoc preIs:			Exp lhs [\ ]<< "is" >>[\ ] TConst typeName
			
		 	> preAssignLoc:				Identifier!fvar id "=" Exp ex
			> preAssign: 				FunNamePart+ funNames Identifier!fvar id "=" Exp exp
			
			// call-by-reference: assignment 
			| preAssignLocDeref: 		"deref" Identifier!fvar!ivar!mvar id "=" Exp exp
			> preAssignVarDeref:  		"deref" FunNamePart+ funNames Identifier!fvar!ivar!mvar id "=" Exp exp
			
		
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
			| muYield:                  "yield" "(" Exp exp "," {Exp ","}+ exps ")"
			> muYield: 					"yield" !>> "("
			
			| muExhaust:                "exhaust" !>> "("
			
			| muGuard:                  "guard" Exp exp
			
			// call-by-reference: expressions that return a value location
			| preLocRef:     			"ref" Identifier!fvar!rvar id
			| preVarRef:      			"ref" FunNamePart+ funNames Identifier!fvar!rvar id
			
			| bracket					"(" Exp exp ")"
			;
			
syntax TypeCase = muTypeCase: 			"case" TConst id ":" Exp exp ;		

keyword Keywords = 
              "module" | "declares" | "function" | "coroutine" | "return" | 
			  "prim" | "muprim" | "if" | "else" |  "while" |
              "create" | "init" | "next" | "yield" | "exhaust" | "hasNext" |
              "guard" |
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
			| preAssignLocList:			"[" Identifier!fvar!rvar id1 "," Identifier!fvar!rvar id2 "]" "=" Exp exp
			> preList:					"[" {Exp ","}* exps "]"
			;
