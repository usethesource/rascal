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


syntax LocalVarDec =
  @prefer localVarDec: (Anno | VarMod)* Type {VarDec ","}+ 
  ;

syntax TypeParams =
   typeParams: "\<" {TypeParam ","}+ "\>" 
  ;

syntax Protected =
   protected: "protected" !>> [$ 0-9 A-Z _ a-z] 
  ;

syntax Static =
   static: "static" !>> [$ 0-9 A-Z _ a-z] 
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
   classDecHead: (ClassMod | Anno)* "class" !>> [$ 0-9 A-Z _ a-z] Id TypeParams? Super? Interfaces? 
  ;

lexical SignedInteger =
  [+ \-]? [0-9]+ 
  ;

syntax ClassMod =
  Static 
  | Public 
  | Abstract 
  | Protected 
  | Final 
  | StrictFP 
  | Private 
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

syntax Public =
   \public: "public" !>> [$ 0-9 A-Z _ a-z] 
  ;

syntax FloatType =
   float: "float" !>> [$ 0-9 A-Z _ a-z] 
  |  double: "double" !>> [$ 0-9 A-Z _ a-z] 
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
  Static 
  | Public 
  | Final 
  ;

syntax SwitchBlock =
   switchBlock: "{" SwitchGroup* SwitchLabel* "}" 
  ;

syntax CondMid =
  bracket "?" Expr ":" 
  ;

syntax WildcardBound =
   wildcardLowerBound: "super" !>> [$ 0-9 A-Z _ a-z] RefType 
  |  wildcardUpperBound: "extends" !>> [$ 0-9 A-Z _ a-z] RefType 
  ;

lexical EscChar =
  "\\" 
  ;

syntax EnumDecHead =
   enumDecHead: (Anno | ClassMod)* "enum" !>> [$ 0-9 A-Z _ a-z] Id Interfaces? 
  ;

syntax PackageOrTypeName =
   packageOrTypeName: PackageOrTypeName "." Id 
  |  packageOrTypeName: Id 
  ;

lexical OctaEscape =
   octaEscape3: "\\" [0-3] [0-7] [0-7] 
  |  octaEscape2: "\\" [0-3] LastOcta !>> [0-7] 
  |  octaEscape1: "\\" LastOcta !>> [0-7] 
  |  octaEscape2: "\\" [4-7] [0-7] 
  ;

syntax InterfaceDecHead =
   interfaceDecHead: (InterfaceMod | Anno)* "interface" !>> [$ 0-9 A-Z _ a-z] Id TypeParams? ExtendsInterfaces? 
  ;

syntax IntType =
   long: "long" !>> [$ 0-9 A-Z _ a-z] 
  |  short: "short" !>> [$ 0-9 A-Z _ a-z] 
  |  char: "char" !>> [$ 0-9 A-Z _ a-z] 
  |  \int: "int" !>> [$ 0-9 A-Z _ a-z] 
  |  byte: "byte" !>> [$ 0-9 A-Z _ a-z] 
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
   extendsInterfaces: "extends" !>> [$ 0-9 A-Z _ a-z] {InterfaceType ","}+ 
  ;

lexical EscEscChar =
  "\\\\" 
  ;

syntax FormalParam =
   param: (Anno | VarMod)* Type VarDecId 
  |  varArityParam: (Anno | VarMod)* Type "..." VarDecId 
  ;

syntax StaticInit =
   staticInit: "static" !>> [$ 0-9 A-Z _ a-z] Block 
  ;

lexical DeciNumeral =
  [1-9] [0-9]* 
  | "0" 
  ;

syntax EnumConstArgs =
  bracket "(" {Expr ","}* ")" 
  ;

syntax Volatile =
   volatile: "volatile" !>> [$ 0-9 A-Z _ a-z] 
  ;

syntax LocalVarDecStm =
  @prefer localVarDecStm: LocalVarDec ";" 
  ;

keyword HexaSignificandKeywords =
  [0] [X x] "." 
  ;

start syntax CompilationUnit =
   compilationUnit: PackageDec? ImportDec* TypeDec*
  ;

lexical StringChars =
  FooStringChars 
  ;

syntax EnumConst =
   enumConst: Id EnumConstArgs? ClassBody? 
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
  Public 
  | Static 
  | Transient 
  | Protected 
  | Volatile 
  | Final 
  | Private 
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

syntax PackageName =
   packageName: {Id "."}+ 
  ;

syntax ConstrBody =
   constrBody: "{" ConstrInv? BlockStm* "}" 
  ;

syntax FieldAccess =
   superField: "super" !>> [$ 0-9 A-Z _ a-z] "." Id 
  |  qSuperField: TypeName "." "super" !>> [$ 0-9 A-Z _ a-z] "." Id 
  ;

