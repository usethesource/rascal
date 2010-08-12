module rascal::syntax::RascalRascal


syntax Module
	= Default: Header header Body body ;

syntax NamedBackslash
	= NamedBackslash
	#  [\<\>\\] ;

syntax PathChars
	= lex URLChars [|] ;

syntax TagString
	= lex "{" TagChar* "}" ;

syntax RealLiteral
	= RealLiteral
	#  [a-zA-Z0-9_] ;

syntax Assignment
	= IfDefined: "?=" 
	| Division: "/=" 
	| Product: "*=" 
	| Intersection: "&=" 
	| Subtraction: "-=" 
	| Default: "=" 
	| Addition: "+=" ;

syntax RascalReservedKeywords
	= RascalReservedKeywords
	#  [a-zA-Z0-9_\-] ;

syntax QualifiedName
	= QualifiedName
	#  [:]  [:] ;

syntax Variable
	= UnInitialized: Name name 
	| Initialized: Name name "=" Expression initial ;

syntax StringLiteral
	= Template: PreStringChars pre StringTemplate template StringTail tail 
	| Interpolated: PreStringChars pre Expression expression StringTail tail 
	| NonInterpolated: StringConstant constant ;

syntax Name
	= Name
	#  [a-zA-Z0-9_] ;

syntax PreStringChars
	= lex [\"] StringCharacter* [\<] ;

syntax ProtocolTail
	= Post: PostProtocolChars post 
	| Mid: MidProtocolChars mid Expression expression ProtocolTail tail ;

syntax Variant
	= NAryConstructor: Name name "(" {TypeArg ","}* arguments ")" ;

syntax LongLiteral
	= /*prefer()*/ DecimalLongLiteral: DecimalLongLiteral decimalLong 
	| /*prefer()*/ HexLongLiteral: HexLongLiteral hexLong 
	| /*prefer()*/ OctalLongLiteral: OctalLongLiteral octalLong ;

syntax Assignable
	= FieldAccess: Assignable receiver "." Name field 
	| Subscript: Assignable receiver "[" Expression subscript "]" 
	| IfDefinedOrDefault: Assignable receiver "?" Expression defaultExpression 
	> non-assoc (  non-assoc Constructor: Name name "(" {Assignable ","}+ arguments ")"  
		> non-assoc Annotation: Assignable receiver "@" Name annotation 
		> Tuple: "\<" {Assignable ","}+ elements "\>" 
	);

syntax Assignable
	= Variable: QualifiedName qualifiedName ;

syntax UnicodeEscape
	= lex "\\" [u]+ [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] ;

syntax LocalVariableDeclaration
	= Dynamic: "dynamic" Declarator declarator 
	| Default: Declarator declarator ;

syntax FunctionBody
	= Default: "{" Statement* statements "}" ;

syntax TimeZonePart
	= lex [+\-] [0-1] [0-9] ":" [0-5] [0-9] 
	| lex "Z" 
	| lex [+\-] [0-1] [0-9] 
	| lex [+\-] [0-1] [0-9] [0-5] [0-9] ;

syntax RegExpLiteral
	= lex "/" RegExp* "/" RegExpModifier ;

syntax EscapedName
	= EscapedName
	#  [a-zA-Z0-9_\-] ;

syntax OctalIntegerLiteral
	= OctalIntegerLiteral
	#  [a-zA-Z0-9_] ;

syntax DatePart
	= lex [0-9] [0-9] [0-9] [0-9] [0-1] [0-9] [0-3] [0-9] 
	| lex [0-9] [0-9] [0-9] [0-9] "-" [0-1] [0-9] "-" [0-3] [0-9] ;

syntax Target
	= Labeled: Name name 
	| Empty: ;

syntax Renaming
	= Default: Name from "=\>" Name to ;

syntax TagChar
	= lex ![}] 
	| lex [\\] [\\}] ;

syntax FunctionModifier
	= Java: "java" ;

syntax HexLongLiteral
	= HexLongLiteral
	#  [a-zA-Z0-9_] ;

syntax RegExpModifier
	= lex [imsd]* ;

syntax Alternative
	= NamedType: Name name Type type ;

