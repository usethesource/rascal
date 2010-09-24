module zoo::pico::syntax::Main

start syntax Program = program: "begin" Decls decls {Statement  ";"}* body "end" ;

syntax Decls = "declare" {IdType ","}* decls ";" ;
 
syntax Statement = assign: Id var ":="  Exp val
                 | cond:   "if" Exp cond "then" {Statement ";"}*  thenPart "else" {Statement ";"}* elsePart
                 | cond:   "if" Exp cond "then" {Statement ";"}*  thenPart
                 | loop:   "while" Exp cond "do" {Statement ";"}* body "od"
                 ;

syntax IdType = Id id ":" Type type;
   
syntax Type = natural:"natural" 
            | string:"string" 
            | nil:"nil-type"
            ;

syntax Exp = id: Id name
           | strcon: Str string
           | natcon: Nat natcon;
           // | bracket "(" Exp e ")";
           // > concat: Exp lhs "||" Exp rhs
           // > left (add: Exp lhs "+" Exp rhs
                  // |min: Exp lhs "-" Exp rhs
                  // )
           // ;

syntax Exp = TransitiveClosure: Exp argument "+" 
	       | TransitiveReflexiveClosure: Exp argument "*" 
           > List      : "[" {Exp ","}* elements "]"
           ;
                
syntax Exp
	= NonEmptyBlock  : "{" Statement+ statements "}" 
	| bracket Bracket: "(" Exp expression ")" 
	| StepRange      : "[" Exp first "," Exp second ".." Exp last "]" 
	| Reducer        : "(" Exp init "|" Exp result "|" {Exp ","}+ generators ")" 
	| Any            : "any" "(" {Exp ","}+ generators ")" 
	| All            : "all" "(" {Exp ","}+ generators ")" 
	| Set            : "{" {Exp ","}* elements "}" 
	| Range          : "[" Exp first ".." Exp last "]" 
	| Tuple          : "\<" {Exp ","}+ elements "\>" 
	| List           : "[" {Exp ","}* elements "]"
	| It             : "it" 
	| right IfThenElse: Exp condition "?" Exp thenExp ":" Exp elseExp 
	>  Subscript  : Exp expression "[" {Exp ","}+ subscripts "]" 
	| FieldAccess : Exp expression "." Id field 
	| FieldUpdate : Exp expression "[" Id key "=" Exp replacement "]" 
	| Subscript   : Exp expression "[" {Exp ","}+ subscripts "]" 
	| CallOrTree  : Exp expression "(" {Exp ","}* arguments ")" 
	> IsDefined: Exp argument "?" 
	> Negation: "!" Exp argument 
	| Negative: "-" Exp argument 
	> TransitiveClosure: Exp argument "+" 
	| TransitiveReflexiveClosure: Exp argument "*" 
	> SetAnnotation: Exp expression "[" "@" Id name "=" Exp value "]" 
	| GetAnnotation: Exp expression "@" Id name 
	> left Composition: Exp lhs "o" Exp rhs 
	> left ( Product: Exp lhs "*" Exp rhs  
		   | Join   : Exp lhs "join" Exp rhs 
	       )
	> left ( Modulo: Exp lhs "%" Exp rhs  
		   | Division: Exp lhs "/" Exp rhs 
	       )
	> left Intersection: Exp lhs "&" Exp rhs 
	> left ( Addition   : Exp lhs "+" Exp rhs  
		   | Subtraction: Exp lhs "-" Exp rhs 
	       )
	> non-assoc (  NotIn: Exp lhs "notin" Exp rhs  
		        |  In: Exp lhs "in" Exp rhs 
	)
	> non-assoc ( Equals         : Exp lhs "==" Exp rhs   
	            | GreaterThanOrEq: Exp lhs "\>=" Exp rhs  
		        | LessThanOrEq   : Exp lhs "\<=" Exp rhs 
		        | LessThan       : Exp lhs "\<" Exp rhs 
		        | GreaterThan    : Exp lhs "\>" Exp rhs 
		        | NonEquals      : Exp lhs "!=" Exp rhs 
		        | Equals         : Exp lhs "==" Exp rhs
	            )
	> non-assoc IfDefinedOtherwise: Exp lhs "?" Exp rhs 
	> non-assoc ( Implication: Exp lhs "==\>" Exp rhs  
		        | Equivalence: Exp lhs "\<==\>" Exp rhs 
	            )
	> left And: Exp lhs "&&" Exp rhs 
	> left Or: Exp lhs "||" Exp rhs 
	> CallOrTree: Exp expression "(" {Exp ","}* arguments ")" 
   
	; 
           
syntax Id  = lex [a-z][a-z0-9]* # [a-z0-9];
syntax Nat = lex [0-9]+ ;
syntax Str = lex "\"" ![\"]*  "\"";

layout Pico = WhitespaceAndComment*  
            # [\ \t\n\r]
            # "%"
            ;

syntax WhitespaceAndComment 
   = lex [\ \t\n\r]
   | lex "%" ![%]* "%"
   | lex "%%" ![\n]* "\n"
   ;