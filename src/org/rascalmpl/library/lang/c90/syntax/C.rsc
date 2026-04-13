@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::c90::\syntax::C

syntax Statement 
	= "{" Declaration* Statement* "}" 
	| Identifier ":" Statement 
	| "case" Expression ":" Statement 
	| "default" ":" Statement 
	| ";" 
	| Expression ";" 
	| "if" "(" Expression ")" Statement 
	| "if" "(" Expression ")" Statement "else" Statement 
	| "switch" "(" Expression ")" Statement 
	| "while" "(" Expression ")" Statement 
	| "do" Statement "while" "(" Expression ")" ";" 
	| "for" "(" Expression? ";" Expression? ";" Expression? ")" Statement 
	| "goto" Identifier ";" 
	| "continue" ";" 
	| "break" ";" 
	| "return" ";" 
	| "return" Expression ";"
	;

syntax Expression 
	= variable: Identifier 
	| @category="number" HexadecimalConstant 
	| @category="number" IntegerConstant 
	| @category="string" CharacterConstant 
	| @category="number" FloatingPointConstant 
	| @category="string" StringConstant 
	| Expression "[" Expression "]" 
	| Expression "(" {NonCommaExpression ","}* ")" 
	| "sizeof" "(" TypeName ")" 
	| bracket \bracket: "(" Expression ")" 
	| Expression "." Identifier 
	| Expression "-\>" Identifier 
	| Expression "++" 
	| Expression "--" 
	> [+] !<< "++" Expression 
	| [\-] !<< "--" Expression 
	| "&" Expression 
	| "*" Expression 
	| "+" Expression 
	| "-" Expression 
	| "~" Expression 
	| "!" Expression 
	| sizeOfExpression: "sizeof" Expression exp // May be ambiguous with "sizeof(TypeName)".
	| "(" TypeName ")" Expression 
	> left ( multiplicationExpression: Expression lexp "*" Expression rexp // May be ambiguous with "TypeName *Declarator".
	       | Expression "/" Expression 
	       | Expression "%" Expression
	       ) 
	> left ( Expression "+" Expression 
	       | Expression "-" Expression
	       )
	> left ( Expression "\<\<" Expression 
	       | Expression "\>\>" Expression
		   )
	> left ( Expression "\<" Expression 
	       | Expression "\>" Expression 
	       | Expression "\<=" Expression 
	       | Expression "\>=" Expression
	       )
	> left ( Expression "==" Expression 
	       | Expression "!=" Expression
	       )
    > left Expression "&" Expression 
	> left Expression "^" Expression 
	> left Expression "|" Expression 
	> left Expression "&&" Expression 
	> left Expression "||" Expression 
	> right Expression "?" Expression ":" Expression 
	> right ( Expression "=" Expression 
	        | Expression "*=" Expression 
	        | Expression "/=" Expression 
	        | Expression "%=" Expression 
	        | Expression "+=" Expression 
	        | Expression "-=" Expression 
	        | Expression "\<\<=" Expression 
	        | Expression "\>\>=" Expression 
	        | Expression "&=" Expression 
	        | Expression "^=" Expression 
	        | Expression "
	        | =" Expression
			)
	> left commaExpression: Expression "," Expression
	;

syntax NonCommaExpression = nonCommaExpression: Expression expr;

lexical Identifier = ([a-zA-Z_] [a-zA-Z0-9_]* !>> [a-zA-Z0-9_]) \ Keyword;

syntax AnonymousIdentifier = ;

keyword Keyword 
	= "auto" 
	| "break" 
	| "case" 
	| "char" 
	| "const" 
	| "continue" 
	| "default" 
	| "do" 
	| "double" 
	| "else" 
	| "enum" 
	| "extern" 
	| "float" 
	| "for" 
	| "goto" 
	| "if" 
	| "int" 
	| "long" 
	| "register" 
	| "return" 
	| "short" 
	| "signed" 
	| "sizeof" 
	| "static" 
	| "struct" 
	| "switch" 
	| "typedef" 
	| "union" 
	| "unsigned" 
	| "void" 
	| "volatile" 
	| "while"
	;

syntax Declaration 
	= declarationWithInitDecls: Specifier+ specs {InitDeclarator ","}+ initDeclarators ";" 
	| declarationWithoutInitDecls: Specifier+ specs ";" // Avoid.
	;

syntax GlobalDeclaration 
	= globalDeclarationWithInitDecls: Specifier* specs0 {InitDeclarator ","}+ initDeclarators ";" 
	| globalDeclarationWithoutInitDecls: Specifier+ specs1 ";" // Avoid.
	;

syntax InitDeclarator 
	= Declarator decl 
	| Declarator decl "=" Initializer
	;

syntax Specifier 
	= storageClass: StorageClass 
	| typeSpecifier: TypeSpecifier 
	| typeQualifier: TypeQualifier
	;

syntax StorageClass 
	= typeDef: "typedef" 
	| "extern" 
	| "static" 
	| "auto" 
	| "register"
	;

