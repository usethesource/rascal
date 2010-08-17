module rascal::syntax::RascalRascal

syntax BooleanLiteral
	= lex "false" 
	| lex "true" ;

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
	= UnInitialized: Name name 
	| Initialized: Name name "=" Expression initial ;

syntax OctalIntegerLiteral
	= lex [0] [0-7]+ 
	#  [0-9A-Z_a-z] ;

syntax TypeArg
	= Named: Type type Name name 
	| Default: Type type ;

syntax Catch
	= Binding: "catch" Pattern pattern ":" Statement body 
	| Default: "catch" ":" Statement body ;

syntax Renaming
	= Default: Name from "=\>" Name to ;

syntax PathChars
	= lex URLChars [|] ;

syntax Signature
	= WithThrows: Type type FunctionModifiers modifiers Name name Parameters parameters "throws" {Type ","}+ exceptions 
	| NoThrows: Type type FunctionModifiers modifiers Name name Parameters parameters ;

syntax HexLongLiteral
	= lex [0] [Xx] [0-9A-Fa-f]+ [Ll] 
	#  [0-9A-Z_a-z] ;

syntax TimePartNoTZ
	= lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] [,.] [0-9] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] [,.] [0-9] [0-9] [0-9] 
	| lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] 
	| lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] [,.] [0-9] [0-9] [0-9] 
	| lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] [,.] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] [,.] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] [,.] [0-9] [0-9] ;

syntax DecimalLongLiteral
	= lex "0" [Ll] 
	| lex [1-9] [0-9]* [Ll] 
	#  [0-9A-Z_a-z] ;

syntax Header
	= Default: Tags tags "module" QualifiedName name Import* imports 
	| Parameters: Tags tags "module" QualifiedName name ModuleParameters params Import* imports ;

syntax Name
	= lex [A-Z_a-z] [0-9A-Z_a-z]* 
	| lex EscapedName 
	#  [0-9A-Z_a-z] 
	- /*reject()*/ RascalReservedKeywords ;

syntax Kind
	= View: "view" 
	| All: "all" 
	| Variable: "variable" 
	| Anno: "anno" 
	| Data: "data" 
	| Function: "function" 
	| Rule: "rule" 
	| Alias: "alias" 
	| Module: "module" 
	| Tag: "tag" ;

syntax Test
	= Unlabeled: Tags tags "test" Expression expression 
	| Labeled: Tags tags "test" Expression expression ":" StringLiteral labeled ;

syntax ImportedModule
	= ActualsRenaming: QualifiedName name ModuleActuals actuals Renamings renamings 
	| Default: QualifiedName name 
	| Renamings: QualifiedName name Renamings renamings 
	| Actuals: QualifiedName name ModuleActuals actuals ;

syntax IntegerLiteral
	= /*prefer()*/ DecimalIntegerLiteral: DecimalIntegerLiteral decimal 
	| /*prefer()*/ HexIntegerLiteral: HexIntegerLiteral hex 
	| /*prefer()*/ OctalIntegerLiteral: OctalIntegerLiteral octal ;

syntax Target
	= Empty: 
	| Labeled: Name name ;

syntax FunctionBody
	= Default: "{" Statement* statements "}" ;