syntax ModuleActuals
	= Default: "[" {Type ","}+ types "]" ;

syntax Body
	= Toplevels: Toplevel* toplevels ;

syntax Catch
	= Default: "catch" ":" Statement body 
	| Binding: "catch" Pattern pattern ":" Statement body ;

syntax StringMiddle
	= Interpolated: MidStringChars mid Expression expression StringMiddle tail 
	| Template: MidStringChars mid StringTemplate template StringMiddle tail 
	| Mid: MidStringChars mid ;

syntax DateAndTime
	= lex "$" DatePart "T" TimePartNoTZ TimeZonePart? ;

syntax StringTail
	= MidInterpolated: MidStringChars mid Expression expression StringTail tail 
	| Post: PostStringChars post 
	| MidTemplate: MidStringChars mid StringTemplate template StringTail tail ;

syntax Statement
	= non-assoc (  non-assoc Append: "append" DataTarget dataTarget Statement statement  
		> non-assoc Throw: "throw" Statement statement 
		> non-assoc Insert: "insert" DataTarget dataTarget Statement statement 
		> Assignment: Assignable assignable Assignment operator Statement statement 
		> non-assoc Return: "return" Statement statement 
	)
	> FunctionDeclaration: FunctionDeclaration functionDeclaration 
	| VariableDeclaration: LocalVariableDeclaration declaration ";" ;

syntax Statement
	= Assignment: Assignable assignable Assignment operator Statement statement 
	| Assert: "assert" Expression expression ";" 
	| Expression: Expression expression ";" 
	| IfThen: Label label "if" "(" {Expression ","}+ conditions ")" Statement thenStatement NoElseMayFollow noElseMayFollow 
	| IfThenElse: Label label "if" "(" {Expression ","}+ conditions ")" Statement thenStatement "else" Statement elseStatement 
	| FunctionDeclaration: FunctionDeclaration functionDeclaration 
	| Switch: Label label "switch" "(" Expression expression ")" "{" Case+ cases "}" 
	| Return: "return" Statement statement 
	| Fail: "fail" Target target ";" 
	| Break: "break" Target target ";" 
	| TryFinally: "try" Statement body Catch+ handlers "finally" Statement finallyBody 
	| Throw: "throw" Statement statement 
	| VariableDeclaration: LocalVariableDeclaration declaration ";" 
	| For: Label label "for" "(" {Expression ","}+ generators ")" Statement body 
	| DoWhile: Label label "do" Statement body "while" "(" Expression condition ")" ";" 
	| Append: "append" DataTarget dataTarget Statement statement 
	| EmptyStatement: ";" 
	| Continue: "continue" Target target ";" 
	| Solve: "solve" "(" {QualifiedName ","}+ variables Bound bound ")" Statement body 
	| GlobalDirective: "global" Type type {QualifiedName ","}+ names ";" 
	| Visit: Label label Visit visit 
	| AssertWithMessage: "assert" Expression expression ":" Expression message ";" 
	| non-assoc Try: "try" Statement body Catch+ handlers 
	| Insert: "insert" DataTarget dataTarget Statement statement 
	| While: Label label "while" "(" {Expression ","}+ conditions ")" Statement body 
	| NonEmptyBlock: Label label "{" Statement+ statements "}" ;

syntax Statement
	= Expression: Expression expression ";" 
	> Visit: Label label Visit visit 
	| NonEmptyBlock: "{" Statement+ statements "}" ;

syntax DataTarget
	= Labeled: Name label ":" 
	| Empty: ;

syntax Toplevel
	= GivenVisibility: Declaration declaration ;

syntax DecimalIntegerLiteral
	= DecimalIntegerLiteral
	#  [a-zA-Z0-9_] ;

syntax FunctionModifiers
	= List: FunctionModifier* modifiers ;

syntax ImportedModule
	= Default: QualifiedName name 
	| ActualsRenaming: QualifiedName name ModuleActuals actuals Renamings renamings 
	| Renamings: QualifiedName name Renamings renamings 
	| Actuals: QualifiedName name ModuleActuals actuals ;

syntax PathPart
	= Interpolated: PrePathChars pre Expression expression PathTail tail 
	| NonInterpolated: PathChars pathChars ;

