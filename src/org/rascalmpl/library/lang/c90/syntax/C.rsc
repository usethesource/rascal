@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::c90::\syntax::C

import ParseTree;

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
	| @category="Constant" HexadecimalConstant 
	| @category="Constant" IntegerConstant 
	| @category="Constant" CharacterConstant 
	| @category="Constant" FloatingPointConstant 
	| @category="Constant" StringConstant 
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
	= globalDeclarationWithInitDecls: Specifier* specs {InitDeclarator ","}+ initDeclarators ";" 
	| globalDeclarationWithoutInitDecls: Specifier+ specs ";" // Avoid.
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
	| bracket \bracket: "(" AbstractDeclarator decl ")" 
	| arrayDeclarator: PrototypeDeclarator decl "[" Expression? exp "]" 
	| functionDeclarator: PrototypeDeclarator decl "(" PrototypeParameters? params ")" 
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
	| @category="Comment" comment: Comment
	;

//--------------------------------------------------------------------------

public Tree SizeOfExpression(Expression exp){ // May be ambiguous with "sizeof(TypeName)".
   list[Tree] children;
   if(appl(prod(_,_,attrs([_*,term(cons("bracket")),_*])),children) := exp){
      Tree child = children[1];
      if(appl(prod(label("variable",_),_,_),_) := child){
         if(unparse(child) in typeDefs){
            filter;
         }
      }
   }
   
   fail;
}

public Tree MultiplicationExpression(Expression lexp, Expression rexp){ // May be ambiguous with "TypeName *Declarator".
   if(appl(prod(label("variable",_),_,_),_) := lexp){
      if(unparse(lexp) in typeDefs){
         filter;
      }
   }
   
   fail;
}

public Tree NonCommaExpression(Expression expr){
   if(appl(prod(label("commaExpression",_),_,_),_) := expr){
      filter;
   }
   
   fail;
}

public Tree DeclarationWithInitDecls(Specifier+ specs, {InitDeclarator ","}+ initDeclarators){
   list[Tree] specChildren;
   if(appl(_,specChildren) := specs){
      TypeSpecifier theType = findType(specChildren);
      
      for(spec <- specs){
         if(appl(prod(label("storageClass",_),_,_),typeSpecifier) := spec){
            if([_*,appl(prod(label("typeDef",_),_,_),_),_*] := typeSpecifier){
               list[tuple[str var, InitDeclarator initDecl]] variables = findVariableNames(initDeclarators);
               for(tuple[str var, InitDeclarator initDecl] variableTuple <- variables){
                  str variable = variableTuple.var;
                  InitDeclarator initDecl = variableTuple.initDecl;
                  tuple[list[Specifier], Declarator] modifiers = findModifiers(specChildren, initDecl.decl);
                  typeDefs += (variable:<theType, modifiers>); // Record the typedef.
               }
            }
         }
      }
      
      if(hasCustomType(specChildren)){
         if(unparse(theType) notin typeDefs){
            filter;
         } // Fail if not typedeffed. And may be ambiguous with "Exp * Exp".
      }
   }
   
   fail;
}

public Tree DeclarationWithoutInitDecls(Specifier+ specs){
   list[Tree] specChildren;
   if(appl(_,specChildren) := specs){
      TypeSpecifier theType = findType(specChildren);
      
      for(spec <- specChildren){
         if(appl(prod(label("typeSpecifier",_),_,_),typeSpecifier) := spec){
            if(identifier:appl(prod(label("identifier",_),_,_),_) := typeSpecifier[0]){
               if(identifier != theType) filter;
            }
         }
      } // May be ambiguous with Spec* {InitDecl ","}*
      
      if(appl(prod(label("identifier",_),_,_),_) := theType){
         if(unparse(theType) notin typeDefs){
            filter;
         } // Fail if not typedeffed. And may be ambiguous with "Exp * Exp".
      }
   }
   
   fail;
}

public Tree GlobalDeclarationWithInitDecls(Specifier* specs, {InitDeclarator ","}+ initDeclarators){
   list[Tree] specChildren;
   if(appl(_,specChildren) := specs){
      TypeSpecifier theType = findType(specChildren);
      
      for(spec <- specs){
         if(appl(prod(label("storageClass",_),_,_),typeSpecifier) := spec){
            if([_*,appl(prod(label("typeDef",_),_,_),_),_*] := typeSpecifier){
               list[tuple[str var, InitDeclarator initDecl]] variables = findVariableNames(initDeclarators);
               for(tuple[str var, InitDeclarator initDecl] variableTuple <- variables){
                  str variable = variableTuple.var;
                  InitDeclarator initDecl = variableTuple.initDecl;
                  tuple[list[Specifier], Declarator] modifiers = findModifiers(specChildren, initDecl.decl);
                  typeDefs += (variable:<theType, modifiers>); // Record the typedef.
               }
            }
         }
      }
       
      if(hasCustomType(specChildren)){
         if(unparse(theType) notin typeDefs){
            filter;
         } // Fail if not typedeffed. And may be ambiguous with "Exp * Exp".
      }
   }
   
   fail;
}

