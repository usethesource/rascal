module lang::hotdrink::Expressions

extend lang::hotdrink::Lexical;

syntax Expression
  = Name
  | @category="Constant" Number 
  | Boolean
  | @category="StringLiteral" String
  | "empty"
  | Array
  | Identifier
  | Dictionary
  | bracket "(" Expression ")"
  | Expression "[" Expression "]"
  | Expression "." Identifier
  | Expression "(" {Expression ","}* ")"  // more permissive to allow JS stuff
  > "+" Expression 
  | "-" Expression
  | "!" Expression
  > left (
        Expression "*" Expression
      | Expression "/" Expression
	  | Expression "%" Expression  
  )
  > left (
  	    Expression "+" Expression
  	  | Expression "-" Expression
  )
  > non-assoc (
  	    Expression "\<" Expression
  	  | Expression "\<=" Expression
  	  | Expression "\>=" Expression
  	  | Expression "\>" Expression
  )
  > non-assoc (
  		Expression "==" Expression
  	  | Expression "!=" Expression
  )
  > left Expression "&&" Expression
  > left Expression "||" Expression
  > Expression "?" Expression ":" Expression
  ;

syntax Array
  = "[" {Expression ","}* "]"
  ;

syntax Dictionary
  = "{" {NamedArgument ","}* "}"
  ;
  
syntax NamedArgument
  = Identifier ":" Expression
  ;
  
syntax Name
  = "@" Identifier
  ;
  
syntax Boolean
  = "true"
  | "false"
  ;