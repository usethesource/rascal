@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Anastasia Izmaylova - A.Izmaylova@cwi.nl}
module lang::oberon0::AST

@functor FModule #Ident, #Declarations, #list[Statement]  
data Module = 
	\mod(Ident name, Declarations decls, list[Statement] body, Ident endName)
	;
	
@functor FDeclarations #list[ConstDecl], #list[TypeDecl], #list[VarDecl]
data Declarations 
	= decls(list[ConstDecl] consts, list[TypeDecl] types, list[VarDecl] vars)
	;

@functor FConstDecl #Ident, #Expression
data ConstDecl 
	= constDecl(Ident name, Expression \value)
	;
		
@functor FTypeDecl #Ident, #Type
data TypeDecl 
	= typeDecl(Ident name, Type \type)
	;
	
@functor FVarDecl #list[Ident], #Type
data VarDecl 
	= varDecl(list[Ident] names, Type \type)
	;

@functor FType #Ident
data Type 
	= user(Ident name)
	;

@functor FStatement #Ident, #Expression, #list[Statement], #list[tuple[Expression,list[Statement]]]
data Statement 
	= assign(Ident var, Expression exp)
	| ifThen(Expression condition, list[Statement] body, list[tuple[Expression, list[Statement]]] elseIfs, list[Statement] elsePart)
	| whileDo(Expression condition, list[Statement] body)
	;
	
@functor FExpression #int, #Ident, #Expression
data Expression 
	= nat(int val)
	| \true()
	| \false()
	| lookup(Ident var)
	| neg(Expression exp)
	| pos(Expression exp)
	| not(Expression exp)
	| mul(Expression lhs, Expression rhs)
	| div(Expression lhs, Expression rhs)
	| \mod(Expression lhs, Expression rhs)
	| amp(Expression lhs, Expression rhs)
	| add(Expression lhs, Expression rhs)
	| sub(Expression lhs, Expression rhs)
	| or(Expression lhs, Expression rhs)
	| eq(Expression lhs, Expression rhs)
	| neq(Expression lhs, Expression rhs)
	| lt(Expression lhs, Expression rhs)
	| gt(Expression lhs, Expression rhs)
	| leq(Expression lhs, Expression rhs)
	| geq(Expression lhs, Expression rhs)
	;

@functor FIdent #str
data Ident 
	= id(str name)
	;

anno loc Ident@location;
anno loc Module@location;
anno loc VarDecl@location;
anno loc TypeDecl@location;
anno loc ConstDecl@location;
anno loc Declarations@location;
anno loc Type@location;
anno loc Statement@location;
anno loc Expression@location;

