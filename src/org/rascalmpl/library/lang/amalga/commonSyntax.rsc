module lang::amalga::commonSyntax

layout Layout = WhiteSpaceAndComment* !>> [\ \t\n\r#];

lexical WhiteSpaceAndComment 
	= [\ \t\n\r]
	| @category="Comment" ws2: "#" ![#]+ "#"
	| @category="Comment" ws3: "##" ![\n]* $
	;
  
lexical Id 
	= name: [a-zA-Z][a-zA-Z0-9]*  !>> [a-zA-Z0-9] \Keywords
    | operator: "`" [!@#$%^&*_+/] op "`" Id? name
    ;
    
lexical IntegerLiteral = [0-9]+ !>> [0-9];

lexical String = "\"" ![\"\n]* "\""; 

lexical Bool  = "false"| "true";

syntax Expression = Exp";";

syntax Type 
  = imgWithIndices: "img" "[" Id x "," Id y "]"
  |imgWithIndicesWithColor: "img" "[" Id x "," Id y "," Id c"]"
  | img: "img"
  | \int: "int"
  | uint8: "uint8_t"
  | uint16: "uint16_t"
  | uint32: "uint32_t"
  | int32: "int32_t"
  | vector: "[" {Exp ","}* params"]"
  | cast: Type s "\<"Type tpCast"\>"
  ;
  
  syntax Exp 
  = boolean : Bool
  | number: IntegerLiteral
  | string: String
  | var: Id id \Keywords
  | call: Id id "(" {Exp ","}* parameters ")"
  | let: Id id "=" Exp body
  | mat: Id id "[" {Exp ","}* params"]"
  | let: Exp left "=" "(" Type type ")" typeExp Exp body
  | truthCases: "case" ":" {Exp "|"}+ values "-\>" Exp val";"
  | stackcolumns: Exp receiver "[" ":" "]"
  | cast2: "cast" "\<" Type type"\>" "(" Exp expression ")"
  | range: Exp ":" Exp
  | compose: Exp lhs "o" Exp rhs
  | result: "return" Exp function
  | bracket "(" Exp ")"
  | min: "min" "(" Exp val "," Exp val2 ")"
  | dot: Exp left"." Exp right
  > left square: Exp lhs "^" [2]
  > left mul: Exp lhs "*" Exp rhs
  > left assign: Exp lhs "=" Exp rhs 
  > right div: Exp lhs "/" Exp rhs
  > left ( add: Exp lhs "+" Exp rhs
         | sub: Exp lhs "-" Exp rhs
         )
  > xor: Exp "^^" Exp
  > non-assoc ( eq: Exp "==" Exp
              | neq: Exp "!=" Exp
              | less: Exp "\<" Exp
              | greater: Exp "\>" Exp
              | lesseq: Exp "\<=" Exp
              | greaterrq: Exp "\>=" Exp
              ) 
  > and: Exp "&&" Exp
  > or:  Exp "||" Exp
  > not: "!" Exp
 ;  
 
 keyword Keywords  
	= "true" 
	| "false" 
	| "let" 
	| "in" 
	| "o" 
	| "Evaluate" 
	| "function" 
	| "zeros"
	| "ones" 
	| "identity" 
	| "return" 
	| "randomFloat"
	| "randomUint"
	| "randomInt" 
	| "size" 
	| "while" 
	| "for" 
	| "end"
	| "func"
	| "exec"
	| "loadImage"
	| "run"
	| "renderImage"
	| "save"
	| "assert"
	| "img"
  	| "int"
  	| "uint8_t"
  	| "uint16_t"
  	| "uint32_t"
 	| "int32_t"
	;