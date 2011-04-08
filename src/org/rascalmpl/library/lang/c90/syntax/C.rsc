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
                             if("<child>" in typeDefs){
                                  fail;
                               }
                          }
                       }
                    } | // May be ambiguous with "sizeof(TypeName)".
                    "(" TypeName ")" Expression >
                    left (
                         Expression lexp "*" Expression rexp {
                            if(appl(prod(_,_,attrs([_*,term(cons("Variable")),_*])),_) := lexp){
                               if("<child>" in typeDefs){
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

syntax Declaration = Specifier* specs {InitDeclarator ","}+ initDeclarators ";" {
                        list[Tree] specChildren;
                        if(appl(_,specChildren) := specs){
                           if([_*,appl(prod(_,_,attrs([_*,term(cons("TypeDef")),_*])),_),_*] := specChildren){
                              str declType = findType(specChildren);
                              list[tuple[str var, InitDeclarator initDecl]] variables = findVariableNames(initDeclarators);
                              for(tuple[str var, InitDeclarator initDecl] variableTuple <- variables){
                                 str variable = variableTuple.var;
                                 InitDeclarator initDecl = variableTuple.initDecl;
                                 tuple[list[str], Declarator] modifiers = findModifiers(specChildren, initDecl);
                                 typeDefs += (variable:<declType, modifiers>); // Record the typedef.
                              }
                           }
                           
                           if(hasCustomType(specChildren)){
                              str declType = findType(specChildren);
                              if("<declType>" notin typeDefs){
                                 fail;
                              } // May be ambiguous with "Exp * Exp".
                           }
                        }
                     }
                     ;

syntax InitDeclarator = Declarator decl |
                        Declarator decl "=" Initializer
                       ;

syntax Specifier = Identifier: Identifier |
                   Void: "void" |
                   Char: "char" |
                   Short: "short" |
                   Int: "int" |
                   Long: "long" |
                   Float: "float" |
                   Double: "double" |
                   Struct: "struct" Identifier |
                   StructDecl: "struct" Identifier "{" StructDeclaration+ "}" |
                   StructAnonDecl: "struct" "{" StructDeclaration+ "}" |
                   Union: "union" Identifier |
                   UnionDecl: "union" Identifier "{" StructDeclaration+ "}" |
                   UnionAnonDecl: "union" "{" StructDeclaration+ "}" |
                   Enum: "enum" Identifier |
                   EnumDecl: "enum" Identifier "{" {Enumerator ","}+ "}" |
                   EnumAnonDecl: "enum" "{" {Enumerator ","}+ "}" |
                   "signed" |
                   "unsigned" |
                   "const" |
                   "volatile" |
                   TypeDef: "typedef" |
                   "extern" |
                   "static" |
                   "auto" |
                   "register" |
                   ;

syntax StructDeclaration = Specifier* specs {StructDeclarator ","}+ ";" // TODO Disallow typedef specifier and such.
                           ;

syntax StructDeclarator = Declarator |
                          Declarator? ":" Expression
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

syntax Pointer = PointerContent+
                 ;

syntax PointerContent = "*" Specifier+ specs; // TODO: Only allow type qualifiers and identifiers.

syntax Enumerator = Identifier |
                    Identifier "=" NonCommaExpression
                    ;

syntax AbstractDeclarator = Identifier: AnonymousIdentifier |
                            bracket Bracket: "(" AbstractDeclarator decl ")" |
                            ArrayDeclarator: AbstractDeclarator decl "[" Expression? exp "]" |
                            FunctionDeclarator: AbstractDeclarator decl "(" Parameters? ")" >
                            non-assoc PointerDeclarator: Pointer AbstractDeclarator decl
                            ;

syntax Declarator = Identifier: Identifier |
                    bracket Bracket: "(" Declarator decl ")" |
                    ArrayDeclarator: Declarator decl "[" Expression? exp "]" |
                    FunctionDeclarator: Declarator decl "(" Parameters? params ")" >
                    non-assoc PointerDeclarator: Pointer pointer Declarator decl
                    ;

syntax Parameter = Specifier+ Declarator |
                   Specifier+ AbstractDeclarator
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
                             Declaration
                             ;

syntax FunctionDefinition = Specifier* Declarator Declaration* "{" Declaration* Statement* "}" // TODO Disallow typedef specifier and such.
                            ;

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


map[str name, tuple[str var, tuple[list[str], Declarator] modifiers] cType] typeDefs = (); // Name to type mapping.

private str findType(list[Tree] specs){
	str cType = "int"; // If no type is defined the type is int.
	
    if([_*,appl(prod(_,_,attrs([_*,term(cons("Void")),_*])),_),_*] := specs){
       cType = "void";
    }else if([_*,appl(prod(_,_,attrs([_*,term(cons("Char")),_*])),_),_*] := specs){
       cType = "char";
    }else if([_*,appl(prod(_,_,attrs([_*,term(cons("Short")),_*])),_),_*] := specs){
       cType = "short";
    }else if([_*,appl(prod(_,_,attrs([_*,term(cons("Long")),_*])),_),_*] := specs){
       cType = "long";
    }else if([_*,appl(prod(_,_,attrs([_*,term(cons("Float")),_*])),_),_*] := specs){
       cType = "float";
    }else if([_*,appl(prod(_,_,attrs([_*,term(cons("Double")),_*])),_),_*] := specs){
       cType = "double";
    }else if([_*,identifier:appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_),_*] := specs){
       cType = "<identifier>";
    }else if([_*,theStruct:appl(prod(_,_,attrs([_*,term(cons("Struct")),_*])),_),_*] := specs){
       cType = "<theStruct>";
    }else if([_*,theStruct:appl(prod(_,_,attrs([_*,term(cons("StructDecl")),_*])),_),_*] := specs){
       cType = "<theStruct>";
    }else if([_*,theStruct:appl(prod(_,_,attrs([_*,term(cons("StructAnonDecl")),_*])),_),_*] := specs){
       cType = "<theStruct>";
    }else if([_*,theUnion:appl(prod(_,_,attrs([_*,term(cons("Union")),_*])),_),_*] := specs){
       cType = "<theUnion>";
    }else if([_*,theUnion:appl(prod(_,_,attrs([_*,term(cons("UnionDecl")),_*])),_),_*] := specs){
       cType = "<theUnion>";
    }else if([_*,theUnion:appl(prod(_,_,attrs([_*,term(cons("UnionAnonDecl")),_*])),_),_*] := specs){
       cType = "<theUnion>";
    }else if([_*,theEnum:appl(prod(_,_,attrs([_*,term(cons("Enum")),_*])),_),_*] := specs){
       cType = "<theEnum>";
    }else if([_*,theEnum:appl(prod(_,_,attrs([_*,term(cons("EnumDecl")),_*])),_),_*] := specs){
       cType = "<theEnum>";
    }else if([_*,theEnum:appl(prod(_,_,attrs([_*,term(cons("EnumAnonDecl")),_*])),_),_*] := specs){
       cType = "<theEnum>";
    }else if([_*,appl(prod(_,_,attrs([_*,term(cons("Int")),_*])),_),_*] := specs){
       cType = "int"; // Do this one last, since you can have things like "long int" or "short int". In these cases anything other then "int" is what you want.
    }
	
	return cType;
}

private list[str] cTypes = ["void", "char", "short", "int", "long", "float", "double"];

private list[str] cStructUnionEnumIdentTypes = ["Identifier", "Struct", "StructDecl", "StructAnonDecl", "Union", "UnionDecl", "UnionAnonDecl", "Enum", "EnumDecl", "EnumAnonDecl"];

private bool hasCustomType(list[Tree] specs){
	for(spec <- specs, "<spec>" notin cTypes){
		if("<spec>" != "typedef"){
			if([_*,cStructUnionEnumIdentType,_*] := cStructUnionEnumIdentTypes, appl(prod(_,_,attrs([_*,term(cons("<cStructUnionEnumIdentType>")),_*])),_) := spec){
				;
			}else{
				return false;
			}
		}
	}
	return true;
}

private tuple[list[str], Declarator] findModifiers(list[Tree] specs, InitDeclarator initDecl){
	list[str] modifiers = [];
	
	modifiers = for(spec <- specs, "<spec>" notin cTypes){
		if("<spec>" != "typedef"){
			if([_*,cStructUnionEnumIdentType,_*] := cStructUnionEnumIdentTypes, appl(prod(_,_,attrs([_*,term(cons("<cStructUnionEnumIdentType>")),_*])),_) := spec){
				;
			}else{
				append("<spec>");
			}
		}
	}
	
	return <modifiers, initDecl.decl>;
}

private str findVariableInDeclarator(Declarator decl){
	if(appl(prod(_,_,attrs([_*,term(cons("Identifier")),_*])),_) := decl){
		return "<decl>";
	}else if(appl(prod(_,_,attrs([_*,term(cons("Bracket")),_*])),_) := decl){
		return walkOverDeclarator(decl.decl);
	}else if(appl(prod(_,_,attrs([_*,term(cons("FunctionDeclarator")),_*])),_) := decl){
		return walkOverDeclarator(decl.decl);
	}else if(appl(prod(_,_,attrs([_*,term(cons("PointerDeclarator")),_*])),_) := decl){
		return walkOverDeclarator(decl.decl);
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
