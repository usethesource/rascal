@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Rodrigo Bonifacio - rbonifacio@unb.br - CIC/UnB}
@doc{
This Java grammar is based on the following references:

* https://docs.oracle.com/javase/specs/jls/se8/html/jls-19.html
* https://github.com/antlr/grammars-v4/blob/master/java8/Java8.g4
* Rascal Java15 grammar

#### Benefits

* the grammar is well-annotated with the source information

#### Pitfalls

* the grammar contains too many non-terminals for the expression sub-language (priorities and associativities
are still encoded with non-terminals)
* same for the Statements; too many non-terminals for handy use with concrete-syntax patterns
* the grammar is not well tested
} 

module lang::java::\syntax::Java18

start syntax CompilationUnit = PackageDeclaration? Imports TypeDeclaration*;

syntax Literal = IntegerLiteral
  			   | FloatingPointLiteral
  			   | BooleanLiteral
  			   | CharacterLiteral
  			   | StringLiteral
  			   | NullLiteral
  			   ;
  		
syntax Imports = ImportDeclaration*;
  		
/*
 * Productions from §4 (Types, Values, and Variables)
 */
 
 syntax Type = PrimitiveType
             | ReferenceType
             ;
             
 syntax PrimitiveType = Annotation* NumericType
                      | Annotation* "boolean" 
                      ;
                      
 syntax NumericType = IntegralType
                    | FloatingPointType
                    ;
                    
syntax IntegralType = "byte" 
                    | "short" 
                    | "int" 
                    | "long" 
                    | "char"
                    ;
                    
syntax FloatingPointType = "float" | "double" ;

syntax ReferenceType = ClassOrInterfaceType 
                     | arrayType: ArrayType
                     ;
                     
syntax ClassOrInterfaceType = ClassType 
                     ;
                                          
syntax ClassType = Annotation* Identifier TypeArguments? 
                 | ClassOrInterfaceType "." Annotation* Identifier TypeArguments?
                 ;                                          
syntax InterfaceType = ClassType;

syntax TypeVariable = Annotation* Identifier;

syntax ArrayType = PrimitiveType Dims 
                 | ClassOrInterfaceType Dims 
                 ;
                 
syntax Dims = Annotation* "[" "]" (Annotation* "[" "]")*; 

syntax TypeParameter = typeParameter: TypeParameterModifier* Identifier TypeBound? ;

syntax TypeParameterModifier = Annotation; 

syntax TypeBound = "extends" { ClassOrInterfaceType "&" }+
                 ;
                 
syntax AdditionalBound = "&" InterfaceType ;


syntax TypeArguments = "\<"  {TypeArgument ","}* "\>" ;

syntax TypeArgument = ReferenceType 
                    | Wildcard
                    ;
                    
syntax Wildcard = Annotation* "?" WildcardBounds? ;

syntax WildcardBounds = "extends" ReferenceType
	                  |	"super" ReferenceType
	                  ;                                    

/*
 * Productions from §6 (Names)
 */
 
syntax TypeName = Identifier
                | PackageOrTypeName "." Identifier
                ;
                
syntax PackageOrTypeName = Identifier 
                         | PackageOrTypeName "." Identifier
                         ;
syntax ExpressionName = Identifier 
                      | AmbiguousName "." Identifier                        
                      ;
                     
syntax MethodName = Identifier;

syntax PackageName = Identifier 
                   | PackageName "." Identifier
                   ;
                   
syntax AmbiguousName = Identifier 
                     | AmbiguousName "." Identifier
                     ;
                     
                     
/*
 * Productions from §7 (Packages)
 */                    
 
syntax PackageDeclaration = PackageModifier* "package" {Identifier "."}+ ";" ;

syntax PackageModifier = Annotation ;

syntax ImportDeclaration = importDeclaration: SingleTypeImportDeclaration       // import Class; 
                         | TypeImportOnDemandDeclaration     // import br.unb.rascal.*;
                         | SingleStaticImportDeclaration     // import static br.unb.rascal.Foo.m;
                         | StaticImportOnDemandDeclaration   // import static br.unb.rascal.Foo.*;
                         ;
 
