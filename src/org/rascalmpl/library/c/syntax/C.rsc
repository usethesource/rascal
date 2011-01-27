module c::syntax::C

syntax Statement = "{" Declaration* Statement*  "}" |
                   Identifier ":" Statement |
                   "case" Expression ":" Statement |
                   "default" ":" Statement |
                   ";" |
                   Expression ";" |
                   "if" "(" Expression ")" Statement |
                   "if" "(" Expression ")" Statement "else" Statement |
                   "switch" "(" Expression ")" Statement |
                   "while" "(" Expression ")" Statement |
                   "do" Statement "while" "(" Expression ")" ";" |
                   "for" "(" Expression? ";" Expression? ";" Expression? ")" Statement |
                   "goto" Identifier ";" |
                   "continue" ";" |
                   "break" ";" |
                   "return" ";" |
                   "return" Expression ";"
                   ;

syntax Expression = Identifier |
                    HexadecimalConstant |  // {category("Constant")}
                    IntegerConstant |  // {category("Constant")}
                    CharacterConstant |  // {category("Constant")}
                    FloatingPointConstant |  // {category("Constant")}
                    StringConstant |  // {category("Constant")}
                    "sizeof" "(" TypeName ")" |
                    bracket "(" Expression ")"
                    ;

syntax Identifier = lex [a-zA-Z\_][a-zA-Z\_0-9]*
                    # [0-9a-zA-Z\_]
                    - Keyword
                    ;

syntax AnonymousIdentifier = 
                             ;

syntax Keyword = "auto" |
                 "break" |
                 "case" |
                 "char" |
                 "const" |
                 "continue" |
                 "default" |
                 "do" |
                 "double" |
                 "else" |
                 "enum" |
                 "extern" |
                 "float" |
                 "for" |
                 "goto" |
                 "if" |
                 "int" |
                 "long" |
                 "register" |
                 "return" |
                 "short" |
                 "signed" |
                 "sizeof" |
                 "static" |
                 "struct" |
                 "switch" |
                 "typedef" |
                 "union" |
                 "unsigned" |
                 "void" |
                 "volatile" |
                 "while"
                 # [0-9a-zA-Z\_]
                 ;

syntax Declaration = Specifier+ {InitDeclarator ","}+ ";" |
                     Specifier+ ";"  ///////////////// {avoid}
                     ;

syntax InitDeclarator = Declarator |
                        Declarator "=" Initializer
                       ;

syntax Specifier = Identifier |
                   "typedef" |
                   "extern" |
                   "static" |
                   "auto" |
                   "register" |
                   "void" |
                   "char" |
                   "short" |
                   "int" |
                   "long" |
                   "float" |
                   "double" |
                   "signed" |
                   "unsigned" |
                   "const" |
                   "volatile" |
                   "struct"|"union" Identifier "{" StructDeclaration+ "}" |
                   "struct"|"union" "{" StructDeclaration+ "}" |
                   "struct"|"union" Identifier |
                   "enum" "{" {Enumerator ","}+  "}" |
                   "enum" Identifier "{" {Enumerator ","}+  "}" |
                   "enum" Identifier
                   ;

syntax StructDeclaration = Specifier+ {StructDeclarator ","}+ ";"
                           ;

syntax StructDeclarator = Declarator |
                          ":" Expression |
                          Declarator ":" Expression
                          ;

syntax Parameters = {Parameter ","}+ MoreParameters?
                    ;

syntax MoreParameters = "," "..."
                        ;

syntax Initializer = Expression |
                     "{" {Initializer ","}+ ","?  "}"
                     ;

syntax TypeName = Specifier+ AbstractDeclarator
                  ;

syntax Pointer = ("*" Specifier*)+
                 ;

syntax Enumerator = Identifier |
                    Identifier "=" Expression
                    ;

syntax AbstractDeclarator = 
                            ;

syntax Declarator = non-assoc Pointer Declarator |
                    Identifier |
                    bracket "(" Declarator ")" |
                    Declarator "[" Expression? "]" |
                    Declarator "(" Parameters? ")"
                    ;

syntax Parameter = Specifier+ Declarator |
                   Specifier+ AbstractDeclarator
                   ;