public Tree GlobalDeclarationWithoutInitDecls(Specifier+ specs){
   list[Tree] specChildren;
   if(appl(_,specChildren) := specs){
      TypeSpecifier theType = findType(specChildren);
      
      if(appl(prod(label("identifier",_),_,_),_) := theType){
         if(unparse(theType) notin typeDefs){
            filter;
         } // Fail if not typedeffed. And may be ambiguous with "Exp * Exp".
      }
      
      for(spec <- specChildren){
         if(appl(prod(label("typeSpecifier",_),_,_),typeSpecifier) := spec){
            if(identifier:appl(prod(label("identifier",_),_,_),_) := typeSpecifier[0]){
               if(identifier != theType) filter;
            }
         }
      } // May be ambiguous with Spec* {InitDecl ","}*
   }
   
   fail;
}

public Tree StructDeclWithDecl(Specifier+ specs, {StructDeclarator ","}+ declarators){
   list[Tree] specChildren;
   if(appl(_,specChildren) := specs){
      TypeSpecifier theType = findType(specChildren);
      
      for(spec <- specChildren){
         if(appl(prod(label("typeSpecifier",_),_,_),typeSpecifier) := spec){
            if(identifier:appl(prod(label("identifier",_),_,_),_) := typeSpecifier[0]){
               if(identifier != theType) filter;
            }
         }
      }
      
      if(hasCustomType(specChildren)){
         if(unparse(theType) notin typeDefs){
            filter;
         } // Fail if not typedeffed.
      }
   }
   
   fail;
}

public Tree StructDeclWithoutDecl(Specifier+ specs){
   list[Tree] specChildren;
   if(appl(_,specChildren) := specs){
      TypeSpecifier theType = findType(specChildren);
      
      if(appl(prod(label("identifier",_),_,_),_) := theType){   
         if(unparse(theType) notin typeDefs){
            filter;
         } // Fail if not typedeffed. And may be ambiguous with "Exp * Exp".
      }
      
      for(spec <- specChildren){
         if(appl(prod(label("typeSpecifier",_),_,_),typeSpecifier) := spec){
            if(identifier:appl(prod(label("identifier",_),_,_),_) := typeSpecifier[0]){
               if(identifier != theType) filter;
            }
         }
      } // May be ambiguous with Spec* {StructDecl ","}*
   }
   
   fail;
}

public Tree DefaultFunctionDefinition(Specifier* specs, Declarator declarator, Declaration* _, Declaration* _, Statement* _){
   if(!(appl(prod(label("functionDeclarator",_),_,_),_) := declarator) &&
         !(appl(prod(label("bracket",_),_,_),_) := declarator)){
      filter;
   }
   
   list[Tree] specChildren;
   if(appl(_,specChildren) := specs){
      TypeSpecifier theType = findType(specChildren);
      
      if(appl(prod(label("identifier",_),_,_),_) := theType){   
         if(unparse(theType) notin typeDefs){
            filter;
         } // Fail if not typedeffed.
      }
      
      for(spec <- specChildren){
         if(appl(prod(label("typeSpecifier",_),_,_),typeSpecifier) := spec){
            if(identifier:appl(prod(label("identifier",_),_,_),_) := typeSpecifier[0]){
               if(identifier != theType) filter;
            }
         }else if(appl(prod(label("storageClass",_),_,_),storageClass) := spec){
            if(appl(prod(label("typeDef",_),_,_),_) := storageClass[0]){
               filter;
            }
         } // Certain storage parameters are not allowed.
      } // May be ambiguous with the K&R style function parameter definition thing.
   }
   
   fail;
}

public Tree DefaultFunctionPrototype(Specifier* specs, PrototypeDeclarator decl){
   if(!(appl(prod(label("functionDeclarator",_),_,_),_) := decl) &&
         !(appl(prod(label("bracket",_),_,_),_) := decl)){
      filter;
   }
   
   list[Tree] specChildren;
   if(appl(_,specChildren) := specs){
      TypeSpecifier theType = findType(specChildren);
      
      if(appl(prod(label("identifier",_),_,_),_) := theType){   
         if(unparse(theType) notin typeDefs){
            filter;
         } // Fail if not typedeffed.
      }
      
      for(spec <- specChildren){
         if(appl(prod(label("typeSpecifier",_),_,_),typeSpecifier) := spec){
            if(identifier:appl(prod(label("identifier",_),_,_),_) := typeSpecifier[0]){
               if(identifier != theType) filter;
            }
         }else if(appl(prod(label("storageClass",_),_,_),storageClass) := spec){
            if(appl(prod(label("typeDef",_),_,_),_) := storageClass[0]){
               filter;
            }
         } // Certain storage parameters are not allowed (also fixes ambiguity with declarations).
      } // May be ambiguous with the K&R style function parameter definition thing.
   }
   
   fail;
}

