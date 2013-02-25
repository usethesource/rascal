@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module lang::oberon0::syntax::Declarations

import lang::oberon0::syntax::Layout;
import lang::oberon0::syntax::Statements;
import lang::oberon0::syntax::Types;
import lang::oberon0::syntax::Lexical;
import lang::oberon0::syntax::Expressions;

syntax Declarations 
	= decls: ConstSect? consts TypeSect? types VarSect? vars
	;

syntax ConstDecl 
	= constDecl: Ident name "=" Expression value ";"
	;
	
syntax ConstSect 
	= "CONST" ConstDecl* consts
	;

syntax TypeDecl 
	= typeDecl: Ident name "=" Type type ";"
	;

syntax TypeSect 
	= "TYPE" TypeDecl* types
	;

syntax VarDecl 
	= varDecl: {Ident ","}* names ":" Type type ";"
	;
	
syntax VarSect 
	= @Foldable "VAR" VarDecl* vars
	;

keyword Keywords 
	= "VAR" 
	| "TYPE" 
	| "CONST" 
	;
	