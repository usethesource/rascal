@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Davy Landman - Davy.Landman@cwi.nl - CWI}

// The grammar was based on the SDF2 definition in the 
// Java-frontend project for Stratego/XT
// https://github.com/metaborg/java-front

module lang::java::\syntax::Java15

start syntax CompilationUnit =
   compilationUnit: PackageDec? ImportDec* TypeDec*
  ;
  
syntax PackageDec =
   packageDec: Anno* "package"  PackageName ";" 
  ;
  
syntax PackageName =
   packageName: {Id "."}+ 
  ;
  
syntax ImportDec 
  = typeImportDec: "import"  TypeName ";" 
  | typeImportOnDemandDec: "import"  PackageName "." "*" ";" 
  | staticImportOnDemandDec: "import"  "static"  TypeName "." "*" ";" 
  | staticImportDec: "import"  "static"  TypeName "." Id ";" 
  ;
  
syntax TypeDec =
  InterfaceDec 
  | ClassDec 
  |  semicolon: ";" 
  ;
  
syntax InterfaceDec 
  = AnnoDecHead "{" AnnoElemDec* "}"
  | InterfaceDecHead "{" InterfaceMemberDec* "}" 
  ;

syntax AnnoDecHead 
  = annoDecHead: (InterfaceMod | Anno)* "@" "interface"  Id 
  ;
  
syntax AnnoElemDec
  = semicolon: ";" 
  | ClassDec 
  | ConstantDec 
  | InterfaceDec 
  | annoMethodDec: (Anno | AbstractMethodMod)* Type Id "(" ")" DefaultVal? ";" 
  ;
  
syntax InterfaceDecHead =
   interfaceDecHead: (InterfaceMod | Anno)* "interface"  Id TypeParams? ExtendsInterfaces? 
  ;

syntax LocalVarDec =
  @prefer localVarDec: (Anno | VarMod)* Type {VarDec ","}+ 
  ;

syntax TypeParams =
   typeParams: "\<" {TypeParam ","}+ "\>" 
  ;

syntax Literal =
  FloatLiteral 
  | CharLiteral 
  | BoolLiteral 
  | ClassLiteral 
  | StringLiteral 
  | NullLiteral 
  | IntLiteral 
  ;

syntax ClassDec =
  EnumDec 
  |  classDec: ClassDecHead ClassBody 
  ;

syntax ClassDecHead =
   classDecHead: (ClassMod | Anno)* "class"  Id TypeParams? Super? Interfaces? 
  ;

lexical SignedInteger =
  [+ \-]? [0-9]+ 
  ;

syntax ClassMod =
  "static" 
  | "public" 
  | "abstract" 
  | "protected" 
  | "final"
  | "strictfp" 
  | "private" 
  ;

lexical LEX[StringLiteral] =
   string: "\"" StringPart* "\"" 
  ;

syntax SwitchGroup =
   switchGroup: SwitchLabel+ BlockStm+ 
  ;

syntax ClassBodyDec =
  InstanceInit 
  | ClassMemberDec 
  | StaticInit 
  | ConstrDec 
  ;


syntax FloatType =
   float: "float"  
  |  double: "double"  
  ;

lexical HexaSignificand =
  [0] [X x] [0-9 A-F a-f]* "." [0-9 A-F a-f]* 
  | [0] [X x] [0-9 A-F a-f]+ 
  ;

lexical OctaNumeral =
  [0] [0-7]+ 
  ;

lexical HexaNumeral =
  [0] [X x] [0-9 A-F a-f]+ 
  ;

syntax ClassMemberDec =
   semicolon: ";" 
  | ClassDec 
  | MethodDec 
  | FieldDec 
  | InterfaceDec 
  ;

lexical LEX[CharLiteral] =
   char: "\'" CharContent "\'" 
  ;

syntax ConstantDec =
   constantDec: (ConstantMod | Anno)* Type {VarDec ","}+ ";" 
  ;

syntax ConstantMod =
  "static" 
  | "public" 
  | "final"
  ;

