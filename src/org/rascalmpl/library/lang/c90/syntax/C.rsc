@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module C

import ParseTree;

syntax Statement = "{" Declaration* Statement* "}" |
                   Identifier ":" Statement |
                   "case" Expression ":" Statement |
                   "default" ":" Statement |
                   ";" |
                   Expression ";" |
                   "if" "(" Expression ")" Statement |
                   "if" "(" Expression ")" Statement "else" Statement |
                   "switch" "(" Expression ")" Statement |
                   "while" "(" Expression ")" Statement |
                   "do" Statement "while" "(" Expression ")" ";" |
                   "for" "(" Expression? ";" Expression? ";" Expression? ")" Statement |
                   "goto" Identifier ";" |
                   "continue" ";" |
                   "break" ";" |
                   "return" ";" |
                   "return" Expression ";"
                   ;

syntax Expression = Variable: Identifier |
                    @category="Constant" HexadecimalConstant |
                    @category="Constant" IntegerConstant |
                    @category="Constant" CharacterConstant |
                    @category="Constant" FloatingPointConstant |
                    @category="Constant" StringConstant |
                    Expression "[" Expression "]" |
                    Expression "(" {NonCommaExpression ","}* ")" |
                    "sizeof" "(" TypeName ")" |
                    bracket Bracket: "(" Expression ")" |
                    Expression "." Identifier |
                    Expression "-\>" Identifier |
                    Expression "++" |
                    Expression "--" >
                    "++" Expression |
                    "--" Expression |
                    "&" Expression |
                    "*" Expression |
                    "+" Expression |
                    "-" Expression |
                    "~" Expression |
                    "!" Expression |
                    "sizeof" Expression exp {
                       list[Tree] children;
                       if(appl(prod(_,_,attrs([_*,term(cons("Bracket")),_*])),children) := exp){
                          Tree child = children[1];
                          if(appl(prod(_,_,attrs([_*,term(cons("Variable")),_*])),_) := child){
                             if(unparse(child) in typeDefs){
                                  fail;
                               }
                          }
                       }
                    } | // May be ambiguous with "sizeof(TypeName)".
                    "(" TypeName ")" Expression >
                    left (
                         Expression lexp "*" Expression rexp {
                            if(appl(prod(_,_,attrs([_*,term(cons("Variable")),_*])),_) := lexp){
                               if(unparse(child) in typeDefs){
                                  fail;
                               }
                            }
                         } | // May be ambiguous with "TypeName *Declarator".
                         Expression "/" Expression |
                         Expression "%" Expression
                    ) >
                    left (
                         Expression "+" Expression |
                         Expression "-" Expression
                    ) >
                    left (
                         Expression "\<\<" Expression |
                         Expression "\>\>" Expression
                    ) >
                    left (
                         Expression "\<" Expression |
                         Expression "\>" Expression |
                         Expression "\<=" Expression |
                         Expression "\>=" Expression
                    ) >
                    left (
                         Expression "==" Expression |
                         Expression "!=" Expression
                    ) >
                    left Expression "&" Expression >
                    left Expression "^" Expression >
                    left Expression "|" Expression >
                    left Expression "&&" Expression >
                    left Expression "||" Expression >
                    right Expression "?" Expression ":" Expression >
                    right (
                          Expression "=" Expression |
                          Expression "*=" Expression |
                          Expression "/=" Expression |
                          Expression "%=" Expression |
                          Expression "+=" Expression |
                          Expression "-=" Expression |
                          Expression "\<\<=" Expression |
                          Expression "\>\>=" Expression |
                          Expression "&=" Expression |
                          Expression "^=" Expression |
                          Expression "|=" Expression
                    ) >
                    left CommaExpression: Expression "," Expression
                    ;

syntax NonCommaExpression = Expression expr {
                               if(appl(prod(_,_,attrs([_*,term(cons("CommaExpression")),_*])),_) := expr){
                                  fail;
                               }
                            }
                            ;

syntax "+" = ... # [+];

syntax "-" = ... # [\-];

syntax "&" = ... # [&];

syntax Identifier = lex [a-zA-Z_] [a-zA-Z0-9_]*
                    - Keyword
                    # [a-zA-Z0-9_]
                    ;

syntax AnonymousIdentifier = 
                             ;