syntax TypeArg
	= Default: Type type 
	| Named: Type type Name name ;

syntax BooleanLiteral
	= lex "true" 
	| lex "false" ;

syntax IntegerLiteral
	= /*prefer()*/ DecimalIntegerLiteral: DecimalIntegerLiteral decimal 
	| /*prefer()*/ HexIntegerLiteral: HexIntegerLiteral hex 
	| /*prefer()*/ OctalIntegerLiteral: OctalIntegerLiteral octal ;

syntax Label
	= Empty: 
	| Default: Name name ":" ;

syntax Kind
	= Rule: "rule" 
	| All: "all" 
	| Variable: "variable" 
	| Anno: "anno" 
	| Data: "data" 
	| Function: "function" 
	| View: "view" 
	| Alias: "alias" 
	| Module: "module" 
	| Tag: "tag" ;

syntax MidProtocolChars
	= lex "\>" URLChars "\<" ;

syntax JustDate
	= lex "$" DatePart ;

syntax Literal
	= String: StringLiteral stringLiteral 
	| RegExp: RegExpLiteral regExpLiteral 
	| Boolean: BooleanLiteral booleanLiteral 
	| Integer: IntegerLiteral integerLiteral 
	| Real: RealLiteral realLiteral 
	| DateTime: DateTimeLiteral dateTimeLiteral 
	| Location: LocationLiteral locationLiteral ;

syntax BasicType
	= Real: "real" 
	| ReifiedType: "type" 
	| Node: "node" 
	| Num: "num" 
	| Loc: "loc" 
	| Int: "int" 
	| Bag: "bag" 
	| Relation: "rel" 
	| ReifiedTypeParameter: "parameter" 
	| ReifiedFunction: "fun" 
	| Tuple: "tuple" 
	| String: "str" 
	| Bool: "bool" 
	| ReifiedReifiedType: "reified" 
	| Void: "void" 
	| ReifiedNonTerminal: "non-terminal" 
	| Value: "value" 
	| DateTime: "datetime" 
	| Set: "set" 
	| ReifiedConstructor: "constructor" 
	| List: "list" 
	| Map: "map" 
	| ReifiedAdt: "adt" 
	| Lex: "lex" ;

syntax TimePartNoTZ
	= lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] [,.] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] [,.] [0-9] [0-9] [0-9] 
	| lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] 
	| lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] [,.] [0-9] [0-9] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] [,.] [0-9] 
	| lex [0-2] [0-9] [0-5] [0-9] [0-5] [0-9] [,.] [0-9] [0-9] 
	| lex [0-2] [0-9] ":" [0-5] [0-9] ":" [0-5] [0-9] [,.] [0-9] [0-9] ;

syntax Header
	= Default: Tags tags "module" QualifiedName name Import* imports 
	| Parameters: Tags tags "module" QualifiedName name ModuleParameters params Import* imports ;

syntax Visibility
	= Private: "private" 
	| Default: 
	| Public: "public" ;

syntax LocationLiteral
	= Default: ProtocolPart protocolPart PathPart pathPart ;

syntax HexIntegerLiteral
	= HexIntegerLiteral
	#  [a-zA-Z0-9_] ;

syntax Formals
	= Default: {Formal ","}* formals ;

syntax TypeVar
	= Bounded: "&" Name name "\<:" Type bound 
	| Free: "&" Name name ;