syntax SwitchBlock =
   switchBlock: "{" SwitchGroup* SwitchLabel* "}" 
  ;

syntax CondMid =
  bracket "?" Expr ":" 
  ;

syntax WildcardBound =
   wildcardLowerBound: "super"  RefType 
  |  wildcardUpperBound: "extends"  RefType 
  ;

lexical EscChar =
  "\\" 
  ;

syntax EnumDecHead =
   enumDecHead: (Anno | ClassMod)* "enum"  Id Interfaces? 
  ;

syntax PackageOrTypeName =
   packageOrTypeName: PackageOrTypeName "." Id 
  |  packageOrTypeName: Id 
  ;

lexical OctaEscape 
  = "\\" [0-3] [0-7]+ !>> [0-7] 
  | "\\" [0-7] !>> [0-7] 
  | "\\" [4-7] [0-7] 
  ;


syntax IntType =
   long: "long"  
  |  short: "short"  
  |  char: "char"  
  |  \int: "int"  
  |  byte: "byte"  
  ;

syntax VarInit =
  Expr 
  | ArrayInit 
  ;

syntax EnumBodyDecs =
   enumBodyDecs: ";" ClassBodyDec* 
  ;

syntax ClassType =
   classType: TypeDecSpec TypeArgs? 
  ;

syntax ExtendsInterfaces =
   extendsInterfaces: "extends"  {InterfaceType ","}+ 
  ;

lexical EscEscChar =
  "\\\\" 
  ;

syntax FormalParam =
   param: (Anno | VarMod)* Type VarDecId 
  |  varArityParam: (Anno | VarMod)* Type "..." VarDecId 
  ;

syntax StaticInit =
   staticInit: "static"  Block 
  ;

lexical DeciNumeral =
  [1-9] [0-9]* 
  | "0" 
  ;

syntax EnumConstArgs =
  bracket "(" {Expr ","}* ")" 
  ;

syntax LocalVarDecStm =
  @prefer localVarDecStm: LocalVarDec ";" 
  ;

keyword HexaSignificandKeywords =
  [0] [X x] "." 
  ;


lexical StringChars =
  FooStringChars 
  ;

syntax EnumConst =
   enumConst: Anno* Id EnumConstArgs? ClassBody? 
  ;

lexical LAYOUT =
  [\t-\n \a0C-\a0D \ ] 
  | Comment 
  ;

syntax NumType =
  FloatType 
  | IntType 
  ;

syntax MethodDecHead =
   methodDecHead: (Anno | MethodMod)* TypeParams? ResultType Id "(" {FormalParam ","}* ")" Throws? 
  |  deprMethodDecHead: (MethodMod | Anno)* TypeParams? ResultType Id "(" {FormalParam ","}* ")" Dim+ Throws? 
  ;

syntax Anno =
   \anno: "@" TypeName "(" {ElemValPair ","}* ")" 
  |  markerAnno: "@" TypeName 
  |  singleElemAnno: "@" TypeName "(" ElemVal \ ElemValKeywords ")" 
  ;

lexical CharContent =
  EscapeSeq 
  | UnicodeEscape 
  |  single: SingleChar 
  ;

syntax FieldDec =
   fieldDec: (FieldMod | Anno)* Type {VarDec ","}+ ";" 
  ;

syntax FieldMod =
  "public" 
  | "static" 
  | "transient" 
  | "protected" 
  | "volatile" 
  | "final"
  | "private" 
  ;

lexical Comment =
  "/**/" 
  | "//" EOLCommentChars !>> ![\n \a0D] LineTerminator 
  | "/*" !>> [*] CommentPart* "*/" 
  | "/**" !>> [/] CommentPart* "*/" 
  ;

syntax ArraySubscript =
  bracket "[" Expr "]" 
  ;

syntax FloatLiteral =
   float: HexaFloatLiteral !>> [D F d f] 
  |  float: DeciFloatLiteral \ DeciFloatLiteralKeywords !>> [D F d f] 
  ;