syntax Keyword = "auto" |
                 "break" |
                 "case" |
                 "char" |
                 "const" |
                 "continue" |
                 "default" |
                 "do" |
                 "double" |
                 "else" |
                 "enum" |
                 "extern" |
                 "float" |
                 "for" |
                 "goto" |
                 "if" |
                 "int" |
                 "long" |
                 "register" |
                 "return" |
                 "short" |
                 "signed" |
                 "sizeof" |
                 "static" |
                 "struct" |
                 "switch" |
                 "typedef" |
                 "union" |
                 "unsigned" |
                 "void" |
                 "volatile" |
                 "while"
                 # [a-zA-Z0-9_]
                 ;

syntax Declaration = Specifier+ specs {InitDeclarator ","}+ initDeclarators ";" {
                        list[Tree] specChildren;
                        if(appl(_,specChildren) := specs){
                           for(spec <- specs){
		                      if(appl(prod(_,_,attrs([_*,term(cons("StorageClass")),_*])),typeSpecifier) := spec){
                                 if([_*,appl(prod(_,_,attrs([_*,term(cons("TypeDef")),_*])),_),_*] := typeSpecifier){
                                    TypeSpecifier declType = findType(specChildren);
                                    
                                    list[tuple[str var, InitDeclarator initDecl]] variables = findVariableNames(initDeclarators);
                                    for(tuple[str var, InitDeclarator initDecl] variableTuple <- variables){
                                       str variable = variableTuple.var;
                                       InitDeclarator initDecl = variableTuple.initDecl;
                                       tuple[list[Specifier], Declarator] modifiers = findModifiers(specChildren, initDecl.decl);
                                       typeDefs += (variable:<declType, modifiers>); // Record the typedef.
                                    }
                                 }
                              }
                           }
                           
                           if(hasCustomType(specChildren)){
                              TypeSpecifier declType = findType(specChildren);
                              if(unparse(declType) notin typeDefs){
                                 fail;
                              } // May be ambiguous with "Exp * Exp".
                           }
                        }
                     } |
                     Specifier+ specs ";" {
                        list[Tree] specChildren;
                        if(appl(_,specChildren) := specs){
                           TypeSpecifier theType = findType(specChildren);
                           
                           for(spec <- specChildren){
                              if(appl(prod(_,_,attrs([_*,term(cons("TypeSpecifier")),_*])),typeSpecifier) := spec){
                                 if(identifier:appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_) := typeSpecifier[0]){
                                    if(identifier != theType) fail;
                                 }
                              }
                           } // May be ambiguous with Spec* {InitDecl ","}*
                           
                           if(appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_) := theType){
                              if(unparse(theType) notin typeDefs){
                                 fail;
                              } // May be ambiguous with "Exp * Exp".
                           }
                        }
                     } // Avoid.
                     ;

syntax GlobalDeclaration = Specifier* specs {InitDeclarator ","}+ initDeclarators ";" {
                           list[Tree] specChildren;
                           if(appl(_,specChildren) := specs){
                              for(spec <- specs){
                                 if(appl(prod(_,_,attrs([_*,term(cons("StorageClass")),_*])),typeSpecifier) := spec){
                                    if([_*,appl(prod(_,_,attrs([_*,term(cons("TypeDef")),_*])),_),_*] := typeSpecifier){
                                       TypeSpecifier declType = findType(specChildren);
                                       
                                       list[tuple[str var, InitDeclarator initDecl]] variables = findVariableNames(initDeclarators);
                                       for(tuple[str var, InitDeclarator initDecl] variableTuple <- variables){
                                          str variable = variableTuple.var;
                                          InitDeclarator initDecl = variableTuple.initDecl;
                                          tuple[list[Specifier], Declarator] modifiers = findModifiers(specChildren, initDecl.decl);
                                          typeDefs += (variable:<declType, modifiers>); // Record the typedef.
                                       }
                                    }
                                 }
                              }
                              
                              if(hasCustomType(specChildren)){
                                 TypeSpecifier declType = findType(specChildren);
                                 if(unparse(declType) notin typeDefs){
                                    fail;
                                 } // May be ambiguous with "Exp * Exp".
                              }
                           }
                        } |
                        Specifier+ specs ";" {
                           list[Tree] specChildren;
                           if(appl(_,specChildren) := specs){
                              TypeSpecifier theType = findType(specChildren);
                              
                              for(spec <- specChildren){
                                 if(appl(prod(_,_,attrs([_*,term(cons("TypeSpecifier")),_*])),typeSpecifier) := spec){
                                    if(identifier:appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_) := typeSpecifier[0]){
                                       if(identifier != theType) fail;
                                    }
                                 }
                              } // May be ambiguous with Spec* {InitDecl ","}*
                                 
                              if(appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_) := theType){
                                 if(unparse(theType) notin typeDefs){
                                    fail;
                                 } // May be ambiguous with "Exp * Exp".
                              }
                           }
                        } // Avoid.
                        ;