//-------------------------------------------------

map[str name, tuple[TypeSpecifier var, tuple[list[Specifier], Declarator] modifiers] cType] typeDefs = (); // Name to type mapping.

private TypeSpecifier findType(list[Tree] specs){
	TypeSpecifier cType;
	
	list[Tree] typeSpecs = [];
	for(spec <- specs){
		if(appl(prod(label("typeSpecifier",_),_,_),typeSpecifier) := spec){
			typeSpecs += typeSpecifier[0];
		}
	}
	
	// This is order dependant, so don't convert this to a switch.
	if([_*,voidType:appl(prod(label("void",_),_,_),_),_*] := typeSpecs){
		cType = voidType;
	}else if([_*,charType:appl(prod(label("char",_),_,_),_),_*] := typeSpecs){
		cType = charType;
	}else if([_*,shortType:appl(prod(label("short",_),_,_),_),_*] := typeSpecs){
		cType = shortType;
	}else if([_*,longType:appl(prod(label("long",_),_,_),_),_*] := typeSpecs){
		cType = longType;
	}else if([_*,floatType:appl(prod(label("float",_),_,_),_),_*] := typeSpecs){
		cType = floatType;
	}else if([_*,doubleType:appl(prod(label("double",_),_,_),_),_*] := typeSpecs){
		cType = doubleType;
	}else if([_*,intType:appl(prod(label("int",_),_,_),_),_*] := typeSpecs){
		cType = intType; // Do this one last, since you can have things like "long int" or "short int". In these cases anything other then "int" is what you want.
	}else if([_*,theStruct:appl(prod(label("struct",_),_,_),_),_*] := typeSpecs){
		cType = theStruct;
	}else if([_*,theStruct:appl(prod(label("structDecl",_),_,_),_),_*] := typeSpecs){
		cType = theStruct;
	}else if([_*,theStruct:appl(prod(label("structAnonDecl",_),_,_),_),_*] := typeSpecs){
		cType = theStruct;
	}else if([_*,theUnion:appl(prod(label("union",_),_,_),_),_*] := typeSpecs){
		cType = theUnion;
	}else if([_*,theUnion:appl(prod(label("unionDecl",_),_,_),_),_*] := typeSpecs){
		cType = theUnion;
	}else if([_*,theUnion:appl(prod(label("unionAnonDecl",_),_,_),_),_*] := typeSpecs){
		cType = theUnion;
	}else if([_*,theEnum:appl(prod(label("enum",_),_,_),_),_*] := typeSpecs){
		cType = theEnum;
	}else if([_*,theEnum:appl(prod(label("enumDecl",_),_,_),_),_*] := typeSpecs){
		cType = theEnum;
	}else if([_*,theEnum:appl(prod(label("enumAnonDecl",_),_,_),_),_*] := typeSpecs){
		cType = theEnum;
	}else if([_*,identifier:appl(prod(label("identifier",_),_,_),_),_*] := typeSpecs){
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
		if(appl(prod(label("typeSpecifier",_),_,_),typeSpecifier) := spec){
			return (appl(prod(label("identifier",_),_,_),_) := typeSpecifier[0]);
		}
	}
	return false;
}

private tuple[list[Specifier], Declarator] findModifiers(list[Tree] specs, Declarator decl){
	list[Specifier] modifiers = [];
	
	for(spec <- specs){
		if(appl(prod(label("typeQualifier",_),_,_),typeQualifier) := spec){
			modifiers += spec;
		}else if(appl(prod(label("storageClass",_),_,_),storageClass) := spec){
			modifiers += spec;
		}
	}
	
	return <modifiers, decl>;
}

private str findVariableInDeclarator(Declarator decl){
	if(appl(prod(label("identifier",_),_,_),_) := decl){
		return unparse(decl);
	}else if(appl(prod(label("bracket",_),_,_),_) := decl){
		return findVariableInDeclarator(decl.decl);
	}else if(appl(prod(label("arrayDeclarator",_),_,_),_) := decl){
		return findVariableInDeclarator(decl.decl);
	}else if(appl(prod(label("functionDeclarator",_),_,_),_) := decl){
		return findVariableInDeclarator(decl.decl);
	}else if(appl(prod(label("pointerDeclarator",_),_,_),_) := decl){
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