syntax ConstrBody =
   constrBody: "{" ConstrInv? BlockStm* "}" 
  ;

syntax FieldAccess =
   superField: "super"  "." Id 
  |  qSuperField: TypeName "." "super"  "." Id 
  ;

syntax FieldAccess =
   field: Expr!postDecr!postIncr!preDecr!preIncr!not!complement!plus!plusDec!minus!remain!div!mul!rightShift!uRightShift!leftShift!instanceOf!gt!ltEq!lt!gtEq!eq!notEq!and!excOr!or!lazyAnd!lazyOr!cond!assign!assignLeftShift!assignOr!assignAnd!assignRightShift!assignMul!assignRemain!assignPlus!assignExcOr!assignDiv!assignURightShift!assignMinus!exprName!castRef!castPrim "." Id 
  ;

lexical OctaLiteral =
  OctaNumeral !>> [0-7] [L l]? 
  ;

syntax ConstrInv =
   altConstrInv: TypeArgs? "this"  "(" {Expr ","}* ")" ";" 
  |  superConstrInv: TypeArgs? "super"  "(" {Expr ","}* ")" ";" 
  |  qSuperConstrInv: Expr "." TypeArgs? "super"  "(" {Expr ","}* ")" ";" 
  ;

lexical HexaFloatNumeral =
  @prefer HexaSignificand \ HexaSignificandKeywords !>> [0-9 A-F a-f] BinaryExponent 
  ;

syntax IntLiteral =
   hexa: HexaLiteral !>> [L l] 
  |  octa: OctaLiteral !>> [L l] 
  |  deci: DeciLiteral !>> [L l] 
  ;

lexical HexaLiteral =
  HexaNumeral !>> [0-9 A-F a-f] [L l]? 
  ;

syntax InterfaceMemberDec =
  ClassDec 
  |  semicolon: ";" 
  | InterfaceDec 
  | AbstractMethodDec 
  | ConstantDec 
  ;

syntax ElemValPair =
   elemValPair: Id "=" ElemVal \ ElemValKeywords 
  ;

syntax CatchClause =
   \catch: "catch"  "(" FormalParam ")" Block 
  ;

syntax ArrayInit =
   arrayInit: "{" {VarInit ","}* "," "}" 
  |  arrayInit: "{" {VarInit ","}* "}" 
  ;

syntax VarDecId =
   arrayVarDecId: Id Dim+ 
  | Id 
  ;

syntax Modifier =
  "final"
  | "strictfp" 
  | "private" 
  | "synchronized" 
  | "volatile" 
  | "protected" 
  | "transient" 
  | "abstract" 
  | "native" 
  | "static" 
  | "public" 
  ;

lexical DeciFloatLiteral =
  DeciFloatNumeral [D F d f]? 
  ;

syntax ElemVal =
  Anno 
  |  elemValArrayInit: "{" {ElemVal ","}* "}" 
  |  elemValArrayInit: "{" {ElemVal ","}* "," "}" 
  | Expr!assign
  ;

syntax InterfaceType =
   interfaceType: TypeDecSpec TypeArgs? 
  ;

syntax ConstrMod =
  "public" 
  | "protected" 
  | "private" 
  ;

lexical ID =
  [$ A-Z _ a-z] [$ 0-9 A-Z _ a-z]* 
  ;

syntax ConstrDec =
   constrDec: ConstrHead ConstrBody 
  ;

lexical DeciFloatDigits =
  [0-9]+ 
  | [0-9]* "." [0-9]* 
  ;



syntax ArrayAccess =
   arrayAccess: Expr!postDecr!postIncr!preDecr!preIncr!not!complement!plus!plusDec!minus!remain!div!mul!rightShift!uRightShift!leftShift!instanceOf!gt!ltEq!lt!gtEq!eq!notEq!and!excOr!or!lazyAnd!lazyOr!cond!assign!assignLeftShift!assignOr!assignAnd!assignRightShift!assignMul!assignRemain!assignPlus!assignExcOr!assignDiv!assignURightShift!assignMinus!castRef!castPrim ArraySubscript 
  ;