syntax TypeSpecifier 
	= identifier: Identifier 
	| \void: "void" 
	| char: "char" 
	| short: "short" 
	| \int: "int" 
	| long: "long" 
	| \float: "float" 
	| \double: "double" 
	| "signed" 
	| "unsigned" 
	| struct: "struct" Identifier 
	| structDecl: "struct" Identifier "{" StructDeclaration* "}" 
	| structAnonDecl: "struct" "{" StructDeclaration* "}" 
	| union: "union" Identifier 
	| unionDecl: "union" Identifier "{" StructDeclaration* "}" 
	| unionAnonDecl: "union" "{" StructDeclaration* "}" 
	| enum: "enum" Identifier 
	| enumDecl: "enum" Identifier "{" {Enumerator ","}+ "}" 
	| enumAnonDecl: "enum" "{" {Enumerator ","}+ "}"
	;

syntax TypeQualifier 
	= "const" 
	| "volatile"
	;

syntax StructDeclaration 
	= structDeclWithDecl: Specifier+ specs {StructDeclarator ","}+ ";" // TODO: Disallow store class specifiers.
	| structDeclWithoutDecl: Specifier+ specs ";" // TODO: Disallow store class specifiers. Avoid.
	;

syntax StructDeclarator 
	= Declarator 
	| Declarator? ":" Expression // Prefer the one where 'Declarator' is filled.
	;

syntax Parameters 
	= {Parameter ","}+ MoreParameters? 
	| "void"
	;

syntax MoreParameters = "," "...";

syntax Parameter = Specifier* Declarator;

syntax PrototypeParameter = Specifier* AbstractDeclarator;

syntax PrototypeParameters 
	= {PrototypeParameter ","}+ MoreParameters? 
	| "void"
	;

syntax Initializer 
	= NonCommaExpression 
	| "{" {Initializer ","}+ ","?  "}"
	;

syntax TypeName = Specifier+ AbstractDeclarator;

syntax Enumerator 
	= Identifier 
	| Identifier "=" NonCommaExpression
	;

syntax AbstractDeclarator 
	= identifier: AnonymousIdentifier 
	| bracket \bracket: "(" AbstractDeclarator decl ")" 
	| arrayDeclarator: AbstractDeclarator decl "[" Expression? exp "]" 
	| functionDeclarator: AbstractDeclarator decl "(" Parameters? params ")" 
	> pointerDeclarator: "*" TypeQualifier* qualifiers AbstractDeclarator decl
	;

syntax PrototypeDeclarator 
	= identifier: Identifier 
	| bracket \bracket: "(" AbstractDeclarator abs_decl ")" 
	| arrayDeclarator: PrototypeDeclarator proto_decl "[" Expression? exp "]" 
	| functionDeclarator: PrototypeDeclarator proto_decl "(" PrototypeParameters? params ")" 
	> pointerDeclarator: "*" TypeQualifier* qualifiers PrototypeDeclarator decl
	;

syntax Declarator 
	= identifier: Identifier 
	| bracket \bracket: "(" Declarator decl ")" 
	| arrayDeclarator: Declarator decl "[" Expression? exp "]" 
	| functionDeclarator: Declarator decl "(" Parameters? params ")" 
	> pointerDeclarator: "*" TypeQualifier* qualifiers Declarator decl
	;

lexical IntegerConstant = [0-9]+ [uUlL]* !>> [0-9];

lexical HexadecimalConstant = [0] [xX] [a-fA-F0-9]+ [uUlL]* !>> [a-fA-F0-9];

lexical FloatingPointConstant 
	= [0-9]+ Exponent [fFlL]? 
	| [0-9]* [.] [0-9]+ !>> [0-9] Exponent? [fFlL]? 
	| [0-9]+ [.] Exponent? [fFlL]?
	;

lexical Exponent = [Ee] [+\-]? [0-9]+ !>> [0-9];

lexical CharacterConstant = [L]? [\'] CharacterConstantContent+ [\'];

lexical CharacterConstantContent 
	= [\\] ![] 
	| ![\\\']
	;

lexical StringConstant = [L]? [\"] StringConstantContent* [\"];

lexical StringConstantContent 
	= [\\] ![] 
	| ![\\\"]
	;

// TODO: Type specifiers are required for K&R style parameter declarations, initialization of them is not allowed however.
// TODO: Disallow storage class specifiers as specifiers.
// TODO: Disallow ArrayDeclarators in the declarator.
syntax FunctionDefinition = defaultFunctionDefinition: Specifier* specs Declarator Declaration* "{" Declaration* Statement* "}";

syntax FunctionPrototype = defaultFunctionPrototype: Specifier* specs PrototypeDeclarator decl ";";

syntax ExternalDeclaration 
	= FunctionDefinition 
	| FunctionPrototype 
	| GlobalDeclaration
	;

start syntax TranslationUnit = ExternalDeclaration+;

lexical Comment 
	= [/][*] MultiLineCommentBodyToken* [*][/] 
	| "//" ![\n]* [\n]
	;

lexical MultiLineCommentBodyToken 
	= ![*] 
	| Asterisk
	;

lexical Asterisk = [*] !>> [/];

layout LAYOUTLIST = LAYOUT* !>> [\ \t\n\r];

lexical LAYOUT 
	= whitespace: [\ \t\n\r] 
	| @category="comment" comment: Comment
	;
