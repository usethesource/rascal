module experiments::Compiler::muRascal::SyntaxBeta

/*
 * Optional nonterminals at the beginning or end of productions are deprecated for this specific design of a grammar 
 * because of the manual layout that can interfere with the default one
 *
 * Use of () to enable work around the "prefix-sharing" bug
 * Use of () to enable work around the manual layout  
 */

layout LAYOUTLIST
  = LAYOUT* !>> [\t \n \r \ ] !>> "//" !>> "/*";

lexical NoNL
  = Comment 
  | [\t \ ];
  
layout NoNLList
  = @manual NoNL* !>> [\t \ ] !>> "//" !>> "/*";
  
lexical LAYOUT
  = Comment 
  | [\t-\n \r \ ];
    
lexical Comment
  = @category = "Comment" "/*" (![*] | [*] !>> [/])* "*/" 
  | @category = "Comment" "//" ![\n]* [\n]; 

// lexical Identifier = id: ( [_^@]?[A-Za-z][A-Za-z0-9_]* ) \ Keywords;
lexical Integer =  [0-9]+;
lexical Label = label: ( [$][A-Za-z][A-Za-z0-9]+ !>> [A-Za-z0-9] ) \ Keywords;
lexical FConst = ( [A-Z][A-Z0-9_]* !>> [A-Z0-9_] )                 \ Keywords;
lexical MConst = ( [A-Z][A-Za-z0-9_]* !>> [A-Za-z0-9_] )           \ Keywords;
lexical TConst = @category = "IType" ( [a-z][a-z]* !>> [a-z] )     \ Keywords;

lexical IId = ( [i][A-Z][A-Za-z0-9_]* !>> [A-Za-z0-9_] )           \ Keywords;
lexical RId = ( [r][A-Z][A-Za-z0-9_]* !>> [A-Za-z0-9_] )           \ Keywords;
lexical MId = ( [a-h j-q s-z][A-Za-z0-9_]* !>> [A-Za-z0-9_] )      \ Keywords
			| ( [ir][a-z][A-Za-z0-9_]* !>> [A-Za-z0-9_] )          \ Keywords
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

syntax Sep = () ";" () | {[\n \r] NoNLList}+ ;			

start syntax Module =
			  preMod: 		"module" MConst name TypeDeclaration* types Function* functions
			;
			
syntax TypeDeclaration = preTypeDecl: "declares" String sym;

syntax VarDecl = preVarDecl: Identifier!fvar id 
               | preVarDecl: Identifier!fvar id "=" Exp initializer;

syntax VarDecls = "var" {VarDecl ","}+;

syntax Guard = preGuard: "guard" Exp exp
             | preGuard: "guard" "{" VarDecls locals NoNLList Sep NoNLList Exp exp "}";