syntax ArrayBaseType =
  PrimType 
  | TypeName 
  |  unboundWld: TypeName "\<" "?" "\>" 
  ;

syntax TypeName =
   typeName: PackageOrTypeName "." Id 
  |  typeName: Id 
  ;

lexical DeciLiteral =
  DeciNumeral !>> [. 0-9 D F d f] [L l]? 
  ;

syntax SwitchLabel =
   \default: "default"  ":" 
  |  \case: "case"  Expr ":" 
  ;

syntax ConstrHead =
   constrDecHead: (ConstrMod | Anno)* TypeParams? Id "(" {FormalParam ","}* ")" Throws? 
  ;

syntax Stm =
   \continue: "continue"  Id? ";" 
  |  forEach: "for"  "(" FormalParam ":" Expr ")" Stm 
  |  \try: "try" Block CatchClause* "finally"  Block 
  |  \throw: "throw"  Expr ";" 
  | Block 
  |  assertStm: "assert"  Expr ":" Expr ";" 
  |  \for: "for" "(" {Expr ","}* ";" Expr? ";" {Expr ","}* ")" Stm 
  |  \try: "try"  Block CatchClause+ 
  |  labeled: Id ":" Stm 
  |  \for: "for"  "(" LocalVarDec ";" Expr? ";" {Expr ","}* ")" Stm 
  |  \switch: "switch"  "(" Expr ")" SwitchBlock 
  |  \if: "if" "(" Expr ")" Stm "else"  Stm 
  |  doWhile: "do"  Stm "while"  "(" Expr ")" ";" 
  |  synchronized: "synchronized"  "(" Expr ")" Block 
  | @prefer \if: "if"  "(" Expr ")" Stm 
  |  empty: ";" 
  |  \while: "while"  "(" Expr ")" Stm 
  |  assertStm: "assert"  Expr ";" 
  |  \return: "return"  Expr? ";" 
  |  \break: "break" Id? ";" 
  |  exprStm: Expr ";" 
  ;


syntax NullLiteral =
   null: "null" 
  ;

syntax ExceptionType =
  ClassType 
  ;

lexical EscapeSeq =
  NamedEscape 
  | OctaEscape 
  ;

layout LAYOUTLIST  =
  LAYOUT* !>> [\t-\n \a0C-\a0D \ ] !>> (  [/]  [*]  ) !>> (  [/]  [/]  ) !>> "/*" !>> "//"
  ;

syntax ResultType =
   \void: "void"  
  | Type 
  ;

syntax Expr =
  FieldAccess \ FieldAccessKeywords 
  |  newInstance: "new"  TypeArgs? ClassOrInterfaceType "(" {Expr ","}* ")" ClassBody? 
  |  invoke: MethodSpec "(" {Expr ","}* ")" 
  | bracket "(" Expr ")" 
  |  lit: Literal 
  |  qThis: TypeName "." "this"  
  | ArrayCreationExpr 
  |  this: "this"  
  | ArrayAccess \ ArrayAccessKeywords 
  ;

