@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Davy Landman - Davy.Landman@cwi.nl - CWI}

The grammar was based on the SDF2 definition in the  Java-frontend project for Stratego/XT 
See https://github.com/metaborg/java-front

}
@pitfalls{

* The grammar has been changed to use Rascal's disambiguation constructs rather than SDF2's constructs,
but this has not been well tested.
} 

module lang::java::\syntax::Java15

import ParseTree;

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

lexical LEX_StringLiteral =
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

lexical LEX_CharLiteral =
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



syntax EnumConstArgs =
  bracket "(" {Expr ","}* ")" 
  ;

syntax LocalVarDecStm =
  @prefer localVarDecStm: LocalVarDec ";" 
  ;

keyword HexaSignificandKeywords = "0x." | "0X.";

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
  |  singleElemAnno: "@" TypeName "(" ElemVal ")" 
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
  HexaSignificand \ HexaSignificandKeywords !>> [0-9 A-F a-f] BinaryExponent 
  ;

syntax IntLiteral =
   hexa: HexaLiteral !>> [L l.] 
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
   elemValPair: Id "=" ElemVal 
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

//lexical JavaLetter = [ \a24-\a25 \a41-\a5b \a5f-\a60 \a61-\a7b \u00a2-\u00a6 \u00aa-\u00ab \u00b5-\u00b6 \u00ba-\u00bb \u00c0-\u00d7 \u00d8-\u00f7 \u00f8-\u02c2 \u02c6-\u02d2 \u02e0-\u02e5 \u02ec-\u02ed \u02ee-\u02ef \u0370-\u0375 \u0376-\u0378 \u037a-\u037e \u0386-\u0387 \u0388-\u038b \u038c-\u038d \u038e-\u03a2 \u03a3-\u03f6 \u03f7-\u0482 \u048a-\u0528 \u0531-\u0557 \u0559-\u055a \u0561-\u0588 \u05d0-\u05eb \u05f0-\u05f3 \u060b-\u060c \u0620-\u064b \u066e-\u0670 \u0671-\u06d4 \u06d5-\u06d6 \u06e5-\u06e7 \u06ee-\u06f0 \u06fa-\u06fd \u06ff-\u0700 \u0710-\u0711 \u0712-\u0730 \u074d-\u07a6 \u07b1-\u07b2 \u07ca-\u07eb \u07f4-\u07f6 \u07fa-\u07fb \u0800-\u0816 \u081a-\u081b \u0824-\u0825 \u0828-\u0829 \u0840-\u0859 \u0904-\u093a \u093d-\u093e \u0950-\u0951 \u0958-\u0962 \u0971-\u0978 \u0979-\u0980 \u0985-\u098d \u098f-\u0991 \u0993-\u09a9 \u09aa-\u09b1 \u09b2-\u09b3 \u09b6-\u09ba \u09bd-\u09be \u09ce-\u09cf \u09dc-\u09de \u09df-\u09e2 \u09f0-\u09f4 \u09fb-\u09fc \u0a05-\u0a0b \u0a0f-\u0a11 \u0a13-\u0a29 \u0a2a-\u0a31 \u0a32-\u0a34 \u0a35-\u0a37 \u0a38-\u0a3a \u0a59-\u0a5d \u0a5e-\u0a5f \u0a72-\u0a75 \u0a85-\u0a8e \u0a8f-\u0a92 \u0a93-\u0aa9 \u0aaa-\u0ab1 \u0ab2-\u0ab4 \u0ab5-\u0aba \u0abd-\u0abe \u0ad0-\u0ad1 \u0ae0-\u0ae2 \u0af1-\u0af2 \u0b05-\u0b0d \u0b0f-\u0b11 \u0b13-\u0b29 \u0b2a-\u0b31 \u0b32-\u0b34 \u0b35-\u0b3a \u0b3d-\u0b3e \u0b5c-\u0b5e \u0b5f-\u0b62 \u0b71-\u0b72 \u0b83-\u0b84 \u0b85-\u0b8b \u0b8e-\u0b91 \u0b92-\u0b96 \u0b99-\u0b9b \u0b9c-\u0b9d \u0b9e-\u0ba0 \u0ba3-\u0ba5 \u0ba8-\u0bab \u0bae-\u0bba \u0bd0-\u0bd1 \u0bf9-\u0bfa \u0c05-\u0c0d \u0c0e-\u0c11 \u0c12-\u0c29 \u0c2a-\u0c34 \u0c35-\u0c3a \u0c3d-\u0c3e \u0c58-\u0c5a \u0c60-\u0c62 \u0c85-\u0c8d \u0c8e-\u0c91 \u0c92-\u0ca9 \u0caa-\u0cb4 \u0cb5-\u0cba \u0cbd-\u0cbe \u0cde-\u0cdf \u0ce0-\u0ce2 \u0cf1-\u0cf3 \u0d05-\u0d0d \u0d0e-\u0d11 \u0d12-\u0d3b \u0d3d-\u0d3e \u0d4e-\u0d4f \u0d60-\u0d62 \u0d7a-\u0d80 \u0d85-\u0d97 \u0d9a-\u0db2 \u0db3-\u0dbc \u0dbd-\u0dbe \u0dc0-\u0dc7 \u0e01-\u0e31 \u0e32-\u0e34 \u0e3f-\u0e47 \u0e81-\u0e83 \u0e84-\u0e85 \u0e87-\u0e89 \u0e8a-\u0e8b \u0e8d-\u0e8e \u0e94-\u0e98 \u0e99-\u0ea0 \u0ea1-\u0ea4 \u0ea5-\u0ea6 \u0ea7-\u0ea8 \u0eaa-\u0eac \u0ead-\u0eb1 \u0eb2-\u0eb4 \u0ebd-\u0ebe \u0ec0-\u0ec5 \u0ec6-\u0ec7 \u0edc-\u0ede \u0f00-\u0f01 \u0f40-\u0f48 \u0f49-\u0f6d \u0f88-\u0f8d \u1000-\u102b \u103f-\u1040 \u1050-\u1056 \u105a-\u105e \u1061-\u1062 \u1065-\u1067 \u106e-\u1071 \u1075-\u1082 \u108e-\u108f \u10a0-\u10c6 \u10d0-\u10fb \u10fc-\u10fd \u1100-\u1249 \u124a-\u124e \u1250-\u1257 \u1258-\u1259 \u125a-\u125e \u1260-\u1289 \u128a-\u128e \u1290-\u12b1 \u12b2-\u12b6 \u12b8-\u12bf \u12c0-\u12c1 \u12c2-\u12c6 \u12c8-\u12d7 \u12d8-\u1311 \u1312-\u1316 \u1318-\u135b \u1380-\u1390 \u13a0-\u13f5 \u1401-\u166d \u166f-\u1680 \u1681-\u169b \u16a0-\u16eb \u16ee-\u16f1 \u1700-\u170d \u170e-\u1712 \u1720-\u1732 \u1740-\u1752 \u1760-\u176d \u176e-\u1771 \u1780-\u17b4 \u17d7-\u17d8 \u17db-\u17dd \u1820-\u1878 \u1880-\u18a9 \u18aa-\u18ab \u18b0-\u18f6 \u1900-\u191d \u1950-\u196e \u1970-\u1975 \u1980-\u19ac \u19c1-\u19c8 \u1a00-\u1a17 \u1a20-\u1a55 \u1aa7-\u1aa8 \u1b05-\u1b34 \u1b45-\u1b4c \u1b83-\u1ba1 \u1bae-\u1bb0 \u1bc0-\u1be6 \u1c00-\u1c24 \u1c4d-\u1c50 \u1c5a-\u1c7e \u1ce9-\u1ced \u1cee-\u1cf2 \u1d00-\u1dc0 \u1e00-\u1f16 \u1f18-\u1f1e \u1f20-\u1f46 \u1f48-\u1f4e \u1f50-\u1f58 \u1f59-\u1f5a \u1f5b-\u1f5c \u1f5d-\u1f5e \u1f5f-\u1f7e \u1f80-\u1fb5 \u1fb6-\u1fbd \u1fbe-\u1fbf \u1fc2-\u1fc5 \u1fc6-\u1fcd \u1fd0-\u1fd4 \u1fd6-\u1fdc \u1fe0-\u1fed \u1ff2-\u1ff5 \u1ff6-\u1ffd \u203f-\u2041 \u2054-\u2055 \u2071-\u2072 \u207f-\u2080 \u2090-\u209d \u20a0-\u20ba \u2102-\u2103 \u2107-\u2108 \u210a-\u2114 \u2115-\u2116 \u2119-\u211e \u2124-\u2125 \u2126-\u2127 \u2128-\u2129 \u212a-\u212e \u212f-\u213a \u213c-\u2140 \u2145-\u214a \u214e-\u214f \u2160-\u2189 \u2c00-\u2c2f \u2c30-\u2c5f \u2c60-\u2ce5 \u2ceb-\u2cef \u2d00-\u2d26 \u2d30-\u2d66 \u2d6f-\u2d70 \u2d80-\u2d97 \u2da0-\u2da7 \u2da8-\u2daf \u2db0-\u2db7 \u2db8-\u2dbf \u2dc0-\u2dc7 \u2dc8-\u2dcf \u2dd0-\u2dd7 \u2dd8-\u2ddf \u2e2f-\u2e30 \u3005-\u3008 \u3021-\u302a \u3031-\u3036 \u3038-\u303d \u3041-\u3097 \u309d-\u30a0 \u30a1-\u30fb \u30fc-\u3100 \u3105-\u312e \u3131-\u318f \u31a0-\u31bb \u31f0-\u3200 \u3400-\u4db6 \u4e00-\u9fcc \ua000-\ua48d \ua4d0-\ua4fe \ua500-\ua60d \ua610-\ua620 \ua62a-\ua62c \ua640-\ua66f \ua67f-\ua698 \ua6a0-\ua6f0 \ua717-\ua720 \ua722-\ua789 \ua78b-\ua78f \ua790-\ua792 \ua7a0-\ua7aa \ua7fa-\ua802 \ua803-\ua806 \ua807-\ua80b \ua80c-\ua823 \ua838-\ua839 \ua840-\ua874 \ua882-\ua8b4 \ua8f2-\ua8f8 \ua8fb-\ua8fc \ua90a-\ua926 \ua930-\ua947 \ua960-\ua97d \ua984-\ua9b3 \ua9cf-\ua9d0 \uaa00-\uaa29 \uaa40-\uaa43 \uaa44-\uaa4c \uaa60-\uaa77 \uaa7a-\uaa7b \uaa80-\uaab0 \uaab1-\uaab2 \uaab5-\uaab7 \uaab9-\uaabe \uaac0-\uaac1 \uaac2-\uaac3 \uaadb-\uaade \uab01-\uab07 \uab09-\uab0f \uab11-\uab17 \uab20-\uab27 \uab28-\uab2f \uabc0-\uabe3 \uac00-\ud7a4 \ud7b0-\ud7c7 \ud7cb-\ud7fc \uf900-\ufa2e \ufa30-\ufa6e \ufa70-\ufada \ufb00-\ufb07 \ufb13-\ufb18 \ufb1d-\ufb1e \ufb1f-\ufb29 \ufb2a-\ufb37 \ufb38-\ufb3d \ufb3e-\ufb3f \ufb40-\ufb42 \ufb43-\ufb45 \ufb46-\ufbb2 \ufbd3-\ufd3e \ufd50-\ufd90 \ufd92-\ufdc8 \ufdf0-\ufdfd \ufe33-\ufe35 \ufe4d-\ufe50 \ufe69-\ufe6a \ufe70-\ufe75 \ufe76-\ufefd \uff04-\uff05 \uff21-\uff3b \uff3f-\uff40 \uff41-\uff5b \uff66-\uffbf \uffc2-\uffc8 \uffca-\uffd0 \uffd2-\uffd8 \uffda-\uffdd \uffe0-\uffe2 \uffe5-\uffe7 \u10000-\u1000c \u1000d-\u10027 \u10028-\u1003b \u1003c-\u1003e \u1003f-\u1004e \u10050-\u1005e \u10080-\u100fb \u10140-\u10175 \u10280-\u1029d \u102a0-\u102d1 \u10300-\u1031f \u10330-\u1034b \u10380-\u1039e \u103a0-\u103c4 \u103c8-\u103d0 \u103d1-\u103d6 \u10400-\u1049e \u10800-\u10806 \u10808-\u10809 \u1080a-\u10836 \u10837-\u10839 \u1083c-\u1083d \u1083f-\u10856 \u10900-\u10916 \u10920-\u1093a \u10a00-\u10a01 \u10a10-\u10a14 \u10a15-\u10a18 \u10a19-\u10a34 \u10a60-\u10a7d \u10b00-\u10b36 \u10b40-\u10b56 \u10b60-\u10b73 \u10c00-\u10c49 \u11003-\u11038 \u11083-\u110b0 \u12000-\u1236f \u12400-\u12463 \u13000-\u1342f \u16800-\u16a39 \u1b000-\u1b002 \u1d400-\u1d455 \u1d456-\u1d49d \u1d49e-\u1d4a0 \u1d4a2-\u1d4a3 \u1d4a5-\u1d4a7 \u1d4a9-\u1d4ad \u1d4ae-\u1d4ba \u1d4bb-\u1d4bc \u1d4bd-\u1d4c4 \u1d4c5-\u1d506 \u1d507-\u1d50b \u1d50d-\u1d515 \u1d516-\u1d51d \u1d51e-\u1d53a \u1d53b-\u1d53f \u1d540-\u1d545 \u1d546-\u1d547 \u1d54a-\u1d551 \u1d552-\u1d6a6 \u1d6a8-\u1d6c1 \u1d6c2-\u1d6db \u1d6dc-\u1d6fb \u1d6fc-\u1d715 \u1d716-\u1d735 \u1d736-\u1d74f \u1d750-\u1d76f \u1d770-\u1d789 \u1d78a-\u1d7a9 \u1d7aa-\u1d7c3 \u1d7c4-\u1d7cc \U020000-\U02a6d7 \U02a700-\U02b735 \U02b740-\U02b81e \U02f800-\U02fa1e];
//lexical JavaLetterDigits = [ \a01-\a09 \a0e-\a1c \a24-\a25 \a30-\a3a \a41-\a5b \a5f-\a60 \a61-\a7b \u007f-\u00a0 \u00a2-\u00a6 \u00aa-\u00ab \u00ad-\u00ae \u00b5-\u00b6 \u00ba-\u00bb \u00c0-\u00d7 \u00d8-\u00f7 \u00f8-\u02c2 \u02c6-\u02d2 \u02e0-\u02e5 \u02ec-\u02ed \u02ee-\u02ef \u0300-\u0375 \u0376-\u0378 \u037a-\u037e \u0386-\u0387 \u0388-\u038b \u038c-\u038d \u038e-\u03a2 \u03a3-\u03f6 \u03f7-\u0482 \u0483-\u0488 \u048a-\u0528 \u0531-\u0557 \u0559-\u055a \u0561-\u0588 \u0591-\u05be \u05bf-\u05c0 \u05c1-\u05c3 \u05c4-\u05c6 \u05c7-\u05c8 \u05d0-\u05eb \u05f0-\u05f3 \u0600-\u0604 \u060b-\u060c \u0610-\u061b \u0620-\u066a \u066e-\u06d4 \u06d5-\u06de \u06df-\u06e9 \u06ea-\u06fd \u06ff-\u0700 \u070f-\u074b \u074d-\u07b2 \u07c0-\u07f6 \u07fa-\u07fb \u0800-\u082e \u0840-\u085c \u0900-\u0964 \u0966-\u0970 \u0971-\u0978 \u0979-\u0980 \u0981-\u0984 \u0985-\u098d \u098f-\u0991 \u0993-\u09a9 \u09aa-\u09b1 \u09b2-\u09b3 \u09b6-\u09ba \u09bc-\u09c5 \u09c7-\u09c9 \u09cb-\u09cf \u09d7-\u09d8 \u09dc-\u09de \u09df-\u09e4 \u09e6-\u09f4 \u09fb-\u09fc \u0a01-\u0a04 \u0a05-\u0a0b \u0a0f-\u0a11 \u0a13-\u0a29 \u0a2a-\u0a31 \u0a32-\u0a34 \u0a35-\u0a37 \u0a38-\u0a3a \u0a3c-\u0a3d \u0a3e-\u0a43 \u0a47-\u0a49 \u0a4b-\u0a4e \u0a51-\u0a52 \u0a59-\u0a5d \u0a5e-\u0a5f \u0a66-\u0a76 \u0a81-\u0a84 \u0a85-\u0a8e \u0a8f-\u0a92 \u0a93-\u0aa9 \u0aaa-\u0ab1 \u0ab2-\u0ab4 \u0ab5-\u0aba \u0abc-\u0ac6 \u0ac7-\u0aca \u0acb-\u0ace \u0ad0-\u0ad1 \u0ae0-\u0ae4 \u0ae6-\u0af0 \u0af1-\u0af2 \u0b01-\u0b04 \u0b05-\u0b0d \u0b0f-\u0b11 \u0b13-\u0b29 \u0b2a-\u0b31 \u0b32-\u0b34 \u0b35-\u0b3a \u0b3c-\u0b45 \u0b47-\u0b49 \u0b4b-\u0b4e \u0b56-\u0b58 \u0b5c-\u0b5e \u0b5f-\u0b64 \u0b66-\u0b70 \u0b71-\u0b72 \u0b82-\u0b84 \u0b85-\u0b8b \u0b8e-\u0b91 \u0b92-\u0b96 \u0b99-\u0b9b \u0b9c-\u0b9d \u0b9e-\u0ba0 \u0ba3-\u0ba5 \u0ba8-\u0bab \u0bae-\u0bba \u0bbe-\u0bc3 \u0bc6-\u0bc9 \u0bca-\u0bce \u0bd0-\u0bd1 \u0bd7-\u0bd8 \u0be6-\u0bf0 \u0bf9-\u0bfa \u0c01-\u0c04 \u0c05-\u0c0d \u0c0e-\u0c11 \u0c12-\u0c29 \u0c2a-\u0c34 \u0c35-\u0c3a \u0c3d-\u0c45 \u0c46-\u0c49 \u0c4a-\u0c4e \u0c55-\u0c57 \u0c58-\u0c5a \u0c60-\u0c64 \u0c66-\u0c70 \u0c82-\u0c84 \u0c85-\u0c8d \u0c8e-\u0c91 \u0c92-\u0ca9 \u0caa-\u0cb4 \u0cb5-\u0cba \u0cbc-\u0cc5 \u0cc6-\u0cc9 \u0cca-\u0cce \u0cd5-\u0cd7 \u0cde-\u0cdf \u0ce0-\u0ce4 \u0ce6-\u0cf0 \u0cf1-\u0cf3 \u0d02-\u0d04 \u0d05-\u0d0d \u0d0e-\u0d11 \u0d12-\u0d3b \u0d3d-\u0d45 \u0d46-\u0d49 \u0d4a-\u0d4f \u0d57-\u0d58 \u0d60-\u0d64 \u0d66-\u0d70 \u0d7a-\u0d80 \u0d82-\u0d84 \u0d85-\u0d97 \u0d9a-\u0db2 \u0db3-\u0dbc \u0dbd-\u0dbe \u0dc0-\u0dc7 \u0dca-\u0dcb \u0dcf-\u0dd5 \u0dd6-\u0dd7 \u0dd8-\u0de0 \u0df2-\u0df4 \u0e01-\u0e3b \u0e3f-\u0e4f \u0e50-\u0e5a \u0e81-\u0e83 \u0e84-\u0e85 \u0e87-\u0e89 \u0e8a-\u0e8b \u0e8d-\u0e8e \u0e94-\u0e98 \u0e99-\u0ea0 \u0ea1-\u0ea4 \u0ea5-\u0ea6 \u0ea7-\u0ea8 \u0eaa-\u0eac \u0ead-\u0eba \u0ebb-\u0ebe \u0ec0-\u0ec5 \u0ec6-\u0ec7 \u0ec8-\u0ece \u0ed0-\u0eda \u0edc-\u0ede \u0f00-\u0f01 \u0f18-\u0f1a \u0f20-\u0f2a \u0f35-\u0f36 \u0f37-\u0f38 \u0f39-\u0f3a \u0f3e-\u0f48 \u0f49-\u0f6d \u0f71-\u0f85 \u0f86-\u0f98 \u0f99-\u0fbd \u0fc6-\u0fc7 \u1000-\u104a \u1050-\u109e \u10a0-\u10c6 \u10d0-\u10fb \u10fc-\u10fd \u1100-\u1249 \u124a-\u124e \u1250-\u1257 \u1258-\u1259 \u125a-\u125e \u1260-\u1289 \u128a-\u128e \u1290-\u12b1 \u12b2-\u12b6 \u12b8-\u12bf \u12c0-\u12c1 \u12c2-\u12c6 \u12c8-\u12d7 \u12d8-\u1311 \u1312-\u1316 \u1318-\u135b \u135d-\u1360 \u1380-\u1390 \u13a0-\u13f5 \u1401-\u166d \u166f-\u1680 \u1681-\u169b \u16a0-\u16eb \u16ee-\u16f1 \u1700-\u170d \u170e-\u1715 \u1720-\u1735 \u1740-\u1754 \u1760-\u176d \u176e-\u1771 \u1772-\u1774 \u1780-\u17d4 \u17d7-\u17d8 \u17db-\u17de \u17e0-\u17ea \u180b-\u180e \u1810-\u181a \u1820-\u1878 \u1880-\u18ab \u18b0-\u18f6 \u1900-\u191d \u1920-\u192c \u1930-\u193c \u1946-\u196e \u1970-\u1975 \u1980-\u19ac \u19b0-\u19ca \u19d0-\u19da \u1a00-\u1a1c \u1a20-\u1a5f \u1a60-\u1a7d \u1a7f-\u1a8a \u1a90-\u1a9a \u1aa7-\u1aa8 \u1b00-\u1b4c \u1b50-\u1b5a \u1b6b-\u1b74 \u1b80-\u1bab \u1bae-\u1bba \u1bc0-\u1bf4 \u1c00-\u1c38 \u1c40-\u1c4a \u1c4d-\u1c7e \u1cd0-\u1cd3 \u1cd4-\u1cf3 \u1d00-\u1de7 \u1dfc-\u1f16 \u1f18-\u1f1e \u1f20-\u1f46 \u1f48-\u1f4e \u1f50-\u1f58 \u1f59-\u1f5a \u1f5b-\u1f5c \u1f5d-\u1f5e \u1f5f-\u1f7e \u1f80-\u1fb5 \u1fb6-\u1fbd \u1fbe-\u1fbf \u1fc2-\u1fc5 \u1fc6-\u1fcd \u1fd0-\u1fd4 \u1fd6-\u1fdc \u1fe0-\u1fed \u1ff2-\u1ff5 \u1ff6-\u1ffd \u200b-\u2010 \u202a-\u202f \u203f-\u2041 \u2054-\u2055 \u2060-\u2065 \u206a-\u2070 \u2071-\u2072 \u207f-\u2080 \u2090-\u209d \u20a0-\u20ba \u20d0-\u20dd \u20e1-\u20e2 \u20e5-\u20f1 \u2102-\u2103 \u2107-\u2108 \u210a-\u2114 \u2115-\u2116 \u2119-\u211e \u2124-\u2125 \u2126-\u2127 \u2128-\u2129 \u212a-\u212e \u212f-\u213a \u213c-\u2140 \u2145-\u214a \u214e-\u214f \u2160-\u2189 \u2c00-\u2c2f \u2c30-\u2c5f \u2c60-\u2ce5 \u2ceb-\u2cf2 \u2d00-\u2d26 \u2d30-\u2d66 \u2d6f-\u2d70 \u2d7f-\u2d97 \u2da0-\u2da7 \u2da8-\u2daf \u2db0-\u2db7 \u2db8-\u2dbf \u2dc0-\u2dc7 \u2dc8-\u2dcf \u2dd0-\u2dd7 \u2dd8-\u2ddf \u2de0-\u2e00 \u2e2f-\u2e30 \u3005-\u3008 \u3021-\u3030 \u3031-\u3036 \u3038-\u303d \u3041-\u3097 \u3099-\u309b \u309d-\u30a0 \u30a1-\u30fb \u30fc-\u3100 \u3105-\u312e \u3131-\u318f \u31a0-\u31bb \u31f0-\u3200 \u3400-\u4db6 \u4e00-\u9fcc \ua000-\ua48d \ua4d0-\ua4fe \ua500-\ua60d \ua610-\ua62c \ua640-\ua670 \ua67c-\ua67e \ua67f-\ua698 \ua6a0-\ua6f2 \ua717-\ua720 \ua722-\ua789 \ua78b-\ua78f \ua790-\ua792 \ua7a0-\ua7aa \ua7fa-\ua828 \ua838-\ua839 \ua840-\ua874 \ua880-\ua8c5 \ua8d0-\ua8da \ua8e0-\ua8f8 \ua8fb-\ua8fc \ua900-\ua92e \ua930-\ua954 \ua960-\ua97d \ua980-\ua9c1 \ua9cf-\ua9da \uaa00-\uaa37 \uaa40-\uaa4e \uaa50-\uaa5a \uaa60-\uaa77 \uaa7a-\uaa7c \uaa80-\uaac3 \uaadb-\uaade \uab01-\uab07 \uab09-\uab0f \uab11-\uab17 \uab20-\uab27 \uab28-\uab2f \uabc0-\uabeb \uabec-\uabee \uabf0-\uabfa \uac00-\ud7a4 \ud7b0-\ud7c7 \ud7cb-\ud7fc \uf900-\ufa2e \ufa30-\ufa6e \ufa70-\ufada \ufb00-\ufb07 \ufb13-\ufb18 \ufb1d-\ufb29 \ufb2a-\ufb37 \ufb38-\ufb3d \ufb3e-\ufb3f \ufb40-\ufb42 \ufb43-\ufb45 \ufb46-\ufbb2 \ufbd3-\ufd3e \ufd50-\ufd90 \ufd92-\ufdc8 \ufdf0-\ufdfd \ufe00-\ufe10 \ufe20-\ufe27 \ufe33-\ufe35 \ufe4d-\ufe50 \ufe69-\ufe6a \ufe70-\ufe75 \ufe76-\ufefd \ufeff-\uff00 \uff04-\uff05 \uff10-\uff1a \uff21-\uff3b \uff3f-\uff40 \uff41-\uff5b \uff66-\uffbf \uffc2-\uffc8 \uffca-\uffd0 \uffd2-\uffd8 \uffda-\uffdd \uffe0-\uffe2 \uffe5-\uffe7 \ufff9-\ufffc \u10000-\u1000c \u1000d-\u10027 \u10028-\u1003b \u1003c-\u1003e \u1003f-\u1004e \u10050-\u1005e \u10080-\u100fb \u10140-\u10175 \u101fd-\u101fe \u10280-\u1029d \u102a0-\u102d1 \u10300-\u1031f \u10330-\u1034b \u10380-\u1039e \u103a0-\u103c4 \u103c8-\u103d0 \u103d1-\u103d6 \u10400-\u1049e \u104a0-\u104aa \u10800-\u10806 \u10808-\u10809 \u1080a-\u10836 \u10837-\u10839 \u1083c-\u1083d \u1083f-\u10856 \u10900-\u10916 \u10920-\u1093a \u10a00-\u10a04 \u10a05-\u10a07 \u10a0c-\u10a14 \u10a15-\u10a18 \u10a19-\u10a34 \u10a38-\u10a3b \u10a3f-\u10a40 \u10a60-\u10a7d \u10b00-\u10b36 \u10b40-\u10b56 \u10b60-\u10b73 \u10c00-\u10c49 \u11000-\u11047 \u11066-\u11070 \u11080-\u110bb \u110bd-\u110be \u12000-\u1236f \u12400-\u12463 \u13000-\u1342f \u16800-\u16a39 \u1b000-\u1b002 \u1d165-\u1d16a \u1d16d-\u1d183 \u1d185-\u1d18c \u1d1aa-\u1d1ae \u1d242-\u1d245 \u1d400-\u1d455 \u1d456-\u1d49d \u1d49e-\u1d4a0 \u1d4a2-\u1d4a3 \u1d4a5-\u1d4a7 \u1d4a9-\u1d4ad \u1d4ae-\u1d4ba \u1d4bb-\u1d4bc \u1d4bd-\u1d4c4 \u1d4c5-\u1d506 \u1d507-\u1d50b \u1d50d-\u1d515 \u1d516-\u1d51d \u1d51e-\u1d53a \u1d53b-\u1d53f \u1d540-\u1d545 \u1d546-\u1d547 \u1d54a-\u1d551 \u1d552-\u1d6a6 \u1d6a8-\u1d6c1 \u1d6c2-\u1d6db \u1d6dc-\u1d6fb \u1d6fc-\u1d715 \u1d716-\u1d735 \u1d736-\u1d74f \u1d750-\u1d76f \u1d770-\u1d789 \u1d78a-\u1d7a9 \u1d7aa-\u1d7c3 \u1d7c4-\u1d7cc \u1d7ce-\u1d800 \U020000-\U02a6d7 \U02a700-\U02b735 \U02b740-\U02b81e \U02f800-\U02fa1e \U0e0001-\U0e0002 \U0e0020-\U0e0080 \U0e0100-\U0e01f0];