syntax InitDeclarator = Declarator decl |
                        Declarator decl "=" Initializer
                       ;

syntax Specifier = StorageClass: StorageClass |
                   TypeSpecifier: TypeSpecifier |
                   TypeQualifier: TypeQualifier
                   ;

syntax StorageClass = TypeDef: "typedef" |
                      "extern" |
                      "static" |
                      "auto" |
                      "register"
                      ;

syntax TypeSpecifier = Identifier: Identifier |
                       Void: "void" |
                       Char: "char" |
                       Short: "short" |
                       Int: "int" |
                       Long: "long" |
                       Float: "float" |
                       Double: "double" |
                       "signed" |
                       "unsigned" |
                       Struct: "struct" Identifier |
                       StructDecl: "struct" Identifier "{" StructDeclaration* "}" |
                       StructAnonDecl: "struct" "{" StructDeclaration* "}" |
                       Union: "union" Identifier |
                       UnionDecl: "union" Identifier "{" StructDeclaration* "}" |
                       UnionAnonDecl: "union" "{" StructDeclaration* "}" |
                       Enum: "enum" Identifier |
                       EnumDecl: "enum" Identifier "{" {Enumerator ","}+ "}" |
                       EnumAnonDecl: "enum" "{" {Enumerator ","}+ "}"
                       ;

syntax TypeQualifier = "const" |
                       "volatile"
                       ;

syntax StructDeclaration = Specifier+ specs {StructDeclarator ","}+ ";" {
                              list[Tree] specChildren;
                              if(appl(_,specChildren) := specs){
                                 TypeSpecifier theType = findType(specChildren);
                                 
                                 for(spec <- specChildren){
                                    if(appl(prod(_,_,attrs([_*,term(cons("TypeSpecifier")),_*])),typeSpecifier) := spec){
                                       if(identifier:appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_) := typeSpecifier[0]){
                                          if(identifier != theType) fail;
                                       }
                                    }
                                 }
                              }
                           } | // TODO: Disallow store class specifiers.
                           Specifier+ specs ";" { // TODO: Disallow store class specifiers.
                              list[Tree] specChildren;
                              if(appl(_,specChildren) := specs){
                                 TypeSpecifier theType = findType(specChildren);
                                 
                                 if(appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_) := theType){   
                                    if(unparse(theType) notin typeDefs){
                                       fail;
                                    } // May be ambiguous with "Exp * Exp".
                                 }
                                 
                                 for(spec <- specChildren){
                                    if(appl(prod(_,_,attrs([_*,term(cons("TypeSpecifier")),_*])),typeSpecifier) := spec){
                                       if(identifier:appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_) := typeSpecifier[0]){
                                          if(identifier != theType) fail;
                                       }
                                    }
                                 } // May be ambiguous with Spec* {StructDecl ","}*
                              }
                           } // Avoid.
                           ;

syntax StructDeclarator = Declarator |
                          Declarator? ":" Expression // Prefer the one where 'Declarator' is filled.
                          ;

syntax Parameters = {Parameter ","}+ MoreParameters?
                    ;

syntax MoreParameters = "," "..."
                        ;

syntax Initializer = NonCommaExpression |
                     "{" {Initializer ","}+ ","?  "}"
                     ;

syntax TypeName = Specifier+ AbstractDeclarator
                  ;

syntax Enumerator = Identifier |
                    Identifier "=" NonCommaExpression
                    ;

syntax AbstractDeclarator = Identifier: AnonymousIdentifier |
                            bracket Bracket: "(" AbstractDeclarator decl ")" |
                            ArrayDeclarator: AbstractDeclarator decl "[" Expression? exp "]" |
                            FunctionDeclarator: AbstractDeclarator decl "(" Parameters? params ")" >
                            PointerDeclarator: "*" TypeQualifier* qualifiers AbstractDeclarator decl
                            ;