syntax Expr =
  right 
    ( right postIncr: Expr "++" 
    | right postDecr: Expr "--" 
    )
  >  plus: "+" !>> [+] Expr 
    |  not: "!" Expr 
    |  complement: "~" Expr 
    |  preIncr: "++" Expr 
    |  preDecr: "--" Expr 
    |  minus: "-" !>> [\-] Expr 
  > left 
      ( left div: Expr "/" !>> [/] Expr 
      | left remain: Expr "%" Expr 
      | left mul: Expr "*" Expr 
      )
  > left 
      ( left minus: Expr "-" !>> [\-] Expr 
      | left plus: Expr "+" !>> [+] Expr 
      )
  > left 
      ( left rightShift: Expr "\>\>" Expr 
      | left uRightShift: Expr "\>\>\>" Expr 
      | left leftShift: Expr "\<\<" Expr 
      )
  > left 
      ( left gtEq: Expr "\>=" Expr 
      | left instanceOf: Expr "instanceof" RefType 
      | left gt: Expr "\>" Expr 
      | left ltEq: Expr "\<=" Expr 
      | left lt: Expr "\<" Expr 
      )
  > left 
      ( left eq: Expr "==" Expr 
      | left notEq: Expr "!=" Expr 
      )
  > left and: Expr "&" Expr 
  > left excOr: Expr "^" Expr 
  > left or: Expr "|" Expr 
  > left lazyAnd: Expr "&&" Expr 
  > left lazyOr: Expr "||" Expr 
  > right cond: Expr CondMid Expr 
  > right 
      ( right assignMul: LHS "*=" Expr 
      | right assignLeftShift: LHS "\<\<=" Expr 
      | right assignOr: LHS "|=" Expr 
      | right assignAnd: LHS "&=" Expr 
      | right assignRightShift: LHS "\>\>=" Expr 
      | right assignRemain: LHS "%=" Expr 
      | right assignPlus: LHS "+=" Expr 
      | right assignExcOr: LHS "^=" Expr 
      | right assign: LHS "=" Expr 
      | right assignDiv: LHS "/=" Expr 
      | right assignURightShift: LHS "\>\>\>=" Expr 
      | right assignMinus: LHS "-=" Expr 
      )
  ;

syntax Expr =
   castPrim: "(" PrimType ")" Expr 
  > left 
      ( left div: Expr "/" !>> [/] Expr 
      | left remain: Expr "%" Expr 
      | left mul: Expr "*" Expr 
      )
  ;

syntax Expr =
   castRef: "(" RefType ")" Expr 
  >  plus: "+" !>> [+] Expr 
    |  preIncr: "++" Expr 
    |  preDecr: "--" Expr 
    |  minus: "-" !>> [\-] Expr 
  ;

syntax Expr =
   qNewInstance: Expr "." "new" TypeArgs? Id TypeArgs? "(" {Expr ","}* ")" ClassBody? 
  > right 
      ( right postDecr: Expr "--" 
      | right postIncr: Expr "++" 
      )
  ;

syntax Expr =
  right 
    ( right postDecr: Expr "--" 
    | right postIncr: Expr "++" 
    )
  >  castPrim: "(" PrimType ")" Expr 
    |  castRef: "(" RefType ")" Expr 
  ;

syntax Expr =
  exprName: ExprName 
  ;