syntax Function =     
                preFunction:  "function"  FunNamePart* funNames FConst name "(" {Identifier!fvar ","}* formals ")"
                              "{" (VarDecls NoNLList Sep NoNLList () !>> [\n \r])? locals {Exp (NoNLList Sep NoNLList)}+ body ";"? "}"
              | preCoroutine: "coroutine" FunNamePart* funNames FConst name "(" {Identifier!fvar ","}* formals ")"
                              Guard? guard
                              "{" (VarDecls NoNLList Sep NoNLList () !>> [\n \r])? locals {Exp (NoNLList Sep NoNLList)}+ body ";"? "}"
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
			
			> muCallPrim: 				"prim" NoNLList "(" String name ")"
			| muCallPrim:               "prim" NoNLList "(" String name "," {Exp ","}+ args ")"
			| muCallMuPrim: 			"muprim" NoNLList "(" String name "," {Exp ","}+ args ")"
			
			| muMulti:                  "multi" "(" Exp exp ")"
			| muOne:                    "one" "(" {Exp ","}+ exps ")"
			| muAll:                    "all" "(" {Exp ","}+ exps ")"
			
			// function call and partial function application
			> muCall: 					Exp!muReturn!muYield!muExhaust exp NoNLList "(" {Exp ","}* args ")"
			| muApply:                  "bind" "(" Exp!muReturn!muYield!muExhaust exp "," {Exp ","}+ args ")"
			
			> left preAddition:			Exp lhs "+"   Exp rhs
			
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
			
			
			> muReturn: 				"return" NoNLList Exp exp
			| muReturn:                 () "return" NoNLList "(" Exp exp "," {Exp ","}+ exps ")"
			| muReturn: 				() () "return"
			
			> preAssignLoc: 			Identifier!fvar id "=" Exp exp
			| preAssign: 				FunNamePart+ funNames Identifier!fvar id "=" Exp exp
			
			// call-by-reference: assignment
			| preAssignLocDeref: 		"deref" Identifier!fvar!ivar!mvar id "=" Exp exp
			> preAssignVarDeref:  		"deref" FunNamePart+ funNames Identifier!fvar!ivar!mvar id "=" Exp exp
			
		
			| preIfelse: 				"if" "(" Exp exp1 ")" "{" {Exp (NoNLList Sep NoNLList)}+ thenPart ";"? "}" "else" "{" {Exp (NoNLList Sep NoNLList)}+ elsePart ";"? "}"
		 	| preIfelse: 				Label label ":" "if" "(" Exp exp1 ")" "{" {Exp (NoNLList Sep NoNLList)}+ thenPart ";"? "}" "else" "{" {Exp (NoNLList Sep NoNLList)}+ elsePart ";"? "}"
			| preWhile: 				"while" "(" Exp cond ")" "{" {Exp (NoNLList Sep NoNLList)}+ body ";"? "}"
			| preWhile: 				Label label ":" "while" "(" Exp cond ")" "{" {Exp (NoNLList Sep NoNLList)}+ body ";"? "}"
			
			| preTypeSwitch:			"typeswitch" "(" Exp exp ")" "{" (TypeCase ";"?)+ cases "default" ":" Exp default ";"? "}"
			
			| muCreate: 				"create" "(" Exp coro ")"
			| muCreate: 				"create" "(" Exp coro "," {Exp ","}+ args ")"
			
			| muNext:   				"next" "(" Exp coro ")"
			| muNext:   				"next" "(" Exp coro "," {Exp ","}+ args ")"
			
			| muYield: 					"yield" NoNLList Exp exp
			| muYield:                  () "yield" NoNLList "(" Exp exp "," {Exp ","}+ exps ")"
			> muYield: 					() () "yield"
			
			| muExhaust:                "exhaust"
			
			// delimited continuations (experimental feature)
			| preContLoc:               "cont"
			| preContVar:               FunNamePart+ funNames "cont"
			| muReset:                  "reset" "(" Exp fun ")"
			| muShift:                  "shift" "(" Exp body ")"
			
			// call-by-reference: expressions that return a value location
			| preLocRef:     			"ref" Identifier!fvar!rvar id
			| preVarRef:      			"ref" FunNamePart+ funNames Identifier!fvar!rvar id
			
			| preBlock:                 "{" {Exp (NoNLList Sep NoNLList)}+ exps ";"? "}"
			
			| bracket					"(" Exp exp ")"
			;
						
syntax TypeCase = muTypeCase: 			"case" TConst id ":" Exp exp ;		

keyword Keywords = 
              "module" | "declares" | "var" | "function" | "coroutine" | "return" | 
			  "prim" | "muprim" | "if" | "else" |  "while" |
              "create" | "next" | "yield" | "exhaust" |
              "guard" |
              "type" |
              "ref" | "deref" |
              "bind" | "cons" | "is" | "mod" | "pow" |
              "typeswitch" | "default" | "case" |
              "multi" | "one" | "all" |
              "cont" | "reset" | "shift"
             ;
             
// Syntactic features that will be removed by the preprocessor. 
            
syntax Exp =
              preIntCon:				Integer txt
            | preStrCon:				String txt
            | preTypeCon:   			"type" String txt
			| preVar: 					Identifier id
			// *local* variables of functions used inside their closures and nested functions
			| preVar: 					FunNamePart+ funNames Identifier id 
			
			| preIfthen:    			"if" "(" Exp exp1 ")" "{" {Exp (NoNLList Sep NoNLList)}+ thenPart ";"? "}"
			| preAssignLocList:			"[" Identifier!fvar!rvar id1 "," Identifier!fvar!rvar id2 "]" "=" Exp exp
			> preList:					"[" {Exp ","}* exps "]"
			;
