@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Davy Landman - Davy.Landman@cwi.nl - CWI}
module lang::java::\syntax::Disambiguate
/*
	Import this module to Disambiguate the ambiguity cause by the prefix operators +/- and infix operators +/-.
	This causes a ambiguity in case of (A) + (B) . This could be (A)(+ (B)) or ((A)) + ((B)).
	We need to have a symbol table to decide if A is a type and thus a TypeCast, or it is a field/variable.
	
	Java lacks operator overloading, therefore, prefix operators only work on numeric types.
	Moreover, there is not support for custom covariance and contravariance.
	Therefore, only if (A) is a primary/primary numeric type can it be a prefix expression.
	
	We therefore have added this complete but not sound disambiguation as a separate module.
	
	These following cases will result in a incorrect parse tree:
	
	- Shadowing of Integer/Double/Float
	- An invalid type cast: (String)+(A) where A has a numeric type
	  (This expression would be an uncompilable, and we would disambiguate it as a infix expression) 
*/
import ParseTree;
import String;
import IO;
import lang::java::\syntax::Java15;

bool isNumeric((RefType)`Byte`) = true;
bool isNumeric((RefType)`java.lang.Byte`) = true;
bool isNumeric((RefType)`Character`) = true;
bool isNumeric((RefType)`java.lang.Character`) = true;
bool isNumeric((RefType)`Short`) = true;
bool isNumeric((RefType)`java.lang.Short`) = true;
bool isNumeric((RefType)`Integer`) = true;
bool isNumeric((RefType)`java.lang.Integer`) = true;
bool isNumeric((RefType)`Long`) = true;
bool isNumeric((RefType)`java.lang.Long`) = true;
bool isNumeric((RefType)`Float`) = true;
bool isNumeric((RefType)`java.lang.Float`) = true;
bool isNumeric((RefType)`Double`) = true;
bool isNumeric((RefType)`java.lang.Double`) = true;

default bool isNumeric(RefType r) = false;

Expr amb({cast:(Expr)`(<RefType t>) <Expr e>`, infix:appl(_,[(Expr)`(<ExprName n>)`,_*])}) {
	// (A) + 1
	if (isNumeric(t)) {
		return cast;
	}
	else {
		return infix;	
	}
} 

Expr amb({cast:appl(_,[_*,(Expr)`(<RefType t>) <Expr e>`]), infix:appl(_,[appl(_,[_*,(Expr)`(<ExprName n>)`]),_*])}) {
	// 1 + (A) + 1
	if (isNumeric(t)) {
		return cast;
	}
	else {
		return infix;	
	}
}