syntax SingleTypeImportDeclaration = "import" TypeName ";"+ ;

syntax TypeImportOnDemandDeclaration = "import" PackageOrTypeName "." "*" ";"+ ;

syntax SingleStaticImportDeclaration = "import" "static" TypeName "." Identifier ";"+;

syntax StaticImportOnDemandDeclaration = "import" "static" TypeName "." "*" ";"+ ;                         


syntax TypeDeclaration = ClassDeclaration ";"*
                       | InterfaceDeclaration ";"* 
                       ;

syntax ClassDeclaration = NormalClassDeclaration
                        | EnumDeclaration 
                        ;
                        
syntax NormalClassDeclaration = normalClassDeclaration: ClassModifier* "class" Identifier TypeParameters? Superclass? Superinterfaces? ClassBody ;

syntax ClassModifier = Annotation 
                     | "public" 
                     | "protected" 
                     | "private" 
                     | "abstract" 
                     | "static" 
                     | "final" 
                     | "strictfp"
                     ;

syntax TypeParameters = typeParameters : "\<" {TypeParameter ","}+ "\>" ; 

syntax Superclass = "extends" ClassType ;

syntax Superinterfaces = "implements" {InterfaceType ","}+ ;

syntax ClassBody = classBody : "{" ClassBodyDeclaration* decls "}" ";"? ;

syntax ClassBodyDeclaration = ClassMemberDeclaration 
                            | InstanceInitializer 
                            | StaticInitializer 
                            | ConstructorDeclaration 
                            ;
                            
syntax ClassMemberDeclaration = FieldDeclaration 
                              | MethodDeclaration 
                              | ClassDeclaration 
                              | InterfaceDeclaration 
                              //| ";"
                              ;
                              
syntax FieldDeclaration = fieldDeclaration: FieldModifier* UnannType VariableDeclaratorList ";"+ ;

syntax FieldModifier = Annotation 
                     | "public" 
                     | "protected" 
                     | "private" 
                     | "static" 
                     | "final" 
                     | "transient" 
                     | "volatile"
                     ;

syntax VariableDeclaratorList = variableDeclaratorList: {VariableDeclarator ","}+ ; 

syntax VariableDeclarator = variableDeclarator: VariableDeclaratorId ("=" VariableInitializer)? ;

syntax VariableDeclaratorId = Identifier Dims? ;

syntax VariableInitializer = Expression 
                           | ArrayInitializer
                           ;                                                                               

syntax UnannType = UnannPrimitiveType 
                 | UnannReferenceType
                 ;
                 
syntax UnannPrimitiveType = NumericType 
                          | "boolean" 
                          ;

syntax UnannReferenceType = UnannClassOrInterfaceType 
                          | UnannArrayType
                          ;
                          
syntax UnannClassOrInterfaceType = UnannClassType 
                                 ; 
                          
syntax UnannClassType = Identifier TypeArguments? 
                      | UnannClassOrInterfaceType "." Annotation* Identifier TypeArguments?;
               
syntax UnannInterfaceType = UnannClassType ; 

syntax UnannTypeVariable = Identifier ; 

syntax UnannArrayType = UnannPrimitiveType Dims 
               | UnannClassOrInterfaceType Dims 
               ;

syntax MethodDeclaration = methodDeclaration: MethodModifier* MethodHeader MethodBody ;

syntax MethodModifier = Annotation 
                      | "public" 
                      | "protected" 
                      | "private"
                      | "abstract" 
                      | "static" 
                      | "final" 
                      | "synchronized" 
                      | "native" 
                      | "strictfp"
                      ;

syntax MethodHeader = methodHeader: Result MethodDeclarator Throws?
                    |  TypeParameters Annotation* Result MethodDeclarator Throws?
                    ;
                    
syntax Result = UnannType 
              | "void" 
              ;


syntax MethodDeclarator = Identifier "(" ")" Dims?
                        | Identifier "(" LastFormalParameter ")" Dims?
                        | Identifier "(" {FormalParameter ","}+ ("," LastFormalParameter)?")" Dims?
                        ; 
            
