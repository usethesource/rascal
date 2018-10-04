module lang::rascalcore::compile::muRascal::Syntax

/*
 * Optional nonterminals at the beginning or end of productions are deprecated for this specific design of a grammar 
 * because of the manual layout that can interfere with the default one
 *
 * Use of () to enable work around the "prefix-sharing" bug
 * Use of () to enable work around the manual layout  
 */

layout MuLayoutList
  = MuLayout* !>> [\t \n \r \ ] !>> "//" !>> "/*";

lexical NoNL
  = MuComment 
  | [\t \ ];
  
layout NoNLList
  = @manual NoNL* !>> [\t \ ] !>> "//" !>> "/*";
  
lexical MuLayout
  = MuComment 
  | [\t-\n \r \ ];
    
lexical MuComment
  = @category = "Comment" "/*" (![*] | [*] !>> [/])* "*/" 
  | @category = "Comment" "//" ![\n]* [\n]; 

// lexical Identifier = id: ( [_^@]?[A-Za-z][A-Za-z0-9_]* ) \ Keywords;
lexical Integer =  [0-9]+;
lexical MuLabel = mulabel: ( [$][A-Za-z][A-Za-z0-9]+ !>> [A-Za-z0-9] ) \ Keywords;
lexical FConst = ( [A-Z][A-Z0-9_]* !>> [A-Z0-9_] )                 \ Keywords;
lexical MConst = ( [A-Z][A-Za-z0-9_]* !>> [A-Za-z0-9_] )           \ Keywords;
lexical TConst = @category = "IType" ( [a-z][a-z]* !>> [a-z] )     \ Keywords;

lexical IId = ( [i][A-Z][A-Za-z0-9_]* !>> [A-Za-z0-9_] )           \ Keywords;
lexical RId = ( [r][A-Z][A-Za-z0-9_]* !>> [A-Za-z0-9_] )           \ Keywords;
lexical MId = ( [a-h j-q s-z][A-Za-z0-9_]* !>> [A-Za-z0-9_] )      \ Keywords
			| ( [ir][a-z][A-Za-z0-9_]* !>> [A-Za-z0-9_] )          \ Keywords
			;

lexical Identifier = 
			    fvar: FConst var1
			  |	@category = "IValue" ivar: IId var2
              | @category = "Reference" rvar: RId var3
              | mvar: MId var4
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

syntax Sep = () ";" () | {[\n \r] NoNLList}+ ;	

start syntax MuPreModule =
			  preMod: 		"module" MConst name TypeDeclaration* types Function* functions
			;
			
syntax TypeDeclaration = preTypeDecl: "declares" String sym;

syntax VarDecl = preVarDecl1: Identifier!fvar id 
               | preVarDecl2: Identifier!fvar id "=" Exp initializer;

syntax VarDecls = "var" {VarDecl ","}+;

syntax Guard = preGuard1: "guard" Exp exp
             | preGuard3: "guard" "{" VarDecls locals NoNLList Sep NoNLList Exp exp "}";

syntax Function =     
                preFunction:  "function"  FunNamePart* funNames FConst name "(" {Identifier!fvar ","}* formals ")"
                              "{" (VarDecls NoNLList Sep NoNLList () !>> [\n \r])? locals {Exp (NoNLList Sep NoNLList)}+ body ";"? "}"
              | preCoroutine: "coroutine" FunNamePart* funNames FConst name "(" {Identifier!fvar ","}* formals ")"
                              Guard? guard
                              "{" (VarDecls NoNLList Sep NoNLList () !>> [\n \r])? locals {Exp (NoNLList Sep NoNLList)}+ body ";"? "}"
			;
			
syntax FunNamePart = FConst fconst >> "::" "::" Integer nformals >> "::" "::";
syntax ModNamePart = MConst mconst >> "::" "::";