syntax FieldAccess =
   field: Expr!postDecr!postIncr!preDecr!preIncr!not!complement!plus!plusDec!minus!remain!div!mul!rightShift!uRightShift!leftShift!instanceOf!gt!ltEq!lt!gtEq!eq!notEq!and!excOr!or!lazyAnd!lazyOr!cond!assign!assignLeftShift!assignOr!assignAnd!assignRightShift!assignMul!assignRemain!assignPlus!assignExcOr!assignDiv!assignURightShift!assignMinus!exprName!castRef!castPrim "." Id 
  ;

lexical OctaLiteral =
  OctaNumeral !>> [0-7] [L l]? 
  ;

syntax ConstrInv =
   altConstrInv: TypeArgs? "this" !>> [$ 0-9 A-Z _ a-z] "(" {Expr ","}* ")" ";" 
  |  superConstrInv: TypeArgs? "super" !>> [$ 0-9 A-Z _ a-z] "(" {Expr ","}* ")" ";" 
  |  qSuperConstrInv: Expr "." TypeArgs? "super" !>> [$ 0-9 A-Z _ a-z] "(" {Expr ","}* ")" ";" 
  ;

lexical HexaFloatNumeral =
  HexaSignificand \ HexaSignificandKeywords !>> [0-9 A-F a-f] BinaryExponent 
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
   \catch: "catch" !>> [$ 0-9 A-Z _ a-z] "(" FormalParam ")" Block 
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
  Final 
  | StrictFP 
  | Private 
  | Synchronized 
  | Volatile 
  | Protected 
  | Transient 
  | Abstract 
  | Native 
  | Static 
  | Public 
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
  Public 
  | Protected 
  | Private 
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

lexical LastOcta =
  [0-7] 
  ;

syntax PackageDec =
   packageDec: Anno* "package" !>> [$ 0-9 A-Z _ a-z] PackageName ";" 
  ;

syntax ArrayAccess =
   arrayAccess: Expr!postDecr!postIncr!preDecr!preIncr!not!complement!plus!plusDec!minus!remain!div!mul!rightShift!uRightShift!leftShift!instanceOf!gt!ltEq!lt!gtEq!eq!notEq!and!excOr!or!lazyAnd!lazyOr!cond!assign!assignLeftShift!assignOr!assignAnd!assignRightShift!assignMul!assignRemain!assignPlus!assignExcOr!assignDiv!assignURightShift!assignMinus!castRef!castPrim ArraySubscript 
  ;

syntax ImportDec =
   typeImportDec: "import" !>> [$ 0-9 A-Z _ a-z] TypeName ";" 
  |  staticImportOnDemandDec: "import" !>> [$ 0-9 A-Z _ a-z] "static" !>> [$ 0-9 A-Z _ a-z] TypeName "." "*" ";" 
  |  staticImportDec: "import" !>> [$ 0-9 A-Z _ a-z] "static" !>> [$ 0-9 A-Z _ a-z] TypeName "." Id ";" 
  |  typeImportOnDemandDec: "import" !>> [$ 0-9 A-Z _ a-z] PackageName "." "*" ";" 
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
   \default: "default" !>> [$ 0-9 A-Z _ a-z] ":" 
  |  \case: "case" !>> [$ 0-9 A-Z _ a-z] Expr ":" 
  ;

syntax ConstrHead =
   constrDecHead: (ConstrMod | Anno)* TypeParams? Id "(" {FormalParam ","}* ")" Throws? 
  ;