syntax FormalParameterList = FormalParameters ;
                            
syntax FormalParameters = formalParameter : FormalParameter ("," FormalParameters)?
                        | lastFormalParameter: LastFormalParameter
                        ;                                   

syntax FormalParameter = VariableModifier* mds UnannType atype VariableDeclaratorId vdid;

                        
syntax LastFormalParameter = VariableModifier* UnannType Annotation* "..." VariableDeclaratorId 
                           ;

syntax ReceiverParameter = Annotation* UnannType (Identifier ".")? "this" ;

syntax VariableModifier = Annotation 
                        | "final" 
                        ;
              


syntax Throws = "throws" { ExceptionType "," }+;  

syntax ExceptionType = ClassType 
                     ; 


syntax MethodBody = Block ";"*
                  | ";"
                  ;
                   
syntax InstanceInitializer = Block ;

syntax StaticInitializer = "static" Block ";"* ;

syntax ConstructorDeclaration = ConstructorModifier* ConstructorDeclarator Throws? ConstructorBody ;

syntax ConstructorModifier = Annotation 
                           | "public" 
                           | "protected" 
                           | "private" 
                           | "strictfp"
                           ;
                           
syntax ConstructorDeclarator = TypeParameters? SimpleTypeName "(" FormalParameterList? ")" ;

syntax SimpleTypeName = Identifier ;

syntax ConstructorBody = "{" ExplicitConstructorInvocation? BlockStatements? "}" ";"* ;

syntax ExplicitConstructorInvocation = TypeArguments? "this" "(" ArgumentList? ")" ";"  
                                     | TypeArguments? "super" "(" ArgumentList? ")" ";" 
                                     | ExpressionName "." TypeArguments "super" "(" ArgumentList? ")" ";" 
                                     | Primary "." TypeArguments? "super" "(" ArgumentList? ")" ";"
                                     ;

syntax EnumDeclaration = enumDeclaration : ClassModifier* "enum" Identifier Superinterfaces? EnumBody ;

syntax EnumBody = "{" EnumConstantList? ","? EnumBodyDeclarations? "}" ";"?;

syntax EnumConstantList = { EnumConstant "," }+ ;

syntax EnumConstant = EnumConstantModifier* Identifier ("(" ArgumentList? ")")? ClassBody ? ;

syntax EnumConstantModifier = Annotation ; 

syntax EnumBodyDeclarations =  ";" ClassBodyDeclaration* ;

syntax InterfaceDeclaration = NormalInterfaceDeclaration 
                            | AnnotationTypeDeclaration
                            ;
                            
syntax NormalInterfaceDeclaration = normalInterfaceDeclaration : InterfaceModifier* "interface" Identifier TypeParameters? ExtendsInterfaces? InterfaceBody ;

syntax InterfaceModifier = Annotation 
                         | "public" 
                         | "protected" 
                         | "private" 
                         | "abstract" 
                         | "static" 
                         | "strictfp"
                         ;
                         
syntax ExtendsInterfaces = "extends" {InterfaceType ","}+ ; 

syntax InterfaceBody = "{" InterfaceMemberDeclaration* "}" ";"* ;

syntax InterfaceMemberDeclaration = ConstantDeclaration 
                                  | InterfaceMethodDeclaration 
                                  | ClassDeclaration 
                                  | InterfaceDeclaration 
                                  ;

syntax ConstantDeclaration = ConstantModifier* UnannType VariableDeclaratorList ";" ;

syntax ConstantModifier = Annotation 
                        | "public" 
                        | "static" 
                        | "final"
                        ;
                        
syntax InterfaceMethodDeclaration = InterfaceMethodModifier* MethodHeader MethodBody ";"?;

syntax InterfaceMethodModifier = Annotation 
                               | "public" 
                               | "abstract" 
                               | "default" 
                               | "static" 
                               | "strictfp"
                               ;
                               
syntax AnnotationTypeDeclaration = InterfaceModifier* "@" "interface" Identifier AnnotationTypeBody ;

syntax AnnotationTypeBody = "{" AnnotationTypeMemberDeclaration* "}" ;