syntax Expression
	= Expression "+" Expression 
	| Expression "*" Expression 
	> "-" Expression 
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
	| Tuple: "\<" {Expression ","}+ elements "\>" 
	| VoidClosure: Parameters parameters "{" Statement* statements "}" 
	| Closure: Type type Parameters parameters "{" Statement+ statements "}" 
	| QualifiedName: QualifiedName qualifiedName 
	| ReifiedType: BasicType basicType "(" {Expression ","}* arguments ")" 
	| FieldAccess: Expression expression "." Name field 
	| CallOrTree: Expression expression "(" {Expression ","}* arguments ")" 
	| FieldUpdate: Expression expression "[" Name key "=" Expression replacement "]" 
	| Subscript: Expression expression "[" {Expression ","}+ subscripts "]" 
	| FieldProject: Expression expression "\<" {Field ","}+ fields "\>" 
	> IsDefined: Expression argument "?" 
	> Negative: "-" Expression argument 
	| Negation: "!" Expression argument 
	> TransitiveReflexiveClosure: Expression argument "*" 
	| TransitiveClosure: Expression argument "+" 
	> SetAnnotation: Expression expression "[" "@" Name name "=" Expression value "]" 
	| GetAnnotation: Expression expression "@" Name name 
	> left Composition: Expression lhs "o" Expression rhs 
	> left (  left Product: Expression lhs "*" Expression rhs  
		> left Join: Expression lhs "join" Expression rhs 
	)
	> left (  left Division: Expression lhs "/" Expression rhs  
		> left Modulo: Expression lhs "%" Expression rhs 
	)
	> left Intersection: Expression lhs "&" Expression rhs 
	> left (  left Subtraction: Expression lhs "-" Expression rhs  
		> left Addition: Expression lhs "+" Expression rhs 
	)
	> non-assoc (  non-assoc In: Expression lhs "in" Expression rhs  
		> non-assoc NotIn: Expression lhs "notin" Expression rhs 
	)
	> non-assoc (  non-assoc GreaterThanOrEq: Expression lhs "\>=" Expression rhs  
		> non-assoc LessThanOrEq: Expression lhs "\<=" Expression rhs 
		> non-assoc LessThan: Expression lhs "\<" Expression rhs 
		> non-assoc GreaterThan: Expression lhs "\>" Expression rhs 
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
	= Extend: "extend" ImportedModule module ";" 
	| Default: "import" ImportedModule module ";" 
	| Syntax: SyntaxDefinition syntax ;

syntax Body
	= Toplevels: Toplevel* toplevels ;

syntax URLChars
	= lex [\000-\b\013-\f\016-\037!-;=-{}-\u15151515]* ;

syntax TimeZonePart
	= lex [+\-] [0-1] [0-9] 
	| lex "Z" 
	| lex [+\-] [0-1] [0-9] ":" [0-5] [0-9] 
	| lex [+\-] [0-1] [0-9] [0-5] [0-9] ;

syntax ProtocolPart
	= Interpolated: PreProtocolChars pre Expression expression ProtocolTail tail 
	| NonInterpolated: ProtocolChars protocolChars ;

syntax StringTemplate
	= For: "for" "(" {Expression ","}+ generators ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| While: "while" "(" Expression condition ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| IfThenElse: "if" "(" {Expression ","}+ conditions ")" "{" Statement* preStatsThen StringMiddle thenString Statement* postStatsThen "}" "else" "{" Statement* preStatsElse StringMiddle elseString Statement* postStatsElse "}" 
	| IfThen: "if" "(" {Expression ","}+ conditions ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| DoWhile: "do" "{" Statement* preStats StringMiddle body Statement* postStats "}" "while" "(" Expression condition ")" ;

syntax PreStringChars
	= lex [\"] StringCharacter* [\<] ;

syntax CaseInsensitiveStringConstant
	= /*term(category("Constant"))*/ lex "\'" StringCharacter* "\'" ;

syntax Backslash
	= lex [\\] 
	#  [/\<\>\\] ;

syntax Label
	= Empty: 
	| Default: Name name ":" ;

syntax NoElseMayFollow
	= Default: 
	#  [e]  [l]  [s]  [e] ;

syntax MidProtocolChars
	= lex "\>" URLChars "\<" ;

syntax NamedBackslash
	= lex [\\] 
	#  [\<\>\\] ;

syntax JustDate
	= lex "$" DatePart ;

syntax Field
	= Name: Name fieldName 
	| Index: IntegerLiteral fieldIndex ;

syntax PostPathChars
	= lex "\>" URLChars "|" ;

syntax PathPart
	= NonInterpolated: PathChars pathChars 
	| Interpolated: PrePathChars pre Expression expression PathTail tail ;

syntax DatePart
	= lex [0-9] [0-9] [0-9] [0-9] "-" [0-1] [0-9] "-" [0-3] [0-9] 
	| lex [0-9] [0-9] [0-9] [0-9] [0-1] [0-9] [0-3] [0-9] ;

syntax FunctionModifier
	= Java: "java" ;

syntax Assignment
	= Intersection: "&=" 
	| Division: "/=" 
	| Product: "*=" 
	| Subtraction: "-=" 
	| Default: "=" 
	| Addition: "+=" 
	| IfDefined: "?=" ;

syntax Assignable
	= FieldAccess: Assignable receiver "." Name field 
	| Subscript: Assignable receiver "[" Expression subscript "]" 
	| IfDefinedOrDefault: Assignable receiver "?" Expression defaultExpression 
	> non-assoc (  Tuple: "\<" {Assignable ","}+ elements "\>"  
		> non-assoc Annotation: Assignable receiver "@" Name annotation 
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
	= lex [\000-!#-&(-;=?-\[\]-\u15151515] 
	| lex UnicodeEscape 
	| lex OctalEscapeSequence 
	| lex "\\" [\"\'\<\>\\bfnrt] ;

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
	#  [\-0-9A-Z_a-z] ;

syntax Formal
	= TypeName: Type type Name name ;

syntax Parameters
	= VarArgs: "(" Formals formals "..." ")" 
	| Default: "(" Formals formals ")" ;

syntax RegExp
	= lex [\000-.0-;=?-\[\]-\u15151515] 
	| lex "\<" Name "\>" 
	| lex "\<" Name ":" NamedRegExp* "\>" 
	| lex [\\] [/\<\>\\] 
	| lex Backslash ;

syntax LocalVariableDeclaration
	= Dynamic: "dynamic" Declarator declarator 
	| Default: Declarator declarator ;

syntax RealLiteral
	= lex "." [0-9]+ [DFdf]? 
	| lex [0-9]+ "." [0-9]* [Ee] [+\-]? [0-9]+ [DFdf]? 
	| lex "." [0-9]+ [Ee] [+\-]? [0-9]+ [DFdf]? 
	| lex [0-9]+ [Ee] [+\-]? [0-9]+ [DFdf]? 
	| lex [0-9]+ [DFdf] 
	| lex [0-9]+ "." [0-9]* [DFdf]? 
	| lex [0-9]+ [Ee] [+\-]? [0-9]+ [DFdf] 
	#  [0-9A-Z_a-z] ;

syntax LocationLiteral
	= Default: ProtocolPart protocolPart PathPart pathPart ;

syntax ShellCommand
	= Unimport: "unimport" QualifiedName name 
	| Undeclare: "undeclare" QualifiedName name 
	| Help: "help" 
	| SetOption: "set" QualifiedName name Expression expression 
	| Edit: "edit" QualifiedName name 
	| ListDeclarations: "declarations" 
	| Quit: "quit" 
	| History: "history" 
	| Test: "test" 
	| ListModules: "modules" ;

syntax StringMiddle
	= Template: MidStringChars mid StringTemplate template StringMiddle tail 
	| Mid: MidStringChars mid 
	| Interpolated: MidStringChars mid Expression expression StringMiddle tail ;

syntax QualifiedName
	= Default: {Name "::"}+ names 
	#  [:]  [:] ;

syntax DecimalIntegerLiteral
	= lex [1-9] [0-9]* 
	| lex "0" 
	#  [0-9A-Z_a-z] ;

syntax DataTypeSelector
	= Selector: QualifiedName sort "." Name production ;

syntax StringTail
	= MidInterpolated: MidStringChars mid Expression expression StringTail tail 
	| Post: PostStringChars post 
	| MidTemplate: MidStringChars mid StringTemplate template StringTail tail ;

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
	= Post: PostPathChars post 
	| Mid: MidPathChars mid Expression expression PathTail tail ;

syntax Visibility
	= Default: 
	| Private: "private" 
	| Public: "public" ;

syntax StringLiteral
	= NonInterpolated: StringConstant constant 
	| Interpolated: PreStringChars pre Expression expression StringTail tail 
	| Template: PreStringChars pre StringTemplate template StringTail tail ;

syntax Renamings
	= Default: "renaming" {Renaming ","}+ renamings ;

syntax Tags
	= Default: Tag* tags ;

syntax Formals
	= Default: {Formal ","}* formals ;

syntax PostProtocolChars
	= lex "\>" URLChars "://" ;

syntax Statement
	= While: Label label "while" "(" {Expression ","}+ conditions ")" Statement body 
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
	| Assignment: Assignable assignable Assignment operator Statement statement 
	| GlobalDirective: "global" Type type {QualifiedName ","}+ names ";" 
	| Visit: Label label Visit visit 
	| AssertWithMessage: "assert" Expression expression ":" Expression message ";" 
	| non-assoc Try: "try" Statement body Catch+ handlers 
	| Insert: "insert" DataTarget dataTarget Statement statement 
	| non-assoc (  non-assoc Return: "return" Statement statement  
		> non-assoc Throw: "throw" Statement statement 
		> non-assoc Insert: "insert" DataTarget dataTarget Statement statement 
		> Assignment: Assignable assignable Assignment operator Statement statement 
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
	> IterStar: Symbol symbol "*" 
	| Optional: Symbol symbol "?" 
	| Iter: Symbol symbol "+" 
	| Default: Type type {Variable ","}+ variables ;

syntax Bound
	= Empty: 
	| Default: ";" Expression expression ;

syntax RascalReservedKeywords
	= "bag" 
	| "true" 
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
	| "LAYOUT" 
	| "layout" 
	| "join" 
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
	| "type" 
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
	#  [\-0-9A-Z_a-z] ;

syntax Declaration
	= Variable: Tags tags Visibility visibility Type type {Variable ","}+ variables ";" 
	| Annotation: Tags tags Visibility visibility "anno" Type annoType Type onType "@" Name name ";" 
	| Test: Test test ";" 
	| Data: Tags tags Visibility visibility "data" UserType user "=" {Variant "|"}+ variants ";" 
	| Tag: Tags tags Visibility visibility "tag" Kind kind Name name "on" {Type ","}+ types ";" 
	| Alias: Tags tags Visibility visibility "alias" UserType user "=" Type base ";" 
	| Function: FunctionDeclaration functionDeclaration 
	| DataAbstract: Tags tags Visibility visibility "data" UserType user ";" 
	| View: Tags tags Visibility visibility "view" Name view "\<:" Name superType "=" {Alternative "|"}+ alts ";" 
	| Rule: Tags tags "rule" Name name PatternWithAction patternAction ";" ;

syntax Type
	= bracket Bracket: "(" Type type ")" 
	| User: UserType user 
	| Function: FunctionType function 
	| Basic: BasicType basic 
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
	= Abstract: Tags tags Visibility visibility Signature signature ";" 
	| Default: Tags tags Visibility visibility Signature signature FunctionBody body ;

syntax PreProtocolChars
	= lex "|" URLChars "\<" ;

syntax NamedRegExp
	= lex "\<" Name "\>" 
	| lex [\\] [/\<\>\\] 
	| lex NamedBackslash 
	| lex [\000-.0-;=?-\[\]-\u15151515] ;

syntax Toplevel
	= GivenVisibility: Declaration declaration ;

syntax PostStringChars
	= lex [\>] StringCharacter* [\"] ;

syntax HexIntegerLiteral
	= lex [0] [Xx] [0-9A-Fa-f]+ 
	#  [0-9A-Z_a-z] ;

syntax OctalEscapeSequence
	= lex "\\" [0-7] 
	| lex "\\" [0-3] [0-7] [0-7] 
	| lex "\\" [0-7] [0-7] 
	#  [0-7] ;

syntax TypeVar
	= Bounded: "&" Name name "\<:" Type bound 
	| Free: "&" Name name ;

syntax BasicType
	= ReifiedFunction: "fun" 
	| Loc: "loc" 
	| Node: "node" 
	| Num: "num" 
	| ReifiedType: "type" 
	| Int: "int" 
	| Bag: "bag" 
	| Relation: "rel" 
	| ReifiedTypeParameter: "parameter" 
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

syntax OctalLongLiteral
	= lex [0] [0-7]+ [Ll] 
	#  [0-9A-Z_a-z] ;

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
	= /*term(category("Comment"))*/ Empty: "@" Name name 
	| /*term(category("Comment"))*/ Default: "@" Name name TagString contents 
	| /*term(category("Comment"))*/ Expression: "@" Name name "=" Expression expression ;

syntax ModuleActuals
	= Default: "[" {Type ","}+ types "]" ;



syntax "map" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "loc" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "default" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "assert" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "visit" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "throws" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "value" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "datetime" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "fun" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "non-terminal" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "set" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "one" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "public" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "o" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "module" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "extend" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "append" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "fail" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "if" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "node" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "case" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "return" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "while" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "str" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "switch" =
	...
	# [\-0-9A-Z_a-z] ;

syntax ":" =
	...
	# [:] ;

syntax "type" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "reified" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "notin" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "else" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "\>" =
	...
	# [=] ;

syntax "?" =
	...
	# [=] ;

syntax "solve" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "dynamic" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "\<" =
	...
	# [=] ;

syntax "=" =
	...
	# [=] ;

syntax "false" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "!" =
	...
	# [~] ;

syntax "!" =
	...
	# [=] ;

syntax "on " =
	...
	# [\-0-9A-Z_a-z] ;

syntax "&" =
	...
	# [=] ;

syntax "&" =
	...
	# [&] ;

syntax "in" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "*" =
	...
	# [=] ;

syntax "join" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "+" =
	...
	# [=] ;

syntax "it" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "." =
	...
	# [.] ;

syntax "/" =
	...
	# [=] ;

syntax "," =
	...
	# [.] [.] ;

syntax "-" =
	...
	# [=] ;

syntax "tuple" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "for" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "constructor" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "rule" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "throw" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "bool" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "int" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "any" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "test" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "void" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "tag" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "://" =
	...
	# [\t-\n\r\ ] ;

syntax "repeat" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "rel" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "list" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "real" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "private" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "finally" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "\<=" =
	...
	# [=] [\>] ;

syntax "num" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "true" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "adt" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "catch" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "try" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "insert" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "global" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "==" =
	...
	# [\>] ;

syntax "all" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "import" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "view" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "data" =
	...
	# [\-0-9A-Z_a-z] ;

syntax "anno" =
	...
	# [\-0-9A-Z_a-z] ;
