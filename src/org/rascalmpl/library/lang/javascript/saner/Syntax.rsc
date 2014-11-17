@doc{
A saner grammar for JavaScript

It assumes:
- semicolons are present
- there is no comma expression
}

@contributor{
Tijs van der Storm - CWI (storm@cwi.nl)
}
module lang::javascript::saner::Syntax

start syntax Source 
  = source: Statement*
  ;

syntax Statement 
  = varDecl: VarDecl
  | empty: ";"
  | block: "{" Statement* "}" 
  | expression: Expression!function ";"
  
  // Block level things
  | function: Function
  | ifThen: "if" "(" Expression cond ")" Statement () !>> "else" 
  | ifThenElse: "if" "(" Expression cond ")" Statement "else" Statement
  | doWhile: "do" Statement "while" "(" Expression cond ")" ";"
  | whileDo: "while" "(" Expression cond ")" Statement
  | forDo: "for" "(" {Expression ","}* ";" {Expression ","}* conds ";" {Expression ","}* ops ")" Statement
  | forDoDeclarations: "for" "(" "var" {VariableDeclarationNoIn ","}+ ";" {Expression ","}* conds ";" {Expression ","}* ops ")" Statement  
  | forIn: "for" "(" Expression var "in" Expression obj ")" Statement
  | forInDeclaration: "for" "(" "var" Id "in" Expression obj ")" Statement
  | with: "with" "(" Expression scope ")" Statement

  // Non local control flow
  | returnExp: "return"  Expression exp ";"
  | returnNoExp: "return" ";"
  | throwExp: "throw" Expression expression ";"
  | throwNoExp: "throw" ";"
  | continueLabel: "continue" Id ";"
  | continueNoLabel: "continue" ";"
  | breakLabel: "break" Id ";"
  | breakNoLabel: "break" ";"
  | debugger: "debugger" ";"
  | labeled: Id ":" Statement
 
  | switchCase: "switch" "(" Expression cond ")" "{" CaseClause* clauses "}"
  | tryCatch: "try" Statement "catch" "(" Id ")" Statement
  | tryFinally: "try" Statement "finally" Statement
  | tryCatchFinally: "try" Statement "catch" "(" Id ")" Statement "finally" Statement
  ;

syntax VariableDeclaration 
  = init: Id id "=" Expression exp
  | nonInit: Id id
  ;

syntax VariableDeclarationNoIn
  = init: Id id "=" Expression!inn exp
  | nonInit: Id id
  ;


syntax CaseClause 
  = caseOf: "case" Expression ":" Statement*
  | defaultCase: "default" ":" Statement*
  ;
   
syntax Function
  = "function" Id name "(" {Id ","}* parameters ")" "{" Statement* "}"
  | "function" "(" {Id ","}* parameters ")" "{" Statement* "}"
  ;

// Todo: Check associativity https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Operator_Precedence
// Todo: Right now you can put any type of Expression on the lhs of a variableAssignment like: 5 = y; We only want to do this for a few cases however
// Rather than exclude everything other than those cases it would be much easier to whitelist the few that ARE allowed.
syntax Expression
  = array: "[" {Expression ","}*  ","? "]"
  | objectDefinition:"{" {PropertyAssignment ","}* ","? "}"
  | this: "this"
  | var: Id 
  | literal: Literal
  | bracket \bracket: "(" Expression ")" 
  | function: Function
  > property: Expression "." Id 
  | call: Expression "(" { Expression ","}* ")" 
  | member: Expression "[" Expression "]" 
  > new: "new" Expression
  > postIncr: Expression "++"
  | postDec: Expression "--"
  > delete: "delete" Expression
  | typeof: "typeof" Expression
  | preIncr: "++" Expression
  | preDecr: "--" Expression
  | prefixPlus: "+" !>> [+=] Expression
  | prefixMin: "-" !>> [\-=] Expression
  | binNeg: "~" Expression
  | not: "!" !>> [=] Expression
  >
  left (
      mul: Expression "*" !>> [*=] Expression
    | div: Expression "/" !>> [/=] Expression
    | rem: Expression "%" !>> [%=] Expression
  )
  >
  left (
      add: Expression "+" !>> [+=]  Expression
    | sub: Expression "-" !>> [\-=] Expression
  )
  > // right???
  left (
      shl: Expression "\<\<" Expression
    | shr: Expression "\>\>" !>> [\>] Expression
    | shrr: Expression "\>\>\>" Expression
  )
  >
  non-assoc (
      lt: Expression "\<" Expression
    | leq: Expression "\<=" Expression
    | gt: Expression "\>" Expression
    | geq: Expression "\>=" Expression
    | instanceof: Expression "instanceof" Expression
    | inn: Expression "in" Expression
  )
  >
  right (
      eqq: Expression "===" Expression
    | neqq: Expression "!==" Expression
    | eq: Expression "==" !>> [=] Expression 
    | neq: Expression "!=" !>> [=] Expression
  )
  > right binAnd: Expression "&" !>> [&=] Expression
  > right binXor: Expression "^" !>> [=] Expression
  > right binOr: Expression "|" !>> [|=] Expression
  > left and: Expression "&&" Expression
  > left or: Expression "||" Expression
  > cond: Expression!cond "?" Expression!cond ":" Expression
  > right (
      assign: Expression "=" !>> ([=][=]?) Expression
    | assignMul: Expression "*=" Expression
    | assignDiv: Expression "/=" Expression
    | assignRem: Expression "%=" Expression
    | assignAdd: Expression "+=" Expression
    | assignSub: Expression "-=" Expression
    | assignShl: Expression "\<\<=" Expression
    | assignShr: Expression "\>\>=" Expression
    | assignShrr: Expression "\>\>\>=" Expression
    | assignBinAnd: Expression "&=" Expression
    | assignBinXor: Expression "^=" Expression
    | assignBinOr: Expression "|=" Expression
  )
  ;
  
  