syntax StringCharacter
	= lex OctalEscapeSequence 
	| lex UnicodeEscape 
	| lex "\\" [btnfr\"\'\\\<\>] 
	| lex ![\"\'\\\<\>] ;

syntax NoElseMayFollow
	= 
	#  "else";

syntax MidPathChars
	= lex "\>" URLChars "\<" ;

syntax PostProtocolChars
	= lex "\>" URLChars "://" ;

syntax OctalEscapeSequence
	= OctalEscapeSequence
	#  [0-7] ;

syntax PrePathChars
	= lex URLChars "\<" ;

syntax DateTimeLiteral
	= /*prefer()*/ TimeLiteral: JustTime time 
	| /*prefer()*/ DateLiteral: JustDate date 
	| /*prefer()*/ DateAndTimeLiteral: DateAndTime dateAndTime ;

syntax ProtocolChars
	= lex [|] URLChars "://" ;

syntax Command
	= /*avoid()*/ Declaration: Declaration declaration 
	| /*prefer()*/ Expression: Expression expression 
	| Shell: ":" ShellCommand command 
	| Statement: Statement statement 
	| Import: Import imported ;

syntax Command
	= /*prefer()*/ Expression: Expression expression 
	> NonEmptyBlock: "{" Statement+ statements "}" ;

syntax Backslash
	= Backslash
	#  [/\<\>\\] ;

syntax Tags
	= Default: Tag* tags ;

syntax Declarator
	= Default: Type type {Variable ","}+ variables 
	> Iter: Symbol symbol "+" 
	| Optional: Symbol symbol "?" 
	| IterStar: Symbol symbol "*" ;

syntax Declarator
	= Default: Type type {Variable ","}+ variables ;

syntax StringTemplate
	= While: "while" "(" Expression condition ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| For: "for" "(" {Expression ","}+ generators ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| IfThenElse: "if" "(" {Expression ","}+ conditions ")" "{" Statement* preStatsThen StringMiddle thenString Statement* postStatsThen "}" "else" "{" Statement* preStatsElse StringMiddle elseString Statement* postStatsElse "}" 
	| IfThen: "if" "(" {Expression ","}+ conditions ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| DoWhile: "do" "{" Statement* preStats StringMiddle body Statement* postStats "}" "while" "(" Expression condition ")" ;

syntax JustTime
	= lex "$T" TimePartNoTZ TimeZonePart? ;

syntax Signature
	= NoThrows: Type type FunctionModifiers modifiers Name name Parameters parameters 
	| WithThrows: Type type FunctionModifiers modifiers Name name Parameters parameters "throws" {Type ","}+ exceptions ;

syntax Declaration
	= Function: FunctionDeclaration functionDeclaration 
	| Annotation: Tags tags Visibility visibility "anno" Type annoType Type onType "@" Name name ";" 
	| Test: Test test ";" 
	| Data: Tags tags Visibility visibility "data" UserType user "=" {Variant "|"}+ variants ";" 
	| Tag: Tags tags Visibility visibility "tag" Kind kind Name name "on" {Type ","}+ types ";" 
	| Alias: Tags tags Visibility visibility "alias" UserType user "=" Type base ";" 
	| Variable: Tags tags Visibility visibility Type type {Variable ","}+ variables ";" 
	| View: Tags tags Visibility visibility "view" Name view "\<:" Name superType "=" {Alternative "|"}+ alts ";" 
	| DataAbstract: Tags tags Visibility visibility "data" UserType user ";" 
	| Rule: Tags tags "rule" Name name PatternWithAction patternAction ";" ;

syntax Import
	= Syntax: SyntaxDefinition syntax 
	| Extend: "extend" ImportedModule module ";" 
	| Default: "import" ImportedModule module ";" ;

syntax FunctionDeclaration
	= Abstract: Tags tags Visibility visibility Signature signature ";" 
	| Default: Tags tags Visibility visibility Signature signature FunctionBody body ;

syntax ModuleParameters
	= Default: "[" {TypeVar ","}+ parameters "]" ;

syntax DecimalLongLiteral
	= DecimalLongLiteral
	#  [a-zA-Z0-9_] ;

syntax Tag
	= /*term(category("Comment"))*/ Default: "@" Name name TagString contents 
	| /*term(category("Comment"))*/ Empty: "@" Name name 
	| /*term(category("Comment"))*/ Expression: "@" Name name "=" Expression expression ;

syntax PostStringChars
	= lex [\>] StringCharacter* [\"] ;

syntax Test
	= Unlabeled: Tags tags "test" Expression expression 
	| Labeled: Tags tags "test" Expression expression ":" StringLiteral labeled ;

syntax PathTail
	= Post: PostPathChars post 
	| Mid: MidPathChars mid Expression expression PathTail tail ;

syntax Bound
	= Default: ";" Expression expression 
	| Empty: ;

syntax DataTypeSelector
	= Selector: QualifiedName sort "." Name production ;

syntax URLChars
	= lex ![\ \t\n\r|\<]* ;

syntax Parameters
	= VarArgs: "(" Formals formals "..." ")" 
	| Default: "(" Formals formals ")" ;

syntax Type
	= Selector: DataTypeSelector selector 
	| User: UserType user 
	| Function: FunctionType function 
	| bracket Bracket: "(" Type type ")" 
	| Structured: StructuredType structured 
	| Basic: BasicType basic 
	| Variable: TypeVar typeVar ;

syntax StructuredType
	= Default: BasicType basicType "[" {TypeArg ","}+ arguments "]" ;

syntax UserType
	= Parametric: QualifiedName name "[" {Type ","}+ parameters "]" 
	| Name: QualifiedName name ;

syntax CaseInsensitiveStringConstant
	= /*term(category("Constant"))*/ lex "\'" StringCharacter* "\'" ;

syntax ShellCommand
	= Test: "test" 
	| Help: "help" 
	| Undeclare: "undeclare" QualifiedName name 
	| SetOption: "set" QualifiedName name Expression expression 
	| Edit: "edit" QualifiedName name 
	| Unimport: "unimport" QualifiedName name 
	| ListDeclarations: "declarations" 
	| Quit: "quit" 
	| History: "history" 
	| ListModules: "modules" ;

syntax Expression
	= Expression "*" Expression 
	| Expression "+" Expression 
	> "-" Expression ;

syntax Expression
	= Subscript: Expression expression "[" {Expression ","}+ subscripts "]" 
	> ReifyType: "#" Type type ;

syntax Expression
	= ReifiedType: BasicType basicType "(" {Expression ","}* arguments ")" 
	| bracket Bracket: "(" Expression expression ")" 
	| NonEmptyBlock: "{" Statement+ statements "}" 
	| Range: "[" Expression first ".." Expression last "]" 
	| StepRange: "[" Expression first "," Expression second ".." Expression last "]" 
	| CallOrTree: Expression expression "(" {Expression ","}* arguments ")" 
	| VoidClosure: Parameters parameters "{" Statement* statements "}" 
	| Closure: Type type Parameters parameters "{" Statement+ statements "}" 
	| Tuple: "\<" {Expression ","}+ elements "\>" 
	| Set: "{" {Expression ","}* elements "}" 
	| Visit: Label label Visit visit 
	| Literal: Literal literal 
	| QualifiedName: QualifiedName qualifiedName 
	| List: "[" {Expression ","}* elements "]" 
	| ReifyType: "#" Type type 
	| Map: "(" {Mapping[Expression] ","}* mappings ")" ;

syntax Expression
	= FieldUpdate: Expression expression "[" Name key "=" Expression replacement "]" 
	| FieldAccess: Expression expression "." Name field 
	| CallOrTree: Expression expression "(" {Expression ","}* arguments ")" 
	| ReifiedType: BasicType basicType "(" {Expression ","}* arguments ")" 
	| Subscript: Expression expression "[" {Expression ","}+ subscripts "]" 
	| FieldProject: Expression expression "\<" {Field ","}+ fields "\>" 
	> IsDefined: Expression argument "?" 
	> Negation: "!" Expression argument 
	| Negative: "-" Expression argument 
	> TransitiveClosure: Expression argument "+" 
	| TransitiveReflexiveClosure: Expression argument "*" 
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
	> non-assoc (  non-assoc NotIn: Expression lhs "notin" Expression rhs  
		> non-assoc In: Expression lhs "in" Expression rhs 
	)
	> non-assoc (  non-assoc LessThanOrEq: Expression lhs "\<=" Expression rhs  
		> non-assoc LessThan: Expression lhs "\<" Expression rhs 
		> non-assoc GreaterThan: Expression lhs "\>" Expression rhs 
		> non-assoc GreaterThanOrEq: Expression lhs "\>=" Expression rhs 
	)
	> left (  left Equals: Expression lhs "==" Expression rhs  
		> right IfThenElse: Expression condition "?" Expression thenExp ":" Expression elseExp 
		> left NonEquals: Expression lhs "!=" Expression rhs 
	)
	> non-assoc IfDefinedOtherwise: Expression lhs "?" Expression rhs 
	> non-assoc (  right Implication: Expression lhs "==\>" Expression rhs  
		> right Equivalence: Expression lhs "\<==\>" Expression rhs 
	)
	> left And: Expression lhs "&&" Expression rhs 
	> left Or: Expression lhs "||" Expression rhs ;

syntax MidStringChars
	= lex [\>] StringCharacter* [\<] ;

syntax FunctionType
	= TypeArguments: Type type "(" {TypeArg ","}* arguments ")" ;

syntax PostPathChars
	= lex "\>" URLChars "|" ;

syntax NamedRegExp
	= lex "\<" Name "\>" 
	| lex ![/\<\>\\] 
	| lex [\\] [/\<\>\\] 
	| lex NamedBackslash ;

syntax OctalLongLiteral
	= OctalLongLiteral
	#  [a-zA-Z0-9_] ;

syntax Renamings
	= Default: "renaming" {Renaming ","}+ renamings ;

syntax Field
	= Index: IntegerLiteral fieldIndex 
	| Name: Name fieldName ;

syntax StringConstant
	= /*term(category("Constant"))*/ lex "\"" StringCharacter* "\"" ;

syntax PreProtocolChars
	= lex "|" URLChars "\<" ;

syntax ProtocolPart
	= NonInterpolated: ProtocolChars protocolChars 
	| Interpolated: PreProtocolChars pre Expression expression ProtocolTail tail ;

syntax Mapping[&Expression]
	= Default: &Expression from ":" &Expression to ;

syntax RegExp
	= lex "\<" Name "\>" 
	| lex ![/\<\>\\] 
	| lex [\\] [/\<\>\\] 
	| lex "\<" Name ":" NamedRegExp* "\>" 
	| lex Backslash ;

syntax Formal
	= TypeName: Type type Name name ;



syntax "one" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "fun" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "void" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "list" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "set" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "switch" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "test" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "try" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "bool" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "dynamic" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "global" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "import" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "anno" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "," =
	...
	# [.] [.] ;

syntax "fail" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "visit" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "datetime" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "any" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "true" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "insert" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "private" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "for" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "reified" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "\<=" =
	...
	# [=] [\>] ;

syntax "+" =
	...
	# [=] ;

syntax "*" =
	...
	# [=] ;

syntax "data" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "/" =
	...
	# [=] ;

syntax "throw" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "." =
	...
	# [.] ;

syntax "on " =
	...
	# [a-zA-Z0-9\-_] ;

syntax "int" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "else" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "-" =
	...
	# [=] ;

syntax "real" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "rel" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "assert" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "!" =
	...
	# [=] ;

syntax "!" =
	...
	# [~] ;

syntax "module" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "return" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "&" =
	...
	# [&] ;

syntax "&" =
	...
	# [=] ;

syntax "loc" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "num" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "constructor" =
	...
	# [a-zA-Z0-9\-_] ;

syntax ":" =
	...
	# [:] ;

syntax "?" =
	...
	# [=] ;

syntax "notin" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "notin" =
	...
	# [a-zA-Z\-_0-9] ;

syntax "\>" =
	...
	# [=] ;

syntax "str" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "=" =
	...
	# [=] ;

syntax "type" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "join" =
	...
	# [a-zA-Z\-_0-9] ;

syntax "join" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "\<" =
	...
	# [=] ;

syntax "it" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "in" =
	...
	# [a-zA-Z\-_0-9] ;

syntax "in" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "finally" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "catch" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "rule" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "if" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "adt" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "repeat" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "non-terminal" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "map" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "tuple" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "view" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "default" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "node" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "throws" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "solve" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "value" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "public" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "tag" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "extend" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "==" =
	...
	# [\>] ;

syntax "append" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "o" =
	...
	# [a-zA-Z\-_0-9] ;

syntax "all" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "://" =
	...
	# [\ \t\n\r] ;

syntax "case" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "while" =
	...
	# [a-zA-Z0-9\-_] ;

syntax "false" =
	...
	# [a-zA-Z0-9\-_] ;