syntax Exp  =
//			  muLab: 					Label lid
			
			// non-nested, named functions inside or ouside a given module
			  preFunNN:             	ModNamePart modName FConst fid >> "::" "::" Integer nformals
			// nested functions inside a current module
			| preFunN:              	FunNamePart+ funNames FConst fid >> "::" "::" Integer nformals
			
			| muConstr: 				"cons" FConst cid
			
			// call-by-reference: uses of variables that refer to a value location in contrast to a value
			| preLocDeref:  			"deref" Identifier!fvar!ivar!mvar id
			| preVarDeref:   			"deref" FunNamePart+ funNames Identifier!fvar!ivar!mvar id
			
			| preMuCallPrim1: 			"prim" NoNLList "(" String name ")"
			| preMuCallPrim2:           "prim" NoNLList "(" String name "," {Exp ","}+ largs1 ")"
	
			| muCallMuPrim: 			"muprim" NoNLList "(" String name "," {Exp ","}+ largs1 ")"
			
			| muMulti:                  "multi" "(" Exp exp ")"
			//| muOne2:                   "one" "(" {Exp ","}+ exps ")"
			//| muAll:                    "all" "(" {Exp ","}+ exps ")"
			
			// function call and partial function application
			| muCall: 					Exp!muReturn!muYield0!muYield1!muYield2!muExhaust exp NoNLList "(" {Exp ","}* largs0 ")"
			| muApply:                  "bind" "(" Exp!muReturn0!muReturn1!muYield0!muYield1!muYield2!muExhaust exp "," {Exp ","}+ largs1 ")"
			
			| preSubscript:             Exp exp NoNLList "[" Exp index "]"
			| preList:					"[" {Exp ","}* exps0 "]"
			
			> left ( preDivision:       Exp lhs "/"   Exp rhs
			       | preMultiplication: Exp lhs "*"   Exp rhs
			       )
			
			> left ( preAddition:		Exp lhs "+"   Exp rhs
			       | preSubtraction:	Exp lhs "-"   Exp rhs
			       )
			
			> left preModulo:           Exp lhs "mod" Exp rhs
		    > left prePower :           Exp lhs "pow" Exp rhs
		    
			> non-assoc ( preLess:		   Exp lhs "\<"  Exp rhs
			            | preLessEqual:	   Exp lhs "\<=" Exp rhs
			            | preGreater:	   Exp lhs "\>"  Exp rhs
			            | preGreaterEqual: Exp lhs "\>=" Exp rhs
			            )
			
			> non-assoc ( preEqual:		Exp lhs "=="  Exp rhs
			            | preNotEqual:	Exp lhs "!="  Exp rhs
			            )
				
			> left preAnd:				Exp lhs "&&" Exp rhs
			> left preOr:               Exp lhs "||" Exp rhs
			> non-assoc preIs:			Exp lhs [\ ]<< "is" >>[\ ] TConst typeName
			
			> non-assoc preAssignSubscript:  Exp exp1 NoNLList "[" Exp index "]" "=" Exp exp2
			//| preAssignLocList:			"[" Identifier!fvar!rvar id1 "," Identifier!fvar!rvar id2 "]" "=" Exp exp
			
			> preAssignLoc: 			Identifier!fvar id "=" Exp exp
			| preAssign: 				FunNamePart+ funNames Identifier!fvar id "=" Exp exp
			
			// call-by-reference: assignment
			| preAssignLocDeref: 		"deref" Identifier!fvar!ivar!mvar id "=" Exp exp
			| preAssignVarDeref:  		"deref" FunNamePart+ funNames Identifier!fvar!ivar!mvar id "=" Exp exp
			
		
			| preIfelse: 				"if" "(" Exp exp1 ")" "{" {Exp (NoNLList Sep NoNLList)}+ thenPart ";"? "}" "else" "{" {Exp (NoNLList Sep NoNLList)}+ elsePart ";"? "}" 	
		 	| preIfelse: 				MuLabel label ":" "if" "(" Exp exp1 ")" "{" {Exp (NoNLList Sep NoNLList)}+ thenPart ";"? "}" "else" "{" {Exp (NoNLList Sep NoNLList)}+ elsePart ";"? "}"			
			| preWhile: 				"while" "(" Exp cond ")" "{" {Exp (NoNLList Sep NoNLList)}+ body ";"? "}"
			| preWhile: 				MuLabel label ":" "while" "(" Exp cond ")" "{" {Exp (NoNLList Sep NoNLList)}+ body ";"? "}"
			
			| preTypeSwitch:			"typeswitch" "(" Exp exp ")" "{" (TypeCase ";"?)+ type_cases "default" ":" Exp default ";"? "}"
			// Note: switch has not been added to concrete muRascal
			// preSwitch:				"switch" "(" Exp exp ")" "{" (Case ";"?)+ cases "default" ":" Exp default ";"? "}"
			
			| muCreate1: 				"create" "(" Exp coro ")"
			| muCreate2: 				"create" "(" Exp coro "," {Exp ","}+ largs1 ")"
			
			| muNext1:   				"next" "(" Exp coro ")"
			| muNext2:   				"next" "(" Exp coro "," {Exp ","}+ largs1 ")"
			
			> muReturn1: 				"return" NoNLList Exp exp
			| muReturn0: 				() () "return"
			
			| muYield1: 					"yield" NoNLList Exp exp
			| muYield2:                  () "yield" NoNLList "(" Exp exp "," {Exp ","}+ exps1 ")"
			| muYield0: 					() () "yield"
			
			| muExhaust:                "exhaust"
			
			// call-by-reference: expressions that return a value location
			| preLocRef:     			"ref" Identifier!fvar!rvar id
			| preVarRef:      			"ref" FunNamePart+ funNames Identifier!fvar!rvar id
			
			| preBlock:                 "{" {Exp (NoNLList Sep NoNLList)}+ bexps ";"? "}"
			
			| bracket					"(" Exp exp ")"
			;
						
syntax TypeCase = muTypeCase: 			"case" TConst id ":" Exp exp ;	
//syntax Case = muCase: 					"case" String id ":" Exp exp ;				

keyword Keywords = 
              "module" | "declares" | "var" | "function" | "coroutine" | "return" | 
			  "prim" | "muprim" | "if" | "else" |  "while" |
              "create" | "next" | "yield" | "exhaust" |
              "guard" |
              "type" |
              "ref" | "deref" |
              "bind" | "cons" | "is" | "mod" | "pow" |
              "typeswitch" | "default" | "case" | "switch" |
              "multi" | "one" | "all" |
              "cont" | "reset" | "shift"
             ;
             
// Syntactic features that will be removed by the preprocessor. 
            
syntax Exp =
              preIntCon:				Integer txt1
            | preStrCon:				String txt2
            | preTypeCon:   			"type" String txt3
			| preVar: 					Identifier pid
			// *local* variables of functions used inside their closures and nested functions
			| preVar: 					FunNamePart+ funNames Identifier pid 
			
			| preIfthen:    			"if" "(" Exp exp1 ")" "{" {Exp (NoNLList Sep NoNLList)}+ thenPart ";"? "}"
			;