syntax AnnotationTypeMemberDeclaration = AnnotationTypeElementDeclaration 
                                       | ConstantDeclaration 
                                       | ClassDeclaration 
                                       | InterfaceDeclaration 
                                       | InterfaceMethodDeclaration
                                    //   | ";"
                                       ;

syntax AnnotationTypeElementDeclaration = AnnotationTypeElementModifier* UnannType Identifier "(" ")" Dims? DefaultValue? ;

syntax AnnotationTypeElementModifier = Annotation 
                                     | "public" 
                                     | "abstract"
                                     ;
                                     
syntax DefaultValue = "default" ElementValue ";"*;

syntax Annotation = NormalAnnotation 
                  | MarkerAnnotation 
                  | SingleElementAnnotation
                  ;

syntax NormalAnnotation = "@" TypeName "(" ElementValuePairList? ")" ;

syntax ElementValuePairList = {ElementValuePair ","}+ ;

syntax ElementValuePair = Identifier "=" ElementValue ;

syntax ElementValue = ConditionalExpression 
                    | ElementValueArrayInitializer 
                    | Annotation
                    ;
                    
syntax ElementValueArrayInitializer = "{" ElementValueList? ","? "}" ;

syntax ElementValueList = { ElementValue "," }*;

syntax MarkerAnnotation = "@" TypeName ;

syntax SingleElementAnnotation = "@" TypeName "(" ElementValue ")" ;

/*
 * Productions from §10 (Arrays)
 */
 
syntax ArrayInitializer = "{" VariableInitializerList? ","? "}" ; 

syntax VariableInitializerList = { VariableInitializer "," }+ ;

/*
 * Productions from §14 (Blocks and Statements)
 */
 
syntax Block = "{" BlockStatements? "}" ;
             

syntax BlockStatements = BlockStatement BlockStatement* ;

syntax BlockStatement = LocalVariableDeclarationStatement 
                      | ClassDeclaration 
                      | Statement
                      ;
                      
syntax LocalVariableDeclarationStatement = LocalVariableDeclaration ";"+ ;

syntax LocalVariableDeclaration = VariableModifier* UnannType VariableDeclaratorList ;

syntax Statement = StatementWithoutTrailingSubstatement 
                 | LabeledStatement 
                 | IfThenStatement 
                 | IfThenElseStatement 
                 | WhileStatement 
                 | ForStatement
                 ; 
                 
syntax StatementNoShortIf = StatementWithoutTrailingSubstatement 
                          | LabeledStatementNoShortIf 
                          | IfThenElseStatementNoShortIf 
                          | WhileStatementNoShortIf 
                          | ForStatementNoShortIf
                          ; 
                          
syntax StatementWithoutTrailingSubstatement = Block 
                                            | EmptyStatement 
                                            | ExpressionStatement 
                                            | AssertStatement 
                                            | SwitchStatement 
                                            | DoStatement 
                                            | BreakStatement 
                                            | ContinueStatement 
                                            | ReturnStatement 
                                            | SynchronizedStatement 
                                            | ThrowStatement 
                                            | TryStatement
                                            ;
syntax EmptyStatement = ";" ; 

syntax LabeledStatement = Identifier ":" Statement ;

syntax LabeledStatementNoShortIf = Identifier ":"  StatementNoShortIf ; 

syntax ExpressionStatement = StatementExpression ";" ;

syntax StatementExpression = Assignment 
                           | PreIncrementExpression 
                           | PreDecrementExpression 
                           | PostIncrementExpression 
                           | PostDecrementExpression 
                           | MethodInvocation 
                           | ClassInstanceCreationExpression
                           ;
                           
                           
syntax IfThenStatement = "if" "(" Expression ")" Statement ;

syntax IfThenElseStatement = "if" "(" Expression ")" StatementNoShortIf "else" Statement ;

syntax IfThenElseStatementNoShortIf = "if" "(" Expression ")" StatementNoShortIf "else" StatementNoShortIf ;

syntax AssertStatement = "assert" Expression ";"   
                       | "assert" Expression ":" Expression ";" 
                       ; 
                      