syntax Stm =
   \continue: "continue" !>> [$ 0-9 A-Z _ a-z] Id? ";" 
  |  forEach: "for" !>> [$ 0-9 A-Z _ a-z] "(" FormalParam ":" Expr ")" Stm 
  |  \try: "try" !>> [$ 0-9 A-Z _ a-z] Block CatchClause* "finally" !>> [$ 0-9 A-Z _ a-z] Block 
  |  \throw: "throw" !>> [$ 0-9 A-Z _ a-z] Expr ";" 
  | Block 
  |  assertStm: "assert" !>> [$ 0-9 A-Z _ a-z] Expr ":" Expr ";" 
  |  \for: "for" !>> [$ 0-9 A-Z _ a-z] "(" {Expr ","}* ";" Expr? ";" {Expr ","}* ")" Stm 
  |  \try: "try" !>> [$ 0-9 A-Z _ a-z] Block CatchClause+ 
  |  labeled: Id ":" Stm 
  |  \for: "for" !>> [$ 0-9 A-Z _ a-z] "(" LocalVarDec ";" Expr? ";" {Expr ","}* ")" Stm 
  |  \switch: "switch" !>> [$ 0-9 A-Z _ a-z] "(" Expr ")" SwitchBlock 
  |  \if: "if" !>> [$ 0-9 A-Z _ a-z] "(" Expr ")" Stm "else" !>> [$ 0-9 A-Z _ a-z] Stm 
  |  doWhile: "do" !>> [$ 0-9 A-Z _ a-z] Stm "while" !>> [$ 0-9 A-Z _ a-z] "(" Expr ")" ";" 
  |  synchronized: "synchronized" !>> [$ 0-9 A-Z _ a-z] "(" Expr ")" Block 
  | @prefer \if: "if" !>> [$ 0-9 A-Z _ a-z] "(" Expr ")" Stm 
  |  empty: ";" 
  |  \while: "while" !>> [$ 0-9 A-Z _ a-z] "(" Expr ")" Stm 
  |  assertStm: "assert" !>> [$ 0-9 A-Z _ a-z] Expr ";" 
  |  \return: "return" !>> [$ 0-9 A-Z _ a-z] Expr? ";" 
  |  \break: "break" !>> [$ 0-9 A-Z _ a-z] Id? ";" 
  |  exprStm: Expr ";" 
  ;

syntax Bool =
   \false: "false" 
  |  \true: "true" 
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
   \void: "void" !>> [$ 0-9 A-Z _ a-z] 
  | Type 
  ;