syntax Declarator = Identifier: Identifier |
                    bracket Bracket: "(" Declarator decl ")" |
                    ArrayDeclarator: Declarator decl "[" Expression? exp "]" |
                    FunctionDeclarator: Declarator decl "(" Parameters? params ")" >
                    PointerDeclarator: "*" TypeQualifier* qualifiers Declarator decl
                    ;

syntax Parameter = Specifier* Declarator
                   ;

syntax HexadecimalConstant = lex [0] [xX] [a-fA-F0-9]+ [uUlL]*
                             # [a-fA-F0-9]
                             ;

syntax IntegerConstant = lex [0-9]+ [uUlL]*
                         # [0-9]
                         ;

syntax CharacterConstant = lex [L]? [\'] CharacterConstantContent+ [\']
                           ;

syntax CharacterConstantContent = lex [\\] ![] |
                                  lex ![\\\']
                                  ;

syntax FloatingPointConstant = lex [0-9]+ Exponent [fFlL]? |
                               lex [0-9]* [.] [0-9]+ Exponent? [fFlL]? |
                               lex [0-9]+ [.] Exponent? [fFlL]?
                               # [0-9]
                               ;

syntax StringConstant = lex [L]? [\"] StringConstant* [\"]
                        ;

syntax StringConstantContent = lex [\\] ![] |
                               lex ![\\\"]
                               ;

syntax Exponent = lex [Ee] [+\-]? [0-9]+
                  ;

syntax ExternalDeclaration = FunctionDefinition |
                             GlobalDeclaration
                             ;

// TODO Fix the collision between the last Specifier and the Declarator for K&R syle function definitions.
syntax FunctionDefinition = Specifier* Declarator Declaration* "{" Declaration* Statement* "}" // TODO: Type specifiers are required for K&R style parameter declarations, initialization of them is not allowed however.
                            ; // TODO Disallow StorageSpecifiers as specifiers. Disallow ArrayDeclarators in the Declarator.

start syntax TranslationUnit = ExternalDeclaration+
                               ;

syntax Comment = lex [/][*] MultiLineCommentBodyToken* [*][/] |
                 lex "//" ![\n]* [\n]
                 ;

syntax MultiLineCommentBodyToken = lex ![*] |
                                   lex Asterisk
                                   ;

syntax Asterisk = lex [*]
                  # [/]
                  ;

layout LAYOUTLIST = LAYOUT*
                    # [\ \t\n\r]
                    ;

syntax LAYOUT = lex Whitespace: [\ \t\n\r] |
                @category="Comment" lex Comment: Comment
                ;


map[str name, tuple[TypeSpecifier var, tuple[list[Specifier], Declarator] modifiers] cType] typeDefs = (); // Name to type mapping.

private TypeSpecifier findType(list[Tree] specs){
	TypeSpecifier cType;
	
	list[Tree] typeSpecs = [];
	for(spec <- specs){
		if(appl(prod(_,_,attrs([_*,term(cons("TypeSpecifier")),_*])),typeSpecifier) := spec){
			typeSpecs += typeSpecifier[0];
		}
	}
	
	// This is order dependant, so don't convert this to a switch.
	if([_*,voidType:appl(prod(_,_,attrs([_*,term(cons("Void")),_*])),_),_*] := typeSpecs){
		cType = voidType;
	}else if([_*,charType:appl(prod(_,_,attrs([_*,term(cons("Char")),_*])),_),_*] := typeSpecs){
		cType = charType;
	}else if([_*,shortType:appl(prod(_,_,attrs([_*,term(cons("Short")),_*])),_),_*] := typeSpecs){
		cType = shortType;
	}else if([_*,longType:appl(prod(_,_,attrs([_*,term(cons("Long")),_*])),_),_*] := typeSpecs){
		cType = longType;
	}else if([_*,floatType:appl(prod(_,_,attrs([_*,term(cons("Float")),_*])),_),_*] := typeSpecs){
		cType = floatType;
	}else if([_*,doubleType:appl(prod(_,_,attrs([_*,term(cons("Double")),_*])),_),_*] := typeSpecs){
		cType = doubleType;
	}else if([_*,intType:appl(prod(_,_,attrs([_*,term(cons("Int")),_*])),_),_*] := typeSpecs){
		cType = intType; // Do this one last, since you can have things like "long int" or "short int". In these cases anything other then "int" is what you want.
	}else if([_*,theStruct:appl(prod(_,_,attrs([_*,term(cons("Struct")),_*])),_),_*] := typeSpecs){
		cType = theStruct;
	}else if([_*,theStruct:appl(prod(_,_,attrs([_*,term(cons("StructDecl")),_*])),_),_*] := typeSpecs){
		cType = theStruct;
	}else if([_*,theStruct:appl(prod(_,_,attrs([_*,term(cons("StructAnonDecl")),_*])),_),_*] := typeSpecs){
		cType = theStruct;
	}else if([_*,theUnion:appl(prod(_,_,attrs([_*,term(cons("Union")),_*])),_),_*] := typeSpecs){
		cType = theUnion;
	}else if([_*,theUnion:appl(prod(_,_,attrs([_*,term(cons("UnionDecl")),_*])),_),_*] := typeSpecs){
		cType = theUnion;
	}else if([_*,theUnion:appl(prod(_,_,attrs([_*,term(cons("UnionAnonDecl")),_*])),_),_*] := typeSpecs){
		cType = theUnion;
	}else if([_*,theEnum:appl(prod(_,_,attrs([_*,term(cons("Enum")),_*])),_),_*] := typeSpecs){
		cType = theEnum;
	}else if([_*,theEnum:appl(prod(_,_,attrs([_*,term(cons("EnumDecl")),_*])),_),_*] := typeSpecs){
		cType = theEnum;
	}else if([_*,theEnum:appl(prod(_,_,attrs([_*,term(cons("EnumAnonDecl")),_*])),_),_*] := typeSpecs){
		cType = theEnum;
	}else if([_*,identifier:appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_),_*] := typeSpecs){
		cType = identifier;
	}else{
		// Bah.
		// If no type is defined the type is int.
		cType = appl(prod([lit("int")],sort("TypeSpecifier"),\no-attrs()),[appl(prod([\char-class([range(105,105)]),\char-class([range(110,110)]),\char-class([range(116,116)])],lit("int"),attrs([literal()])),[char(105),char(110),char(116)])]);
	}
	
	return cType;
}

private bool hasCustomType(list[Tree] specs){
	for(spec <- specs){
		if(appl(prod(_,_,attrs([_*,term(cons("TypeSpecifier")),_*])),typeSpecifier) := spec){
			return (appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_) := typeSpecifier[0]);
		}
	}
	return false;
}

private tuple[list[Specifier], Declarator] findModifiers(list[Tree] specs, Declarator decl){
	list[Specifier] modifiers = [];
	
	for(spec <- specs){
		if(appl(prod(_,_,attrs([_*,term(cons("TypeQualifier")),_*])),typeQualifier) := spec){
			modifiers += spec;
		}else if(appl(prod(_,_,attrs([_*,term(cons("StorageClass")),_*])),storageClass) := spec){
			modifiers += spec;
		}
	}
	
	return <modifiers, decl>;
}

private str findVariableInDeclarator(Declarator decl){
	if(appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_) := decl){
		return unparse(decl);
	}else if(appl(prod(_,_,attrs([_*,term(cons("Bracket")),_*])),_) := decl){
		return findVariableInDeclarator(decl.decl);
	}else if(appl(prod(_,_,attrs([_*,term(cons("ArrayDeclarator")),_*])),_) := decl){
		return findVariableInDeclarator(decl.decl);
	}else if(appl(prod(_,_,attrs([_*,term(cons("FunctionDeclarator")),_*])),_) := decl){
		return findVariableInDeclarator(decl.decl);
	}else if(appl(prod(_,_,attrs([_*,term(cons("PointerDeclarator")),_*])),_) := decl){
		return findVariableInDeclarator(decl.decl);
	}
	
	throw "This can\'t happen";
}

private list[tuple[str var, InitDeclarator initDecl]] findVariableNames({InitDeclarator ","}+ initDecls){
	list[tuple[str var, InitDeclarator initDecl]] variables = [];
	for(initDecl <- initDecls){
		str var = findVariableInDeclarator(initDecl.decl);
		
		variables += <var, initDecl>;
	}
	
	return variables;
}
