module rascal::syntax::RascalRascal


syntax BooleanLiteral
	= lex "true" 
	| lex "false" ;

syntax Literal
	= String: StringLiteral stringLiteral 
	| RegExp: RegExpLiteral regExpLiteral 
	| Boolean: BooleanLiteral booleanLiteral 
	| Real: RealLiteral realLiteral 
	| Integer: IntegerLiteral integerLiteral 
	| DateTime: DateTimeLiteral dateTimeLiteral 
	| Location: LocationLiteral locationLiteral ;

syntax Module
	= Default: Header header Body body ;

syntax Alternative
	= NamedType: Name name Type type ;

syntax ModuleParameters
	= Default: "[" {TypeVar ","}+ parameters "]" ;

syntax DateAndTime
	= lex "$" DatePart "T" TimePartNoTZ TimeZonePart? ;

syntax UnicodeEscape
	= lex "\\" [u]+ [0-9A-Fa-f] [0-9A-Fa-f] [0-9A-Fa-f] [0-9A-Fa-f] ;

syntax Variable
	= Initialized: Name name "=" Expression initial 
	| UnInitialized: Name name ;

syntax OctalIntegerLiteral
	= lex [0] [0-7]+ 
	# [0-9A-Z_a-z] ;

syntax TypeArg
	= Default: Type type 
	| Named: Type type Name name ;

syntax Catch
	= Binding: "catch" Pattern pattern ":" Statement body 
	| Default: "catch" ":" Statement body ;

syntax Renaming
	= Default: Name from "=\>" Name to ;

syntax PathChars
	= lex URLChars [|] ;

syntax HexLongLiteral
	= lex [0] [Xx] [0-9A-Fa-f]+ [Ll] 
	# [0-9A-Z_a-z] ;

syntax Signature
	= NoThrows: Type type FunctionModifiers modifiers Name name Parameters parameters 
	| WithThrows: Type type FunctionModifiers modifiers Name name Parameters parameters "throws" {Type ","}+ exceptions ;

syntax TimePartNoTZ
	= lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] [,.] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] [,.] [0-9] [0-9] [0-9] 
	| lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] 
	| lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] [,.] [0-9] [0-9] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] [,.] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] [,.] [0-9] [0-9] 
	| lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] [,.] [0-9] [0-9] ;

syntax DecimalLongLiteral
	= lex "0" [Ll] 
	| lex [1-9] [0-9]* [Ll] 
	# [0-9A-Z_a-z] ;

syntax Header
	= Parameters: Tags tags "module" QualifiedName name ModuleParameters params Import* imports 
	| Default: Tags tags "module" QualifiedName name Import* imports ;

syntax Name
	= lex [A-Z_a-z] [0-9A-Z_a-z]* 
	| lex EscapedName 
	# [0-9A-Z_a-z] 
	- /*reject()*/ RascalReservedKeywords ;

syntax Kind
	= All: "all" 
	| Variable: "variable" 
	| Anno: "anno" 
	| Data: "data" 
	| Function: "function" 
	| Rule: "rule" 
	| View: "view" 
	| Alias: "alias" 
	| Module: "module" 
	| Tag: "tag" ;

syntax Test
	= Unlabeled: Tags tags "test" Expression expression 
	| Labeled: Tags tags "test" Expression expression ":" StringLiteral labeled ;

syntax ImportedModule
	= Default: QualifiedName name 
	| ActualsRenaming: QualifiedName name ModuleActuals actuals Renamings renamings 
	| Renamings: QualifiedName name Renamings renamings 
	| Actuals: QualifiedName name ModuleActuals actuals ;

syntax IntegerLiteral
	= /*prefer()*/ HexIntegerLiteral: HexIntegerLiteral hex 
	| /*prefer()*/ OctalIntegerLiteral: OctalIntegerLiteral octal 
	| /*prefer()*/ DecimalIntegerLiteral: DecimalIntegerLiteral decimal ;

syntax Target
	= Labeled: Name name 
	| Empty: ;

syntax FunctionBody
	= Default: "{" Statement* statements "}" ;