syntax SwitchStatement = "switch" "(" Expression ")" SwitchBlock ; 

syntax SwitchBlock = "{" SwitchBlockStatementGroups SwitchLabel* "}" ;

syntax SwitchBlockStatementGroups = SwitchBlockStatementGroup* ;

syntax SwitchBlockStatementGroup = SwitchLabels BlockStatements ;

syntax SwitchLabels = SwitchLabel+ ; 

syntax SwitchLabel = "case" ConstantExpression ":" 
                   | "default" ":" 
                   ;
                   
syntax EnumConstantName = Identifier ;  

syntax WhileStatement = whileStatement: "while" "(" Expression ")" Statement ; 

syntax WhileStatementNoShortIf = "while" "(" Expression ")" StatementNoShortIf ;

syntax DoStatement = "do" Statement "while" "(" Expression ")" ;

syntax ForStatement = BasicForStatement  
                    | EnhancedForStatement
                    ;
                    
syntax ForStatementNoShortIf = BasicForStatementNoShortIf 
                             | EnhancedForStatementNoShortIf
                             ;
                             
syntax BasicForStatement = "for" "(" ForInit? ";" Expression? ";" ForUpdate? ")" Statement ;

syntax BasicForStatementNoShortIf = "for" "(" ForInit? ";" Expression? ";" ForUpdate? ")" StatementNoShortIf ;

syntax ForInit = StatementExpressionList 
               | LocalVariableDeclaration
               ;
               
syntax  ForUpdate = StatementExpressionList ;
               
syntax StatementExpressionList = {StatementExpression ","} + ;

syntax EnhancedForStatement = enhancedForStatement: "for" "(" VariableModifier* UnannType VariableDeclaratorId ":" Expression ")" Statement ;

syntax EnhancedForStatementNoShortIf = "for" "(" VariableModifier* UnannType VariableDeclaratorId ":" Expression ")" StatementNoShortIf ;

syntax BreakStatement = "break" Identifier? ";" ;

syntax ContinueStatement = "continue" Identifier? ";" ;

syntax ReturnStatement = "return" Expression? ";" ;

syntax ThrowStatement = "throw" Expression ";" ;

syntax SynchronizedStatement = "synchronized" "(" Expression ")" Block ;

syntax TryStatement = "try" Block Catches 
                    | "try" Block Catches? Finally 
                    | TryWithResourcesStatement
                    ;
                    
syntax Catches = CatchClause+ ;

syntax CatchClause = "catch" "(" CatchFormalParameter ")" Block ;

syntax CatchFormalParameter = VariableModifier* CatchType VariableDeclaratorId ; 

syntax CatchType = UnannClassType ("|" ClassType)* ;

syntax Finally = "finally" Block ; 

syntax TryWithResourcesStatement = "try" ResourceSpecification Block Catches? Finally? ;

syntax ResourceSpecification = "(" ResourceList ";"? ")" ; 

syntax ResourceList = { Resource ";" }*;

syntax Resource = VariableModifier* UnannType VariableDeclaratorId "=" Expression ;

/*
 * Productions from §15 (Expressions)
 */

syntax Primary = PrimaryNoNewArray 
                | ArrayCreationExpression
                ; 
                
syntax PrimaryNoNewArray = Literal 
                          | ClassLiteral 
                          | "this" 
                          | TypeName "." "this" 
                          | "(" Expression ")" 
                          | ClassInstanceCreationExpression 
                          | FieldAccess 
                          | ArrayAccess 
                          | MethodInvocation 
                          | MethodReference               
                          ;
                          
syntax ClassLiteral = TypeName ("[" "]")* "." "class" 
                    | NumericType ("[" "]")* "." "class" 
                    | "boolean" ("[" "]")* "." "class" 
                    | "void" "." "class"              
                    ;
                    
syntax ClassInstanceCreationExpression = UnqualifiedClassInstanceCreationExpression 
                                       | ExpressionName "." UnqualifiedClassInstanceCreationExpression 
                                       | Primary "." UnqualifiedClassInstanceCreationExpression
                                       ; 

