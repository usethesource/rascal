@synopsis{A saner grammar for JavaScript

It assumes:
- semicolons are present
- there is no comma expression}

@contributor{
Tijs van der Storm - CWI (storm@cwi.nl)
}
module lang::javascript::saner::Syntax

start syntax Source 
  = source: Statement* statements
  ; 

syntax Statement 
  = varDecl: VarDecl varDecl
  | empty: ";"
  | block: "{" Statement* statements "}" 
  | expression: Expression!function expression ";"
  
  // Block level things
  | function: Function function
  | ifThen: "if" "(" Expression cond ")" Statement body () !>> "else" 
  | ifThenElse: "if" "(" Expression cond ")" Statement body "else" Statement elseBody
  | doWhile: "do" Statement body "while" "(" Expression cond ")" ";"
  | whileDo: "while" "(" Expression cond ")" Statement body
  | forDo: "for" "(" {Expression ","}* inits ";" {Expression ","}* conds ";" {Expression ","}* ops ")" Statement body
  | forDoDeclarations: "for" "(" "var" {VariableDeclarationNoIn ","}+ decls ";" {Expression ","}* conds ";" {Expression ","}* ops ")" Statement body  
  | forIn: "for" "(" Expression var "in" Expression obj ")" Statement body
  | forInDeclaration: "for" "(" "var" Id varId "in" Expression obj ")" Statement body
  | with: "with" "(" Expression scope ")" Statement body

  // Non local control flow
  | returnExp: "return"  Expression result ";"
  | returnNoExp: "return" ";"
  | throwExp: "throw" Expression result ";"
  | throwNoExp: "throw" ";"
  | continueLabel: "continue" Id label ";"
  | continueNoLabel: "continue" ";"
  | breakLabel: "break" Id label ";"
  | breakNoLabel: "break" ";"
  | debugger: "debugger" ";"
  | labeled: Id label ":" Statement statement
 
  | switchCase: "switch" "(" Expression cond ")" "{" CaseClause* clauses "}"
  | tryCatch: "try" Statement body "catch" "(" Id varId ")" Statement catchBody
  | tryFinally: "try" Statement body "finally" Statement finallyBody
  | tryCatchFinally: "try" Statement body "catch" "(" Id varId ")" Statement catchBody "finally" Statement finallyBody
  ;

syntax VariableDeclaration 
  = init: Id id "=" Expression init
  | nonInit: Id id
  ;

syntax VariableDeclarationNoIn
  = init: Id id "=" Expression!inn init
  | nonInit: Id id
  ;


syntax CaseClause 
  = caseOf: "case" Expression guard ":" Statement* body
  | defaultCase: "default" ":" Statement* body
  ;
   
syntax Function
  = "function" Id name "(" {Id ","}* parameters ")" "{" Statement* statements "}"
  | "function" "(" {Id ","}* parameters ")" "{" Statement* statements "}"
  ;

// Todo: Check associativity https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Operator_Precedence
// Todo: Right now you can put any type of Expression on the lhs of a variableAssignment like: 5 = y; We only want to do this for a few cases however
// Rather than exclude everything other than those cases it would be much easier to whitelist the few that ARE allowed.
syntax Expression
  = array: "[" {Expression ","}* elements  ","? "]"
  | objectDefinition:"{" {PropertyAssignment ","}* properties ","? "}"
  | this: "this"
  | var: Id name
  | literal: Literal literal
  | bracket \bracket: "(" Expression arg ")" 
  | function: Function function
  > property: Expression obj "." Id fieldId 
  | call: Expression func "(" { Expression ","}* params ")" 
  | member: Expression obj "[" Expression field "]" 
  > new: "new" Expression cons
  > postIncr: Expression arg "++"
  | postDec: Expression arg "--"
  > delete: "delete" Expression arg
  | typeof: "typeof" Expression arg
  | preIncr: "++" Expression arg
  | preDecr: "--" Expression arg
  | prefixPlus: "+" !>> [+=] Expression arg
  | prefixMin: "-" !>> [\-=] Expression arg
  | binNeg: "~" Expression arg
  | not: "!" !>> [=] Expression arg
  >
  left (
      mul: Expression lhs "*" !>> [*=] Expression rhs
    | div: Expression lhs "/" !>> [/=] Expression rhs
    | rem: Expression lhs "%" !>> [%=] Expression rhs
  )
  >
  left (
      add: Expression lhs "+" !>> [+=]  Expression rhs
    | sub: Expression lhs "-" !>> [\-=] Expression rhs
  )
  > // right???
  left (
      shl: Expression lhs "\<\<" Expression rhs
    | shr: Expression lhs "\>\>" !>> [\>] Expression rhs
    | shrr: Expression lhs "\>\>\>" Expression rhs
  )
  >
  non-assoc (
      lt: Expression lhs "\<" Expression rhs
    | leq: Expression lhs "\<=" Expression rhs
    | gt: Expression lhs "\>" Expression rhs
    | geq: Expression lhs "\>=" Expression rhs
    | instanceof: Expression lhs "instanceof" Expression rhs
    | inn: Expression lhs "in" Expression rhs
  )
  >
  right (
      eqq: Expression lhs "===" Expression rhs
    | neqq: Expression lhs "!==" Expression rhs
    | eq: Expression lhs "==" !>> [=] Expression rhs
    | neq: Expression lhs "!=" !>> [=] Expression rhs
  )
  > right binAnd: Expression lhs "&" !>> [&=] Expression rhs
  > right binXor: Expression lhs "^" !>> [=] Expression rhs
  > right binOr: Expression lhs "|" !>> [|=] Expression rhs
  > left and: Expression lhs "&&" Expression rhs
  > left or: Expression lhs "||" Expression rhs
  > cond: Expression!cond cond "?" Expression!cond then ":" Expression elseExp
  > right (
      assign: Expression lhs "=" !>> [=] Expression rhs
    | assignMul: Expression lhs "*=" Expression rhs
    | assignDiv: Expression lhs "/=" Expression rhs
    | assignRem: Expression lhs "%=" Expression rhs
    | assignAdd: Expression lhs "+=" Expression rhs
    | assignSub: Expression lhs "-=" Expression rhs
    | assignShl: Expression lhs "\<\<=" Expression rhs
    | assignShr: Expression lhs "\>\>=" Expression rhs
    | assignShrr: Expression lhs "\>\>\>=" Expression rhs
    | assignBinAnd: Expression lhs "&=" Expression rhs
    | assignBinXor: Expression lhs "^=" Expression rhs
    | assignBinOr: Expression lhs "|=" Expression rhs
  )
  ;
  
  
syntax VarDecl
  = "var" {VariableDeclaration ","}+ declarations ";"
  ;
  

syntax PropertyName
 = id: Id name
 | string: String key
 | numeric: Numeric numeric
 ;

syntax PropertyAssignment
  = property: PropertyName name ":" Expression value
  | get: "get" PropertyName name "(" ")" "{" Statement* body "}"
  | \set: "set" PropertyName name "(" Id x ")" "{" Statement* body "}"
  ;


syntax Literal
 = null: "null"
 | boolean: Boolean bool
 | numeric: Numeric num
 | string: String str
 | regexp: RegularExpression regexp
 ;

syntax Boolean
  = t: "true"
  | f: "false"
  ;

syntax Numeric
  = decimal: [a-zA-Z$_0-9] !<< Decimal decimal
  | hexadecimal: [a-zA-Z$_0-9] !<< HexInteger hexInt
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

lexical UnicodeEscapeSequence
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
  = @category="comment" "/*" CommentChar* "*/"
  | @category="comment" "//" ![\n]*  $
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
    "with" |
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