syntax VarDecl
  = "var" {VariableDeclaration ","}+ declarations ";"
  ;
  

syntax PropertyName
 = Id
 | String
 | Numeric
 ;

syntax PropertyAssignment
  = property: PropertyName ":" Expression
  | "get" PropertyName "(" ")" "{" Statement* "}"
  | "set" PropertyName "(" Id ")" "{" Statement* "}"
  ;


syntax Literal
 = "null"
 | Boolean
 | Numeric
 | String
 | RegularExpression
 ;

syntax Boolean
  = "true"
  | "false"
  ;

syntax Numeric
  = [a-zA-Z$_0-9] !<< Decimal
  | [a-zA-Z$_0-9] !<< HexInteger
  ;

lexical Decimal
  = DecimalInteger [.] [0-9]* ExponentPart?
  | [.] [0-9]+ ExponentPart?
  | DecimalInteger ExponentPart?
  ;

lexical DecimalInteger
  = [0]
  | [1-9][0-9]*
  !>> [0-9]
  ;

lexical ExponentPart
  = [eE] SignedInteger
  ;

lexical SignedInteger
  = [+\-]? [0-9]+ !>> [0-9]
  ;

lexical HexInteger
  = [0] [Xx] [0-9a-fA-F]+ !>> [a-zA-Z_]
  ;

lexical String
  = [\"] DoubleStringChar* [\"]
  | [\'] SingleStringChar* [\']
  ;

lexical DoubleStringChar
  = ![\"\\\n]
  | [\\] EscapeSequence
  ;

lexical SingleStringChar
  = ![\'\\\n]
  | [\\] EscapeSequence
  ;



lexical EscapeSequence
  = CharacterEscapeSequence
  | [0] !>> [0-9]
  | HexEscapeSequence
  | UnicodeEscapeSequence
  ;

lexical CharacterEscapeSequence
  = SingleEscapeCharacter
  | NonEscapeCharacter
  ;

lexical SingleEscapeCharacter
  = [\'\"\\bfnrtv]
  ;

lexical NonEscapeCharacter
  // SourceCharacter but not one of EscapeCharacter or LineTerminator
  = ![\n\'\"\\bfnrtv0-9xu]
  ;

lexical EscapeCharacter
  = SingleEscapeCharacter
  | [0-9]
  | [xu]
  ;


  
lexical HexDigit
  = [a-fA-F0-9]
  ;

lexical HexEscapeSequence
  = [x] HexDigit HexDigit
  ;

syntax UnicodeEscapeSequence
  = "u" HexDigit HexDigit HexDigit HexDigit
  ;

lexical RegularExpression
  = [/] RegularExpressionBody [/] RegularExpressionFlags
  ;

lexical RegularExpressionBody
  = RegularExpressionFirstChar RegularExpressionChar*
  ;

lexical RegularExpressionFirstChar
  = ![*/\[\n\\]
  | RegularExpressionBackslashSequence
  | RegularExpressionClass
  ;

lexical RegularExpressionChar
  = ![/\[\n\\]
  | RegularExpressionBackslashSequence
  | RegularExpressionClass
  ;

lexical RegularExpressionBackslashSequence
  = [\\] ![\n]
  ;

lexical RegularExpressionClass
  = [\[] RegularExpressionClassChar* [\]]
  ;

lexical RegularExpressionClassChar
  = ![\n\]\\]
  | RegularExpressionBackslashSequence
  ;

lexical RegularExpressionFlags
  = [a-zA-Z]* !>> [a-zA-Z]
  ;


lexical Whitespace
  = [\t-\n\r\ ]
  ;

lexical Comment
  = @category="Comment" "/*" CommentChar* "*/"
  | @category="Comment" "//" ![\n]*  $
  ;

lexical CommentChar
  = ![*]
  | [*] !>> [/]
  ;


lexical LAYOUT
  = Whitespace
  | Comment
  ;

layout LAYOUTLIST
  = LAYOUT*
  !>> [\t\ \n]
  !>> "/*"
  !>> "//" ;


lexical Id 
  = ([a-zA-Z$_0-9] !<< [$_a-zA-Z] [a-zA-Z$_0-9]* !>> [a-zA-Z$_0-9]) \ Reserved
  ;


keyword Reserved =
    "break" |
    "case" |
    "catch" |
    "continue" |
    "debugger" |
    "default" |
    "delete" |
    "do" |
    "else" |
    "finally" |
    "for" |
    "function" |
    "if" |
    "instanceof" |
    "in" |
    "new" |
    "return" |
    "switch" |
    "this" |
    "throw" |
    "try" |
    "typeof" |
    "var" |
    "void" |
    "while" |
    "with"
    "abstract" |
    "boolean" |
    "byte" |
    "char" |
    "class" |
    "const" |
    "double" |
    "enum" |
    "export" |
    "extends" |
    "final" |
    "float" |
    "goto" |
    "implements" |
    "import" |
    "interface" |
    "int" |
    "long" |
    "native" |
    "package" |
    "private" |
    "protected" |
    "public" |
    "short" |
    "static" |
    "super" |
    "synchronized" |
    "throws" |
    "transient" |
    "volatile" |
    "null" |
    "true" |
    "false"
  ;