lexical ID =
	// Yes, this would be more correct, but REALLY slow at the moment
	//JavaLetter JavaLetterDigits* 
	//
	// therefore we go the ascii route:
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

syntax FloatLiteral =
   float: HexaFloatLiteral !>> [D F d f] 
  |  float: DeciFloatLiteral !>> [D F d f] 
  ;

lexical DeciFloatLiteral 
    = [0-9] !<< [0-9]+ DeciFloatExponentPart [D F d f]?
    | [0-9] !<< [0-9]+ [D F d f]
    | [0-9] !<< [0-9]+ "." [0-9]* !>> [0-9] DeciFloatExponentPart? [D F d f]?
    | [0-9] !<< "." [0-9]+ !>> [0-9] DeciFloatExponentPart? [D F d f]?
  ;
  
lexical DeciLiteral =
  DeciNumeral !>> [. 0-9 D F d f] [L l]? 
  ;

lexical DeciNumeral 
  = [1-9] [0-9]* 
  | "0" 
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
  FieldAccess  
  |  newInstance: "new"  TypeArgs? ClassOrInterfaceType "(" {Expr ","}* ")" ClassBody? 
  |  invoke: MethodSpec "(" {Expr ","}* ")" 
  | bracket "(" Expr ")" 
  |  lit: Literal 
  |  qThis: TypeName "." "this"  
  | ArrayCreationExpr 
  |  this: "this"  
  | ArrayAccess  
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
  LEX_StringLiteral 
  ;

syntax AbstractMethodDec =
   abstractMethodDec: (Anno | AbstractMethodMod)* TypeParams? ResultType Id "(" {FormalParam ","}* ")" Throws? ";" 
  |  deprAbstractMethodDec: (Anno | AbstractMethodMod)* TypeParams? ResultType Id "(" {FormalParam ","}* ")" Dim+ Throws? ";" 
  ; 

syntax AbstractMethodMod =
  "abstract" 
  | "public" 
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
   defaultVal: "default"  ElemVal  
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
  | bracket "(" LHS ")"
  | ArrayAccess  
  | FieldAccess  
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
   methodName: MethodName 
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
  LEX_CharLiteral 
  ;

lexical EndOfFile =
  
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

bool expectedAmb({(Expr)`(<RefType t>) <Expr e>`, appl(_,[(Expr)`(<ExprName n>)`,*_])}) = true; // (A) + 1
bool expectedAmb({appl(_,[*_,(Expr)`(<RefType t>) <Expr e>`]), appl(_,[appl(_,[*_,(Expr)`(<ExprName n>)`]),*_])}) = true; // 1 + (A) + 1
default bool expectedAmb(set[Tree] t) = false;
          