syntax Expression
	= Tuple: "\<" {Expression ","}+ elements "\>" 
	| bracket Bracket: "(" Expression expression ")" 
	| StepRange: "[" Expression first "," Expression second ".." Expression last "]" 
	| Visit: Label label Visit visit 
	| Set: "{" {Expression ","}* elements "}" 
	| Subscript: Expression expression "[" {Expression ","}+ subscripts "]" 
	> ReifyType: "#" Type type 
	| ReifiedType: BasicType basicType "(" {Expression ","}* arguments ")" 
	| Literal: Literal literal 
	| ReifyType: "#" Type type 
	| NonEmptyBlock: "{" Statement+ statements "}" 
	| Range: "[" Expression first ".." Expression last "]" 
	| CallOrTree: Expression expression "(" {Expression ","}* arguments ")" 
	| Expression "*" Expression 
	| Expression "+" Expression 
	> "-" Expression 
	| VoidClosure: Parameters parameters "{" Statement* statements "}" 
	| Closure: Type type Parameters parameters "{" Statement+ statements "}" 
	| QualifiedName: QualifiedName qualifiedName 
	| CallOrTree: Expression expression "(" {Expression ","}* arguments ")" 
	| FieldAccess: Expression expression "." Name field 
	| FieldUpdate: Expression expression "[" Name key "=" Expression replacement "]" 
	| ReifiedType: BasicType basicType "(" {Expression ","}* arguments ")" 
	| Subscript: Expression expression "[" {Expression ","}+ subscripts "]" 
	| FieldProject: Expression expression "\<" {Field ","}+ fields "\>" 
	> IsDefined: Expression argument "?" 
	> Negative: "-" Expression argument 
	| Negation: "!" Expression argument 
	> TransitiveClosure: Expression argument "+" 
	| TransitiveReflexiveClosure: Expression argument "*" 
	> SetAnnotation: Expression expression "[" "@" Name name "=" Expression value "]" 
	| GetAnnotation: Expression expression "@" Name name 
	> left Composition: Expression lhs "o" Expression rhs 
	> left (  left Product: Expression lhs "*" Expression rhs  
		> left Join: Expression lhs "join" Expression rhs 
	)
	> left (  left Modulo: Expression lhs "%" Expression rhs  
		> left Division: Expression lhs "/" Expression rhs 
	)
	> left Intersection: Expression lhs "&" Expression rhs 
	> left (  left Subtraction: Expression lhs "-" Expression rhs  
		> left Addition: Expression lhs "+" Expression rhs 
	)
	> non-assoc (  non-assoc In: Expression lhs "in" Expression rhs  
		> non-assoc NotIn: Expression lhs "notin" Expression rhs 
	)
	> non-assoc (  non-assoc LessThanOrEq: Expression lhs "\<=" Expression rhs  
		> non-assoc LessThan: Expression lhs "\<" Expression rhs 
		> non-assoc GreaterThan: Expression lhs "\>" Expression rhs 
		> non-assoc GreaterThanOrEq: Expression lhs "\>=" Expression rhs 
	)
	> left (  right IfThenElse: Expression condition "?" Expression thenExp ":" Expression elseExp  
		> left NonEquals: Expression lhs "!=" Expression rhs 
		> left Equals: Expression lhs "==" Expression rhs 
	)
	> non-assoc IfDefinedOtherwise: Expression lhs "?" Expression rhs 
	> non-assoc (  right Implication: Expression lhs "==\>" Expression rhs  
		> right Equivalence: Expression lhs "\<==\>" Expression rhs 
	)
	> left And: Expression lhs "&&" Expression rhs 
	> left Or: Expression lhs "||" Expression rhs 
	| List: "[" {Expression ","}* elements "]" 
	| Map: "(" {Mapping[Expression] ","}* mappings ")" ;

syntax UserType
	= Parametric: QualifiedName name "[" {Type ","}+ parameters "]" 
	| Name: QualifiedName name ;

syntax Import
	= Default: "import" ImportedModule module ";" 
	| Extend: "extend" ImportedModule module ";" 
	| Syntax: SyntaxDefinition syntax ;

syntax Body
	= Toplevels: Toplevel* toplevels ;

syntax URLChars
	= lex [\000-\b\013-\f\016-\037!-;=-{}-\u15151515]* ;