lexical NamedEscape =
   namedEscape: "\\" [\" \' \\ b f n r t] 
  ;

syntax ArrayType =
   arrayType: Type "[" "]" 
  ;

syntax ClassBody =
   classBody: "{" ClassBodyDec* "}" 
  ;

lexical BinaryExponent =
  [P p] SignedInteger !>> [0-9] 
  ;

lexical BlockCommentChars =
  ![* \\]+ 
  ;

syntax TypeDecSpec =
   member: TypeDecSpec TypeArgs "." Id 
  | TypeName 
  ;

syntax PrimType =
   boolean: "boolean"  
  | NumType 
  ;

syntax EnumDec =
   enumDec: EnumDecHead EnumBody 
  ;

keyword Keyword =
  "continue" 
  | "package" 
  | "short" 
  | "boolean" 
  | "for" 
  | "extends" 
  | "do" 
  | "strictfp" 
  | "if" 
  | "enum" 
  | "synchronized" 
  | "else" 
  | "interface" 
  | "return" 
  | "private" 
  | "volatile" 
  | "default" 
  | "throws" 
  | "static" 
  | "long" 
  | "throw" 
  | "this" 
  | "catch" 
  | "super" 
  | "const" 
  | "switch" 
  | "int" 
  | "implements" 
  | "native" 
  | "abstract" 
  | "break" 
  | "goto" 
  | "final" 
  | "class" 
  | "byte" 
  | "instanceof" 
  | "void" 
  | "finally" 
  | "try" 
  | "new" 
  | "float" 
  | "public" 
  | "transient" 
  | "char" 
  | "assert" 
  | "case" 
  | "while" 
  | "double" 
  | "protected" 
  | "import" 
  ;

lexical FooStringChars =
  ([\a00] | ![\n \a0D \" \\])+ 
  ;

syntax ActualTypeArg =
   wildcard: "?" WildcardBound? 
  | Type 
  ;

lexical StringPart =
  UnicodeEscape 
  | EscapeSeq 
  |  chars: StringChars !>> ![\n \a0D \" \\]  !>> [\a00]
  ;

syntax MethodName =
   methodName: AmbName "." Id 
  |  methodName: Id 
  ;

keyword FieldAccessKeywords =
  ExprName "." Id 
  ;

lexical EOLCommentChars =
  ![\n \a0D]* 
  ;

syntax InterfaceMod =
  "protected" 
  | "public" 
  | "static" 
  | "abstract" 
  | "private" 
  | "strictfp" 
  ;


lexical SingleChar =
  ![\n \a0D \' \\] 
  ;

syntax ClassLiteral =
   voidClass: "void"  "." "class"  
  |  class: Type "." "class"  
  ;

syntax StringLiteral =
  LEX[StringLiteral] 
  ;

syntax AbstractMethodDec =
   abstractMethodDec: (Anno | AbstractMethodMod)* TypeParams? ResultType Id "(" {FormalParam ","}* ")" Throws? ";" 
  |  deprAbstractMethodDec: (Anno | AbstractMethodMod)* TypeParams? ResultType Id "(" {FormalParam ","}* ")" Dim+ Throws? ";" 
  ;

syntax AbstractMethodMod =
  "abstract" 
  | "public" 
  ;

keyword ElemValKeywords =
  LHS "=" Expr 
  ;

lexical CommentPart =
  UnicodeEscape 
  | BlockCommentChars !>> ![* \\] 
  | EscChar !>> [\\ u] 
  | Asterisk !>> [/] 
  | EscEscChar 
  ;

syntax Id =
   id: [$ A-Z _ a-z] !<< ID \ IDKeywords !>> [$ 0-9 A-Z _ a-z] 
  ;

keyword ArrayAccessKeywords =
  ArrayCreationExpr ArraySubscript 
  ;

syntax TypeBound =
   typeBound: "extends"  {ClassOrInterfaceType "&"}+ 
  ;

syntax BoolLiteral
  = \false: "false" 
  | \true: "true" 
  ;


syntax MethodBody =
   noMethodBody: ";" 
  | Block 
  ;

syntax ExprName =
   exprName: AmbName "." Id 
  |  exprName: Id 
  ;

syntax DefaultVal =
   defaultVal: "default"  ElemVal \ ElemValKeywords 
  ;

syntax MethodDec =
   methodDec: MethodDecHead MethodBody 
  ;

syntax AmbName =
   ambName: Id 
  |  ambName: AmbName "." Id 
  ;

syntax MethodMod =
  "static" 
  | "protected" 
  | "synchronized" 
  | "strictfp" 
  | "private" 
  | "final"
  | "public" 
  | "native" 
  | "abstract" 
  ;

syntax RefType =
  ArrayType 
  | ClassOrInterfaceType 
  ;

syntax ArrayCreationExpr =
   newArray: "new"  ArrayBaseType DimExpr+ Dim* !>> "["
  |  newArray: "new" ArrayBaseType Dim+ !>> "[" ArrayInit 
  ;



syntax LHS =
  ExprName 
  | ArrayAccess \ ArrayAccessKeywords 
  | FieldAccess \ FieldAccessKeywords 
  ;

syntax TypeArgs =
   typeArgs: "\<" {ActualTypeArg ","}+ "\>" 
  ;


syntax TypeParam =
   typeParam: TypeVarId TypeBound? 
  ;

lexical DeciFloatExponentPart =
  [E e] SignedInteger !>> [0-9] 
  ;

syntax MethodSpec =
   method: MethodName 
  |  superMethod: "super"  "." TypeArgs? Id 
  |  genericMethod: AmbName "." TypeArgs Id 
  |  qSuperMethod: TypeName "." "super"  "." TypeArgs? Id 
  ;

syntax MethodSpec =
   method: Expr!postDecr!postIncr!preDecr!preIncr!not!complement!plus!plusDec!minus!remain!div!mul!rightShift!uRightShift!leftShift!instanceOf!gt!ltEq!lt!gtEq!eq!notEq!and!excOr!or!lazyAnd!lazyOr!cond!assign!assignLeftShift!assignOr!assignAnd!assignRightShift!assignMul!assignRemain!assignPlus!assignExcOr!assignDiv!assignURightShift!assignMinus!exprName!castPrim!castRef "." TypeArgs? Id 
  ;

syntax Type =
  PrimType 
  | RefType 
  ;

syntax Super =
   superDec: "extends"  ClassType 
  ;

syntax CharLiteral =
  LEX[CharLiteral] 
  ;

lexical EndOfFile =
  
  ;

keyword DeciFloatLiteralKeywords =
  [0-9]+ 
  ;

keyword DeciFloatDigitsKeywords =
  "." 
  ;

syntax InstanceInit =
   instanceInit: Block 
  ;

keyword IDKeywords =
  "null" 
  | Keyword 
  | "true" 
  | "false" 
  ;

syntax EnumBody =
   enumBody: "{" {EnumConst ","}* EnumBodyDecs? "}" 
  |  enumBody: "{" {EnumConst ","}* "," EnumBodyDecs? "}" 
  ;


lexical DeciFloatNumeral
	= [0-9] !<< [0-9]+ DeciFloatExponentPart
	| [0-9] !<< [0-9]+ >> [D F d f]
	| [0-9] !<< [0-9]+ "." [0-9]* !>> [0-9] DeciFloatExponentPart?
	| [0-9] !<< "." [0-9]+ !>> [0-9] DeciFloatExponentPart?
  ;

lexical CarriageReturn =
  [\a0D] 
  ;

syntax Throws =
   throwsDec: "throws"  {ExceptionType ","}+ 
  ;

syntax Block =
   block: "{" BlockStm* "}" 
  ;

syntax TypeVar =
   typeVar: TypeVarId 
  ;

syntax Dim =
   dim: "[" "]" 
  ;

syntax TypeVarId =
  Id 
  ;

lexical UnicodeEscape =
   unicodeEscape: "\\" [u]+ [0-9 A-F a-f] [0-9 A-F a-f] [0-9 A-F a-f] [0-9 A-F a-f] 
  ;

lexical LineTerminator =
  [\n] 
  | EndOfFile !>> ![] 
  | [\a0D] [\n] 
  | CarriageReturn !>> [\n] 
  ;

lexical HexaFloatLiteral =
  HexaFloatNumeral [D F d f]? 
  ;

syntax BlockStm =
  Stm 
  |  classDecStm: ClassDec 
  | LocalVarDecStm 
  ;

syntax DimExpr =
   dim: "[" Expr "]" 
  ;

syntax Interfaces =
   implementsDec: "implements"  {InterfaceType ","}+ 
  ;

lexical Asterisk =
  "*" 
  ;

syntax VarDec =
   varDec: VarDecId "=" VarInit 
  |  varDec: VarDecId 
  ;

syntax VarMod =
  "final"
  ;

syntax ClassOrInterfaceType =
   classOrInterfaceType: TypeDecSpec TypeArgs? 
  ;


extend lang::sdf2::filters::DirectThenCountPreferAvoid;

bool expectedAmb({(Expr)`(<RefType t>) <Expr e>`, appl(_,[(Expr)`(<ExprName n>)`,_*])}) = true; // (A) + 1
bool expectedAmb({appl(_,[_*,(Expr)`(<RefType t>) <Expr e>`]), appl(_,[appl(_,[_*,(Expr)`(<ExprName n>)`]),_*])}) = true; // 1 + (A) + 1
default bool expectedAmb(set[Tree] t) = false;
          