syntax UnqualifiedClassInstanceCreationExpression = "new" TypeArguments? ClassOrInterfaceTypeToInstantiate "(" ArgumentList? ")" 
                                                  | AIC ;
                                                  
syntax AIC = "new" TypeArguments? ClassOrInterfaceTypeToInstantiate "(" ArgumentList? ")" ClassBody ; 

syntax ClassOrInterfaceTypeToInstantiate = {AnnotatedType "."}* TypeArgumentsOrDiamond? ;

syntax AnnotatedType = Annotation* Identifier ;

syntax TypeArgumentsOrDiamond = TypeArguments  
                              ;
                                       
syntax FieldAccess = Primary "." Identifier 
                   | "super" "." Identifier 
                   | TypeName "." "super" "." Identifier
                   ;
                  
syntax ArrayAccess = ExpressionName "[" Expression "]" 
                   | PrimaryNoNewArray "[" Expression "]" 
                   ;
                  
syntax MethodInvocation = MethodName "(" ArgumentList? ")"  
                        | ExpressionName "." TypeArguments? Identifier "(" ArgumentList? ")" 
                        | Primary "." TypeArguments? Identifier "(" ArgumentList? ")"  
                        | "super" "." TypeArguments? Identifier "(" ArgumentList? ")"  
                        | TypeName "." "super" "." TypeArguments? Identifier "(" ArgumentList? ")" 
                        ;
                        
syntax ArgumentList = { Expression "," }+ ; 

syntax MethodReference = ExpressionName "::" TypeArguments? Identifier 
                       | Primary "::" TypeArguments? Identifier 
                       | "super" "::" TypeArguments? Identifier 
                       | TypeName "." "super" "::" TypeArguments? Identifier 
                       | ClassType "::" TypeArguments? "new" 
                       | ArrayType "::" "new"
                       ;                         
                  
syntax ArrayCreationExpression = "new" PrimitiveType DimExprs Dims? 
                               | "new" ClassOrInterfaceType DimExprs Dims? 
                               | "new" PrimitiveType Dims ArrayInitializer 
                               | "new" ClassOrInterfaceType Dims ArrayInitializer
                               ;
                               
syntax DimExprs = DimExpr+ ;

syntax DimExpr = Annotation* "[" Expression "]" ;

                  
syntax Expression = LambdaExpression 
                  | AssignmentExpression
                  ;
                  
syntax LambdaExpression = LambdaParameters "-\>" LambdaBody ;

syntax LambdaParameters = Identifier 
                        | "(" FormalParameterList? ")"
                        | "(" InferredFormalParameterList ")" 
                        ;
                        
syntax InferredFormalParameterList = { Identifier "," }+; 

syntax LambdaBody = Expression 
                  | Block                               
                  ;
                  
syntax AssignmentExpression = ConditionalExpression 
                            | Assignment
                            ;
                            
syntax Assignment = LeftHandSide AssignmentOperator Expression ;

syntax LeftHandSide = ExpressionName 
                    | FieldAccess 
                    | ArrayAccess
                    ; 
                    
syntax AssignmentOperator = "=" 
                          | "*="  
                          | "/="  
                          | "%="  
                          | "+="  
                          | "-="  
                          | "\<\<="  
                          | "\>\>="  
                          | "\>\>\>="  
                          | "&="
                          | "^="  
                          | "|=" 
                          ; 
                          
syntax ConditionalExpression = ConditionalOrExpression 
                             | ConditionalOrExpression "?" Expression ":" ConditionalExpression 
                             | ConditionalOrExpression "?" Expression ":" LambdaExpression
                             ;
                             
syntax ConditionalOrExpression = ConditionalAndExpression 
                               | ConditionalOrExpression "||" ConditionalAndExpression                              
                               ;
                               
syntax ConditionalAndExpression = InclusiveOrExpression 
                                | ConditionalAndExpression "&&" InclusiveOrExpression
                                ;
                                
syntax InclusiveOrExpression = ExclusiveOrExpression 
                             | InclusiveOrExpression "|" ExclusiveOrExpression
                             ;