syntax Expr =
  FieldAccess \ FieldAccessKeywords 
  |  newInstance: "new" !>> [$ 0-9 A-Z _ a-z] TypeArgs? ClassOrInterfaceType "(" {Expr ","}* ")" ClassBody? 
  |  invoke: MethodSpec "(" {Expr ","}* ")" 
  | bracket "(" Expr ")" 
  |  lit: Literal 
  |  qThis: TypeName "." "this" !>> [$ 0-9 A-Z _ a-z] 
  | ArrayCreationExpr 
  |  this: "this" !>> [$ 0-9 A-Z _ a-z] 
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
      | left instanceOf: Expr "instanceof" !>> [$ 0-9 A-Z _ a-z] RefType 
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
   qNewInstance: Expr "." "new" !>> [$ 0-9 A-Z _ a-z] TypeArgs? Id TypeArgs? "(" {Expr ","}* ")" ClassBody? 
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
   boolean: "boolean" !>> [$ 0-9 A-Z _ a-z] 
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
  ![\n \a0D \" \\]+ 
  ;

syntax ActualTypeArg =
   wildcard: "?" WildcardBound? 
  | Type 
  ;

lexical StringPart =
  UnicodeEscape 
  | EscapeSeq 
  |  chars: StringChars !>> ![\n \a0D \" \\] 
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
  Protected 
  | Public 
  | Static 
  | Abstract 
  | Private 
  | StrictFP 
  ;

syntax InterfaceDec =
  AnnoDec 
  |  interfaceDec: InterfaceDecHead "{" InterfaceMemberDec* "}" 
  ;

lexical SingleChar =
  ![\n \a0D \' \\] 
  ;

syntax Native =
   native: "native" !>> [$ 0-9 A-Z _ a-z] 
  ;

syntax ClassLiteral =
   voidClass: "void" !>> [$ 0-9 A-Z _ a-z] "." "class" !>> [$ 0-9 A-Z _ a-z] 
  |  class: Type "." "class" !>> [$ 0-9 A-Z _ a-z] 
  ;

syntax Synchronized =
   synchronized: "synchronized" !>> [$ 0-9 A-Z _ a-z] 
  ;

syntax StringLiteral =
  LEX[StringLiteral] 
  ;

syntax AbstractMethodDec =
   abstractMethodDec: (Anno | AbstractMethodMod)* TypeParams? ResultType Id "(" {FormalParam ","}* ")" Throws? ";" 
  |  deprAbstractMethodDec: (Anno | AbstractMethodMod)* TypeParams? ResultType Id "(" {FormalParam ","}* ")" Dim+ Throws? ";" 
  ;

syntax AbstractMethodMod =
  Abstract 
  | Public 
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
   typeBound: "extends" !>> [$ 0-9 A-Z _ a-z] {ClassOrInterfaceType "&"}+ 
  ;

syntax BoolLiteral =
   \bool: Bool 
  ;

syntax AnnoElemDec =
  EnumDec 
  |  semicolon: ";" 
  | ClassDec 
  | ConstantDec 
  | InterfaceDec 
  | AnnoDec 
  |  annoMethodDec: AbstractMethodMod* Type Id "(" ")" DefaultVal? ";" 
  ;

syntax Abstract =
   abstract: "abstract" !>> [$ 0-9 A-Z _ a-z] 
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
   defaultVal: "default" !>> [$ 0-9 A-Z _ a-z] ElemVal \ ElemValKeywords 
  ;

syntax MethodDec =
   methodDec: MethodDecHead MethodBody 
  ;

syntax AmbName =
   ambName: Id 
  |  ambName: AmbName "." Id 
  ;

syntax MethodMod =
  Static 
  | Protected 
  | Synchronized 
  | StrictFP 
  | Private 
  | Final 
  | Public 
  | Native 
  | Abstract 
  ;

syntax RefType =
  ArrayType 
  | ClassOrInterfaceType 
  ;

syntax ArrayCreationExpr =
   newArray: "new" !>> [$ 0-9 A-Z _ a-z] ArrayBaseType DimExpr+ Dim* !>> "["
  |  newArray: "new" !>> [$ 0-9 A-Z _ a-z] ArrayBaseType Dim+ !>> "[" ArrayInit 
  ;

syntax TypeDec =
  InterfaceDec 
  | ClassDec 
  |  semicolon: ";" 
  ;

syntax Final =
   final: "final" !>> [$ 0-9 A-Z _ a-z] 
  ;

syntax LHS =
  ExprName 
  | ArrayAccess \ ArrayAccessKeywords 
  | FieldAccess \ FieldAccessKeywords 
  ;

syntax TypeArgs =
   typeArgs: "\<" {ActualTypeArg ","}+ "\>" 
  ;

syntax AnnoDec =
   annoDec: AnnoDecHead "{" AnnoElemDec* "}" 
  ;

syntax AnnoDecHead =
   annoDecHead: (InterfaceMod | Anno)* "@" "interface" !>> [$ 0-9 A-Z _ a-z] Id 
  ;

syntax TypeParam =
   typeParam: TypeVarId TypeBound? 
  ;

lexical DeciFloatExponentPart =
  [E e] SignedInteger !>> [0-9] 
  ;

syntax MethodSpec =
   method: MethodName 
  |  superMethod: "super" !>> [$ 0-9 A-Z _ a-z] "." TypeArgs? Id 
  |  genericMethod: AmbName "." TypeArgs Id 
  |  qSuperMethod: TypeName "." "super" !>> [$ 0-9 A-Z _ a-z] "." TypeArgs? Id 
  ;

syntax MethodSpec =
   method: Expr!postDecr!postIncr!preDecr!preIncr!not!complement!plus!plusDec!minus!remain!div!mul!rightShift!uRightShift!leftShift!instanceOf!gt!ltEq!lt!gtEq!eq!notEq!and!excOr!or!lazyAnd!lazyOr!cond!assign!assignLeftShift!assignOr!assignAnd!assignRightShift!assignMul!assignRemain!assignPlus!assignExcOr!assignDiv!assignURightShift!assignMinus!exprName!castPrim!castRef "." TypeArgs? Id 
  ;

syntax Type =
  PrimType 
  | RefType 
  ;

syntax Super =
   superDec: "extends" !>> [$ 0-9 A-Z _ a-z] ClassType 
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

syntax Private =
   \private: "private" !>> [$ 0-9 A-Z _ a-z] 
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
   throwsDec: "throws" !>> [$ 0-9 A-Z _ a-z] {ExceptionType ","}+ 
  ;

syntax Block =
   block: "{" BlockStm* "}" 
  ;

syntax TypeVar =
   typeVar: TypeVarId 
  ;

syntax Transient =
   transient: "transient" !>> [$ 0-9 A-Z _ a-z] 
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

syntax StrictFP =
   strictFP: "strictfp" !>> [$ 0-9 A-Z _ a-z] 
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
   implementsDec: "implements" !>> [$ 0-9 A-Z _ a-z] {InterfaceType ","}+ 
  ;

lexical Asterisk =
  "*" 
  ;

syntax VarDec =
   varDec: VarDecId "=" VarInit 
  |  varDec: VarDecId 
  ;

syntax VarMod =
  Final 
  ;

syntax ClassOrInterfaceType =
   classOrInterfaceType: TypeDecSpec TypeArgs? 
  ;


extend lang::sdf2::filters::PreferAvoid;
extend lang::sdf2::filters::CountPreferAvoid;

bool expectedAmb({(Expr)`(<RefType t>) <Expr e>`, appl(_,[(Expr)`(<ExprName n>)`,_*])}) = true; // (A) + 1
bool expectedAmb({appl(_,[_*,(Expr)`(<RefType t>) <Expr e>`]), appl(_,[appl(_,[_*,(Expr)`(<ExprName n>)`]),_*])}) = true; // 1 + (A) + 1
default bool expectedAmb(set[Tree] t) = false;
          