syntax HexadecimalConstant = lex [0] [xX] [a-fA-F0-9]+ [uUlL]*
                             # [a-fA-F0-9]
                             ;

syntax IntegerConstant = lex [0-9]+ [uUlL]*
                         # [0-9]
                         ;

syntax CharacterConstant = lex [L]? [\'] (([\\]~[])|~[\\\'])+ [\']
                           ;

syntax FloatingPointConstant = lex [0-9]+ Exponent [fFlL]? |
                               lex [0-9]* [\.] [0-9]+ Exponent? [fFlL]? |
                               lex [0-9]+ [\.] Exponent? [fFlL]?
                               # [0-9]
                               ;

syntax StringConstant = lex [L]? [\"] ( ([\\]~[]) | ~[\\\"] )* [\"]
                        ;

syntax Exponent = lex [Ee] [\+\-]? [0-9]+
                  ;

syntax ExternalDeclaration = FunctionDefinition |
                             Declaration
                             ;

syntax FunctionDefinition = Specifier* Declarator Declaration* "{" Declaration* Statement* "}"
                            ;

syntax TranslationUnit = ExternalDeclaration+
                         ;


//////////////////////////// SDF /////////////////////////


context-free syntax

Expression                -> {Expression ","}+ 


context-free restrictions

"&" -/- [\&]
"-" -/- [\-]
"+" -/- [\+]


context-free priorities

Expression                -> Initializer >
Expression "," Expression -> Expression,

Expression                -> {Expression ","}+ >
Expression "," Expression -> Expression,

Identifier "=" Expression -> Enumerator >
Expression "," Expression -> Expression

{
Declarator "(" Parameters? ")" 		-> Declarator
Declarator "[" Expression? "]" 	-> Declarator
}
> Pointer Declarator 			-> Declarator

{
Expression "[" Expression "]"	  -> Expression 
Expression "(" {Expression ","}* ")"  -> Expression 
Expression "." Identifier 		  -> Expression 
Expression "->" Identifier 	  -> Expression
Expression "++" 		           -> Expression
Expression "--" 		           -> Expression
} <0>
> 
{
"++" Expression 		 -> Expression
"--" Expression 		 -> Expression
"&" Expression 		 -> Expression
"*" Expression 	 	 -> Expression
"+" Expression 		 -> Expression
"-" Expression 		 -> Expression
"~" Expression 		 -> Expression
"!" Expression 		 -> Expression
"sizeof" Expression 	 -> Expression
"(" TypeName ")" Expression -> Expression 
}
> { left:
Expression "*" Expression -> Expression {left}
Expression "/" Expression -> Expression {left}
Expression "%" Expression -> Expression {left}}
> { left:
Expression "+" Expression -> Expression {left}
Expression "-" Expression -> Expression {left}}
> { left:
Expression "<<" Expression -> Expression {left}
Expression ">>" Expression -> Expression {left}}
> { left:
Expression "<" Expression -> Expression {left}
Expression ">" Expression -> Expression {left}
Expression "<=" Expression -> Expression {left}
Expression ">=" Expression -> Expression {left}
} >
{left:
Expression "==" Expression -> Expression {left}
Expression "!=" Expression -> Expression {left}
}
> Expression "&" Expression -> Expression {left}
> Expression "^" Expression -> Expression {left}
> Expression "|" Expression -> Expression {left}
> Expression "&&" Expression -> Expression {left}
> Expression "||" Expression -> Expression {left}
> Expression "?" Expression ":" Expression -> Expression {right}
<0,4> > 
{right:
Expression "=" Expression -> Expression {right}
Expression "*=" Expression -> Expression {right}
Expression "/=" Expression -> Expression {right}
Expression "%=" Expression -> Expression {right}
Expression "+=" Expression -> Expression {right}
Expression "-=" Expression -> Expression {right}
Expression "<<=" Expression -> Expression {right}
Expression ">>=" Expression -> Expression {right}
Expression "&=" Expression -> Expression {right}
Expression "^=" Expression -> Expression {right}
Expression "|=" Expression -> Expression {right}
}
>
Expression "," Expression -> Expression {left}

Expression "?" Expression ":" Expression -> Expression {right}
 <0> >
Expression "?" Expression ":" Expression -> Expression