syntax ExclusiveOrExpression = AndExpression 
                      | ExclusiveOrExpression "^" AndExpression
                      ;
                      
syntax AndExpression = EqualityExpression 
                     | AndExpression "&" EqualityExpression
                     ;                                
                     
syntax EqualityExpression = RelationalExpression 
                          | EqualityExpression "==" RelationalExpression 
                          | EqualityExpression "!=" RelationalExpression
                          ; 
                          
syntax RelationalExpression = ShiftExpression 
                            | RelationalExpression "\<" ShiftExpression 
                            | RelationalExpression "\>" ShiftExpression 
                            | RelationalExpression "\<=" ShiftExpression 
                            | RelationalExpression "\>=" ShiftExpression 
                            | RelationalExpression "instanceof" ReferenceType
                            ;

syntax ShiftExpression = AdditiveExpression 
                       | ShiftExpression "\<\<" AdditiveExpression 
                       | ShiftExpression "\>\>" AdditiveExpression 
                       | ShiftExpression "\>\>\>" AdditiveExpression
                       ;
                       
syntax AdditiveExpression = MultiplicativeExpression 
                          | AdditiveExpression "+" !>> "+" MultiplicativeExpression 
                          | AdditiveExpression "-" !>> "-" MultiplicativeExpression
                          ;
                          
syntax MultiplicativeExpression = UnaryExpression 
                                | MultiplicativeExpression "*" UnaryExpression 
                                | MultiplicativeExpression "/" UnaryExpression 
                                | MultiplicativeExpression "%" UnaryExpression
                                ;
                                
syntax UnaryExpression = PreIncrementExpression 
                       | PreDecrementExpression 
                       > "+" !>> "+" UnaryExpression 
                       | "-" !>> "-" UnaryExpression 
                       | UnaryExpressionNotPlusMinus
                       ;
                       
syntax PreIncrementExpression = "++" UnaryExpression ; 

syntax PreDecrementExpression = "--" UnaryExpression ;

syntax UnaryExpressionNotPlusMinus = PostfixExpression 
                                   | "~" UnaryExpression 
                                   | "!" UnaryExpression 
                                   | CastExpression                                
                                   ;
                                   
syntax PostfixExpression = Primary 
                         | ExpressionName 
                         | PostIncrementExpression 
                         | PostDecrementExpression                                   
                         ;
                         
syntax PostIncrementExpression = PostfixExpression "++" ;

syntax PostDecrementExpression = PostfixExpression "--" ;

syntax CastExpression = "(" PrimitiveType ")" UnaryExpression 
                      | "(" ReferenceType AdditionalBound* ")" UnaryExpressionNotPlusMinus 
                      | "(" ReferenceType AdditionalBound* ")" LambdaExpression
                      ; 
syntax ConstantExpression = Expression ;

// Lexical Definitions 

lexical SignedInteger =
  [+ \-]? [0-9]+ 
  ;
  
lexical LEX_StringLiteral =
   string: "\"" StringPart* "\"" 
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
  
lexical LEX_CharLiteral =
   char: "\'" CharContent "\'" 
  ;

lexical EscChar =
  "\\" 
  ;

lexical OctaEscape 
  = "\\" [0-3] [0-7]+ !>> [0-7] 
  | "\\" [0-7] !>> [0-7] 
  | "\\" [4-7] [0-7] 
  ;

lexical EscEscChar =
  "\\\\" 
  ;

lexical DeciNumeral =
  "0"
  | [1-9]
  | [1-9] [0-9 _]* [0-9];
 
 
keyword HexaSignificandKeywords =
  [0] [X x] "." 
  ;

lexical BinaryNumeral =
  "0" [b B] [0-1] [0-1 _]* !>> [0-1]*
  ;

lexical StringChars =
  FooStringChars 
  ;

lexical LAYOUT =
  [\t-\n \a0C-\a0D \ ] 
  | Comment 
  ;

lexical CharContent =
  EscapeSeq 
  | UnicodeEscape 
  |  single: SingleChar 
  ;