syntax TimeZonePart
	= lex [+\-] [0-1] [0-9] [0-5] [0-9] 
	| lex "Z" 
	| lex [+\-] [0-1] [0-9] ":" [0-5] [0-9] 
	| lex [+\-] [0-1] [0-9] ;

syntax ProtocolPart
	= Interpolated: PreProtocolChars pre Expression expression ProtocolTail tail 
	| NonInterpolated: ProtocolChars protocolChars ;

syntax StringTemplate
	= DoWhile: "do" "{" Statement* preStats StringMiddle body Statement* postStats "}" "while" "(" Expression condition ")" 
	| While: "while" "(" Expression condition ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| For: "for" "(" {Expression ","}+ generators ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| IfThenElse: "if" "(" {Expression ","}+ conditions ")" "{" Statement* preStatsThen StringMiddle thenString Statement* postStatsThen "}" "else" "{" Statement* preStatsElse StringMiddle elseString Statement* postStatsElse "}" 
	| IfThen: "if" "(" {Expression ","}+ conditions ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" ;

syntax PreStringChars
	= lex [\"] StringCharacter* [\<] ;

syntax CaseInsensitiveStringConstant
	= /*term(category("Constant"))*/ lex "\'" StringCharacter* "\'" ;

syntax Backslash
	= lex [\\] 
	# [/\<\>\\] ;

syntax Label
	= Default: Name name ":" 
	| Empty: ;

syntax NoElseMayFollow
	= Default: 
	# [e] [l] [s] [e] ;

syntax MidProtocolChars
	= lex "\>" URLChars "\<" ;

syntax NamedBackslash
	= lex [\\] 
	# [\<\>\\] ;

syntax Field
	= Index: IntegerLiteral fieldIndex 
	| Name: Name fieldName ;

syntax JustDate
	= lex "$" DatePart ;

syntax PostPathChars
	= lex "\>" URLChars "|" ;

syntax PathPart
	= Interpolated: PrePathChars pre Expression expression PathTail tail 
	| NonInterpolated: PathChars pathChars ;

syntax DatePart
	= lex [0-9] [0-9] [0-9] [0-9] [0-1] [0-9] [0-3] [0-9] 
	| lex [0-9] [0-9] [0-9] [0-9] "-" [0-1] [0-9] "-" [0-3] [0-9] ;

syntax FunctionModifier
	= Java: "java" ;

syntax Assignment
	= Addition: "+=" 
	| Division: "/=" 
	| Product: "*=" 
	| Intersection: "&=" 
	| Subtraction: "-=" 
	| Default: "=" 
	| IfDefined: "?=" ;

syntax Assignable
	= FieldAccess: Assignable receiver "." Name field 
	| Subscript: Assignable receiver "[" Expression subscript "]" 
	| IfDefinedOrDefault: Assignable receiver "?" Expression defaultExpression 
	> non-assoc (  non-assoc Annotation: Assignable receiver "@" Name annotation  
		> Tuple: "\<" {Assignable ","}+ elements "\>" 
		> non-assoc Constructor: Name name "(" {Assignable ","}+ arguments ")" 
	)
	| Variable: QualifiedName qualifiedName ;

syntax StringConstant
	= /*term(category("Constant"))*/ lex "\"" StringCharacter* "\"" ;

syntax TagChar
	= lex [\\] [\\}] 
	| lex [\000-|~-\u15151515] ;

syntax DataTarget
	= Empty: 
	| Labeled: Name label ":" ;

syntax StringCharacter
	= lex OctalEscapeSequence 
	| lex UnicodeEscape 
	| lex "\\" [\"\'\<\>\\bfnrt] 
	| lex [\000-!#-&(-;=?-\[\]-\u15151515] ;

syntax JustTime
	= lex "$T" TimePartNoTZ TimeZonePart? ;

syntax MidStringChars
	= lex [\>] StringCharacter* [\<] ;

syntax ProtocolChars
	= lex [|] URLChars "://" ;

syntax RegExpModifier
	= lex [dims]* ;

syntax EscapedName
	= lex [\\] [A-Z_a-z] [\-0-9A-Z_a-z]* 
	# [\-0-9A-Z_a-z] ;

syntax Formal
	= TypeName: Type type Name name ;

syntax Parameters
	= VarArgs: "(" Formals formals "..." ")" 
	| Default: "(" Formals formals ")" ;

syntax RegExp
	= lex "\<" Name ":" NamedRegExp* "\>" 
	| lex "\<" Name "\>" 
	| lex [\\] [/\<\>\\] 
	| lex [\000-.0-;=?-\[\]-\u15151515] 
	| lex Backslash ;

syntax LocalVariableDeclaration
	= Dynamic: "dynamic" Declarator declarator 
	| Default: Declarator declarator ;

syntax RealLiteral
	= lex "." [0-9]+ [Ee] [+\-]? [0-9]+ [DFdf]? 
	| lex [0-9]+ "." [0-9]* [Ee] [+\-]? [0-9]+ [DFdf]? 
	| lex [0-9]+ [Ee] [+\-]? [0-9]+ [DFdf]? 
	| lex [0-9]+ [DFdf] 
	| lex "." [0-9]+ [DFdf]? 
	| lex [0-9]+ "." [0-9]* [DFdf]? 
	| lex [0-9]+ [Ee] [+\-]? [0-9]+ [DFdf] 
	# [0-9A-Z_a-z] ;

syntax LocationLiteral
	= Default: ProtocolPart protocolPart PathPart pathPart ;

syntax ShellCommand
	= Quit: "quit" 
	| Undeclare: "undeclare" QualifiedName name 
	| Help: "help" 
	| SetOption: "set" QualifiedName name Expression expression 
	| Edit: "edit" QualifiedName name 
	| Unimport: "unimport" QualifiedName name 
	| ListDeclarations: "declarations" 
	| History: "history" 
	| Test: "test" 
	| ListModules: "modules" ;

syntax StringMiddle
	= Template: MidStringChars mid StringTemplate template StringMiddle tail 
	| Mid: MidStringChars mid 
	| Interpolated: MidStringChars mid Expression expression StringMiddle tail ;

syntax QualifiedName
	= Default: {Name "::"}+ names 
	# [:] [:] ;

syntax DecimalIntegerLiteral
	= lex "0" 
	| lex [1-9] [0-9]* 
	# [0-9A-Z_a-z] ;

syntax DataTypeSelector
	= Selector: QualifiedName sort "." Name production ;

syntax StringTail
	= Post: PostStringChars post 
	| MidTemplate: MidStringChars mid StringTemplate template StringTail tail 
	| MidInterpolated: MidStringChars mid Expression expression StringTail tail ;

syntax TagString
	= lex "{" TagChar* "}" ;

syntax Command
	= /*prefer()*/ Expression: Expression expression 
	> NonEmptyBlock: "{" Statement+ statements "}" 
	| /*prefer()*/ Expression: Expression expression 
	| /*avoid()*/ Declaration: Declaration declaration 
	| Shell: ":" ShellCommand command 
	| Statement: Statement statement 
	| Import: Import imported ;

syntax ProtocolTail
	= Mid: MidProtocolChars mid Expression expression ProtocolTail tail 
	| Post: PostProtocolChars post ;

syntax PathTail
	= Mid: MidPathChars mid Expression expression PathTail tail 
	| Post: PostPathChars post ;

syntax Visibility
	= Public: "public" 
	| Default: 
	| Private: "private" ;

syntax StringLiteral
	= Interpolated: PreStringChars pre Expression expression StringTail tail 
	| Template: PreStringChars pre StringTemplate template StringTail tail 
	| NonInterpolated: StringConstant constant ;

syntax Renamings
	= Default: "renaming" {Renaming ","}+ renamings ;

syntax Tags
	= Default: Tag* tags ;

syntax Formals
	= Default: {Formal ","}* formals ;

syntax PostProtocolChars
	= lex "\>" URLChars "://" ;

syntax Statement
	= Assignment: Assignable assignable Assignment operator Statement statement 
	| Expression: Expression expression ";" 
	| IfThen: Label label "if" "(" {Expression ","}+ conditions ")" Statement thenStatement NoElseMayFollow noElseMayFollow 
	| Assert: "assert" Expression expression ";" 
	| IfThenElse: Label label "if" "(" {Expression ","}+ conditions ")" Statement thenStatement "else" Statement elseStatement 
	| FunctionDeclaration: FunctionDeclaration functionDeclaration 
	| Switch: Label label "switch" "(" Expression expression ")" "{" Case+ cases "}" 
	| Return: "return" Statement statement 
	| Fail: "fail" Target target ";" 
	| Break: "break" Target target ";" 
	| TryFinally: "try" Statement body Catch+ handlers "finally" Statement finallyBody 
	| Throw: "throw" Statement statement 
	| For: Label label "for" "(" {Expression ","}+ generators ")" Statement body 
	| VariableDeclaration: LocalVariableDeclaration declaration ";" 
	| DoWhile: Label label "do" Statement body "while" "(" Expression condition ")" ";" 
	| EmptyStatement: ";" 
	| Append: "append" DataTarget dataTarget Statement statement 
	| Solve: "solve" "(" {QualifiedName ","}+ variables Bound bound ")" Statement body 
	| Continue: "continue" Target target ";" 
	| GlobalDirective: "global" Type type {QualifiedName ","}+ names ";" 
	| Visit: Label label Visit visit 
	| AssertWithMessage: "assert" Expression expression ":" Expression message ";" 
	| non-assoc Try: "try" Statement body Catch+ handlers 
	| While: Label label "while" "(" {Expression ","}+ conditions ")" Statement body 
	| Insert: "insert" DataTarget dataTarget Statement statement 
	| non-assoc (  Assignment: Assignable assignable Assignment operator Statement statement  
		> non-assoc Throw: "throw" Statement statement 
		> non-assoc Insert: "insert" DataTarget dataTarget Statement statement 
		> non-assoc Return: "return" Statement statement 
		> non-assoc Append: "append" DataTarget dataTarget Statement statement 
	)
	> FunctionDeclaration: FunctionDeclaration functionDeclaration 
	| VariableDeclaration: LocalVariableDeclaration declaration ";" 
	| Expression: Expression expression ";" 
	> Visit: Label label Visit visit 
	| NonEmptyBlock: "{" Statement+ statements "}" 
	| NonEmptyBlock: Label label "{" Statement+ statements "}" ;

syntax StructuredType
	= Default: BasicType basicType "[" {TypeArg ","}+ arguments "]" ;

syntax FunctionType
	= TypeArguments: Type type "(" {TypeArg ","}* arguments ")" ;

syntax Declarator
	= Default: Type type {Variable ","}+ variables 
	| Default: Type type {Variable ","}+ variables 
	> IterStar: Symbol symbol "*" 
	| Optional: Symbol symbol "?" 
	| Iter: Symbol symbol "+" ;

syntax Bound
	= Empty: 
	| Default: ";" Expression expression ;

syntax RascalReservedKeywords
	= "type" 
	| "true" 
	| "bag" 
	| "num" 
	| "node" 
	| "finally" 
	| "private" 
	| "real" 
	| "fail" 
	| "list" 
	| "lex" 
	| "if" 
	| "tag" 
	| "append" 
	| "extend" 
	| "repeat" 
	| "rel" 
	| "void" 
	| "non-assoc" 
	| "assoc" 
	| "test" 
	| "anno" 
	| "join" 
	| "layout" 
	| "LAYOUT" 
	| "data" 
	| "it" 
	| "bracket" 
	| "import" 
	| "in" 
	| "view" 
	| "global" 
	| "false" 
	| "all" 
	| "dynamic" 
	| "solve" 
	| "try" 
	| "catch" 
	| "notin" 
	| "reified" 
	| "else" 
	| "insert" 
	| "switch" 
	| "return" 
	| "case" 
	| "while" 
	| "adt" 
	| "str" 
	| "throws" 
	| "visit" 
	| "tuple" 
	| "for" 
	| "assert" 
	| "loc" 
	| "default" 
	| "on" 
	| "map" 
	| "alias" 
	| "lang" 
	| "int" 
	| "any" 
	| "module" 
	| "bool" 
	| "public" 
	| "one" 
	| "throw" 
	| "set" 
	| "cf" 
	| "fun" 
	| "non-terminal" 
	| "rule" 
	| "constructor" 
	| "datetime" 
	| "value" 
	# [\-0-9A-Z_a-z] ;

syntax Declaration
	= View: Tags tags Visibility visibility "view" Name view "\<:" Name superType "=" {Alternative "|"}+ alts ";" 
	| Annotation: Tags tags Visibility visibility "anno" Type annoType Type onType "@" Name name ";" 
	| Test: Test test ";" 
	| Data: Tags tags Visibility visibility "data" UserType user "=" {Variant "|"}+ variants ";" 
	| Tag: Tags tags Visibility visibility "tag" Kind kind Name name "on" {Type ","}+ types ";" 
	| Alias: Tags tags Visibility visibility "alias" UserType user "=" Type base ";" 
	| Function: FunctionDeclaration functionDeclaration 
	| Variable: Tags tags Visibility visibility Type type {Variable ","}+ variables ";" 
	| DataAbstract: Tags tags Visibility visibility "data" UserType user ";" 
	| Rule: Tags tags "rule" Name name PatternWithAction patternAction ";" ;

syntax Type
	= Basic: BasicType basic 
	| User: UserType user 
	| bracket Bracket: "(" Type type ")" 
	| Function: FunctionType function 
	| Structured: StructuredType structured 
	| Variable: TypeVar typeVar 
	| Selector: DataTypeSelector selector ;

syntax RegExpLiteral
	= lex "/" RegExp* "/" RegExpModifier ;

syntax FunctionModifiers
	= List: FunctionModifier* modifiers ;

syntax Variant
	= NAryConstructor: Name name "(" {TypeArg ","}* arguments ")" ;

syntax FunctionDeclaration
	= Default: Tags tags Visibility visibility Signature signature FunctionBody body 
	| Abstract: Tags tags Visibility visibility Signature signature ";" ;

syntax PreProtocolChars
	= lex "|" URLChars "\<" ;

syntax NamedRegExp
	= lex [\000-.0-;=?-\[\]-\u15151515] 
	| lex "\<" Name "\>" 
	| lex [\\] [/\<\>\\] 
	| lex NamedBackslash ;

syntax Toplevel
	= GivenVisibility: Declaration declaration ;

syntax PostStringChars
	= lex [\>] StringCharacter* [\"] ;

syntax HexIntegerLiteral
	= lex [0] [Xx] [0-9A-Fa-f]+ 
	# [0-9A-Z_a-z] ;

syntax TypeVar
	= Free: "&" Name name 
	| Bounded: "&" Name name "\<:" Type bound ;

syntax OctalEscapeSequence
	= lex "\\" [0-7] 
	| lex "\\" [0-3] [0-7] [0-7] 
	| lex "\\" [0-7] [0-7] 
	# [0-7] ;

syntax OctalLongLiteral
	= lex [0] [0-7]+ [Ll] 
	# [0-9A-Z_a-z] ;

syntax BasicType
	= Bag: "bag" 
	| Loc: "loc" 
	| Node: "node" 
	| Num: "num" 
	| ReifiedType: "type" 
	| Int: "int" 
	| Relation: "rel" 
	| ReifiedTypeParameter: "parameter" 
	| ReifiedFunction: "fun" 
	| Real: "real" 
	| Tuple: "tuple" 
	| String: "str" 
	| Bool: "bool" 
	| ReifiedReifiedType: "reified" 
	| Void: "void" 
	| ReifiedNonTerminal: "non-terminal" 
	| Value: "value" 
	| DateTime: "datetime" 
	| Set: "set" 
	| Map: "map" 
	| List: "list" 
	| ReifiedConstructor: "constructor" 
	| ReifiedAdt: "adt" 
	| Lex: "lex" ;

syntax DateTimeLiteral
	= /*prefer()*/ TimeLiteral: JustTime time 
	| /*prefer()*/ DateLiteral: JustDate date 
	| /*prefer()*/ DateAndTimeLiteral: DateAndTime dateAndTime ;

syntax PrePathChars
	= lex URLChars "\<" ;

syntax Mapping[&Expression]
	= Default: &Expression from ":" &Expression to ;

syntax LongLiteral
	= /*prefer()*/ DecimalLongLiteral: DecimalLongLiteral decimalLong 
	| /*prefer()*/ OctalLongLiteral: OctalLongLiteral octalLong 
	| /*prefer()*/ HexLongLiteral: HexLongLiteral hexLong ;

syntax MidPathChars
	= lex "\>" URLChars "\<" ;

syntax Tag
	= /*term(category("Comment"))*/ Expression: "@" Name name "=" Expression expression 
	| /*term(category("Comment"))*/ Default: "@" Name name TagString contents 
	| /*term(category("Comment"))*/ Empty: "@" Name name ;

syntax ModuleActuals
	= Default: "[" {Type ","}+ types "]" ;



syntax "map"
	= ...
	# ;

syntax "loc"
	= ...
	# ;

syntax "default"
	= ...
	# ;

syntax "assert"
	= ...
	# ;

syntax "visit"
	= ...
	# ;

syntax "throws"
	= ...
	# ;

syntax "value"
	= ...
	# ;

syntax "datetime"
	= ...
	# ;

syntax "fun"
	= ...
	# ;

syntax "non-terminal"
	= ...
	# ;

syntax "set"
	= ...
	# ;

syntax "one"
	= ...
	# ;

syntax "public"
	= ...
	# ;

syntax "o"
	= ...
	# ;

syntax "module"
	= ...
	# ;

syntax "append"
	= ...
	# ;

syntax "extend"
	= ...
	# ;

syntax "fail"
	= ...
	# ;

syntax "if"
	= ...
	# ;

syntax "node"
	= ...
	# ;

syntax "case"
	= ...
	# ;

syntax "return"
	= ...
	# ;

syntax "str"
	= ...
	# ;

syntax "while"
	= ...
	# ;

syntax "switch"
	= ...
	# ;

syntax ":"
	= ...
	# ;

syntax "type"
	= ...
	# ;

syntax "notin"
	= ...
	# ;

syntax "reified"
	= ...
	# ;

syntax "else"
	= ...
	# ;

syntax "\>"
	= ...
	# ;

syntax "?"
	= ...
	# ;

syntax "solve"
	= ...
	# ;

syntax "dynamic"
	= ...
	# ;

syntax "\<"
	= ...
	# ;

syntax "="
	= ...
	# ;

syntax "false"
	= ...
	# ;

syntax "!"
	= ...
	# ;

syntax "!"
	= ...
	# ;

syntax "on "
	= ...
	# ;

syntax "&"
	= ...
	# ;

syntax "&"
	= ...
	# ;

syntax "in"
	= ...
	# ;

syntax "*"
	= ...
	# ;

syntax "join"
	= ...
	# ;

syntax "+"
	= ...
	# ;

syntax "it"
	= ...
	# ;

syntax "."
	= ...
	# ;

syntax "/"
	= ...
	# ;

syntax ","
	= ...
	# ;

syntax "-"
	= ...
	# ;

syntax "tuple"
	= ...
	# ;

syntax "for"
	= ...
	# ;

syntax "constructor"
	= ...
	# ;

syntax "rule"
	= ...
	# ;

syntax "throw"
	= ...
	# ;

syntax "bool"
	= ...
	# ;

syntax "int"
	= ...
	# ;

syntax "any"
	= ...
	# ;

syntax "test"
	= ...
	# ;

syntax "void"
	= ...
	# ;

syntax "tag"
	= ...
	# ;

syntax "://"
	= ...
	# ;

syntax "repeat"
	= ...
	# ;

syntax "rel"
	= ...
	# ;

syntax "list"
	= ...
	# ;

syntax "real"
	= ...
	# ;

syntax "private"
	= ...
	# ;

syntax "finally"
	= ...
	# ;

syntax "\<="
	= ...
	# ;

syntax "num"
	= ...
	# ;

syntax "true"
	= ...
	# ;

syntax "adt"
	= ...
	# ;

syntax "catch"
	= ...
	# ;

syntax "try"
	= ...
	# ;

syntax "insert"
	= ...
	# ;

syntax "global"
	= ...
	# ;

syntax "=="
	= ...
	# ;

syntax "all"
	= ...
	# ;

syntax "import"
	= ...
	# ;

syntax "view"
	= ...
	# ;

syntax "data"
	= ...
	# ;

syntax "anno"
	= ...
	# ;