lexical Comment =
  "/**/" 
  | "//" EOLCommentChars !>> ![\n \a0D] LineTerminator 
  | "/*" !>> [*] CommentPart* "*/" 
  | "/**" !>> [/] CommentPart* "*/" 
  ;

syntax FloatingPointLiteral =
   float: HexaFloatLiteral !>> [D F d f] 
  |  float: DeciFloatLiteral \ DeciFloatLiteralKeywords !>> [D F d f] 
  ;

lexical OctaLiteral =
  OctaNumeral !>> [0-7] [L l]? 
  ;

lexical HexaFloatNumeral =
  HexaSignificand \ HexaSignificandKeywords !>> [0-9 A-F a-f] BinaryExponent 
  ;

syntax IntegerLiteral =
   hexa: HexaLiteral !>> [L l.] 
  |  octa: OctaLiteral !>> [L l] 
  |  deci: DeciLiteral !>> [L l] 
  |  binary: BinaryIntegerLiteral !>> [L l]
  ;

lexical HexaLiteral =
  HexaNumeral !>> [0-9 A-F a-f] [L l]? 
  ;

lexical DeciFloatLiteral =
  DeciFloatNumeral [D F d f]? 
  ;
  
lexical BinaryIntegerLiteral =
  BinaryNumeral [L l]?
  ; 
  
lexical ID =
	// Yes, this would be more correct, but REALLY slow at the moment
	//JavaLetter JavaLetterDigits* 
	//
	// therefore we go the ascii route:
  [$ A-Z _ a-z] [$ 0-9 A-Z _ a-z]* 
  ; 
  
lexical DeciFloatDigits =
  [0-9]+ 
  | [0-9]* "." [0-9]* 
  ;

lexical DeciLiteral =
  DeciNumeral !>> [. 0-9 D F d f] [L l]? 
  ;

lexical EscapeSeq =
  NamedEscape 
  | OctaEscape 
  ;

layout LAYOUTLIST  =
  LAYOUT* !>> [\t-\n \a0C-\a0D \ ] !>> (  [/]  [*]  ) !>> (  [/]  [/]  ) !>> "/*" !>> "//"
  ;

lexical NamedEscape =
   namedEscape: "\\" [\" \' \\ b f n r t] 
  ;
 
 
lexical BinaryExponent =
  [P p] SignedInteger !>> [0-9] 
  ;

lexical BlockCommentChars =
  ![* \\]+ 
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

lexical StringPart =
  UnicodeEscape 
  | EscapeSeq 
  |  chars: StringChars !>> ![\n \a0D \" \\]  !>> [\a00]
  ;

keyword FieldAccessKeywords =
  ExpressionName "." ID 
  ;

lexical EOLCommentChars =
  ![\n \a0D]* 
  ;

lexical SingleChar =
  ![\n \a0D \' \\] 
  ;

keyword ElemValKeywords =
  LeftHandSide "=" Expression 
  ;

lexical CommentPart =
  UnicodeEscape 
  | BlockCommentChars !>> ![* \\] 
  | EscChar !>> [\\ u] 
  | Asterisk !>> [/] 
  | EscEscChar 
  ;

syntax Identifier =
   id: [$ A-Z _ a-z] !<< ID \ IDKeywords !>> [$ 0-9 A-Z _ a-z] 
  ;
  
keyword ArrayAccessKeywords =
  ArrayCreationExpression ArrayAccess 
  ;

syntax BooleanLiteral
  = \false: "false" 
  | \true: "true" 
  ;

lexical DeciFloatExponentPart =
  [E e] SignedInteger !>> [0-9] 
  ;

lexical EndOfFile =
  
  ;

keyword DeciFloatLiteralKeywords =
  [0-9]+ 
  ;

keyword DeciFloatDigitsKeywords =
  "." 
  ;

keyword IDKeywords =
  "null" 
  | Keyword 
  | "true" 
  | "false" 
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

lexical Asterisk =
  "*" 
  ;
  
syntax CharacterLiteral =
  LEX_CharLiteral 
  ;  
  
syntax StringLiteral =
  LEX_StringLiteral 
  ;  
  
syntax Null = "null" ;   

syntax NullLiteral = Null ;
